package com.bairock.iot.eleInfo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DbTest {
	
	public static void main(String[] args) {
		// 通信管理机
		MsgManager manager = CreateManager.createMsgManager();
		
		EntityManagerFactory em = Persistence.createEntityManagerFactory("eleinfo");

		EntityManager eManager = em.createEntityManager();
		eManager.getTransaction().begin();

		eManager.persist(manager);
		eManager.getTransaction().commit();
		//eManager.close();

//		EntityManager eManager1 = em.createEntityManager();
//		eManager1.getTransaction().begin();
//		eManager1.remove(eManager1.merge(user));
//		eManager1.getTransaction().commit();
//		eManager1.close();

		Config config = new Config();
		config.setDevicePort(20000);
		config.setServerIp("192.168.1.116");
		
		eManager.getTransaction().begin();
		eManager.persist(config);
		eManager.getTransaction().commit();
		eManager.close();
		
		em.close();
	}
}
