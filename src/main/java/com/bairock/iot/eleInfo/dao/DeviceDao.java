package com.bairock.iot.eleInfo.dao;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.bairock.iot.eleInfo.Device;
import com.bairock.iot.eleInfo.listener.StartUpListener;

public class DeviceDao {
	
	private Logger logger = Logger.getLogger(this.getClass().getName()); 
	
	public void update(Device device) {
		EntityManager eManager = StartUpListener.eManager;
		try {
			eManager.getTransaction().begin();
			eManager.merge(device);
			eManager.getTransaction().commit();
		}catch (Exception e) {
			eManager.getTransaction().rollback();
			e.printStackTrace();
			logger.error(e.getMessage() + "说明:" + device.getCoding());
		}
	}
}
