package edu.nju.controller.msgqueue.operation;

import java.io.Serializable;

import edu.nju.controller.msgqueue.OperationQueue;
import edu.nju.main.JMineSweeper;
import edu.nju.model.service.GameModelService;

public class StartGameOperation extends MineOperation {

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		GameModelService game = OperationQueue.getGameModel();
		
		game.startGame();
	}

}
