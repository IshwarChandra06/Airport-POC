package com.eikona.tech.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eikona.tech.dto.PaginationDto;
import com.eikona.tech.entity.Transaction;
import com.eikona.tech.service.impl.TransactionServiceImpl;
import com.eikona.tech.util.ImageProcessingUtil;

@Controller
public class TransactionController {

	@Autowired
	private TransactionServiceImpl transactionService;
	
	@Autowired
	private ImageProcessingUtil imageProcessingUtil;

	@GetMapping({"/transaction","/"})
	@PreAuthorize("hasAuthority('transaction_view')")
	public String transactionList() {
		return "transaction/transaction_list";
	}
	
	
	@RequestMapping(value = "/api/search/transaction", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('transaction_view')")
	public @ResponseBody PaginationDto<Transaction> search(String sDate,String eDate,String device, int pageno, String sortField, String sortDir) {
		
		PaginationDto<Transaction> dtoList = transactionService.searchByField(sDate, eDate, device, pageno, sortField, sortDir);
		
		setTransactionImage(dtoList);
		return dtoList;
	}
	
	private void setTransactionImage(PaginationDto<Transaction> dtoList) {
		List<Transaction> eventsList = dtoList.getData();
		List<Transaction> transactionList = new ArrayList<Transaction>();
		for (Transaction trans : eventsList) {
			byte[] image = imageProcessingUtil.searchTransactionImage(trans);
			trans.setCropImageByte(image);
			transactionList.add(trans);
		}
		dtoList.setData(transactionList);
	}
}
