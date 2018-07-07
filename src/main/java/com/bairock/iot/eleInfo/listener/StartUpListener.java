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
import com.bairock.iot.eleInfo.DataAddress;
import com.bairock.iot.eleInfo.Device;
import com.bairock.iot.eleInfo.MsgManager;
import com.bairock.iot.eleInfo.MyServer;
import com.bairock.iot.eleInfo.Omnibus;
import com.bairock.iot.eleInfo.communication.MyOnValueChangedListener;
import com.bairock.iot.eleInfo.communication.MyWebSocketHelper;
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
	
	public static Device d1;
	public static Device d2;
	public static Device d3;
	
	private MyServer myServer;
	
    public void contextDestroyed(ServletContextEvent arg0)  { 
    	eManager.close();
    	em.close();
    	myServer.close();
    }

    public void contextInitialized(ServletContextEvent arg0)  { 
    	em = Persistence.createEntityManagerFactory("eleinfo");
    	eManager = em.createEntityManager();
    	listManager = new MsgManagerDao().findAll();
    	setListener();
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
    						map.put("coding", dev.getCoding());
    						map.put("value", dev.getValue());
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
    				}
    			}
    		}
    	}
    }
}
