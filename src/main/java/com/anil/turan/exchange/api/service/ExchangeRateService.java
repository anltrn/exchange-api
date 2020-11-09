package com.anil.turan.exchange.api.service;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.anil.turan.exchange.api.controller.request.RequestForNewExchange;
import com.anil.turan.exchange.api.controller.response.ResponseConversionList;
import com.anil.turan.exchange.api.controller.response.ResponseForExchange;

@Component
public interface ExchangeRateService {
	public Double getExchangeRate(String currencySymbol1, String currencySymbol2);
	
	public ResponseForExchange exchangeToNewCurrency(RequestForNewExchange request);
	
	public ResponseConversionList getConversionList(Date transactionDate, Long transactionId, int page, int size);
}
