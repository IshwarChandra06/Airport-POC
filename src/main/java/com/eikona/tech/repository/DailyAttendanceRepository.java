package com.eikona.tech.repository;

import java.util.Date;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eikona.tech.entity.DailyReport;

@Repository
public interface DailyAttendanceRepository extends DataTablesRepository<DailyReport, Long> {

	@Query("select d from com.eikona.tech.entity.DailyReport d where d.date >=:sDate and d.date <=:eDate and d.poiId =:poiId")
	DailyReport findByDateAndPoiIdCustom(Date sDate, Date eDate, String poiId);

	@Query("select d from com.eikona.tech.entity.DailyReport d where d.poiId =:poiId and d.outTime is null")
	DailyReport findByPoiId(String poiId);

}
