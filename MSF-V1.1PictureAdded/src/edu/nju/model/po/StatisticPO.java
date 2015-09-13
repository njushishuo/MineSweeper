package edu.nju.model.po;

import com.sun.org.apache.regexp.internal.REUtil;

public class StatisticPO {

	private double winrate;
	private int wins;
	private int sum;
	private int level;
	private int width;
	private int height;
	private int time;
	
	public StatisticPO() {
		this(0,0,0,0,9,9);
	}

	public StatisticPO(double winrate, int wins, int sum, int level, int width,
			int height) {
		super();
		this.winrate = winrate;
		this.wins = wins;
		this.sum = sum;
		this.level = level;
		this.width = width;
		this.height = height;
	}
	
	public int getTime (){
		return time;
	}
	
	public void setTime(int time){
		this.time = time ;
	}

	public double getWinrate() {
		return winrate=wins/sum;
	}

	public void setWinrate(double winrate) {
		this.winrate = winrate;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}
	
	public void addWins(){
		this.wins++;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}
	
	public void addSum(){
		this.sum++;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
}
