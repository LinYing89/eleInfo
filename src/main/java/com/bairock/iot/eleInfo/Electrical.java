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
		eleInfo.setAxA(by[0] << 8 | by[1]);
		eleInfo.setBxA(by[2] << 8 | by[3]);
		eleInfo.setCxA(by[4] << 8 | by[5]);
		eleInfo.setAxV((by[6] << 8 | by[7]) / 100f);
		eleInfo.setBxV((by[8] << 8 | by[9]) / 100f);
		eleInfo.setCxV((by[10] << 8 | by[11]) / 100f);
		if(by.length >= 14) {
			eleInfo.setYinshu((by[12] << 8 | by[13]) / 100f);
		}
		
	}
	
}
