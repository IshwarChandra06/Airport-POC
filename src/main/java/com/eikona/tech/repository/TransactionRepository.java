package com.eikona.tech.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eikona.tech.entity.Transaction;

@Repository
public interface TransactionRepository extends DataTablesRepository<Transaction, Long>{

	Transaction findByAppearanceId(String appearance_id);
	
	@Query("SELECT tr FROM com.eikona.tech.entity.Transaction as tr where tr.punchDate >=:sDate and tr.punchDate <=:eDate "
			+ "and tr.poiId is not null order by tr.punchDateStr asc, tr.punchTimeStr asc")
	List<Transaction> getTransactionDateCustom(Date sDate, Date eDate);
	
}
