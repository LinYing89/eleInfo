package com.bairock.iot.eleInfo.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.bairock.iot.eleInfo.AlarmInfo;
import com.bairock.iot.eleInfo.CollectorTerminal;
import com.bairock.iot.eleInfo.Config;
import com.bairock.iot.eleInfo.DataAddress;
import com.bairock.iot.eleInfo.Device;
import com.bairock.iot.eleInfo.EleInfo;
import com.bairock.iot.eleInfo.Electrical;
import com.bairock.iot.eleInfo.MsgManager;
import com.bairock.iot.eleInfo.Omnibus;
import com.bairock.iot.eleInfo.ValueTrigger;
import com.bairock.iot.eleInfo.communication.MyClient;
import com.bairock.iot.eleInfo.communication.MyOnTriggedChangedListener;
import com.bairock.iot.eleInfo.communication.MyOnValueChangedListener;
import com.bairock.iot.eleInfo.communication.MyServer;
import com.bairock.iot.eleInfo.communication.MyWebSocketHelper;
import com.bairock.iot.eleInfo.dao.ConfigDao;
import com.bairock.iot.eleInfo.dao.MsgManagerDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Application Lifecycle Listener implementation class StartUpListener
 *
 */
public class StartUpListener implements ServletContextListener {

	private static EntityManagerFactory em = null;
	public static EntityManager eManager = null;
	
	public static List<MsgManager> listManager = null;
	public static Config config;
	
	public static Device d1;
	public static Device d2;
	public static Device d3;
	public static Device c1;
	
	private MyServer myServer;
	
    public void contextDestroyed(ServletContextEvent arg0)  { 
    	eManager.close();
    	em.close();
    	myServer.close();
    	MyClient.getIns().closeHandler();
    }

    public void contextInitialized(ServletContextEvent arg0)  { 
    	em = Persistence.createEntityManagerFactory("eleinfo");
    	eManager = em.createEntityManager();
    	listManager = new MsgManagerDao().findAll();
    	config = new ConfigDao().find();
    	
    	setListener();
    	
    	MyClient.getIns().link();
    	
    	MyServer.PORT = config.getDevicePort();
    	myServer = new MyServer();
    	try {
			myServer.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static MsgManager findMsgManager(int managerNum) {
    	MsgManager manager = null;
    	for(MsgManager mm : listManager) {
    		if(mm.getNum() == managerNum) {
    			manager = mm;
    		}
    	}
    	return manager;
    }
    
    private void setListener() {
    	for(MsgManager m : listManager) {
    		for(Omnibus o : m.getListOmnibus()) {
    			for(CollectorTerminal c : o.getListCollectorTerminal()) {
    				for(DataAddress d : c.getListDataAddress()) {
    					for(Device dev : d.getListDevice()) {
    						if(dev.getCoding().equals("d1")) {
    							d1 = dev;
    						}else if(dev.getCoding().equals("d2")) {
    							d2 = dev;
    						}else if(dev.getCoding().equals("d3")) {
    							d3 = dev;
    						}else if(dev.getCoding().equals("c1")) {
    							c1 = dev;
    							for(ValueTrigger t : c1.getListValueTrigger()) {
    								t.setOnTriggedChangedListener(new MyOnTriggedChangedListener());
    							}
    						}
    						dev.setOnValueChangedListener(new MyOnValueChangedListener());
    					}
    				}
    			}
    		}
    	}
    }
	
    public static List<AlarmInfo> findAlarmInfo() {
    	List<AlarmInfo> list = new ArrayList<AlarmInfo>();
    	for(MsgManager m : listManager) {
    		for(Omnibus o : m.getListOmnibus()) {
    			for(CollectorTerminal c : o.getListCollectorTerminal()) {
    				for(DataAddress d : c.getListDataAddress()) {
    					for(Device dev : d.getListDevice()) {
    						list.addAll(dev.getListAlarmInfo());
    					}
    				}
    			}
    		}
    	}
    	return list;
    }
    
    public static void refreshValues() {
    	for(MsgManager m : listManager) {
    		for(Omnibus o : m.getListOmnibus()) {
    			for(CollectorTerminal c : o.getListCollectorTerminal()) {
    				for(DataAddress d : c.getListDataAddress()) {
    					for(Device dev : d.getListDevice()) {
    						Map<String, Object> map = new HashMap<>();
    						if(dev instanceof Electrical) {
    							EleInfo ei = ((Electrical) dev).getEleInfo();
    							map.put("id", 0);
    							map.put("coding", "ele");
    							map.put("axA", ei.getAxA());
    							map.put("bxA", ei.getAxA());
    							map.put("cxA", ei.getAxA());
    							map.put("axV", ei.getAxA());
    							map.put("bxV", ei.getAxA());
    							map.put("cxV", ei.getAxA());
    							map.put("yinshu", ei.getAxA());
    							map.put("axyg", ei.axYouGongPower());
    							map.put("axwg", ei.axWuGongPower());
    							map.put("bxyg", ei.bxYouGongPower());
    							map.put("bxwg", ei.bxWuGongPower());
    							map.put("zyg", ei.zongYouGongPower());
    							map.put("zwg", ei.zongWuGongPower());
    							sendMap(map);
    							continue;
    						}
    						//0表示设备状态或值
    						map.put("id", 0);
    						map.put("coding", dev.getCoding());
    						map.put("value", dev.getValue());
    						sendMap(map);
    						if(dev.getCoding().equals("c1")) {
    							//温度
    							for(ValueTrigger trigger : dev.getListValueTrigger()) {
    								map = new HashMap<>();
    	    						//1表示触发值
    	    						map.put("id", 1);
    	    						map.put("symbol", trigger.getCompareSymbol());
    	    						map.put("coding", dev.getCoding());
    	    						map.put("value", trigger.getTriggerValue());
    	    						sendMap(map);
    							}
    						}
    					}
    				}
    			}
    		}
    	}
    }
    
    public static void refreshEleInfo() {
    	for(MsgManager m : listManager) {
    		for(Omnibus o : m.getListOmnibus()) {
    			for(CollectorTerminal c : o.getListCollectorTerminal()) {
    				for(DataAddress d : c.getListDataAddress()) {
    					for(Device dev : d.getListDevice()) {
    						Map<String, Object> map = new HashMap<>();
    						if(dev instanceof Electrical) {
    							EleInfo ei = ((Electrical) dev).getEleInfo();
    							map.put("id", 0);
    							map.put("coding", "ele");
    							map.put("axA", ei.getAxA());
    							map.put("bxA", ei.getAxA());
    							map.put("cxA", ei.getAxA());
    							map.put("axV", ei.getAxA());
    							map.put("bxV", ei.getAxA());
    							map.put("cxV", ei.getAxA());
    							map.put("yinshu", ei.getAxA());
    							map.put("axyg", ei.axYouGongPower());
    							map.put("axwg", ei.axWuGongPower());
    							map.put("bxyg", ei.bxYouGongPower());
    							map.put("bxwg", ei.bxWuGongPower());
    							map.put("zyg", ei.zongYouGongPower());
    							map.put("zwg", ei.zongWuGongPower());
    							sendMap(map);
    							continue;
    						}
    					}
    				}
    			}
    		}
    	}
    }
    
    public static void testEleInfo() {
    	Map<String, Object> map = new HashMap<>();
    	map.put("id", 0);
		map.put("coding", "ele");
		map.put("axA", 1);
		map.put("bxA", 2);
		map.put("cxA", 3);
		map.put("axV", 4);
		map.put("bxV", 5);
		map.put("cxV", 6);
		map.put("yinshu", 7);
		map.put("axyg", 8);
		map.put("axwg", 9);
		map.put("bxyg", 10);
		map.put("bxwg", 11);
		map.put("zyg", 100);
		map.put("zwg", 33);
		sendMap(map);
    }
    
    public static void sendMap(Map<String, Object> map) {
    	ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(map);
			if (null != json) {
				MyWebSocketHelper.sendGroupMessage(json);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }
    
    public static byte getAction(Device device) {
		byte action;
		if (device.getValue() == 0) {
			action = 0;
		} else {
			action = (byte) 0xff;
		}
		return action;
	}
    
    public static byte[] createByteMsg(byte[] b1) {
    	if(b1 == null) {
    		return null;
    	}
		byte[] byVerify = getVerify(b1);
		return unitArray(b1, byVerify);
	}
    public static byte[] unitArray(byte[] b1, byte[] b2) {
		byte[] byMsg = new byte[b1.length + b2.length];
		System.arraycopy(b1, 0, byMsg, 0, b1.length);
		System.arraycopy(b2, 0, byMsg, b1.length, b2.length);
		return byMsg;
	}
    public static byte[] getVerify(byte[] by) {
		byte[] bysum = new byte[2];
		int chksum = 0;
		for (int i = 0; i < by.length; i++) {
			chksum += by[i];
		}
		bysum[1] = (byte) (chksum & 0xFF);
		bysum[0] = (byte) (chksum >> 8 & 0xFF);
		return bysum;
	}
}
