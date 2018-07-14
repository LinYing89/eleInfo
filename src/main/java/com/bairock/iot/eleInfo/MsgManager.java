package com.bairock.iot.eleInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.log4j.Logger;

/**
 * 通信管理机
 * @author 44489
 *
 */
@Entity
public class MsgManager {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	//通信管理号
	private int num;
	
	private String name;

	@OneToMany(mappedBy = "msgManager", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Omnibus> listOmnibus = new ArrayList<>();
	
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
	
	public List<Omnibus> getListOmnibus() {
		return listOmnibus;
	}

	public void setListOmnibus(List<Omnibus> listOmnibus) {
		this.listOmnibus = listOmnibus;
	}

	public void addOmnibus(Omnibus bus) {
		if(null != bus) {
			bus.setMsgManager(this);
			listOmnibus.add(bus);
		}
	}
	
	public void removeOmnibus(Omnibus bus) {
		listOmnibus.remove(bus);
	}
	
	public Omnibus findOmnibus(int omnibusNum) {
		Omnibus omnibus = null;
		for(Omnibus o : listOmnibus) {
			if(o.getNum() == omnibusNum) {
				omnibus = o;
			}
		}
		return omnibus;
	}
	
	public void handler(byte[] by) {
		
		//去掉校验码
		byte[] byAllData = Arrays.copyOfRange(by, 0, by.length - 2);
		
		int index = 0;
		
		int byStart = 0;
		while(index < byAllData.length) {
			byStart = index;
			//index指到数据长度第一个字节
			index += 4;
			int dataLen = byAllData[index] << 8 | byAllData[index + 1];
			//index指到数据长度第二字节
			index++;
			//index指到数据最后一个字节
			index += dataLen;
			byte[] byMsgOne = Arrays.copyOfRange(byAllData, byStart, index + 1);
			Omnibus omnibus = findOmnibus(byMsgOne[0]);
			if(null == omnibus) {
				Logger logger = Logger.getLogger(this.getClass().getName());
				logger.error("总线号不存在: num:" + byMsgOne[0]);
				return;
			}
			byte[] by1 = Arrays.copyOfRange(byMsgOne, 1, byMsgOne.length);
			omnibus.handler(by1);
			//index指到报文2第一个字节
			index++;
		}
		
//		Omnibus omnibus = findOmnibus(by[0]);
//		byte[] by1 = Arrays.copyOfRange(by, 1, by.length);
//		omnibus.handler(by1);
	}
}
