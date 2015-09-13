package edu.nju.controller.impl;

import edu.nju.controller.msgqueue.OperationQueue;
import edu.nju.controller.msgqueue.operation.MineOperation;
import edu.nju.controller.msgqueue.operation.SetupClientOperation;
import edu.nju.controller.msgqueue.operation.SetupHostOperation;
import edu.nju.controller.service.HostControllerService;

public class HostControllerImpl implements HostControllerService{
	@Override
	public boolean serviceetupHost(){
		// TODO Auto-generated method stub
		MineOperation op = new SetupHostOperation();
		OperationQueue.addMineOperation(op);
		return true;
	}
}
