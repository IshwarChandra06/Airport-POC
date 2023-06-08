package com.eikona.tech.util;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.util.IOUtils;
import org.springframework.stereotype.Component;

import com.eikona.tech.entity.DailyReport;
import com.eikona.tech.entity.Transaction;

@Component
public class ImageProcessingUtil {


	public byte[] searchTransactionImage(Transaction trans) {

		byte[] bytes = null;
		try {
			if(null!=trans.getCropImagePath()) {
				String imagePath = trans.getCropImagePath();
				InputStream inputStream = new FileInputStream(imagePath);

				bytes = IOUtils.toByteArray(inputStream);
				Base64.encodeBase64String(bytes);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytes;
	}
	
	public byte[] searchTransactionImage(DailyReport dailyReport) {

		byte[] bytes = null;
		try {
			if(null!=dailyReport.getCropImagePath()) {
				String imagePath = dailyReport.getCropImagePath();
				InputStream inputStream = new FileInputStream(imagePath);

				bytes = IOUtils.toByteArray(inputStream);
				Base64.encodeBase64String(bytes);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytes;
	}
}
