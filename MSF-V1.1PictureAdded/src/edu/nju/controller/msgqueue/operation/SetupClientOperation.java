package edu.nju.controller.msgqueue.operation;

import java.io.Serializable;

import edu.nju.controller.msgqueue.OperationQueue;
import edu.nju.main.JMineSweeper;
import edu.nju.model.impl.ChessBoardModelImpl;
import edu.nju.model.impl.GameModelImpl;
import edu.nju.model.impl.ParameterModelImpl;
import edu.nju.model.impl.StatisticModelImpl;
import edu.nju.network.client.ClientAdapter;
import edu.nju.network.client.ClientInHandler;
import edu.nju.network.client.ClientInHandlerImpl;
import edu.nju.network.client.ClientServiceImpl;
import edu.nju.network.modelProxy.GameModelProxy;
import edu.nju.view.MainFrame;

public class SetupClientOperation extends MineOperation {
	ClientServiceImpl clientS = new ClientServiceImpl();
	ClientInHandlerImpl clientH = new ClientInHandlerImpl();
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		JMineSweeper.isClient=true;
		
		clientH.addObserver(JMineSweeper.operationQueue);
		clientS.init("127.0.0.1", clientH);
	}
	
}
