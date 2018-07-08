package com.bairock.iot.eleInfo;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 * 触发器
 * 
 * @author 44489
 *
 */
@Entity
public class ValueTrigger {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private boolean enable = false;
	private float triggerValue;

	private String info;

	// 源设备，触发连锁的设备源
	@ManyToOne
	private Device sourceDevice;

	// 目标设备，被连锁的设备
	@ManyToOne
	@JoinColumn(name = "targetDev_id", foreignKey = @ForeignKey(name = "DEV_ID_FK"))
	private Device targetDevice;

	@Transient
	private boolean trigged = false;
	
	private CompareSymbol compareSymbol = CompareSymbol.LESS;
	
	//目标设备的目标值
	private int targetValue;
	
	@Transient
	private OnTriggedChangedListener onTriggedChangedListener;
	
	public Long getId() {
		return id;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public float getTriggerValue() {
		return triggerValue;
	}

	public void setTriggerValue(float triggerValue) {
		this.triggerValue = triggerValue;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public CompareSymbol getCompareSymbol() {
		return compareSymbol;
	}

	public void setCompareSymbol(CompareSymbol compareSymbol) {
		this.compareSymbol = compareSymbol;
	}

	public int getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(int targetValue) {
		this.targetValue = targetValue;
	}

	public Device getSourceDevice() {
		return sourceDevice;
	}

	public void setSourceDevice(Device sourceDevice) {
		this.sourceDevice = sourceDevice;
	}

	public Device getTargetDevice() {
		return targetDevice;
	}

	public void setTargetDevice(Device targetDevice) {
		this.targetDevice = targetDevice;
	}

	public boolean triggering(float value) {
		boolean trigging = false;
		switch(compareSymbol) {
		case LESS:
			trigging = value < triggerValue;
			break;
		case EQUAL:
			trigging = Math.abs(triggerValue - value) < 0.01;
			break;
		case GREAT:
			trigging = value > triggerValue;
			break;
		}
		if(trigged) {
			// if trigged, don't trigger again until trigging is false to set trigged to false,
			//then can trigger again
			if(trigging) {
				trigging = false;
			}else {
				setTrigged(false);
			}
		}else {
			setTrigged(trigging);
		}
		return trigging;
	}
	
	public void setTrigged(boolean trigged) {
		if(this.trigged != trigged) {
			this.trigged = trigged;
			if(null != onTriggedChangedListener) {
				onTriggedChangedListener.onTriggedChanged(this, trigged);
			}
		}
	}
	
	public OnTriggedChangedListener getOnTriggedChangedListener() {
		return onTriggedChangedListener;
	}

	public void setOnTriggedChangedListener(OnTriggedChangedListener onTriggedChangedListener) {
		this.onTriggedChangedListener = onTriggedChangedListener;
	}

	/**
	 * 已触发标志改变事件
	 * 由未触发到触发，或由已触发到未触发
	 * @author 44489
	 *
	 */
	public interface OnTriggedChangedListener {
		/**
		 * 已触发标志改变事件
		 * 由未触发到触发，或由已触发到未触发
		 * @param trigger
		 * @param trigged true为由未触发变为触发，false为由触发变为未触发
		 */
		void onTriggedChanged(ValueTrigger trigger, boolean trigged);
	}
}
