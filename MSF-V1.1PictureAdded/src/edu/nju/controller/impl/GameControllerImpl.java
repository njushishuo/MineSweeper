package edu.nju.controller.impl;
import edu.nju.controller.msgqueue.OperationQueue;
import edu.nju.controller.msgqueue.operation.DoubleClickOperation;
import edu.nju.controller.msgqueue.operation.LeftClickOperation;
import edu.nju.controller.msgqueue.operation.MineOperation;
import edu.nju.controller.msgqueue.operation.RightClickOperation;
import edu.nju.controller.service.GameControllerService;
import edu.nju.main.JMineSweeper;
import edu.nju.network.client.ClientAdapter;
import edu.nju.network.client.ClientService;
import edu.nju.network.client.ClientServiceImpl;
public class GameControllerImpl implements GameControllerService{

	@Override
	public boolean handleLeftClick(int x, int y) {
		// TODO Auto-generated method stub
		MineOperation op = new LeftClickOperation(x,y);
		if(!JMineSweeper.isClient){
		   OperationQueue.addMineOperation(op);
		}else{
		   op.setAttribute(false);
	       ClientAdapter.write(op);
		}
		return true;
	}

	@Override
	public boolean handleRightClick(int x, int y) {
		// TODO Auto-generated method stub
		MineOperation op = new RightClickOperation(x,y);
		
		if(!JMineSweeper.isClient){
               
			   OperationQueue.addMineOperation(op);
			   
			}else{
				 op.setAttribute(false);
		         ClientAdapter.write(op);
			}
		return true;
	}

	@Override
	public boolean handleDoubleClick(int x, int y) {
		// TODO Auto-generated method stub
		MineOperation op = new DoubleClickOperation(x,y);
		if(!JMineSweeper.isClient){
			   OperationQueue.addMineOperation(op);
		}else{
			 op.setAttribute(false);
		     ClientAdapter.write(op);
		}
		return true;
	}

}
