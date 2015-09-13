package edu.nju.network.host;


import java.io.IOException;

  
public class ServerAdapter {
	protected static HostThread socket;
	protected static HostInHandler handler;
	 
	static boolean init(HostInHandler h){
	    try {
			
			socket = new HostThread();
			handler = h;
			//主机线程开始！
			socket.start();
			return true;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e1){
			e1.printStackTrace();
		}
		
		return false;
	}
//	public static void write(String str){
//		socket.write(str);
//	}
	
	
	//向客户写
	public static void write(Object o){
		socket.write(o);
	}
	
	//from read
	
	//应该叫处理客户输入吧。。
	public static void readData(Object data){
		handler.inputHandle(data);
	}
	
	public static void close(){
		socket.close();
	}
	
	
}
