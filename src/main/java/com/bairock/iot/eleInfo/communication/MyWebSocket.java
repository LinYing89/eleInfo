package com.bairock.iot.eleInfo.communication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;

import com.bairock.iot.eleInfo.CompareSymbol;
import com.bairock.iot.eleInfo.ValueTrigger;
import com.bairock.iot.eleInfo.dao.DeviceDao;
import com.bairock.iot.eleInfo.listener.StartUpListener;
import com.fasterxml.jackson.databind.ObjectMapper;

@ServerEndpoint(value = "/websocket")
public class MyWebSocket {

	private Logger logger = Logger.getLogger(this.getClass().getName()); 
	
	private Session session;

	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		this.session = session;
		MyWebSocketHelper.addGroupWebSocket(this);
	}

	@OnClose
	public void onClose() {
		closeWebSocket();
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		analysisMessage(message);
	}

	@OnError
	public void onError(Session session, Throwable thr) {
		System.out.println(thr.getMessage());
		closeWebSocket();
	}

	private void closeWebSocket() {
		MyWebSocketHelper.removeGroupWebSocket(this);
		session = null;
	}

	public void sendMessage(String message) {
		if (null != session) {
			try {
				Logger logger = Logger.getLogger(this.getClass().getName());
				logger.info(message);
				session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void analysisMessage(String msg) {
		if(msg.equals("H")) {
			sendMessage("H");
//			StartUpListener.refreshEleInfo();
			StartUpListener.testEleInfo();
		}else if (msg.equals("rf")) {
			refreshValues();
			return;
		}else if(msg.startsWith("{")) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				Map<?, ?> map = mapper.readValue(msg, Map.class);
				float great = Float.parseFloat(map.get("great").toString());
				float less = Float.parseFloat(map.get("less").toString());
				for(ValueTrigger t : StartUpListener.c1.getListValueTrigger()) {
					if(t.getCompareSymbol() == CompareSymbol.GREAT) {
						t.setTriggerValue(great);
					}else {
						t.setTriggerValue(less);
					}
				}
				new DeviceDao().update(StartUpListener.c1);
				Map<String, Object> map1 = new HashMap<>();
				//2设置成功
				map1.put("id", 2);
				StartUpListener.sendMap(map1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		byte[] by = null;
		//最终发出去的数组
		byte[] byMsg = null;
		byte action;
		switch (msg) {
		case "d1":
			action = StartUpListener.getAction(StartUpListener.d1);
			by = new byte[] { 00, 07, 00, 00, 00, 04, 00, 00, 05, 00, 05, action, 00};
			break;
		case "d2":
			action = StartUpListener.getAction(StartUpListener.d2);
			by = new byte[] { 00, 07, 00, 00, 00, 04, 00, 00, 05, 00, 06, action, 00};
			break;
		case "d3":
			action = StartUpListener.getAction(StartUpListener.d3);
			by = new byte[] { 00, 07, 00, 00, 00, 04, 00, 00, 05, 00, 07, action, 00};
			break;
		}
		byMsg = StartUpListener.createByteMsg(by);
		if (null != byMsg) {
			logger.info(ServerHandler.bytesToHexString(byMsg));
			ServerHandler.send(byMsg);
		}
	}

	private void refreshValues() {
		StartUpListener.refreshValues();
	}

}
