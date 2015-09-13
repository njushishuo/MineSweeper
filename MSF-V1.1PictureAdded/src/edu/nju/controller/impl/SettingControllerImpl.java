package edu.nju.controller.impl;

import edu.nju.controller.msgqueue.OperationQueue;
import edu.nju.controller.msgqueue.operation.MineOperation;
import edu.nju.controller.msgqueue.operation.StartCustomGameOperation;
import edu.nju.controller.msgqueue.operation.StartEasyGameOperation;
import edu.nju.controller.msgqueue.operation.StartGameOperation;
import edu.nju.controller.msgqueue.operation.StartHardGameOperation;
import edu.nju.controller.msgqueue.operation.StartHellGameOperation;
import edu.nju.controller.service.SettingControllerService;
import edu.nju.main.JMineSweeper;
import edu.nju.model.service.GameModelService;
import edu.nju.network.client.ClientAdapter;

public class SettingControllerImpl implements SettingControllerService{

	@Override
	public boolean setEasyGameLevel() {
		// TODO Auto-generated method stub
		
		if(!JMineSweeper.isClient){
		    OperationQueue.addMineOperation(new StartEasyGameOperation());
		}else{
			ClientAdapter.write(new StartEasyGameOperation());
		}
		return true;
	}

	@Override
	public boolean setHardGameLevel() {
		// TODO Auto-generated method stub
		if(!JMineSweeper.isClient){
		    OperationQueue.addMineOperation(new StartHardGameOperation());
		}else{
			ClientAdapter.write(new StartHardGameOperation());
		}
		return true;
	}

	@Override
	public boolean setHellGameLevel() {
		// TODO Auto-generated method stub
		
		if(!JMineSweeper.isClient){
		    OperationQueue.addMineOperation(new StartHellGameOperation());
		}else{
			ClientAdapter.write(new StartHellGameOperation());
		}
		return true;
	}

	@Override
	public boolean setCustomizedGameLevel(int height, int width, int nums) {
		// TODO Auto-generated method stub
		
//		GameModelService game = OperationQueue.getGameModel();
//		game.setGameLevel("新自定义");
//		game.setGameSize(height	,width, nums);
		if(!JMineSweeper.isClient){
		    OperationQueue.addMineOperation(new StartCustomGameOperation(height,width,nums));
		}else{
			ClientAdapter.write(new StartCustomGameOperation(height,width,nums));
		}
		
		
		return true;
	}

}
