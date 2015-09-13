package edu.nju.controller.msgqueue.operation;

import java.io.Serializable;

import edu.nju.controller.msgqueue.OperationQueue;
import edu.nju.main.JMineSweeper;
import edu.nju.model.service.ChessBoardModelService;

public class DoubleClickOperation extends MineOperation {
	private int x;
	private int y;
	public DoubleClickOperation(int x ,int y){
		this.x = x;
		this.y = y;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		ChessBoardModelService chess = OperationQueue.getChessBoardModel();
		//主机或者单机
				if(this.getAttribute()){
					   chess.quickExcavate(x, y);
				}else{
							//客户
					   chess.quickExcavateClient(x, y);
				}
	}

}