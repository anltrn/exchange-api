package com.anil.turan.exchange.api.service;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.anil.turan.exchange.api.controller.request.RequestForNewExchange;
import com.anil.turan.exchange.api.controller.response.ResponseConversionList;
import com.anil.turan.exchange.api.controller.response.ResponseExchangeTransaction;

@Component
public interface ExchangeRateService {
	public Double getExchangeRate(String baseCurrency, String targetCurrency);
	
	public ResponseExchangeTransaction exchangeToNewCurrency(RequestForNewExchange request);
	
	public ResponseConversionList getConversionList(Date transactionDate, Long transactionId, int page, int size);
}
