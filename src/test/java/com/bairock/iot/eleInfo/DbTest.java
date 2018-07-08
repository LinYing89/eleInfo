package com.bairock.iot.eleInfo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DbTest {

	private Device d1;
	
	public static void main(String[] args) {
		DbTest db = new DbTest();
		// 通信管理机
		MsgManager manager = db.createMsgManager();
		
		EntityManagerFactory em = Persistence.createEntityManagerFactory("intelDev");

		EntityManager eManager = em.createEntityManager();
		eManager.getTransaction().begin();

		eManager.persist(manager);
		eManager.getTransaction().commit();
		eManager.close();

//		EntityManager eManager1 = em.createEntityManager();
//		eManager1.getTransaction().begin();
//		eManager1.remove(eManager1.merge(user));
//		eManager1.getTransaction().commit();
//		eManager1.close();

		em.close();
	}

	public MsgManager createMsgManager() {
		// 通信管理机
		MsgManager manager = new MsgManager();
		manager.setNum(4);
		List<Omnibus> list = createOmnibus();
		for(Omnibus o : list) {
			manager.addOmnibus(o);
		}
		return manager;
	}

	public List<Omnibus> createOmnibus() {
		List<Omnibus> list = new ArrayList<>();
		// 总线0
		Omnibus bus0 = new Omnibus();
		bus0.setNum(0);
		bus0.addCollectorTerminal(createCollectorTerminal0());
		list.add(bus0);
		// 总线1
		Omnibus bus1 = new Omnibus();
		bus1.setNum(1);
		bus1.addCollectorTerminal(createCollectorTerminal2());
		list.add(bus1);
		return list;
	}
	
	public CollectorTerminal createCollectorTerminal0() {
		CollectorTerminal ct = new CollectorTerminal();
		ct.setNum(0);
		DataAddress da = createDataAddress(0x200);
		DevAlarm a1 = new DevAlarm("a1", "门禁");
		DevAlarm a2 = new DevAlarm("a2", "烟雾");
		DevAlarm a3 = new DevAlarm("a3", "水浸");
		d1 = new DevSwitch("d1", "风机");
		DevSwitch d2 = new DevSwitch("d2", "照明");
		DevSwitch d3 = new DevSwitch("d3", "空调");
		da.addDevice(a1);
		da.addDevice(a2);
		da.addDevice(a3);
		da.addDevice(d1);
		da.addDevice(d2);
		da.addDevice(d3);
		
		ct.addDataAddress(da);
		return ct;
	}
	
	public CollectorTerminal createCollectorTerminal2() {
		CollectorTerminal ct = new CollectorTerminal();
		ct.setNum(2);
		DataAddress da = createDataAddress(0x12);
		DevCollector c1 = new DevCollector("c1", "温度");
		ValueTrigger trigger = new ValueTrigger();
		trigger.setEnable(true);
		trigger.setTriggerValue(28);
		trigger.setCompareSymbol(CompareSymbol.GREAT);
		trigger.setTargetDevice(d1);
		trigger.setTargetValue(0);
		trigger.setInfo("当温度大于28度时打开风机");
		c1.addValueTrigger(trigger);
		ValueTrigger trigger2 = new ValueTrigger();
		trigger2.setEnable(true);
		trigger2.setTriggerValue(23);
		trigger2.setCompareSymbol(CompareSymbol.LESS);
		trigger2.setTargetDevice(d1);
		trigger2.setTargetValue(1);
		trigger2.setInfo("当温度低于23度时关闭风机");
		c1.addValueTrigger(trigger2);
		da.addDevice(c1);
		ct.addDataAddress(da);
		
		DataAddress da2 = createDataAddress(0x13);
		DevCollector c2 = new DevCollector("c2", "湿度");
		da2.addDevice(c2);
		ct.addDataAddress(da2);
		return ct;
	}
	public DataAddress createDataAddress(int num) {
		DataAddress addr = new DataAddress();
		addr.setNum(num);
		return addr;
	}
	
}
