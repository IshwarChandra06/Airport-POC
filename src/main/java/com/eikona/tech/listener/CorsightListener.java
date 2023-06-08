package com.eikona.tech.listener;

import java.text.ParseException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.eikona.tech.constants.ApplicationConstants;
import com.eikona.tech.service.impl.CorsightListenerServiceImpl;

@RestController
public class CorsightListener {
	
	@Autowired
	private CorsightListenerServiceImpl corsightListenerServiceImpl;
	
	
	
	@PostMapping(path = "/corsight/events")
	public ResponseEntity<String> corsightHeartReportInfo(@RequestBody JSONObject request) throws ParseException {
       try {
    	   Thread.sleep(120);
		corsightListenerServiceImpl.corsightHeartReportInfo(request);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		

		JSONObject response = new JSONObject();

		response.put(ApplicationConstants.STATUS, ApplicationConstants.SUCCESS);

		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}
	
}
