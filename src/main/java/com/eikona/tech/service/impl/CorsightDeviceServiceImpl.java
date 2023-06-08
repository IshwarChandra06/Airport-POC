package com.eikona.tech.service.impl;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eikona.tech.constants.ApplicationConstants;
import com.eikona.tech.constants.CorsightDeviceConstants;
import com.eikona.tech.constants.NumberConstants;
import com.eikona.tech.util.RequestExecutionUtil;
@Component
public class CorsightDeviceServiceImpl {
	
	@Autowired
	private RequestExecutionUtil requestExecutionUtil;
	
	@Value("${corsight.host.url}")
    private String corsightHost;
	
	@Value("${corsight.poi.port}")
    private String poiPort;
	
	public String addEmployeeToDevice(String base64) {

		String pioId = null;
		try {
			JSONObject requestObj = getAddPoiRequest(base64);

			String poiUrl = ApplicationConstants.HTTPS_COLON_DOUBLE_SLASH + corsightHost + ApplicationConstants.DELIMITER_COLON + poiPort;
			String addPoiUrl =CorsightDeviceConstants.POI_API_ADD_PERSON;

			String responeData = requestExecutionUtil.executeHttpsPostRequest(requestObj, poiUrl, addPoiUrl);

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonResponse = (JSONObject) jsonParser.parse(responeData);
			JSONObject jsonResponseData = (JSONObject) jsonResponse.get(CorsightDeviceConstants.DATA);
			JSONArray jsonArray = (JSONArray) jsonResponseData.get(CorsightDeviceConstants.POIS);
			System.out.println(jsonResponse.toString());

			for (int i = NumberConstants.ZERO; i < jsonArray.size(); i++) {
				JSONObject currobj = (JSONObject) jsonArray.get(i);
				pioId = (String) currobj.get(CorsightDeviceConstants.POI_ID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pioId;

	}

	@SuppressWarnings(ApplicationConstants.UNCHECKED)
	private JSONObject getAddPoiRequest(String base64) throws IOException {
		JSONObject requestObj = new JSONObject();
		JSONArray poiArray = new JSONArray();

		JSONObject poiObj = new JSONObject();
		JSONObject faceObj = new JSONObject();
		JSONObject imgPayloadObj = new JSONObject();
		

		poiObj.put(CorsightDeviceConstants.DISPLAY_IMG, base64);
		
		faceObj.put(CorsightDeviceConstants.FORCE, false);
		faceObj.put(CorsightDeviceConstants.SAVE_CROP, true);
		
		imgPayloadObj.put(CorsightDeviceConstants.IMG, base64);
		faceObj.put(CorsightDeviceConstants.IMAGE_PAYLOAD,imgPayloadObj);
		poiObj.put(CorsightDeviceConstants.FACE, faceObj);
		poiArray.add(poiObj);

		requestObj.put(CorsightDeviceConstants.POIS, poiArray);
		return requestObj;
	}
	public String addPoiToWatchList(String watchlist, String poi) {
		try {

			JSONObject requestObj = getJsonObjectToAddPoiToWatchList(watchlist, poi);

			String httpsUrl = ApplicationConstants.HTTPS_COLON_DOUBLE_SLASH + corsightHost + ApplicationConstants.DELIMITER_COLON + poiPort;
			String postUrl = CorsightDeviceConstants.POI_API_WATCHLIST_ADD;

			String responeData = requestExecutionUtil.executeHttpsPostRequest(requestObj, httpsUrl, postUrl);
			System.out.println(responeData);
			return ApplicationConstants.COMPLETED;

		} catch (Exception e) {
			e.printStackTrace();
			return CorsightDeviceConstants.EMP_NOT_ADDED_TO_WATCHLIST;
		}
	}

	@SuppressWarnings(ApplicationConstants.UNCHECKED)
	private JSONObject getJsonObjectToAddPoiToWatchList(String watchlistId, String poi) {
		JSONObject requestObj = new JSONObject();
		JSONArray poiArray = new JSONArray();

		poiArray.add(poi);

		requestObj.put(CorsightDeviceConstants.POIS, poiArray);
		requestObj.put(CorsightDeviceConstants.WATCHLIST_ID, watchlistId);
		return requestObj;
	}
}
