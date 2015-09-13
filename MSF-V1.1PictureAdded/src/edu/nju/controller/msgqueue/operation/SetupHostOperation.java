package edu.nju.controller.msgqueue.operation;

import java.io.Serializable;

import edu.nju.controller.msgqueue.OperationQueue;
import edu.nju.main.JMineSweeper;
import edu.nju.model.impl.ChessBoardModelImpl;
import edu.nju.model.impl.GameModelImpl;
import edu.nju.model.impl.ParameterModelImpl;
import edu.nju.model.impl.StatisticModelImpl;
import edu.nju.network.host.HostInHandlerImpl;
import edu.nju.network.host.HostServiceImpl;
import edu.nju.view.MainFrame;

public class SetupHostOperation extends MineOperation {
	HostServiceImpl hostS = new HostServiceImpl();
	HostInHandlerImpl hostH = new HostInHandlerImpl();
	

	
	//GameModelImpl game = new GameModelImpl(new StatisticModelImpl(),new ChessBoardModelImpl(new ParameterModelImpl()));
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
	    JMineSweeper.isHost=true;
	    
		JMineSweeper.operationQueue.addObserver(hostS);
		
		hostS.init(hostH);
	
	//	game.addObserver(hostS);
		
	}
      
}
