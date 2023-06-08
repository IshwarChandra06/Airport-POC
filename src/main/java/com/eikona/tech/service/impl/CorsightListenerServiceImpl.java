package com.eikona.tech.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.eikona.tech.constants.ApplicationConstants;
import com.eikona.tech.constants.CorsightDeviceConstants;
import com.eikona.tech.constants.NumberConstants;
import com.eikona.tech.corsight.request.dto.Data;
import com.eikona.tech.entity.Device;
import com.eikona.tech.entity.Transaction;
import com.eikona.tech.entity.Watchlist;
import com.eikona.tech.repository.DeviceRepository;
import com.eikona.tech.repository.TransactionRepository;
import com.eikona.tech.repository.WatchlistRepository;
import com.eikona.tech.util.SavingCropImageUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CorsightListenerServiceImpl {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private WatchlistRepository watchlistRepository;

	@Autowired
	private DeviceRepository deviceRepository;

	@Autowired
	private SavingCropImageUtil savingCropImageUtil;
	
	@Autowired
	private CorsightDeviceServiceImpl corsightDeviceServiceImpl;
	

	/**
	 * In this function, each face transaction is coming as a String response. If
	 * the poi id of transaction matches with any employee then it is considered as
	 * Employee and transaction info is setting into the respective Transaction
	 * columns and save into mata database.
	 * 
	 * @param responseData -Transaction Info is coming as a String response.
	 */
	
	public void corsightHeartReportInfo(@RequestBody JSONObject responseData) { 
		try {

			Data eventData;
			Transaction transaction = null;
		
			if (CorsightDeviceConstants.APPEARANCE.equalsIgnoreCase(responseData.get(CorsightDeviceConstants.EVENT_TYPE).toString())) {
				try {
					eventData = objectMapper.readValue(responseData.toString(), Data.class);
					System.out.println(responseData.toString());
					System.out.println("Trigger = "+eventData.getTrigger());
					String poiId;
					if (eventData.getTrigger()== NumberConstants.TWO) {
						
					
//					if (eventData.getTrigger() == NumberConstants.ONE || eventData.getTrigger() == NumberConstants.TWO || eventData.getTrigger() == NumberConstants.THREE) {
						transaction = transactionRepository.findByAppearanceId((String) eventData.getAppearance_data().getAppearance_id());
						
//					}
					if (null == transaction) {
						transaction = new Transaction();
						transaction.setAppearanceId((String) eventData.getAppearance_data().getAppearance_id());
						transaction=transactionRepository.save(transaction);
						
						
						if((null==eventData.getMatch_data().getPoi_id()) || (eventData.getMatch_data().getPoi_id().isEmpty())) {
							poiId=corsightDeviceServiceImpl.addEmployeeToDevice(eventData.getCrop_data().getFace_crop_img());
							if(null!=poiId) {
								Watchlist watchlist=watchlistRepository.findByName("Visitor");
								corsightDeviceServiceImpl.addPoiToWatchList(watchlist.getWatchlistId(), poiId);
							}
						}else
							poiId=eventData.getMatch_data().getPoi_id();
						
						transaction.setPoiId(poiId);
					}else {
						if((null==transaction.getPoiId()) || (transaction.getPoiId().isEmpty())) {
							poiId=corsightDeviceServiceImpl.addEmployeeToDevice(eventData.getCrop_data().getFace_crop_img());
							if(null!=poiId) {
								Watchlist watchlist=watchlistRepository.findByName("Visitor");
								corsightDeviceServiceImpl.addPoiToWatchList(watchlist.getWatchlistId(), poiId);
							}
						
						transaction.setPoiId(poiId);
					 }
					}

					setPunchDateAndTime(eventData, transaction);
					setTransactionDetails(eventData, transaction);
					transaction.setDeviceName(eventData.getCamera_data().getCamera_description());
					transaction.setSerialNo(eventData.getCamera_data().getCamera_id());
					
					transaction=transactionRepository.save(transaction);

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			JSONObject response = new JSONObject();

			response.put(ApplicationConstants.STATUS, ApplicationConstants.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}
	
	public void setPunchDateAndTime(Data eventData, Transaction transaction) {
		DateFormat dateFormat = new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_OF_US);
		DateFormat timeFormat = new SimpleDateFormat(ApplicationConstants.TIME_FORMAT_24HR);
		SimpleDateFormat sdf = new SimpleDateFormat(ApplicationConstants.DATE_TIME_FORMAT_OF_US_SEPARATED_BY_T);
		String ts =  eventData.getCrop_data().getFrame_timestamp();
		try {
			Date date = sdf.parse(ts);
			String dateString = dateFormat.format(date);
			transaction.setPunchDateStr(dateString);
			transaction.setPunchDate(date);
			transaction.setPunchTimeStr(timeFormat.format(date));
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

//	public void setPunchDateAndTime(Data eventData, Transaction transaction) {
//		DateFormat dateFormat = new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_OF_US);
//		DateFormat timeFormat = new SimpleDateFormat(ApplicationConstants.TIME_FORMAT_24HR);
//		Timestamp ts = new Timestamp((long) eventData.getCrop_data().getFrame_timestamp());
//		Date date = new Date(ts.getTime() * NumberConstants.THOUSAND);
//		String dateString = dateFormat.format(date);
//		transaction.setPunchDateStr(dateString);
//		transaction.setPunchDate(date);
//		transaction.setPunchTimeStr(timeFormat.format(date));
//		
//	}

	public void setTransactionDetails(Data eventData, Transaction transaction) {

		setAppearanceDetailsToTransaction(eventData, transaction);

		if (CorsightDeviceConstants.NOT_MASKED.equalsIgnoreCase(transaction.getMaskStatus()))
			transaction.setWearingMask(true);
		else
			transaction.setWearingMask(false);

		String imagePath = savingCropImageUtil.saveCropImages(eventData.getCrop_data().getFace_crop_img(), transaction);

		transaction.setCropImagePath(imagePath);

		Device device = deviceRepository.findByCameraId(eventData.getCamera_data().getCamera_id());
		if (null != device)
			transaction.setDeviceName(device.getName());
		
	}

	public void setAppearanceDetailsToTransaction(Data eventData, Transaction transaction) {
		try {
			DecimalFormat dec = new DecimalFormat(ApplicationConstants.DECIMAL_FORMAT);
			transaction.setPoiConfidence(Double.parseDouble(dec.format(eventData.getMatch_data().getPoi_confidence())));
		}catch (NumberFormatException e) {
			transaction.setPoiConfidence(0.0);
		}
//		String poiId;
//		if((null==eventData.getMatch_data().getPoi_id()) || (eventData.getMatch_data().getPoi_id().isEmpty())) {
//			poiId=corsightDeviceServiceImpl.addEmployeeToDevice(eventData.getCrop_data().getFace_crop_img());
//			if(null!=poiId) {
//				Watchlist watchlist=watchlistRepository.findByName("Visitor");
//				corsightDeviceServiceImpl.addPoiToWatchList(watchlist.getWatchlistId(), poiId);
//			}
//			
//		}else
//			poiId=eventData.getMatch_data().getPoi_id();
//		
//		transaction.setPoiId(poiId);
		transaction.setMaskedScore(eventData.getCrop_data().getMasked_score());
		transaction.setEventId(eventData.getEvent_id());
		transaction.setAppearanceId(eventData.getAppearance_data().getAppearance_id());
		transaction.setMaskStatus(eventData.getFace_features_data().getMask_outcome());
		transaction.setAge(eventData.getFace_features_data().getAge_group_outcome());
		transaction.setGender(eventData.getFace_features_data().getGender_outcome());
	}
}