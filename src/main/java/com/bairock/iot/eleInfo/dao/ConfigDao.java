package com.bairock.iot.eleInfo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.bairock.iot.eleInfo.Config;
import com.bairock.iot.eleInfo.listener.StartUpListener;

public class ConfigDao {

	public Config find() {
		Config config = null;
		EntityManager eManager = StartUpListener.eManager;
		try {
			eManager.getTransaction().begin();
			List<Config> list = (List<Config>) eManager
					.createQuery("from Config", Config.class).getResultList();
			if(list.size() > 0) {
				config = list.get(0);
			}
			eManager.getTransaction().commit();
		}catch (Exception e) {
			eManager.getTransaction().rollback();
			e.printStackTrace();
		}
		return config;
	}
}
