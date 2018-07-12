package com.bairock.iot.eleInfo.communication;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bairock.iot.eleInfo.AlarmInfo;
import com.bairock.iot.eleInfo.DevAlarm;
import com.bairock.iot.eleInfo.Device;
import com.bairock.iot.eleInfo.Device.OnValueChangedListener;
import com.bairock.iot.eleInfo.HistoryInfo;
import com.bairock.iot.eleInfo.dao.DeviceDao;
import com.bairock.iot.eleInfo.dao.HistoryInfoDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyOnValueChangedListener implements OnValueChangedListener {

	private Logger logger = Logger.getLogger(this.getClass().getName()); 
	
	@Override
	public void onValueChanged(Device device, float value) {
		logger.info("值改变:" + device.getCoding() + " value:" + value);
		
		//添加历史记录
		HistoryInfo hi = new HistoryInfo(new Date(), device, value);
		new HistoryInfoDao().add(hi);
		
		Map<String, Object> map = new HashMap<>();
		map.put("id", 0);
		map.put("coding", device.getCoding());
		map.put("value", value);
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(map);
			if (null != json) {
				MyWebSocketHelper.sendGroupMessage(json);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		if(device instanceof DevAlarm) {
			AlarmInfo ai = new AlarmInfo();
			ai.setAlarmTime(new Date());
			if(value == 0) {
				ai.setInfo(device.getName() + " 报警");
			}else {
				ai.setInfo(device.getName() + " 报警解除");
			}
			device.addAlarmInfo(ai);
			Collections.sort(device.getListAlarmInfo());
			new DeviceDao().update(device);
		}
	}

}
