package com.bairock.iot.eleInfo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * 数据地址
 * @author 44489
 *
 */
@Entity
public class DataAddress {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	//地址编号
	private int num;
	
	private String name;
	
	@ManyToOne
	private CollectorTerminal collectorTerminal;

	@OneToMany(mappedBy = "dataAddress", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Device> listDevice = new ArrayList<>();
	
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

	public CollectorTerminal getCollectorTerminal() {
		return collectorTerminal;
	}

	public void setCollectorTerminal(CollectorTerminal collectorTerminal) {
		this.collectorTerminal = collectorTerminal;
	}

	public List<Device> getListDevice() {
		return listDevice;
	}

	public void setListDevice(List<Device> listDevice) {
		this.listDevice = listDevice;
	}
	
	public void addDevice(Device device) {
		if(null != device) {
			device.setDataAddress(this);
			listDevice.add(device);
		}
	}
	
	public Device findDevice(String deviceNum) {
		Device dev = null;
		for(Device d : listDevice) {
			if(d.getCoding().equals(deviceNum)) {
				dev = d;
			}
		}
		return dev;
	}
	
	public void handler(byte[] by) {
		if(null == by || by.length < 1) {
			return;
		}
		if(num == 0x200) {
			byte byData = by[1];
			findDevice("a1").setValue(byData & 1);
			findDevice("a3").setValue(byData >> 1 & 1);
			findDevice("a2").setValue(byData >> 2 & 1);
			findDevice("d1").setValue(byData >> 5 & 1);
			findDevice("d2").setValue(byData >> 6 & 1);
			findDevice("d3").setValue(byData >> 7 & 1);
		}else if(num == 0) {
			if(by.length >= 2) {
				findDevice("c1").setValue(bytesToInt(new byte[] {by[0], by[1]}) / 100f);
			}
			if(by.length >= 4) {
				findDevice("c2").setValue(bytesToInt(new byte[] {by[2], by[3]}) / 100f);
			}
		}
	}
	
	public int bytesToInt(byte[] by) {
		int value = 0;
		int j = 0;
		for(int i = by.length - 1; i >= 0; i--, j++) {
			value |= ((by[j] & 0xff) << (i * 8));
		}
		return value;
	}
	
	public static void main(String[] args) {
		DataAddress da = new DataAddress();
		byte[] by = new byte[] {0x0, 0x0, 0x01, 0x01};
		int i = da.bytesToInt(by);
		System.out.println(i+"?");
	}
}
