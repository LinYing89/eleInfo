package com.bairock.iot.eleInfo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.bairock.iot.eleInfo.MsgManager;
import com.bairock.iot.eleInfo.listener.StartUpListener;

public class MsgManagerDao {

	private Logger logger = Logger.getLogger(this.getClass().getName()); 
	
	/**
	 * 获取所有通信管理机
	 * @return
	 */
	public List<MsgManager> findAll() {
		EntityManager eManager = StartUpListener.eManager;
		List<MsgManager> listUser = null;
		try {
			eManager.getTransaction().begin();

			listUser = (List<MsgManager>) eManager
					.createQuery("from MsgManager", MsgManager.class).getResultList();
			eManager.getTransaction().commit();
		} catch (Exception e) {
			eManager.getTransaction().rollback();
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return listUser;
	}
}
