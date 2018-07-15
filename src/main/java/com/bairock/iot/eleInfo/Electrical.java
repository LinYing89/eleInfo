package com.bairock.iot.eleInfo;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Electrical extends Device {

	@OneToOne(mappedBy = "electrical", cascade = CascadeType.ALL, orphanRemoval = true)
	private EleInfo eleInfo = new EleInfo();
	
	public Electrical() {
		this("");
	}

	public Electrical(String coding, String name) {
		super(coding, name);
		setEleInfo(new EleInfo());
	}

	public Electrical(String coding) {
		this(coding, "");
	}

	public EleInfo getEleInfo() {
		return eleInfo;
	}

	public void setEleInfo(EleInfo eleInfo) {
		eleInfo.setElectrical(this);
		this.eleInfo = eleInfo;
	}

	@Override
	public void handler(byte[] by) {
		if(null == by || by.length < 12) {
			return;
		}
		eleInfo.setAxA(toFloat(by[0], by[1], 1));
		eleInfo.setBxA(toFloat(by[2], by[3], 1));
		eleInfo.setCxA(toFloat(by[4], by[5], 1));
		eleInfo.setAxV(toFloat(by[6], by[7], 100f));
		eleInfo.setBxV(toFloat(by[8], by[9], 100f));
		eleInfo.setCxV(toFloat(by[10], by[11], 100f));
		if(by.length >= 14) {
			eleInfo.setYinshu(toFloat(by[12], by[13], 100f));
		}
		
	}
	
	public static float toFloat(byte b1, byte b2, float xishu) {
		return (Device.toUnSignInt(b1) << 8 | Device.toUnSignInt(b2)) / xishu;
	}
	
	public static void main(String[] args) {
		System.out.println(toFloat((byte)0x5c, (byte)0xf8, 100f) + "?");
	}
	
}
