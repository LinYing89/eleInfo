package com.bairock.iot.eleInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class AlarmInfo implements Comparable<AlarmInfo>{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String info;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date alarmTime;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Date getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(Date alarmTime) {
		this.alarmTime = alarmTime;
	}
	
	@Transient
	public String getDateStr() {
		DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //HH表示24小时制；
		return dFormat.format(alarmTime);
	}

	@Override
	public int compareTo(AlarmInfo o) {
		if( null == o) {
			return 1;
		}
		return this.getAlarmTime().compareTo(o.getAlarmTime());
	}
	
}
