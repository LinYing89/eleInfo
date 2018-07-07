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
		Omnibus omnibus = findOmnibus(by[0]);
		byte[] by1 = Arrays.copyOfRange(by, 1, by.length);
		omnibus.handler(by1);
	}
}
