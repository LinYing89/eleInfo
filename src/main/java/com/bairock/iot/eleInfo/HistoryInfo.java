package com.bairock.iot.eleInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * 历史记录
 * @author 44489
 *
 */
@Entity
public class HistoryInfo implements Comparable<HistoryInfo>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date time;
	
	@ManyToOne
	@JoinColumn(name = "dev_id", foreignKey = @ForeignKey(name = "DEV_ID_FK"))
	private Device device;
	
	private float value;

	public HistoryInfo() {}
	
	public HistoryInfo(Date time, Device device, float value) {
		super();
		this.time = time;
		this.device = device;
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}
	
	@Transient
	public String getValueStr() {
		if(device instanceof DevSwitch) {
			if(value == 0) {
				return "开";
			}else {
				return "关";
			}
		}else {
			return String.valueOf(value);
		}
	}
	
	@Transient
	public String getDateStr() {
		DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //HH表示24小时制；
		return dFormat.format(time);
	}

	@Override
	public int compareTo(HistoryInfo o) {
		if( null == o) {
			return 1;
		}
		return this.getTime().compareTo(o.getTime());
	}
}
