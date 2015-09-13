package edu.nju.controller.msgqueue.operation;

import edu.nju.controller.msgqueue.OperationQueue;
import edu.nju.model.service.GameModelService;

public class StartCustomGameOperation extends MineOperation {
	int height;
	int width;
	int MineNum;
    public StartCustomGameOperation(int a,int b ,int c) {
		// TODO Auto-generated constructor stub
    	this.height=a;
    	this.width=b;
    	this.MineNum=c;
	}
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		GameModelService game = OperationQueue.getGameModel();
		game.setGameLevel("新自定义");
		game.setGameSize(height	,width, MineNum);
		game.startGame();
	}

}
