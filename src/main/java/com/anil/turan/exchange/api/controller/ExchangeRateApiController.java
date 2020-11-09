package com.anil.turan.exchange.api.controller;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anil.turan.exchange.api.controller.request.RequestForNewExchange;
import com.anil.turan.exchange.api.controller.response.ResponseConversionList;
import com.anil.turan.exchange.api.controller.response.ResponseExchangeTransaction;
import com.anil.turan.exchange.api.exception.ApiRequestException;
import com.anil.turan.exchange.api.service.ExchangeRateService;

@RestController
public class ExchangeRateApiController {
	private static final Logger logger = LoggerFactory.getLogger(ExchangeRateApiController.class);
	private ExchangeRateService exchangeRateService;
	
	public ExchangeRateApiController(ExchangeRateService exchangeRateService) {
		this.exchangeRateService = exchangeRateService;		
	}
	
	@GetMapping("/rateapi")
	public ResponseEntity<?> getExchangeRate(@RequestParam String baseCurrency, @RequestParam String targetCurrency) {
		logger.info("BaseCurrency :" + baseCurrency + " TargetCurrency :" + targetCurrency);
		if(StringUtils.isEmpty(baseCurrency) && StringUtils.isEmpty(targetCurrency)) {
			throw new ApiRequestException("TargetCurrency and BaseCurrency can't be empty");
		}
		else if(StringUtils.isEmpty(baseCurrency)) {
			throw new ApiRequestException("BaseCurrency can't be empty");
		}
		else if(StringUtils.isEmpty(targetCurrency)) {
			throw new ApiRequestException("TargetCurrency can't be empty");
		}
		Double rate = exchangeRateService.getExchangeRate(baseCurrency, targetCurrency);
		logger.info("Rate :" + rate);
		return (ResponseEntity<?>) ResponseEntity.ok(rate);
	}
	
	@PostMapping("/conversionapi")
	public ResponseEntity<ResponseExchangeTransaction> exchangeToNewCurrency(@RequestBody RequestForNewExchange request) {
		logger.info(request.toString());
		if(request.getAmount() == null && 
				StringUtils.isEmpty(request.getBaseCurrency()) 
				&& StringUtils.isEmpty(request.getTargetCurrency())) {
			throw new ApiRequestException("Targetcurrency & baseCurrency & amount can't not be null");
		}
		else if(request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ONE) < 0) {
			throw new ApiRequestException("Amount can't be null or smaller than zero");
		}
		else if(StringUtils.isEmpty(request.getBaseCurrency()) ) {
			throw new ApiRequestException("BaseCurrency can't ben null or empty");
		}
		else if(StringUtils.isEmpty(request.getTargetCurrency())) {
			throw new ApiRequestException("TargetCurrency can't ben null or empty");
		}
		ResponseExchangeTransaction result = exchangeRateService.exchangeToNewCurrency(request);
		logger.info(result.toString());
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/conversionlist")
	public ResponseEntity<ResponseConversionList> getConversionList(@RequestParam(required = false)  @DateTimeFormat(pattern="yyyy-MM-dd") Date transactionDate , @RequestParam (required = false) Long transactionId,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page, 
			@RequestParam(value = "size", required = false, defaultValue = "3") int size) {
		logger.info("Transaction Date: "+transactionDate+" TransactionId: "+transactionId+" Page: "+page+" Size: "+size );
		if(transactionDate == null && transactionId == null) {
			throw new ApiRequestException("Transaction date and transaction id can't be null");
		}
		else if(transactionId != null && transactionId.compareTo(0L) < 0) {
			throw new ApiRequestException("Transaction id can't be lower than 0(zero)");
		}
		ResponseConversionList result = exchangeRateService.getConversionList(transactionDate, transactionId, page, size);
		logger.info((result.getTransactioList().toString()));
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
}
