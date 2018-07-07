package com.bairock.iot.eleInfo.communication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;

import com.bairock.iot.eleInfo.Device;
import com.bairock.iot.eleInfo.ServerHandler;
import com.bairock.iot.eleInfo.listener.StartUpListener;

@ServerEndpoint(value = "/websocket")
public class MyWebSocket {

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
		if (msg.equals("rf")) {
			refreshValues();
			return;
		}
		byte[] by = null;
		//最终发出去的数组
		byte[] byMsg = null;
		byte action = getAction(StartUpListener.d1);
		switch (msg) {
		case "d1":
			by = new byte[] { 00, 00, 00, 00, 00, 04, 00, 00, 05, 00, 04, action, 00};
			break;
		case "d2":
			action = getAction(StartUpListener.d2);
			by = new byte[] { 00, 00, 00, 00, 00, 04, 00, 00, 05, 00, 05, action, 00};
			break;
		case "d3":
			action = getAction(StartUpListener.d3);
			by = new byte[] { 00, 00, 00, 00, 00, 04, 00, 00, 05, 00, 06, action, 00};
			break;
		}
		byMsg = createByteMsg(by);
		if (null != byMsg) {
			ServerHandler.send(byMsg);
		}
	}

	private byte[] createByteMsg(byte[] b1) {
		byte[] byVerify = getVerify(b1);
		return unitArray(b1, byVerify);
	}
	
	private byte[] unitArray(byte[] b1, byte[] b2) {
		byte[] byMsg = new byte[b1.length + b2.length];
		System.arraycopy(b1, 0, byMsg, 0, b1.length);
		System.arraycopy(b2, 0, byMsg, b1.length, b2.length);
		return byMsg;
	}
	
	private byte getAction(Device device) {
		byte action;
		if (device.getValue() == 0) {
			action = 0;
		} else {
			action = (byte) 0xff;
		}
		return action;
	}

	public static byte[] getVerify(byte[] by) {
		byte[] bysum = new byte[2];
		int chksum = 0;
		for (int i = 0; i < by.length; i++) {
			chksum += by[i];
		}
		bysum[1] = (byte) (chksum & 0xFF);
		bysum[0] = (byte) (chksum >> 8 & 0xFF);
		return bysum;
	}

	private void refreshValues() {
		StartUpListener.refreshValues();
	}

}
