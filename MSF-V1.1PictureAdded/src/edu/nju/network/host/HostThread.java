package edu.nju.network.host;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

import edu.nju.controller.msgqueue.OperationQueue;
import edu.nju.controller.msgqueue.operation.LeftClickOperation;
import edu.nju.controller.msgqueue.operation.MineOperation;
import edu.nju.controller.msgqueue.operation.SetupHostOperation;
import edu.nju.model.impl.ChessBoardModelImpl;
import edu.nju.model.po.BlockPO;
import edu.nju.network.Configure;

public class HostThread extends Thread {
	private ServerSocket server;
	private Socket client;
	private ObjectInputStream reader;
	private ObjectOutputStream out;
	
	public HostThread() throws IOException{
		super();
		server = new ServerSocket(Configure.PORT);
		System.out.println("Waiting for Client!!!");
		client = server.accept();
		System.out.println("Client connecting!!!");
		//从客户端读取
		reader = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
		//向客户端写
		out = new ObjectOutputStream(client.getOutputStream());
	}
	
	//read data
	@Override
	public void run(){
		    while(!this.isInterrupted()){
			
			//read from socket
			System.out.println("running------");			
			
			try {
				Object obj = reader.readObject();
				
			      if(obj != null){
					    System.out.println("Got it!!!");
					    ServerAdapter.readData(obj);
				  }
				
			} catch(SocketException se){
				System.out.println("socket connection is closed!!!");
				this.close();
				break;
			}catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			
		}
	}
	
	
	public void close(){
		
		try {
			reader.close();
			out.close();
			client.close();
			server.close();
			this.interrupt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Object write(Object o) {
		try {
			out.writeObject(o);
			System.out.println("成功向客户发送对象！");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}

}
