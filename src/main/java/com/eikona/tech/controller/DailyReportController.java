package com.eikona.tech.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eikona.tech.dto.PaginationDto;
import com.eikona.tech.entity.DailyReport;
import com.eikona.tech.service.impl.DailyAttendanceServiceImpl;
import com.eikona.tech.util.ImageProcessingUtil;

@Controller
public class DailyReportController {

	@Autowired
	private DailyAttendanceServiceImpl dailyAttendanceService;
	
	@Autowired
	private ImageProcessingUtil imageProcessingUtil;
	
	@GetMapping("/daily-reports")
	@PreAuthorize("hasAuthority('dailyreport_view')")
	public String viewHomePage(Model model) {
		return "reports/daily_report";
	}

	
	@GetMapping("/stay-in-hour/exception")
	@PreAuthorize("hasAuthority('dailyreport_view')")
	public String stayInHourException(Model model) {
		return "reports/stay_in_hour_exception";
	}
	
	
	@RequestMapping(value = "/generate/daily-reports", method = RequestMethod.GET)
	public String generateDailyReportsPage(Model model) {
//		, Principal principal
//		User userObj = userRepository.findByUserNameAndIsDeletedFalse(principal.getName());
//		String orgName = userObj.getOrganization().getName();
//		model.addAttribute("organizationList", orgName);
		return "reports/generate_daily_report";
	}
	
	@RequestMapping(value = "/api/search/daily-reports", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('dailyreport_view')")
	public @ResponseBody PaginationDto<DailyReport> search(String sDate,String eDate,int pageno, String sortField, String sortDir) {
		
		PaginationDto<DailyReport> dtoList = dailyAttendanceService.searchByField(sDate, eDate, pageno, sortField, sortDir);
		setTransactionImage(dtoList);
		
		return dtoList;
	}
	
	@RequestMapping(value = "/api/search/stay-in-hour/exception", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('dailyreport_view')")
	public @ResponseBody PaginationDto<DailyReport> searchStayInHourException(String sDate,String eDate,int pageno, String sortField, String sortDir) {
		
		PaginationDto<DailyReport> dtoList = dailyAttendanceService.searchStayInHourException(sDate, eDate, pageno, sortField, sortDir);
		setTransactionImage(dtoList);
		
		return dtoList;
	}
	
	@RequestMapping(value = "/get/data-by-organization", method = RequestMethod.GET)
	public @ResponseBody List<DailyReport> generateDailyReports(String sDate, String eDate) {
//		, Principal principal
//		User userObj = userRepository.findByUserNameAndIsDeletedFalse(principal.getName());
//		String orgName = (null == userObj.getOrganization()? null : userObj.getOrganization().getName());
		
		return dailyAttendanceService.generateDailyAttendance(sDate, eDate);
		
	}
	
//	@RequestMapping(value="/api/daily-attendance/export-to-file",method = RequestMethod.GET)
//	@PreAuthorize("hasAuthority('dailyreport_export')")
//	public void exportToFile(HttpServletResponse response, Long id, String sDate, String eDate, String employeeName,String employeeId, 
//			String designation, String department,String company,String status,String shift, String flag, Principal principal) {
//		User userObj = userRepository.findByUserNameAndIsDeletedFalse(principal.getName());
//		String orgName = (null == userObj.getOrganization()? null : userObj.getOrganization().getName());
//		 response.setContentType("application/octet-stream");
//			DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
//			String currentDateTime = dateFormat.format(new Date());
//			String headerKey = "Content-Disposition";
//			String headerValue = "attachment; filename=Daily_Report" + currentDateTime + "."+flag;
//			response.setHeader(headerKey, headerValue);
//		try {
//			exportDailyReports.fileExportBySearchValue(response, id, sDate, eDate, employeeName,employeeId, designation, department,company, status,shift, flag, orgName );
//		} catch (Exception  e) {
//			e.printStackTrace();
//		}
//	}
	
	private void setTransactionImage(PaginationDto<DailyReport> dtoList) {
		List<DailyReport> dailyList = dtoList.getData();
		List<DailyReport> dailyReportList = new ArrayList<DailyReport>();
		for (DailyReport dailyReport : dailyList) {
			byte[] image = imageProcessingUtil.searchTransactionImage(dailyReport);
			dailyReport.setCropImageByte(image);
			dailyReportList.add(dailyReport);
		}
		dtoList.setData(dailyReportList);
	}
}
