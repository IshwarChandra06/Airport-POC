package com.eikona.tech.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity(name = "daily_report")
public class DailyReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "id")
	private Long id;
	
	@Column(name = "date")
	private Date date;
	
	@Column(name = "age")
	private String age;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "poi_id")
	private String poiId;
	
	@Column(name = "in_time")
	private Date inTime;
	
	@Column(name = "out_time")
	private Date outTime;
	
	@Column(name = "in_time_str")
	private String inTimeStr;
	
	@Column(name = "out_time_str")
	private String outTimeStr;
	
	@Column(name = "stay_in_time")
	private String stayInTime;
	
	@Column(name = "stay_in_hour")
	private Long stayInHour;
	
	@Column(name = "in_mask")
	private String inMask;

	@Column(name = "out_mask")
	private String outMask;
	
	@Column(name = "in_location")
	private String inLocation;

	@Column(name = "out_location")
	private String outLocation;
	
	@Column(name = "missed_out_punch")
	private String missedOutPunch;
	
	@Column
	private String cropImagePath;
	
	@Column
	private byte[] cropImageByte;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPoiId() {
		return poiId;
	}

	public void setPoiId(String poiId) {
		this.poiId = poiId;
	}

	public Date getInTime() {
		return inTime;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	public Date getOutTime() {
		return outTime;
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}

	public String getInTimeStr() {
		return inTimeStr;
	}

	public void setInTimeStr(String inTimeStr) {
		this.inTimeStr = inTimeStr;
	}

	public String getOutTimeStr() {
		return outTimeStr;
	}

	public void setOutTimeStr(String outTimeStr) {
		this.outTimeStr = outTimeStr;
	}

	public String getStayInTime() {
		return stayInTime;
	}

	public void setStayInTime(String stayInTime) {
		this.stayInTime = stayInTime;
	}

	public Long getStayInHour() {
		return stayInHour;
	}

	public void setStayInHour(Long stayInHour) {
		this.stayInHour = stayInHour;
	}

	public String getInMask() {
		return inMask;
	}

	public void setInMask(String inMask) {
		this.inMask = inMask;
	}

	public String getOutMask() {
		return outMask;
	}

	public void setOutMask(String outMask) {
		this.outMask = outMask;
	}

	public String getInLocation() {
		return inLocation;
	}

	public void setInLocation(String inLocation) {
		this.inLocation = inLocation;
	}

	public String getOutLocation() {
		return outLocation;
	}

	public void setOutLocation(String outLocation) {
		this.outLocation = outLocation;
	}

	public String getMissedOutPunch() {
		return missedOutPunch;
	}

	public void setMissedOutPunch(String missedOutPunch) {
		this.missedOutPunch = missedOutPunch;
	}

	public String getCropImagePath() {
		return cropImagePath;
	}

	public void setCropImagePath(String cropImagePath) {
		this.cropImagePath = cropImagePath;
	}

	public byte[] getCropImageByte() {
		return cropImageByte;
	}

	public void setCropImageByte(byte[] cropImageByte) {
		this.cropImageByte = cropImageByte;
	}

}
