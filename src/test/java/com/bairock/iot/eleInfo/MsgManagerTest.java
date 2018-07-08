package com.bairock.iot.eleInfo;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MsgManagerTest {

	MsgManager mm;
	DevSwitch d1;

	@Before
	public void setUp() throws Exception {
		mm = createMsgManager();
	}

	@Test
	public void test() {
		DataAddress da = mm.findOmnibus(0).findCollectorTerminal(0).findDataAddress(0x200);
		Device a1 = da.findDevice("a1");
		Device a2 = da.findDevice("a2");
		Device a3 = da.findDevice("a3");
		Device d1 = da.findDevice("d1");
		Device d2 = da.findDevice("d2");
		Device d3 = da.findDevice("d3");
		// 00100101
		byte[] msg = new byte[] { 0, 0xa, 0, 0, 0, 4, 0, 0, 0x2, 0x00, 0, 2, 0, 0x25, 0, 0 };
		byte[] content = Arrays.copyOfRange(msg, 6, msg.length);
		mm.handler(content);
		assertEquals(1f, a1.getValue(), 0.01);
		assertEquals(0f, a3.getValue(), 0.01);
		assertEquals(1f, a2.getValue(), 0.01);
		assertEquals(0f, d1.getValue(), 0.01);
		assertEquals(1f, d2.getValue(), 0.01);
		assertEquals(0f, d3.getValue(), 0.01);

		// 01011010
		byte[] msg2 = new byte[] { 0, 0xa, 0, 0, 0, 4, 0, 0, 0x2, 0x00, 0, 2, 0, 0x5a, 0, 0 };
		byte[] content2 = Arrays.copyOfRange(msg2, 6, msg2.length);
		mm.handler(content2);
		assertEquals(0f, a1.getValue(), 0.01);
		assertEquals(1f, a3.getValue(), 0.01);
		assertEquals(0f, a2.getValue(), 0.01);
		assertEquals(1f, d1.getValue(), 0.01);
		assertEquals(0f, d2.getValue(), 0.01);
		assertEquals(1f, d3.getValue(), 0.01);
	}
	
	@Test
	public void testCollector() {
		DataAddress da = mm.findOmnibus(1).findCollectorTerminal(2).findDataAddress(0x12);
		Device c1 = da.findDevice("c1");
		DataAddress da2 = mm.findOmnibus(1).findCollectorTerminal(2).findDataAddress(0x13);
		Device c2 = da2.findDevice("c2");
		
		byte[] msg2 = new byte[] { 0, 0xa, 0, 0, 0, 4, 1, 2, 0x0, 0x12, 0, 2, 0, 0x0a, 0, 0 };
		byte[] content2 = Arrays.copyOfRange(msg2, 6, msg2.length);
		mm.handler(content2);
		assertEquals(10f, c1.getValue(), 0.01);
		
		msg2 = new byte[] { 0, 0xa, 0, 0, 0, 4, 1, 2, 0x0, 0x12, 0, 2, 0, 0x05, 0, 0 };
		content2 = Arrays.copyOfRange(msg2, 6, msg2.length);
		mm.handler(content2);
		assertEquals(5f, c1.getValue(), 0.01);
		
		byte[] msg3 = new byte[] { 0, 0xa, 0, 0, 0, 4, 1, 2, 0x0, 0x13, 0, 2, 0, 0x0a, 0, 0 };
		byte[] content3 = Arrays.copyOfRange(msg3, 6, msg3.length);
		mm.handler(content3);
		assertEquals(10f, c2.getValue(), 0.01);
		
		msg3 = new byte[] { 0, 0xa, 0, 0, 0, 4, 1, 2, 0x0, 0x13, 0, 2, 0, 0x06, 0, 0 };
		content3 = Arrays.copyOfRange(msg3, 6, msg3.length);
		mm.handler(content3);
		assertEquals(6f, c2.getValue(), 0.01);
		
		msg3 = new byte[] { 0, 0xa, 0, 0, 0, 4, 1, 2, 0x0, 0x12, 0, 2, 0, 0x20, 0, 0 };
		content3 = Arrays.copyOfRange(msg3, 6, msg3.length);
		mm.handler(content3);
		assertEquals(32f, c1.getValue(), 0.01);
		
		msg3 = new byte[] { 0, 0xa, 0, 0, 0, 4, 1, 2, 0x0, 0x12, 0, 2, 0, 0x3, 0, 0 };
		content3 = Arrays.copyOfRange(msg3, 6, msg3.length);
		mm.handler(content3);
		assertEquals(3f, c1.getValue(), 0.01);
	}

	public MsgManager createMsgManager() {
		// 通信管理机
		MsgManager manager = new MsgManager();
		manager.setNum(4);
		List<Omnibus> list = createOmnibus();
		for (Omnibus o : list) {
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
		setListener(a1);
		setListener(a2);
		setListener(a3);
		setListener(d1);
		setListener(d2);
		setListener(d3);
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
		ct.addDataAddress(da);

		DataAddress da2 = createDataAddress(0x13);
		DevCollector c2 = new DevCollector("c2", "湿度");
		da2.addDevice(c2);
		setListener(c2);
		ct.addDataAddress(da2);
		return ct;
	}

	public DataAddress createDataAddress(int num) {
		DataAddress addr = new DataAddress();
		addr.setNum(num);
		return addr;
	}

	public void setListener(Device dev) {
		dev.setOnValueChangedListener(new Device.OnValueChangedListener() {

			@Override
			public void onValueChanged(Device device, float value) {
				System.out.println("onValueChanged " + device.getCoding() + " value:" + value);
			}
		});
	}
}
