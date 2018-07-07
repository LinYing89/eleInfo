package com.bairock.iot.eleInfo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("DevAlarm")
public class DevAlarm extends Device {

	public DevAlarm() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DevAlarm(String coding) {
		super(coding);
		// TODO Auto-generated constructor stub
	}

	public DevAlarm(String coding, String name) {
		super(coding, name);
		// TODO Auto-generated constructor stub
	}

}
