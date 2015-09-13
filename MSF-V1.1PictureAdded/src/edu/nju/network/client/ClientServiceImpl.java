package edu.nju.network.client;

import java.util.Observable;

import edu.nju.controller.msgqueue.operation.MineOperation;
import edu.nju.model.impl.UpdateMessage;
import edu.nju.network.TransformObject;

public class ClientServiceImpl extends ClientService{

	@Override
	public void submitOperation(MineOperation op) {
		// TODO Auto-generated method stub
		ClientAdapter.write(op);
	}
	
}
