package edu.nju.model.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.nju.main.JMineSweeper;
import edu.nju.model.service.ChessBoardModelService;
import edu.nju.model.service.GameModelService;
import edu.nju.model.service.StatisticModelService;
import edu.nju.model.state.GameResultState;
import edu.nju.model.state.GameState;
import edu.nju.model.vo.GameVO;

public class GameModelImpl extends BaseModel implements GameModelService{
	
	private StatisticModelService statisticModel;
	private ChessBoardModelService chessBoardModel;
	
	private List<GameLevel> levelList;
	
	private GameState gameState;
	private int width;
	private int height;
	private int mineNum;
	private String level;
	private GameLevel gl = null;
	private GameResultState gameResultState;
	public static int time=0;	
	private long startTime;
    public static int currentLevel = 0;
	public GameModelImpl(StatisticModelService statisticModel, ChessBoardModelService chessBoardModel){
		this.statisticModel = statisticModel;
		this.chessBoardModel = chessBoardModel;
		gameState = GameState.OVER;
		
		
		chessBoardModel.setGameModel(this);
		
		levelList = new ArrayList<GameLevel>();
		levelList.add(new GameLevel(2,"大",30,16,99));
		levelList.add(new GameLevel(1,"中",16,16,40));
		levelList.add(new GameLevel(0,"小",9,9,10));
		levelList.add(new GameLevel(3,"新自定义",9,9,10));
		levelList.add(new GameLevel(3,"自定义",9,9,10));
	}

	@Override
	public boolean startGame() {
		// TODO Auto-generated method stub
		gameState = GameState.RUN;
		startTime = Calendar.getInstance().getTimeInMillis();
		
		JMineSweeper.ClientFlag=0;
		JMineSweeper.HostFlag=0;
		
		for(GameLevel tempLevel : levelList){
			if(tempLevel.getName().equals(level)){
				gl = tempLevel;
				currentLevel=tempLevel.getLevel();
				break;
			}
		}
		if(gl == null&&width==0&&height == 0)
			gl = levelList.get(2);
		
		if(gl != null ){
			height = gl.getWidth();
			width = gl.getHeight();
			mineNum = gl.getMineNum();
		}
		
		if(gl.getName()!="新自定义"){
		   this.chessBoardModel.initialize(width, height, mineNum);
		   super.updateChange(new UpdateMessage("start",this.convertToDisplayGame()));
		}
		return true;
	}
	
	@Override
	public boolean gameOver(GameResultState result) {
		// TODO Auto-generated method stub
		
		this.gameState = GameState.OVER;
		this.gameResultState = result;
		
		time = (int)(Calendar.getInstance().getTimeInMillis() - startTime)/1000;
		
		this.statisticModel.recordStatistic(result, time);
		this.statisticModel.showStatistics();
		super.updateChange(new UpdateMessage("end",this.convertToDisplayGame()));
		
		return  true;
	}

	@Override
	public boolean setGameLevel(String level) {
		// TODO Auto-generated method stub
		//输入校验
		this.level = level;
		return true;
	}

	@Override
	public boolean setGameSize(int width, int height, int mineNum) {
		// TODO Auto-generated method stub
		//输入校验
		this.width = width;
		this.height = height;
		this.mineNum = mineNum;
		gl = new GameLevel(3,"自定义", width, height, mineNum);
		levelList.set(4,gl);
		level="自定义";
		this.chessBoardModel.initialize(width, height, mineNum);
		super.updateChange(new UpdateMessage("start",this.convertToDisplayGame()));
		return true;
	}
	
	private GameVO convertToDisplayGame(){
		return new GameVO(gameState, width, height,level, gameResultState, time);
	}
	
	
	public int getCurrentGameLevel (){
	   return 	gl.getLevel();
	}

	@Override
	public List<GameLevel> getGameLevel() {
		// TODO Auto-generated method stub
		return this.levelList;
	}
}