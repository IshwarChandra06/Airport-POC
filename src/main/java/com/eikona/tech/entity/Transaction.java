package com.eikona.tech.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity(name="transaction")
public class Transaction implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "id")
	private Long id;
	
	@Column
	private Date punchDate;
	
	@Column
	private Date punchTime;
	
	@Column
	private String punchDateStr;
	
	@Column
	private String punchTimeStr;
	
	@Column
	private Boolean wearingMask;
	
	@Column
	private String deviceName;

	@Column
	private String serialNo;
	
	@Column
	private String eventId;
	
	@Column
	private String appearanceId;
	
	@Column
	private Double poiConfidence;
	
	@Column
	private String poiId;
	
	@Column
	private String maskStatus;
	
	@Column
	private Double maskedScore;
	
	@Column
	private String age;
	
	@Column
	private String gender;
	
	@Column
	private String searchScore;
	
	@Column
	private String livenessScore;
	
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

	public Date getPunchDate() {
		return punchDate;
	}

	public void setPunchDate(Date punchDate) {
		this.punchDate = punchDate;
	}

	public Date getPunchTime() {
		return punchTime;
	}

	public void setPunchTime(Date punchTime) {
		this.punchTime = punchTime;
	}

	public String getPunchDateStr() {
		return punchDateStr;
	}

	public void setPunchDateStr(String punchDateStr) {
		this.punchDateStr = punchDateStr;
	}

	public String getPunchTimeStr() {
		return punchTimeStr;
	}

	public void setPunchTimeStr(String punchTimeStr) {
		this.punchTimeStr = punchTimeStr;
	}

	public Boolean getWearingMask() {
		return wearingMask;
	}

	public void setWearingMask(Boolean wearingMask) {
		this.wearingMask = wearingMask;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getAppearanceId() {
		return appearanceId;
	}

	public void setAppearanceId(String appearanceId) {
		this.appearanceId = appearanceId;
	}

	public Double getPoiConfidence() {
		return poiConfidence;
	}

	public void setPoiConfidence(Double poiConfidence) {
		this.poiConfidence = poiConfidence;
	}

	public String getPoiId() {
		return poiId;
	}

	public void setPoiId(String poiId) {
		this.poiId = poiId;
	}

	public String getMaskStatus() {
		return maskStatus;
	}

	public void setMaskStatus(String maskStatus) {
		this.maskStatus = maskStatus;
	}

	public Double getMaskedScore() {
		return maskedScore;
	}

	public void setMaskedScore(Double maskedScore) {
		this.maskedScore = maskedScore;
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

	public String getSearchScore() {
		return searchScore;
	}

	public void setSearchScore(String searchScore) {
		this.searchScore = searchScore;
	}

	public String getLivenessScore() {
		return livenessScore;
	}

	public void setLivenessScore(String livenessScore) {
		this.livenessScore = livenessScore;
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
