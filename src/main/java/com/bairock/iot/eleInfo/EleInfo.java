package com.bairock.iot.eleInfo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * 电量信息, 电压\电流\功率
 * @author 44489
 *
 */
@Entity
public class EleInfo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	//A相电流
	private float axA;
	//B相电流
	private float bxA;
	//C相电流
	private float cxA;
	//A相电压
	private float axV;
	//B相电压
	private float bxV;
	//C相电压
	private float cxV;
	//功率因数
	private float yinshu;
	
	@OneToOne
	private Electrical electrical;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public float getAxA() {
		return axA;
	}
	public void setAxA(float axA) {
		this.axA = axA;
	}
	public float getBxA() {
		return bxA;
	}
	public void setBxA(float bxA) {
		this.bxA = bxA;
	}
	public float getCxA() {
		return cxA;
	}
	public void setCxA(float cxA) {
		this.cxA = cxA;
	}
	public float getAxV() {
		return axV;
	}
	public void setAxV(float axV) {
		this.axV = axV;
	}
	public float getBxV() {
		return bxV;
	}
	public void setBxV(float bxV) {
		this.bxV = bxV;
	}
	public float getCxV() {
		return cxV;
	}
	public void setCxV(float cxV) {
		this.cxV = cxV;
	}
	public float getYinshu() {
		return yinshu;
	}
	public void setYinshu(float yinshu) {
		this.yinshu = yinshu;
	}
	
	public Electrical getElectrical() {
		return electrical;
	}
	public void setElectrical(Electrical electrical) {
		this.electrical = electrical;
	}
	/**
	 * A相有功功率
	 * @return
	 */
	public float axYouGongPower() {
		return axA * axV * yinshu;
	}
	
	/**
	 * A相无功功率
	 * @return
	 */
	public float axWuGongPower() {
		return axA * axV * (1 - yinshu);
	}
	
	/**
	 * B相有功功率
	 * @return
	 */
	public float bxYouGongPower() {
		return bxV * bxV * yinshu;
	}
	
	/**
	 * B相无功功率
	 * @return
	 */
	public float bxWuGongPower() {
		return bxA * bxV * (1 - yinshu);
	}
	
	/**
	 * C相有功功率
	 * @return
	 */
	public float cxYouGongPower() {
		return cxA * cxV * yinshu;
	}
	
	/**
	 * C相无功功率
	 * @return
	 */
	public float cxWuGongPower() {
		return cxA * cxV * (1 - yinshu);
	}
	
	/**
	 * 总有功功率
	 * @return
	 */
	public float zongYouGongPower() {
		return axYouGongPower() + bxYouGongPower() + cxYouGongPower();
	}
	
	/**
	 * 总相无功功率
	 * @return
	 */
	public float zongWuGongPower() {
		return axWuGongPower() + bxWuGongPower() + cxWuGongPower();
	}
}
