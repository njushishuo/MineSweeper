package edu.nju.controller.msgqueue.operation;

import java.io.Serializable;

public abstract class MineOperation implements Serializable {
	private boolean isHost=true;
	public abstract void execute();
	public void setAttribute(boolean is){
		isHost=is;
	}
	
	public boolean getAttribute(){
		return this.isHost;
	}
}
