package com.bairock.iot.eleInfo;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class MsgManagerTest {

	MsgManager mm;

	@Before
	public void setUp() throws Exception {
		mm = CreateManager.createMsgManager();
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
		byte[] msg = new byte[] { 0, 0x8, 0, 0, 0, 4, 0, 0, 0x2, 0x00, 0, 2, 0, 0x45, 0, 0 };
		byte[] content = Arrays.copyOfRange(msg, 6, msg.length);
		mm.handler(content);
		assertEquals(0f, a1.getValue(), 0.01);
		assertEquals(0f, a3.getValue(), 0.01);
		assertEquals(1f, a2.getValue(), 0.01);
		assertEquals(0f, d1.getValue(), 0.01);
		assertEquals(1f, d2.getValue(), 0.01);
		assertEquals(0f, d3.getValue(), 0.01);
	}
	
	@Test
	public void testCollector() {
		DataAddress da = mm.findOmnibus(1).findCollectorTerminal(0x21).findDataAddress(0x0);
		Device c1 = da.findDevice("c1");
		Device c2 = da.findDevice("c2");
		
		byte[] msg2 = new byte[] { 0, 0x8, 0, 0, 0, 4, 1, 0x21, 0x0, 0x0, 0, 4, 3, (byte) 0xe8, 4,(byte) 0x4c,0, 0 };
		byte[] content2 = Arrays.copyOfRange(msg2, 6, msg2.length);
		mm.handler(content2);
		assertEquals(10f, c1.getValue(), 0.01);
		assertEquals(11f, c2.getValue(), 0.01);
	}
	
	@Test
	public void testTotal() {
		byte[] msg = new byte[] { 00, 0x12, 00, 00, 00, 04,  01, 0x21, 00, 00, 00, 04, 0x0a, (byte) 0xd6, 0x1d, 0x0f, 00, 00, 02, 00, 00, 02, 00, 0x2f, 56, (byte) 0xb6 };
		byte[] content = Arrays.copyOfRange(msg, 6, msg.length);
		mm.handler(content);
		
		DataAddress da = mm.findOmnibus(1).findCollectorTerminal(0x21).findDataAddress(0x0);
		Device c1 = da.findDevice("c1");
		Device c2 = da.findDevice("c2");
		assertEquals(2774f/100f, c1.getValue(), 0.01);
		assertEquals(7439f/100f, c2.getValue(), 0.01);
		
		DataAddress da2 = mm.findOmnibus(0).findCollectorTerminal(0).findDataAddress(0x200);
		Device a1 = da2.findDevice("a1");
		Device a2 = da2.findDevice("a2");
		Device a3 = da2.findDevice("a3");
		Device d1 = da2.findDevice("d1");
		Device d2 = da2.findDevice("d2");
		Device d3 = da2.findDevice("d3");
		assertEquals(0f, a1.getValue(), 0.01);
		assertEquals(1f, a2.getValue(), 0.01);
		assertEquals(1f, a3.getValue(), 0.01);
		assertEquals(1f, d1.getValue(), 0.01);
		assertEquals(0f, d2.getValue(), 0.01);
		assertEquals(0f, d3.getValue(), 0.01);
	}
	
	@Test
	public void testEleInfo() {
		byte[] msg = new byte[] {02, 0x7c, 00, 00, 00, 0x0e, 0, 1, 0, 2, 0, 3, 0, 4, 0, 5, 0, 6, 0, 7, 56, (byte) 0xb6 };
		mm.handler(msg);
		DataAddress da = mm.findOmnibus(2).findCollectorTerminal(0x7c).findDataAddress(0x0);
		Electrical ele = (Electrical) da.getListDevice().get(0);
		EleInfo ei = ele.getEleInfo();
		assertEquals(1, ei.getAxA(), 0.01);
		assertEquals(2, ei.getBxA(), 0.01);
		assertEquals(3, ei.getCxA(), 0.01);
		assertEquals(0.04, ei.getAxV(), 0.01);
		assertEquals(0.05, ei.getBxV(), 0.01);
		assertEquals(0.06, ei.getCxV(), 0.01);
		assertEquals(0.07, ei.getYinshu(), 0.01);

	}
}
