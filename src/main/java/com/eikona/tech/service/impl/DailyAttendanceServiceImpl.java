package com.eikona.tech.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.eikona.tech.constants.ApplicationConstants;
import com.eikona.tech.constants.NumberConstants;
import com.eikona.tech.dto.PaginationDto;
import com.eikona.tech.entity.DailyReport;
import com.eikona.tech.entity.Transaction;
import com.eikona.tech.repository.DailyAttendanceRepository;
import com.eikona.tech.repository.TransactionRepository;
import com.eikona.tech.util.CalendarUtil;
import com.eikona.tech.util.GeneralSpecificationUtil;

@Service
@EnableScheduling
public class DailyAttendanceServiceImpl {

	@Autowired
	private DailyAttendanceRepository dailyAttendanceRepository;

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private CalendarUtil calendarUtil;
	
	@Autowired
	private GeneralSpecificationUtil<DailyReport> generalSpecificationDailyAttendance;
	
	@Scheduled(cron ="0 0 */3 * * *")
	public void autoGenerateDailyReport() {
		
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String toDay= format.format(new Date());
			
			Date endDate = format.parse(toDay);
			Date startDate = calendarUtil.getConvertedDate(endDate, -1);
				
			generateDailyAttendance(startDate, endDate);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<DailyReport> generateDailyAttendance(String startDate, String endDate) {
		
		if(!startDate.isEmpty() && !endDate.isEmpty()) {
			
			SimpleDateFormat frontendFormat = new SimpleDateFormat("dd/MM/yyyy");
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date sDate =null;
			Date eDate =null;
			try {
				Date sdate = frontendFormat.parse(startDate);
				Date edate = frontendFormat.parse(endDate);
				
				
				sDate = calendarUtil.getConvertedDate(format.parse(format.format(sdate)), 00,00,00);
				eDate= calendarUtil.getConvertedDate(format.parse(format.format(edate)), 23,59,59);
				return generateDailyAttendance(sDate, eDate);
			} catch (ParseException e) {
				e.printStackTrace();
				
				return null;
			}
		}
		
		return null;
		
	}
	
	public List<DailyReport> generateDailyAttendance(Date startDate, Date endDate) {

		List<DailyReport> dailyReportList = new ArrayList<>();
		
		try {
	
			List<Transaction> transactionList = transactionRepository.getTransactionDateCustom(startDate, endDate);

			for (Transaction transaction : transactionList) {
	
				DailyReport dailyReport = dailyAttendanceRepository.findByDateAndPoiIdCustom(startDate, endDate, transaction.getPoiId());
				
//				DailyReport dailyReport = dailyAttendanceRepository.findByPoiId(transaction.getPoiId());
				if(null == dailyReport) {
					
					dailyReport = new DailyReport();
					dailyReport.setPoiId(transaction.getPoiId());
					dailyReport.setAge(transaction.getAge());
					dailyReport.setGender(transaction.getGender());
					dailyReport.setDate(transaction.getPunchDate());
					dailyReport.setInTime(transaction.getPunchDate());
					dailyReport.setInTimeStr(transaction.getPunchTimeStr());
					dailyReport.setInMask(transaction.getMaskStatus());
					dailyReport.setInLocation(transaction.getDeviceName());
					dailyReport.setStayInHour(0l);
					
//					LocalTime empIn = LocalTime.parse(dailyReport.getInTime());
//					LocalTime empOut = LocalTime.now();
//					Long stayInHours = empIn.until(empOut, ChronoUnit.HOURS);
//					dailyReport.setStayInHour(stayInHours);

					dailyReport.setCropImagePath(transaction.getCropImagePath());
					dailyReport.setMissedOutPunch("Yes");
					dailyAttendanceRepository.save(dailyReport);
					
				} else {
					dailyReport.setOutTime(transaction.getPunchDate());
					dailyReport.setOutTimeStr(transaction.getPunchTimeStr());
					dailyReport.setOutMask(transaction.getMaskStatus());
					dailyReport.setOutLocation(transaction.getDeviceName());

					dailyReport.setMissedOutPunch("No");
					
					LocalTime empIn = LocalTime.parse(dailyReport.getInTimeStr());
					LocalTime empOut = LocalTime.parse(dailyReport.getOutTimeStr());

					Long stayInHours = empIn.until(empOut, ChronoUnit.HOURS);
					Long stayInMinutes = empIn.until(empOut, ChronoUnit.MINUTES);
					
					dailyReport.setStayInHour(stayInHours);
					if(stayInHours<0) {
						stayInHours = 24 + stayInHours;
						stayInMinutes = 60 + stayInMinutes;
					}

					dailyReport.setStayInTime(String.valueOf(stayInHours) + ":" + String.valueOf(stayInMinutes % 60));
					dailyAttendanceRepository.save(dailyReport);
				}
				
				dailyReportList.add(dailyReport);
			}
			
			dailyAttendanceRepository.saveAll(dailyReportList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dailyReportList;
	}

	public PaginationDto<DailyReport> searchByField(String sDate, String eDate, int pageno, String sortField,
			String sortDir) {

		Date startDate = null;
		Date endDate = null;
		if (!sDate.isEmpty() && !eDate.isEmpty()) {
			SimpleDateFormat format = new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_OF_US);
			try {
				startDate = format.parse(sDate);
				endDate = format.parse(eDate);
				
				endDate = calendarUtil.getConvertedDate(endDate, NumberConstants.TWENTY_THREE, NumberConstants.FIFTY_NINE, NumberConstants.FIFTY_NINE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (null == sortDir || sortDir.isEmpty()) {
			sortDir = ApplicationConstants.ASC;
		}
		if (null == sortField || sortField.isEmpty()) {
			sortField = ApplicationConstants.ID;
		}
		
		Page<DailyReport> page = getDailyAttendancePage(startDate, endDate, pageno, sortField, sortDir);
		List<DailyReport> employeeShiftList = page.getContent();
		
		List<DailyReport> dailyReportList = new ArrayList<>();
		for(DailyReport dailyReport: employeeShiftList) {
			
			if(null == dailyReport.getOutTimeStr()) {
				DateTimeFormatter localFormate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				LocalDateTime empIn = LocalDateTime.parse(format.format(dailyReport.getInTime()), localFormate);
				
				LocalDateTime empOut = LocalDateTime.now();
				
				Long hours = empIn.until(empOut, ChronoUnit.HOURS);
				Long minutes = empIn.until(empOut, ChronoUnit.MINUTES);
				
				dailyReport.setStayInTime(String.valueOf(hours) + ":" + String.valueOf(minutes % 60));
			}
			
			dailyReportList.add(dailyReport);
		}

		sortDir = (ApplicationConstants.ASC.equalsIgnoreCase(sortDir)) ? ApplicationConstants.DESC : ApplicationConstants.ASC;
		PaginationDto<DailyReport> dtoList = new PaginationDto<DailyReport>(dailyReportList,
				page.getTotalPages(), page.getNumber() + NumberConstants.ONE, page.getSize(), page.getTotalElements(),
				page.getTotalElements(), sortDir, ApplicationConstants.SUCCESS, ApplicationConstants.MSG_TYPE_S);
		
		return dtoList;
	}
	
	private Page<DailyReport> getDailyAttendancePage(Date startDate, Date endDate, int pageno, String sortField,
			String sortDir) {

		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Pageable pageable = PageRequest.of(pageno - NumberConstants.ONE, NumberConstants.TEN, sort);

		Specification<DailyReport> dateSpec = generalSpecificationDailyAttendance.dateSpecification(startDate, endDate, ApplicationConstants.DATE);
		Page<DailyReport> page = dailyAttendanceRepository.findAll(dateSpec, pageable);
		return page;
		
	}

	public PaginationDto<DailyReport> searchStayInHourException(String sDate, String eDate, int pageno,
			String sortField, String sortDir) {

		Date startDate = null;
		Date endDate = null;
		if (!sDate.isEmpty() && !eDate.isEmpty()) {
			SimpleDateFormat format = new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_OF_US);
			try {
				startDate = format.parse(sDate);
				endDate = format.parse(eDate);
				
				endDate = calendarUtil.getConvertedDate(endDate, NumberConstants.TWENTY_THREE, NumberConstants.FIFTY_NINE, NumberConstants.FIFTY_NINE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (null == sortDir || sortDir.isEmpty()) {
			sortDir = ApplicationConstants.ASC;
		}
		if (null == sortField || sortField.isEmpty()) {
			sortField = ApplicationConstants.ID;
		}
		
		Page<DailyReport> page = getStayInHourExceptionPage(startDate, endDate, pageno, sortField, sortDir);
		List<DailyReport> employeeShiftList = page.getContent();
		
		List<DailyReport> dailyReportList = new ArrayList<>();
		for(DailyReport dailyReport: employeeShiftList) {
			
			if(null == dailyReport.getOutTimeStr()) {
				DateTimeFormatter localFormate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				LocalDateTime empIn = LocalDateTime.parse(format.format(dailyReport.getInTime()), localFormate);
				
				LocalDateTime empOut = LocalDateTime.now();
				
				Long hours = empIn.until(empOut, ChronoUnit.HOURS);
				Long minutes = empIn.until(empOut, ChronoUnit.MINUTES);
				
				dailyReport.setStayInTime(String.valueOf(hours) + ":" + String.valueOf(minutes % 60));
			}
			
			dailyReportList.add(dailyReport);
		}

		sortDir = (ApplicationConstants.ASC.equalsIgnoreCase(sortDir)) ? ApplicationConstants.DESC : ApplicationConstants.ASC;
		PaginationDto<DailyReport> dtoList = new PaginationDto<DailyReport>(dailyReportList,
				page.getTotalPages(), page.getNumber() + NumberConstants.ONE, page.getSize(), page.getTotalElements(),
				page.getTotalElements(), sortDir, ApplicationConstants.SUCCESS, ApplicationConstants.MSG_TYPE_S);
		return dtoList;
	}
	
	private Page<DailyReport> getStayInHourExceptionPage(Date startDate, Date endDate, int pageno, String sortField,
			String sortDir) {
		Date date = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateStr= format.format(new Date());
			date = calendarUtil.getConvertedDate(format.parse(dateStr), -3);
				
		}catch (Exception e) {
			e.printStackTrace();
		}

		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Pageable pageable = PageRequest.of(pageno - NumberConstants.ONE, NumberConstants.TEN, sort);

		Specification<DailyReport> dateSpec = generalSpecificationDailyAttendance.dateSpecification(startDate, endDate, ApplicationConstants.DATE);
		Specification<DailyReport> missedOutPunchSpec = generalSpecificationDailyAttendance.stringSpecification("Yes", "missedOutPunch");
		Specification<DailyReport> lessThanSpec = generalSpecificationDailyAttendance.dateCompareSpecification(date, "inTime");
		Specification<DailyReport> greaterThanSpec = generalSpecificationDailyAttendance.greaterThanSpecification(3, "stayInHour");
		Page<DailyReport> page = dailyAttendanceRepository.findAll(dateSpec.and(greaterThanSpec.or(missedOutPunchSpec.and(lessThanSpec))), pageable);
		
		return page;
		
	}
}