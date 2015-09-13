package edu.nju.model.data;

import edu.nju.model.po.StatisticPO;

/**
 * 负责进行统计数据获取和记录操作
 * @author Wangy
 *
 */
public class StatisticData {
	private StatisticPO spo = new StatisticPO() ;
	
	
	public StatisticPO getStatistic(){
		return spo;
	}
	
	public boolean saveStatistic(StatisticPO statistic){
		spo=statistic;
		return true;
	}

}