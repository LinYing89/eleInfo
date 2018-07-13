package com.bairock.iot.eleInfo;

import java.util.ArrayList;
import java.util.List;

public class CreateManager {

	public static MsgManager mm;
	public static DevSwitch d1;
	
	public static MsgManager createMsgManager() {
		// 通信管理机
		MsgManager manager = new MsgManager();
		manager.setNum(4);
		List<Omnibus> list = createOmnibus();
		for (Omnibus o : list) {
			manager.addOmnibus(o);
		}
		return manager;
	}

	public static List<Omnibus> createOmnibus() {
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
		
		//总线2
		Omnibus bus2 = new Omnibus();
		bus2.setNum(2);
		bus2.addCollectorTerminal(createCollectorTerminal7c());
		list.add(bus2);
		return list;
	}

	public static CollectorTerminal createCollectorTerminal0() {
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
		setListener(a1);
		setListener(a2);
		setListener(a3);
		setListener(d1);
		setListener(d2);
		setListener(d3);
		ct.addDataAddress(da);
		return ct;
	}

	public static CollectorTerminal createCollectorTerminal2() {
		CollectorTerminal ct = new CollectorTerminal();
		ct.setNum(0x21);
		DataAddress da = createDataAddress(0);
		DevCollector c1 = new DevCollector("c1", "温度");
		ValueTrigger trigger = new ValueTrigger();
		trigger.setEnable(true);
		trigger.setTriggerValue(28);
		trigger.setCompareSymbol(CompareSymbol.GREAT);
		trigger.setTargetDevice(d1);
		trigger.setTargetValue(0);
		trigger.setOnTriggedChangedListener(new ValueTrigger.OnTriggedChangedListener(){

			@Override
			public void onTriggedChanged(ValueTrigger trigger, boolean trigged) {
				System.out.println("OnTriggedChangedListener " + trigger.getTriggerValue() + trigged);
			}
			
		});
		c1.addValueTrigger(trigger);
		ValueTrigger trigger2 = new ValueTrigger();
		trigger2.setEnable(true);
		trigger2.setTriggerValue(23);
		trigger2.setCompareSymbol(CompareSymbol.LESS);
		trigger2.setTargetDevice(d1);
		trigger2.setTargetValue(1);
		trigger2.setInfo("当温度低于23度时关闭风机");
		trigger2.setOnTriggedChangedListener(new ValueTrigger.OnTriggedChangedListener(){

			@Override
			public void onTriggedChanged(ValueTrigger trigger, boolean trigged) {
				System.out.println("OnTriggedChangedListener " + trigger.getTriggerValue() + trigged);
			}
			
		});
		c1.addValueTrigger(trigger2);
		da.addDevice(c1);
		setListener(c1);

		DevCollector c2 = new DevCollector("c2", "湿度");
		da.addDevice(c2);
		setListener(c2);
		ct.addDataAddress(da);
		return ct;
	}

	public static CollectorTerminal createCollectorTerminal7c() {
		CollectorTerminal ct = new CollectorTerminal();
		ct.setNum(0x7c);
		DataAddress da = createDataAddress(0);
		Electrical ele = new Electrical("ele", "");
		EleInfo ei = new EleInfo();
		ele.setEleInfo(ei);
		da.addDevice(ele);
		ct.addDataAddress(da);
		return ct;
	}
	
	public static DataAddress createDataAddress(int num) {
		DataAddress addr = new DataAddress();
		addr.setNum(num);
		return addr;
	}
	
	public static void setListener(Device dev) {
		dev.setOnValueChangedListener(new Device.OnValueChangedListener() {

			@Override
			public void onValueChanged(Device device, float value) {
				System.out.println("onValueChanged " + device.getCoding() + " value:" + value);
			}
		});
	}
}
