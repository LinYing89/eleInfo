package com.bairock.iot.eleInfo.communication;

import java.util.ArrayList;
import java.util.List;

public class MyWebSocketHelper {
	
private static List<MyWebSocket> listGroupWebSocket = new ArrayList<>();
	
	public static void addGroupWebSocket(MyWebSocket groupWebSocket) {
		if(!listGroupWebSocket.contains(groupWebSocket)) {
			listGroupWebSocket.add(groupWebSocket);
		}
	}
	
	public static void removeGroupWebSocket(MyWebSocket groupWebSocket) {
		listGroupWebSocket.remove(groupWebSocket);
	}
	
	public static List<MyWebSocket> getMyListGroupWebSocket(){
		return listGroupWebSocket;
	}
	
	public static void sendGroupMessage(String msg, MyWebSocket groupWebSocket) {
		if (null != groupWebSocket) {
			groupWebSocket.sendMessage(msg);
		}
	}
	
	public static void sendGroupMessage(String msg) {
		for(MyWebSocket s : listGroupWebSocket) {
			s.sendMessage(msg);
		}
	}
}
