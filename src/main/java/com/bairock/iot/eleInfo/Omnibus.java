package com.bairock.iot.eleInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.log4j.Logger;

/**
 * 总线
 * @author 44489
 *
 */
@Entity
public class Omnibus {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	//总线号
	private int num;
	
	private String name;

	@ManyToOne
	private MsgManager msgManager;
	
	@OneToMany(mappedBy = "omnibus", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CollectorTerminal> listCollectorTerminal = new ArrayList<>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MsgManager getMsgManager() {
		return msgManager;
	}

	public void setMsgManager(MsgManager msgManager) {
		this.msgManager = msgManager;
	}

	public List<CollectorTerminal> getListCollectorTerminal() {
		return listCollectorTerminal;
	}

	public void setListCollectorTerminal(List<CollectorTerminal> listCollectorTerminal) {
		this.listCollectorTerminal = listCollectorTerminal;
	}
	
	public void addCollectorTerminal(CollectorTerminal collectorTerminal) {
		if(null != collectorTerminal) {
			collectorTerminal.setOmnibus(this);
			listCollectorTerminal.add(collectorTerminal);
		}
	}
	
	public CollectorTerminal findCollectorTerminal(int terminalNum) {
		CollectorTerminal terminal = null;
		for(CollectorTerminal c : listCollectorTerminal) {
			if(c.getNum() == terminalNum) {
				terminal = c;
			}
		}
		return terminal;
	}
	
	public void handler(byte[] by) {
		Logger logger = Logger.getLogger(this.getClass().getName());
		CollectorTerminal c = findCollectorTerminal(by[0]);
		if(c == null) {
			logger.error("采集终端不存在: num:" + by[0]);
			return;
		}
		byte[] by1 = Arrays.copyOfRange(by, 1, by.length);
		c.handler(by1);
	}
}
