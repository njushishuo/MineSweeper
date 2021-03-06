package edu.nju.model.po;

import java.io.Serializable;

import edu.nju.main.JMineSweeper;
import edu.nju.model.state.BlockState;
import edu.nju.model.state.DisplayBlockState;
import edu.nju.model.state.GameState;
import edu.nju.model.vo.BlockVO;
/**
 * 后台处理的扫雷块单元
 * @author Wangy
 *
 */
public class BlockPO implements Serializable{

	private BlockState state;
	private int mineNum;
	private boolean isMine;
	private boolean isDoubleClicked;
	private int x;
	private int y;
	
	public BlockPO(int x, int y){
		state = BlockState.UNCLICK;
		isDoubleClicked=false;
		isMine=false;
		this.mineNum = 0;
		this.x = x;
		this.y = y;
	}

	/**
	 * 获得用于在界面上显示的扫雷块
	 * @return
	 */
	public BlockVO getDisplayBlock(GameState gameState){
		DisplayBlockState dbs = null;

		if(state == BlockState.CLICK&&(!isMine)){
			dbs = DisplayBlockState.getClickState(mineNum);
		}
		else if(state == BlockState.UNCLICK){
			dbs = DisplayBlockState.UNCLICK;
		}
		else if(state == BlockState.FLAG){
			dbs = DisplayBlockState.FLAG;
			
		}else if (state == BlockState.FLAG1){
			dbs = DisplayBlockState.FLAG1;
		}
		if(gameState == GameState.OVER){//当游戏为结束状态时
			if(state == BlockState.CLICK&&isMine){
				dbs = DisplayBlockState.Bomb;
			}
			else if((state == BlockState.UNCLICK)&&isMine){
				dbs = DisplayBlockState.MINE;
			}
			else if(state == BlockState.FLAG&&(!isMine)){
				dbs = DisplayBlockState.ERROFLAG;
			}
			else if((state == BlockState.UNCLICK)&&!isMine){
				dbs=DisplayBlockState.UNCLICK;
			}
		}

		BlockVO db = new BlockVO(dbs,x,y);

		return db;
	}

	public BlockState getState() {
		return state;
	}

	public void setState(BlockState state) {
		this.state = state;
	}

	public int getMineNum() {
		return mineNum;
	}

	public void setMineNum(int mineNum) {
		this.mineNum = mineNum;
	}
	
	public void setDoubleClick(boolean isDouble){
		this.isDoubleClicked=isDouble;
	}

	public boolean isDoubleClicked() {
		return isDoubleClicked;
	}
	
	public boolean isMine() {
		return isMine;
	}

	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void addMine(){
		this.mineNum++;
	}
}