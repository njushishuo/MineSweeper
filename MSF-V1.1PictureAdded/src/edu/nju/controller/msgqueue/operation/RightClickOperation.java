package edu.nju.controller.msgqueue.operation;

import java.io.Serializable;

import edu.nju.controller.msgqueue.OperationQueue;
import edu.nju.main.JMineSweeper;
import edu.nju.model.service.ChessBoardModelService;

public class RightClickOperation extends MineOperation {
	private int x;
	private int y;
	
	public  RightClickOperation(int x,int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		ChessBoardModelService chess = OperationQueue.getChessBoardModel();
		//主机或者单机
		if(this.getAttribute()){
		   chess.mark(x, y);
		}else{
			//客户
			chess.markClient(x, y);
		}
	}
	
	

}
