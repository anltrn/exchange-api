package com.anil.turan.exchange.api.controller.response;

import java.util.Map;

public class ResponseExchangeRateDTO {
	
	private Map<String,Double> rates;
	private String base;
	private String date;
	
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Map<String,Double> getRates() {
		return rates;
	}
	public void setRates(Map<String,Double> rates) {
		this.rates = rates;
	}
}
