/*
 *
 TODO to start to program. this program are wrote base on MVC structure
 */
package edu.nju.main;

 
import edu.nju.controller.impl.MenuControllerImpl;
import edu.nju.controller.msgqueue.OperationQueue;
import edu.nju.controller.service.MenuControllerService;
import edu.nju.model.impl.ChessBoardModelImpl;
import edu.nju.model.impl.GameModelImpl;
import edu.nju.model.impl.ParameterModelImpl;
import edu.nju.model.impl.StatisticModelImpl;
import edu.nju.view.MainFrame;

public class JMineSweeper {

	public static MenuControllerService menuController = new MenuControllerImpl();
	public static StatisticModelImpl statisticModel ;
	public static ParameterModelImpl mineNumberModel;
	public static ChessBoardModelImpl mineBoardModel;
	public static GameModelImpl gameModel ;	
	public static OperationQueue operationQueue;
	public static boolean isHost;
	public static boolean isClient;
	public static int HostFlag=0;
	public static int ClientFlag=0;
	public static void main(String[] args) {
		isHost=false;
		isClient=false;
		HostFlag=0;
		ClientFlag=0;
		MainFrame ui = new MainFrame();
		 statisticModel = new StatisticModelImpl();
 	     mineNumberModel = new ParameterModelImpl();
 		 mineBoardModel = new ChessBoardModelImpl(mineNumberModel);
		 gameModel = new GameModelImpl(statisticModel,mineBoardModel);		
 		
		gameModel.addObserver(ui);
		statisticModel.addObserver(ui);
 		mineNumberModel.addObserver(ui.getMineNumberLabel());
 		mineBoardModel.addObserver(ui.getMineBoard());
 		mineBoardModel.addObserver(ui);
 		
 		 operationQueue = new OperationQueue(mineBoardModel, gameModel);
 		Thread operationThread = new Thread(operationQueue);
 		operationThread.start();
 	    try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
 		menuController.startGame();
 		
	}
}

