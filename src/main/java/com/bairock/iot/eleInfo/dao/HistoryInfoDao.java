package com.bairock.iot.eleInfo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.bairock.iot.eleInfo.HistoryInfo;
import com.bairock.iot.eleInfo.listener.StartUpListener;

public class HistoryInfoDao {

	private Logger logger = Logger.getLogger(this.getClass().getName()); 
	
	public void add(HistoryInfo hi) {
		EntityManager eManager = StartUpListener.eManager;
		try {
			eManager.getTransaction().begin();
			eManager.persist(hi);
			eManager.getTransaction().commit();
		}catch (Exception e) {
			eManager.getTransaction().rollback();
			e.printStackTrace();
			logger.error(e.getMessage() + "说明:" + hi.getDevice().getCoding());
		}
	}
	
	public List<HistoryInfo> findAll() {
		List<HistoryInfo> listUser = new ArrayList<>();
		EntityManager eManager = StartUpListener.eManager;
		try {
			eManager.getTransaction().begin();
			listUser = (List<HistoryInfo>) eManager
					.createQuery("from HistoryInfo", HistoryInfo.class).getResultList();
			eManager.getTransaction().commit();
		}catch (Exception e) {
			eManager.getTransaction().rollback();
			e.printStackTrace();
			logger.error(e.getMessage() + "说明:查找历史记录");
		}
		return listUser;
	}
}
