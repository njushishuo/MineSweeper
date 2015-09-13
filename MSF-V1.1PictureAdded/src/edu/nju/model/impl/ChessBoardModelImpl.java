package edu.nju.model.impl;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import edu.nju.controller.msgqueue.OperationQueue;
import edu.nju.controller.msgqueue.operation.LeftClickOperation;
import edu.nju.main.JMineSweeper;
import edu.nju.model.po.BlockPO;
import edu.nju.model.service.ChessBoardModelService;
import edu.nju.model.service.GameModelService;
import edu.nju.model.service.ParameterModelService;
import edu.nju.model.state.BlockState;
import edu.nju.model.state.DisplayBlockState;
import edu.nju.model.state.GameResultState;
import edu.nju.model.state.GameState;
import edu.nju.model.vo.BlockVO;
import edu.nju.network.client.ClientAdapter;
import edu.nju.network.host.ServerAdapter;
import edu.nju.view.WebEndDialog;

public class ChessBoardModelImpl extends BaseModel implements ChessBoardModelService{
	
	private GameModelService gameModel;
	private ParameterModelService parameterModel;
	
	private static BlockPO[][] blockMatrix;
    
	private int mineNum=0;
	private int width=0;
	private int height=0;
	private boolean isFirstClick = false;
	
	//public static boolean isLate = false; 
	public ChessBoardModelImpl(ParameterModelService parameterModel){
		this.parameterModel = parameterModel;
	}
     
	@Override
	public boolean initialize(int width, int height, int mineNum) {
		// TODO Auto-generated method stub
		
		/********************简单示例初始化方法，待完善********************/
		this.width=width;
		this.height=height;
		blockMatrix = new BlockPO[width][height];
		isFirstClick=true;
		//setBlock(mineNum);
		this.mineNum=mineNum;
		this.parameterModel.setMineNum(mineNum);
		/***********请在删除上述内容的情况下，完成自己的内容****************/
		
		return false;
	}

	@Override
	public boolean excavate(int x, int y) {
		// TODO Auto-generated method stub
		
		//第一次布雷
		if(isFirstClick){
		    if(JMineSweeper.isHost){
				setBlock(this.mineNum, x, y);
				synchronizeChessBoard();
				OperationQueue.addMineOperation( new LeftClickOperation(x,y));
			}else if(JMineSweeper.isClient) {
			    setBlock(this.mineNum,x,y);
			    ClientAdapter.write(new LeftClickOperation(x,y));
			}else{
				setBlock(this.mineNum,x,y);
				OperationQueue.addMineOperation(new LeftClickOperation(x, y));
			}
			super.updateChange(new UpdateMessage("timing",new Object() ));
			this.printBlockMatrix();
			isFirstClick=false;	
			System.out.println("ChessBoard is set!!!");
			
		    return true;//执行到这里就结束啦
		} 
		   //挖开
		   if(blockMatrix == null)
			   return false;
		
		   List<BlockPO> blocks = new ArrayList<BlockPO>();
		   BlockPO block = blockMatrix[x][y];
		
		   block.setState(BlockState.CLICK);
		   blocks.add(block);
		   
		   GameState gameState = GameState.RUN;
		   if(block.isMine()){
			  if(JMineSweeper.isClient|JMineSweeper.isHost){
			     gameState = GameState.OVER;			    
			     super.updateChange(new UpdateMessage("gameover",this.getBlockMatrix()));
			     if(JMineSweeper.isClient){
			    	  this.gameModel.gameOver(GameResultState.SUCCESS);
				      WebEndDialog  w = new  WebEndDialog(new JFrame(),false, true);
			          w.show();
			     }else {
			    	 this.gameModel.gameOver(GameResultState.FAIL);
				      WebEndDialog  w = new  WebEndDialog(new JFrame(),true, false);
			          w.show();
			     }
			  }else{
				  gameState = GameState.OVER;
				  this.gameModel.gameOver(GameResultState.FAIL);
				  super.updateChange(new UpdateMessage("gameover",this.getBlockMatrix())); 
			  }
		   }		   
		   
		   int temp=0;
			for(int i=0;i<blockMatrix.length;i++){
				for(int j=0;j<blockMatrix[i].length;j++){
	                 if(!blockMatrix[i][j].isMine()&blockMatrix[i][j].getState()==BlockState.CLICK){
	                	         temp++;
	                 }
				}
			}
			
			if(temp==(this.width*this.height-this.mineNum)){
				gameState=GameState.OVER;
				super.updateChange(new UpdateMessage("gameover",this.getBlockMatrix()));
				      //联机获胜判定
				if(JMineSweeper.isClient|JMineSweeper.isHost){
					  //主机获胜
					  if(JMineSweeper.HostFlag>JMineSweeper.ClientFlag){
						  if(JMineSweeper.isHost){
						       this.gameModel.gameOver(GameResultState.SUCCESS);
						       WebEndDialog  w = new  WebEndDialog(new JFrame(),true, true);
					           w.show();
					      }else if(JMineSweeper.isClient){
					    	  this.gameModel.gameOver(GameResultState.FAIL);
					      WebEndDialog  w = new  WebEndDialog(new JFrame(),false, false);
						  w.show();
					      }
					 //客户获胜
					  }else{
						  if(JMineSweeper.isHost){
							  this.gameModel.gameOver(GameResultState.FAIL);
							  WebEndDialog  w = new  WebEndDialog(new JFrame(),true, false);
						      w.show();
						      }else if(JMineSweeper.isClient){
						      this.gameModel.gameOver(GameResultState.SUCCESS);
						      WebEndDialog  w = new  WebEndDialog(new JFrame(),false, true);
							  w.show();
						      }
					  }
				}else{
					  //单机获胜窗口
					  this.gameModel.gameOver(GameResultState.SUCCESS);
				      super.updateChange(new UpdateMessage("PCwin",this.getBlockMatrix()));
			    }
			}
           
		   super.updateChange(new UpdateMessage("excute",this.getDisplayList(blocks, gameState)));
		   //准备递归
		   if(blockMatrix[x][y].getDisplayBlock(gameState).getState()==DisplayBlockState.ZERO){
  	         quickExcavate(x, y);
           }	       
		   return true;
		
	}
	
	
	
	@Override
	public boolean mark(int x, int y) {
		// TODO Auto-generated method stub
		/********************简单示例标记方法，待完善********************/
		
		/***********请在删除上述内容的情况下，完成自己的内容****************/
		
		if(blockMatrix == null)
			return false;
		
		List<BlockPO> blocks = new ArrayList<BlockPO>();
		BlockPO block = blockMatrix[x][y];
		 
		BlockState state = block.getState();
		GameState gameState = GameState.RUN;
		
		//网络对战中标记错误也会失败
		if(JMineSweeper.isHost|JMineSweeper.isClient){
			  if(state == BlockState.UNCLICK){			
		           //先判断剩余可标记数是否为0；若为零则该方法会返回false
				 if(block.isMine()){
					 JMineSweeper.HostFlag++;
				 }
			     if(this.parameterModel.minusMineNum()){
				    block.setState(BlockState.FLAG);
			     }
			     if(!block.isMine()){
			    	  gameState = GameState.OVER;
					  super.updateChange(new UpdateMessage("gameover",this.getBlockMatrix()));
					  if(JMineSweeper.isClient){
						  this.gameModel.gameOver(GameResultState.SUCCESS);
					      WebEndDialog  w = new  WebEndDialog(new JFrame(),false, true);
					      w.show();
					  }else if(JMineSweeper.isHost){
						  this.gameModel.gameOver(GameResultState.FAIL);
						  WebEndDialog  w = new  WebEndDialog(new JFrame(),true, false);
					      w.show();
					  }
			     }
			    	 
			  }
		
			
		}else {
			    //单机可以标记错误
		         if(state == BlockState.UNCLICK){			
		           //先判断剩余可标记数是否为0；若为零则该方法会返回false
			        if(this.parameterModel.minusMineNum()){
				       block.setState(BlockState.FLAG);
			        }
		        }	
		}
		
		int temp=0;
 		for(int i=0;i<blockMatrix.length;i++){
 			for(int j=0;j<blockMatrix[i].length;j++){
                  if(blockMatrix[i][j].isMine()& BlockState.FLAG== blockMatrix[i][j].getState()){
                 	         temp++;
                  }
 			}
 		}
 		
 		if(temp==this.mineNum){
 			gameState = GameState.OVER;
 			
 			if(JMineSweeper.isClient|JMineSweeper.isHost){
				  //主机获胜
				  if(JMineSweeper.HostFlag>JMineSweeper.ClientFlag){
					  if(JMineSweeper.isHost){
					       this.gameModel.gameOver(GameResultState.SUCCESS);
					       WebEndDialog  w = new  WebEndDialog(new JFrame(),true, true);
				           w.show();
				      }else if(JMineSweeper.isClient){
				    	  this.gameModel.gameOver(GameResultState.FAIL);
				      WebEndDialog  w = new  WebEndDialog(new JFrame(),false, false);
					  w.show();
				      }
				 //客户获胜
				  }else{
					  if(JMineSweeper.isHost){
						  this.gameModel.gameOver(GameResultState.FAIL);
						  WebEndDialog  w = new  WebEndDialog(new JFrame(),true, false);
					      w.show();
					      }else if(JMineSweeper.isClient){
					      this.gameModel.gameOver(GameResultState.SUCCESS);
					      WebEndDialog  w = new  WebEndDialog(new JFrame(),false, true);
						  w.show();
					      }
				  }
			}else{
				  //单机获胜窗口
				  this.gameModel.gameOver(GameResultState.SUCCESS);
				  super.updateChange(new UpdateMessage("gameover",this.getBlockMatrix()));
			      super.updateChange(new UpdateMessage("PCwin",this.getBlockMatrix()));
		    }
 			
		    
 		}	//HERE
		         
	    if(state == BlockState.FLAG){
			block.setState(BlockState.UNCLICK);
			this.parameterModel.addMineNum();
		}
		
		blocks.add(block);		
		super.updateChange(new UpdateMessage("excute",this.getDisplayList(blocks, gameState)));
		
		
		return true;
	}

	@Override
	public boolean quickExcavate(int x, int y) {
		// TODO Auto-generated method stub
		/***********请在此处完成快速挖开方法实现****************/
		if(blockMatrix == null){
			return false;
		}
		
		if(blockMatrix[x][y].isMine()){
			return false;
		}
		List<BlockPO> blocks = new ArrayList<BlockPO>();
		
		int mineNum=0;
		int flagNum=0;
		int width= blockMatrix.length;
		int height = blockMatrix[0].length;
		int prex=0;
		int prey=0;
		for(int i=x-1;i<=x+1;i++){
			for(int j=y-1;j<=y+1;j++){
				if((i>-1&i<width)&(j>-1&j<height)){
			
				    if(blockMatrix[i][j].isMine()){
					    mineNum++;
				    }
				    if(blockMatrix[i][j].getState()==BlockState.FLAG|blockMatrix[i][j].getState()==BlockState.FLAG1){
				      	flagNum++;
				    }
				    if(blockMatrix[i][j].getState()==BlockState.UNCLICK){
					    blocks.add(blockMatrix[i][j]);
				    }
				}
			}
		}
		
		GameState gameState = GameState.RUN;
		if(mineNum==flagNum){
			for(BlockPO block : blocks){
		         block.setState(BlockState.CLICK);
		         
		         if(block.isMine()){
		        	 if(JMineSweeper.isClient|JMineSweeper.isHost){
					     gameState = GameState.OVER;
					     super.updateChange(new UpdateMessage("gameover",this.getBlockMatrix()));
					     if(JMineSweeper.isClient){
					    	 this.gameModel.gameOver(GameResultState.SUCCESS);
						      WebEndDialog  w = new  WebEndDialog(new JFrame(),false, true);
					          w.show();
					     }else if(JMineSweeper.isHost) {			
					    	 this.gameModel.gameOver(GameResultState.FAIL);
						      WebEndDialog  w = new  WebEndDialog(new JFrame(),true, false);
					          w.show();
					     }
					  }else{
						  //单机
						  gameState = GameState.OVER;
						  this.gameModel.gameOver(GameResultState.FAIL);
						  super.updateChange(new UpdateMessage("gameover",this.getBlockMatrix())); 
					  }
		 		}
		         
			}
		}
//		if(gameState == GameState.OVER)	{
//			super.updateChange(new UpdateMessage("gameover",this.getBlockMatrix()));
//		}
		blockMatrix[x][y].setDoubleClick(true);
		super.updateChange(new UpdateMessage("excute",this.getDisplayList(blocks, gameState)));			
        
	 if(blockMatrix[x][y].getDisplayBlock(gameState).getState()==DisplayBlockState.ZERO){
		 for(int tempI = x-1;tempI<=x+1;tempI++){
			 for(int tempJ = y-1;tempJ<=y+1;tempJ++){
				 if((tempI>-1&&tempI<width)&&(tempJ>-1&&tempJ<height)&&(!blockMatrix[tempI][tempJ].isDoubleClicked())){
					 if(blockMatrix[tempI][tempJ].getDisplayBlock(gameState).getState()==DisplayBlockState.ZERO){
				         quickExcavate(tempI, tempJ);									
				     }else{
				    	 
				     }
				 }
			
			 }
		 }
	 } 
	
		return false;
	}

	/**
	 * 示例方法，可选择是否保留该形式
	 * 
	 * 初始化BlockMatrix中的Block，并随机设置mineNum颗雷
	 * 同时可以为每个Block设定附近的雷数
	 * @param mineNum
	 * @return
	 */
	
	
	
	private boolean setBlock(int mineNum,int x,int y){
		int width = blockMatrix.length;
		int height = blockMatrix[0].length;
		int mineSet=0;
		//布雷
		for(int k = 0 ; k<width; k++){
			for (int j = 0 ; j< height; j++){
				blockMatrix[k][j] = new BlockPO(k,j);
			}
		}
		
		System.out.println(width);
		System.out.println(height);
		
		while(mineNum>mineSet){
			
			int harizon=0;
			int vertical=0;
			int temp=(int)(Math.random()*width*height);
			
			harizon=temp/height;
			vertical=temp%height;
			if((harizon==x&vertical==y)|(harizon>=width)|(vertical>=height)|blockMatrix[harizon][vertical].isMine()){
				
			}else{
				blockMatrix[harizon][vertical].setMine(true);
				addMineNum(harizon,vertical);
				mineSet++;
			}
		}
		System.out.println("mineSet :"+mineSet);
		return true;
	}
	
	public void synchronizeChessBoard(){
		if(JMineSweeper.isHost){
			ServerAdapter.write(blockMatrix);
			System.out.println("Sever send block");
		}
		
//		if(JMineSweeper.isClient){
//			ClientAdapter.write(blockMatrix);
//			System.out.println("client send block");
//		}
	}
	
	public static void setBlockPOs(BlockPO[][] temp){
		blockMatrix=temp;
	}
	/**
	 * 示例方法，可选择是否保留该形式
	 * 
	 * 因为中间被设置为了雷，故将(i,j)位置附近的Block雷数加1
	 * @param i
	 * @param j
	 */
	private void addMineNum(int i, int j){
		int width = blockMatrix.length;
		int height = blockMatrix[0].length;
		
		int tempI = i-1;		
		
		for(;tempI<=i+1;tempI++){
			int tempJ = j-1;
			for(;tempJ<=j+1;tempJ++){
				if((tempI>-1&&tempI<width)&&(tempJ>-1&&tempJ<height)){
					System.out.println(i+";"+j+":"+tempI+";"+tempJ+":");
					
					blockMatrix[tempI][tempJ].addMine();
				}
			}
		}
		
	}
	
	/**
	 * 将逻辑对象转化为显示对象
	 * @param blocks
	 * @param gameState
	 * @return
	 */
	private List<BlockVO> getDisplayList(List<BlockPO> blocks, GameState gameState){
		List<BlockVO> result = new ArrayList<BlockVO>();
		for(BlockPO block : blocks){
			if(block != null){
				BlockVO displayBlock = block.getDisplayBlock(gameState);
				if(displayBlock.getState() != null)
				result.add(displayBlock);
			}
		}
		return result;
	}
	
	public BlockPO[][] getBlockMatrix (){
		return blockMatrix;
	}

	@Override
	public void setGameModel(GameModelService gameModel) {
		// TODO Auto-generated method stub
		this.gameModel = gameModel;
	}
	
	private void printBlockMatrix(){
		for(BlockPO[] blocks : this.blockMatrix){
			for(BlockPO b :blocks){
				String p = b.getMineNum()+"";
				if(b.isMine())
					p="*";
				System.out.print(p);
			}
			System.out.println();
		}
	}

	//新增客户的MARK方法	
	@Override
	public boolean markClient(int x, int y) {
		// TODO Auto-generated method stub
	
			if(blockMatrix == null)
				return false;
			
			List<BlockPO> blocks = new ArrayList<BlockPO>();
			BlockPO block = blockMatrix[x][y];
			 
			BlockState state = block.getState();
			GameState gameState = GameState.RUN;
			//网络对战中标记错误也会失败
			if(JMineSweeper.isHost|JMineSweeper.isClient){
				     if(state == BlockState.UNCLICK){			
			           //先判断剩余可标记数是否为0；若为零则该方法会返回false
				     if(block.isMine()){
						JMineSweeper.ClientFlag++;
				     }
				     if(this.parameterModel.minusMineNum()){
					    block.setState(BlockState.FLAG1);
				     }
				     if(!block.isMine()){
				    	  gameState = GameState.OVER;					 
						  super.updateChange(new UpdateMessage("gameover",this.getBlockMatrix()));
						  if(JMineSweeper.isClient){
							  this.gameModel.gameOver(GameResultState.FAIL);
							  WebEndDialog  w = new  WebEndDialog(new JFrame(),false, false);
						      w.show();
						  }else if(JMineSweeper.isHost){
							  this.gameModel.gameOver(GameResultState.SUCCESS);
							  WebEndDialog  w = new  WebEndDialog(new JFrame(),true, true);
						      w.show();
						  }
				     }
				    	 
				  }
			
				
			}else {
				    //单机可以标记错误
			         if(state == BlockState.UNCLICK){			
			           //先判断剩余可标记数是否为0；若为零则该方法会返回false
				        if(this.parameterModel.minusMineNum()){
					       block.setState(BlockState.FLAG1);
				        }
			        }
			}
			int temp=0;
	 		for(int i=0;i<blockMatrix.length;i++){
	 			for(int j=0;j<blockMatrix[i].length;j++){
	                  if(blockMatrix[i][j].isMine()& BlockState.FLAG== blockMatrix[i][j].getState()){
	                 	         temp++;
	                  }
	 			}
	 		}
	 		
	 		if(temp==this.mineNum){
	 			gameState = GameState.OVER;
	 			if(JMineSweeper.isClient|JMineSweeper.isHost){
					  //主机获胜
					  if(JMineSweeper.HostFlag>JMineSweeper.ClientFlag){
						  if(JMineSweeper.isHost){
						       this.gameModel.gameOver(GameResultState.SUCCESS);
						       WebEndDialog  w = new  WebEndDialog(new JFrame(),true, true);
					           w.show();
					      }else if(JMineSweeper.isClient){
					    	  this.gameModel.gameOver(GameResultState.FAIL);
					      WebEndDialog  w = new  WebEndDialog(new JFrame(),false, false);
						  w.show();
					      }
					 //客户获胜
					  }else{
						  if(JMineSweeper.isHost){
							  this.gameModel.gameOver(GameResultState.FAIL);
							  WebEndDialog  w = new  WebEndDialog(new JFrame(),true, false);
						      w.show();
						      }else if(JMineSweeper.isClient){
						      this.gameModel.gameOver(GameResultState.SUCCESS);
						      WebEndDialog  w = new  WebEndDialog(new JFrame(),false, true);
							  w.show();
						      }
					  }
				}else{
					  //单机获胜窗口
					  this.gameModel.gameOver(GameResultState.SUCCESS);
					  super.updateChange(new UpdateMessage("gameover",this.getBlockMatrix()));
				      super.updateChange(new UpdateMessage("PCwin",this.getBlockMatrix()));
			    }
	 			
			    
	 		}	//HERE
		
		    if(state == BlockState.FLAG1){
				block.setState(BlockState.UNCLICK);
				this.parameterModel.addMineNum();
			}
			
			blocks.add(block);	
			
//			int temp=0;
//			for(int i=0;i<blockMatrix.length;i++){
//				for(int j=0;j<blockMatrix[i].length;j++){
//	                 if(blockMatrix[i][j].isMine()& BlockState.FLAG== blockMatrix[i][j].getState()){
//	                	         temp++;
//	                 }
//				}
//			}
//			if(temp==this.mineNum){
//				gameState=GameState.OVER;
//				gameState = GameState.OVER;
//				this.gameModel.gameOver(GameResultState.SUCCESS);
//			}		
			super.updateChange(new UpdateMessage("excute",this.getDisplayList(blocks, gameState)));
			return true;
		
		}
    //新增客户挖开方法
	@Override
	public boolean excavateClient(int x, int y) {
		// TODO Auto-generated method stub
		//第一次布雷
				if(isFirstClick){
				    if(JMineSweeper.isHost){
						setBlock(this.mineNum, x, y);
						synchronizeChessBoard();
						OperationQueue.addMineOperation( new LeftClickOperation(x,y));
					}else if(JMineSweeper.isClient) {
					    setBlock(this.mineNum,x,y);
					    ClientAdapter.write(new LeftClickOperation(x,y));
					}else{
						setBlock(this.mineNum,x,y);
						OperationQueue.addMineOperation(new LeftClickOperation(x, y));
					}
					super.updateChange(new UpdateMessage("timing",new Object() ));
					this.printBlockMatrix();
					isFirstClick=false;	
					System.out.println("ChessBoard is set!!!");
					
				    return true;//执行到这里就结束啦
				} 
				   //挖开
				   if(blockMatrix == null)
					   return false;
				
				   List<BlockPO> blocks = new ArrayList<BlockPO>();
				   BlockPO block = blockMatrix[x][y];
				
				   block.setState(BlockState.CLICK);
				   blocks.add(block);
				   
				   GameState gameState = GameState.RUN;
				   if(block.isMine()){
						  if(JMineSweeper.isClient|JMineSweeper.isHost){
						     gameState = GameState.OVER;						     
						     super.updateChange(new UpdateMessage("gameover",this.getBlockMatrix()));
						     if(JMineSweeper.isClient){
						    	 this.gameModel.gameOver(GameResultState.FAIL);
							      WebEndDialog  w = new  WebEndDialog(new JFrame(),false, false);
						          w.show();
						     }else {				 
						    	 this.gameModel.gameOver(GameResultState.SUCCESS);
							      WebEndDialog  w = new  WebEndDialog(new JFrame(),true, true);
						          w.show();
						     }
						  }else{
							  gameState = GameState.OVER;
							  this.gameModel.gameOver(GameResultState.FAIL);
							  super.updateChange(new UpdateMessage("gameover",this.getBlockMatrix())); 
						  }
					   }		
				
//				   if(gameState == GameState.OVER)	{				   
//					   if(JMineSweeper.isHost){
//						   //向主机的OQ
//						   OperationQueue.addMineOperation(new WebEndGameOperation(true, false));
//						   ServerAdapter.write(new WebEndGameOperation(false,true));
//					   }
			   
//					   if(JMineSweeper.isClient){
						   //向zhuji的OQ
//						   ClientAdapter.write(new WebEndGameOperation(true, true));
//						   OperationQueue.addMineOperation(new WebEndGameOperation(false, false));
	   
//					   super.updateChange(new UpdateMessage("gameover",this.getBlockMatrix()));
//				   }
//				   
				   
				   //胜利
				   int temp=0;
					for(int i=0;i<blockMatrix.length;i++){
						for(int j=0;j<blockMatrix[i].length;j++){
			                 if(!blockMatrix[i][j].isMine()&blockMatrix[i][j].getState()==BlockState.CLICK){
			                	         temp++;
			                 }
						}
					}
					
					if(temp==(this.width*this.height-this.mineNum)){
						gameState=GameState.OVER;
						
						      //联机获胜判定
						if(JMineSweeper.isClient|JMineSweeper.isHost){
							  //主机获胜
							  if(JMineSweeper.HostFlag>JMineSweeper.ClientFlag){
								  if(JMineSweeper.isHost){
									  this.gameModel.gameOver(GameResultState.SUCCESS);
								  WebEndDialog  w = new  WebEndDialog(new JFrame(),true, true);
							      w.show();
							      }else if(JMineSweeper.isClient){
							    	  this.gameModel.gameOver(GameResultState.FAIL);
							      WebEndDialog  w = new  WebEndDialog(new JFrame(),false, false);
								  w.show();
							      }
							 //客户获胜
							  }else{
								  if(JMineSweeper.isHost){
									  this.gameModel.gameOver(GameResultState.FAIL);
									  WebEndDialog  w = new  WebEndDialog(new JFrame(),true, false);
								      w.show();
								      }else if(JMineSweeper.isClient){
								               this.gameModel.gameOver(GameResultState.SUCCESS);
								               WebEndDialog  w = new  WebEndDialog(new JFrame(),false, true);
									           w.show();
								      }
							  }
						}else{
							  this.gameModel.gameOver(GameResultState.SUCCESS);
							  //单机获胜窗口
						      super.updateChange(new UpdateMessage("PCwin",this.getBlockMatrix()));
						      
					    }
					}
		           
				   super.updateChange(new UpdateMessage("excute",this.getDisplayList(blocks, gameState)));
				   //准备递归
				   if(blockMatrix[x][y].getDisplayBlock(gameState).getState()==DisplayBlockState.ZERO){
		  	         quickExcavate(x, y);
		           }	       
				   return true;
	}
    //新增客户快速挖开方法
	@Override
	public boolean quickExcavateClient(int x, int y) {
		// TODO Auto-generated method stub
		/***********请在此处完成快速挖开方法实现****************/
		if(blockMatrix == null){
			return false;
		}
		
		if(blockMatrix[x][y].isMine()){
			return false;
		}
		List<BlockPO> blocks = new ArrayList<BlockPO>();
		
		int mineNum=0;
		int flagNum=0;
		int width= blockMatrix.length;
		int height = blockMatrix[0].length;
		int prex=0;
		int prey=0;
		for(int i=x-1;i<=x+1;i++){
			for(int j=y-1;j<=y+1;j++){
				if((i>-1&i<width)&(j>-1&j<height)){
			
				    if(blockMatrix[i][j].isMine()){
					    mineNum++;
				    }
				    if(blockMatrix[i][j].getState()==BlockState.FLAG|blockMatrix[i][j].getState()==BlockState.FLAG1){
				      	flagNum++;
				    }
				    if(blockMatrix[i][j].getState()==BlockState.UNCLICK){
					    blocks.add(blockMatrix[i][j]);
				    }
				}
			}
		}
		
		GameState gameState = GameState.RUN;
		if(mineNum==flagNum){
			for(BlockPO block : blocks){
		         block.setState(BlockState.CLICK);
		         
		         if(block.isMine()){
		        	 if(JMineSweeper.isClient|JMineSweeper.isHost){
					     gameState = GameState.OVER;
					     
					     super.updateChange(new UpdateMessage("gameover",this.getBlockMatrix()));
					     if(JMineSweeper.isClient){
					    	 this.gameModel.gameOver(GameResultState.FAIL);
						      WebEndDialog  w = new  WebEndDialog(new JFrame(),false, false);
					          w.show();
					     }else {			
					    	 this.gameModel.gameOver(GameResultState.SUCCESS);
						      WebEndDialog  w = new  WebEndDialog(new JFrame(),true, true);
					          w.show();
					     }
					  }else{
						  gameState = GameState.OVER;
						  this.gameModel.gameOver(GameResultState.FAIL);
						  super.updateChange(new UpdateMessage("gameover",this.getBlockMatrix())); 
					  }
		 		}
		         
			}
		}
		if(gameState == GameState.OVER)	{
			super.updateChange(new UpdateMessage("gameover",this.getBlockMatrix()));
		}
		blockMatrix[x][y].setDoubleClick(true);
		super.updateChange(new UpdateMessage("excute",this.getDisplayList(blocks, gameState)));			
        
	 if(blockMatrix[x][y].getDisplayBlock(gameState).getState()==DisplayBlockState.ZERO){
		 for(int tempI = x-1;tempI<=x+1;tempI++){
			 for(int tempJ = y-1;tempJ<=y+1;tempJ++){
				 if((tempI>-1&&tempI<width)&&(tempJ>-1&&tempJ<height)&&(!blockMatrix[tempI][tempJ].isDoubleClicked())){
					 if(blockMatrix[tempI][tempJ].getDisplayBlock(gameState).getState()==DisplayBlockState.ZERO){
				         quickExcavate(tempI, tempJ);									
				     }else{
				    	 
				     }
				 }
			
			 }
		 }
	 } 
	
		return false;
		
	}
	
}
