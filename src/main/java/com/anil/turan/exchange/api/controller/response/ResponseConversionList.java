package com.anil.turan.exchange.api.controller.response;

import java.util.List;

import com.anil.turan.exchange.api.model.entity.Transaction;

public class ResponseConversionList {

	private List<Transaction> transactioList;
	private int totalPage;
	private int currentPage;
	private int totalElements;
	
	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
	}

	public List<Transaction> getTransactioList() {
		return transactioList;
	}

	public void setTransactioList(List<Transaction> transactioList) {
		this.transactioList = transactioList;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
}
