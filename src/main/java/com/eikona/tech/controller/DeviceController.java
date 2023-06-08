package com.eikona.tech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eikona.tech.sync.DeviceSync;


@RestController
public class DeviceController {
	
	
	@Autowired
	private DeviceSync cameraSync;

	@GetMapping("/sync-camera")
	//@PreAuthorize("hasAuthority('device_sync')")
	public String syncDevice(Model model) {
		String message = null;
		try {
			message = cameraSync.syncDevice();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}
}
