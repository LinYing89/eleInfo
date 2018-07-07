package com.bairock.iot.eleInfo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

/**
 * 设备
 * @author 44489
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("Device")
@DiscriminatorColumn(name = "device_type", discriminatorType = DiscriminatorType.STRING)
public class Device {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	//设备号
	private String coding;
	
	private String name;
	
	@ManyToOne
	private DataAddress dataAddress;

	private float value;
	
	@OneToMany(cascade=CascadeType.ALL)  
    @JoinColumn(name="deviceId") 
	@OrderBy(value = "alarmTime desc")
	private List<AlarmInfo> listAlarmInfo = new ArrayList<>();
	
	@Transient
	private OnValueChangedListener onValueChangedListener;
	
	public Device() {}
	
	public Device(String coding) {
		this.coding = coding;
	}
	
	public Device(String coding, String name) {
		this.coding = coding;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCoding() {
		return coding;
	}

	public void setCoding(String coding) {
		this.coding = coding;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataAddress getDataAddress() {
		return dataAddress;
	}

	public void setDataAddress(DataAddress dataAddress) {
		this.dataAddress = dataAddress;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		if(this.value != value) {
			this.value = value;
			if(null != onValueChangedListener) {
				onValueChangedListener.onValueChanged(this, value);
			}
		}
	}

	public List<AlarmInfo> getListAlarmInfo() {
		return listAlarmInfo;
	}

	public void setListAlarmInfo(List<AlarmInfo> listAlarmInfo) {
		this.listAlarmInfo = listAlarmInfo;
	}

	public OnValueChangedListener getOnValueChangedListener() {
		return onValueChangedListener;
	}

	public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
		this.onValueChangedListener = onValueChangedListener;
	}

	public void addAlarmInfo(AlarmInfo ai) {
		if(null != ai) {
			listAlarmInfo.add(ai);
		}
	}
	
	public void removeAlarmInfo(AlarmInfo ai) {
		listAlarmInfo.remove(ai);
	}
	
	public void handler(byte by) {
		
	}
	
	public interface OnValueChangedListener {
		void onValueChanged(Device device, float value);
	}
}
