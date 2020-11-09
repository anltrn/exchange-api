package com.anil.turan.exchange.api.controller.request;

import java.math.BigDecimal;

public class RequestForNewExchange {
	
	private String baseCurrency;
	private String targetCurrency;
	private BigDecimal amount;
	
	
	public String getTargetCurrency() {
		return targetCurrency;
	}
	public void setTargetCurrency(String targetCurrency) {
		this.targetCurrency = targetCurrency;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "RequestForNewExchange [baseCurrency=" + baseCurrency + ", targetCurrency=" + targetCurrency
				+ ", amount=" + amount + "]";
	}
	public String getBaseCurrency() {
		return baseCurrency;
	}
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}
	

}
