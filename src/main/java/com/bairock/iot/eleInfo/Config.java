package com.bairock.iot.eleInfo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Config {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private int devicePort = 20000;
	private String serverIp = "127.0.0.1";
	private int webSocketPort = 80;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getDevicePort() {
		return devicePort;
	}
	public void setDevicePort(int devicePort) {
		this.devicePort = devicePort;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public int getWebSocketPort() {
		return webSocketPort;
	}
	public void setWebSocketPort(int webSocketPort) {
		this.webSocketPort = webSocketPort;
	}
	
}
