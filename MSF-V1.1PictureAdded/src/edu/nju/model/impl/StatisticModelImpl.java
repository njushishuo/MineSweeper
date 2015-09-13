package edu.nju.model.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.processing.Filer;

import edu.nju.controller.msgqueue.OperationQueue;
import edu.nju.model.data.StatisticData;
import edu.nju.model.po.StatisticPO;
import edu.nju.model.service.GameModelService;
import edu.nju.model.service.StatisticModelService;
import edu.nju.model.state.GameResultState;

public class StatisticModelImpl extends BaseModel implements StatisticModelService{
	
	private StatisticData statisticData;
	private static  StatisticPO spo;
	
	private static  int[] sums={0,0,0,0};
	private static  int[] wins ={0,0,0,0};
	
	String s = "";
	double [] rates = new double [4];
	int [] shortestTimes = {999,999,999,999};

	public StatisticModelImpl(){
		//初始化Data
		statisticData = new StatisticData();
		spo = new StatisticPO();
	}

	@Override
	public void recordStatistic(GameResultState result, int time) {
		// TODO Auto-generated method stub
		int level = GameModelImpl.currentLevel;
		spo.addSum();
		this.sums[level]++;
		if(result==GameResultState.SUCCESS){
			spo.addWins();
			this.wins[level]++;
		}
		
		System.out.println("wins : "+wins[level]);
		System.out.println("sums : "+sums[level]);
		spo.setTime(time);
		spo.setLevel(level);
		statisticData.saveStatistic(spo);
		try {
			FileWriter fw = new FileWriter("save.dat");
			BufferedWriter bw = new BufferedWriter(fw);
			if(result==GameResultState.SUCCESS){
			   bw.write(spo.getWins()+" "+spo.getSum()+" "+spo.getTime()+" "+spo.getLevel()+" "+"1");
			}
			else {
			   bw.write(spo.getWins()+" "+spo.getSum()+" "+spo.getTime()+" "+spo.getLevel()+" "+"0");
			}
			bw.close();	
		}catch (IOException e){
			e.printStackTrace();	
		}
	}

	@Override
	public void showStatistics() {
		// TODO Auto-generated method stub
//		String s = "";
//		double [] rates = new double [4];
//		int [] shortestTimes = {999,999,999,999};
		
		
		try{
			FileReader fr = new FileReader("save.dat");
			BufferedReader br = new BufferedReader(fr);
			while(( s =  br.readLine())!=null){
				String[] tempString  = s.split(" ");
				int tempWins=Integer.parseInt(tempString[0]);
				int tempSum = Integer.parseInt(tempString[1]);
				int tempTime = Integer.parseInt(tempString[2]);
				int tempLevel = Integer.parseInt(tempString[3]);
				for(int i=0;i<4;i++){
					if(i==tempLevel){
						
					   rates[i]=((double)wins[i]/(double)sums[i])*100;
					   java.text.DecimalFormat df =new java.text.DecimalFormat("#.00");  
					   rates[i]=Double.parseDouble(df.format(rates[i]));
					    //System.out.println("rate : "+rates[i]);
					   
					   if(tempTime<shortestTimes[i]&tempString[4].equals("1")){
						   shortestTimes[i]=tempTime;
						   System.out.println(shortestTimes[i]);
					   }
					   break;
					}
				}
			   
			};
		}catch (IOException e){
			e.printStackTrace();
		}
		
		ArrayList ratesAndTimes = new ArrayList();
		ratesAndTimes.add(rates);
		ratesAndTimes.add(shortestTimes);
	
//		for (int j = 0 ;j<4 ;j++){
//			System.out.println("rate :"+rates[j]);
//			System.out.println("time :"+shortestTimes[j]);
//		}
		super.updateChange(new UpdateMessage("statistic", ratesAndTimes));
	}

}
