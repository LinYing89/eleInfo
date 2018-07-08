package com.bairock.iot.eleInfo.communication;

import java.util.ArrayList;
import java.util.List;

import com.bairock.iot.eleInfo.Device;

public class MessageAnalysiser {

	private StringBuilder sb = new StringBuilder();

	/**
	 * return the first device
	 * 
	 * @param msg
	 */
	public List<Device> putMsg(String msg) {
		List<Device> listDev = new ArrayList<>();
		if (null != msg) {
			sb.append(msg);
			if (judgeMsgFormate(sb.toString())) {
				analysisReceiveMessage(sb.toString());
				sb.setLength(0);
			}
		}
		return listDev;
	}

	/**
	 * judge the format of receive message, if the format is error, return false,
	 * else return true, you can over write this method if you need.
	 * 
	 * @param msg
	 *            receive message
	 * @return
	 */
	public boolean judgeMsgFormate(String msg) {
		boolean formatOk = false;
		int len = msg.length();
		if (len < 3 || !(msg.substring(len - 3, len - 2)).equals("#")) {
			formatOk = false;
		} else {
			formatOk = true;
		}
		return formatOk;
	}

	/**
	 * analysis receive message, you can to over write this method
	 * 
	 * @param msg
	 */
	public void analysisReceiveMessage(String msg) {
	}
}
