package com.eikona.tech.sync;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eikona.tech.constants.ApplicationConstants;
import com.eikona.tech.constants.CorsightDeviceConstants;
import com.eikona.tech.constants.MessageConstants;
import com.eikona.tech.constants.NumberConstants;
import com.eikona.tech.entity.Device;
import com.eikona.tech.repository.DeviceRepository;
import com.eikona.tech.util.RequestExecutionUtil;

@Component
public class DeviceSync {

	@Autowired
	private DeviceRepository cameraRepository;
	
	@Autowired
	private RequestExecutionUtil requestExecutionUtil;
	
	@Value("${corsight.host.url}")
    private String corsightHost;
	
	@Value("${corsight.camera.port}")
    private String cameraPort;
    
	public String syncDevice() {
		try {

			String url = ApplicationConstants.HTTPS_COLON_DOUBLE_SLASH+corsightHost+ApplicationConstants.DELIMITER_COLON+cameraPort;
			String addUrl = CorsightDeviceConstants.CAMERA_API;

			String responeData = requestExecutionUtil.executeHttpsGetRequest(url, addUrl);
 
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonResponse = (JSONObject) jsonParser.parse(responeData);
			JSONObject jsonResponseData = (JSONObject) jsonResponse.get(CorsightDeviceConstants.DATA);
			JSONArray jsonArray = (JSONArray) jsonResponseData.get(CorsightDeviceConstants.CAMERAS);
			List<Device> cameraList = new ArrayList<>();
			
			for (int i = NumberConstants.ZERO; i < jsonArray.size(); i++) {
				JSONObject jsonData = (JSONObject) jsonArray.get(i);
				Device cameraObj = new Device();
				Device camera = cameraRepository.findByCameraId((String) jsonData.get(CorsightDeviceConstants.CAMERA_ID));
				if (null == camera) {

					cameraObj.setCameraId((String) jsonData.get(CorsightDeviceConstants.CAMERA_ID));

				
				cameraObj.setName((String) jsonData.get(CorsightDeviceConstants.DESCRIPTION));
				cameraList.add(cameraObj);

			}
		}
			cameraRepository.saveAll(cameraList);
			return MessageConstants.SYNC_SUCCESSFULLY;
		} catch (Exception e) {
			e.printStackTrace();
			return MessageConstants.SYNC_FAILED;
		}
	}


	
	}

