package com.bairock.iot.eleInfo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("DevCollector")
public class DevCollector extends Device {

	public DevCollector(String coding, String name) {
		super(coding, name);
		// TODO Auto-generated constructor stub
	}

	public DevCollector() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DevCollector(String coding) {
		super(coding);
		// TODO Auto-generated constructor stub
	}

}
