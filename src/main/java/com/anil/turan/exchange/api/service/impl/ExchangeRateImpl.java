package com.anil.turan.exchange.api.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.anil.turan.exchange.api.controller.ExchangeRateApiController;
import com.anil.turan.exchange.api.controller.request.RequestForNewExchange;
import com.anil.turan.exchange.api.controller.response.ResponseConversionList;
import com.anil.turan.exchange.api.controller.response.ResponseExchangeRateDTO;
import com.anil.turan.exchange.api.controller.response.ResponseExchangeTransaction;
import com.anil.turan.exchange.api.exception.ApiRequestException;
import com.anil.turan.exchange.api.model.dao.TransactionRepository;
import com.anil.turan.exchange.api.model.entity.Transaction;
import com.anil.turan.exchange.api.service.ExchangeRateService;
import com.google.gson.Gson;

@Service
public class ExchangeRateImpl implements ExchangeRateService {

	private static final Logger logger = LoggerFactory.getLogger(ExchangeRateApiController.class);

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	public Double getExchangeRate(String baseCurrency, String targetCurrency) {
		String url = "https://api.ratesapi.io/api/latest?base="+baseCurrency+"&symbols="+targetCurrency;
		logger.info(url);
	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
	    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
	    ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
	    Gson gson= new Gson();
	    ResponseExchangeRateDTO exchangeDTO = gson.fromJson(res.getBody(), ResponseExchangeRateDTO.class);
	    Double currencyRate = exchangeDTO.getRates().get(targetCurrency);
		return currencyRate;

	}

	@Override
	public ResponseExchangeTransaction exchangeToNewCurrency(RequestForNewExchange request) {
		Double rate = getExchangeRate(request.getBaseCurrency(), request.getTargetCurrency());
		ResponseExchangeTransaction response = new ResponseExchangeTransaction();		
		response.setConvertedAmount(request.getAmount().multiply(BigDecimal.valueOf(rate)));
		Transaction entity = new Transaction();
		entity.setTransactionDate(new Date());
		entity.setAmount(request.getAmount());
		entity.setConvertedAmount(response.getConvertedAmount());
		entity.setSourceCurrency(request.getBaseCurrency());
		entity.setTargetCurrency(request.getTargetCurrency());
		Transaction savedData = transactionRepository.save(entity);
		response.setTransactionId(savedData.getTransactionId());
		return response;
	}
	
	@Override
	public ResponseConversionList getConversionList(Date transactionDate, Long transactionId , int page, int size ) {
		ResponseConversionList response = new ResponseConversionList();
		List<Transaction> transactionList = new ArrayList<Transaction>();
		if(transactionId != null) {
			Optional<Transaction> record = Optional.ofNullable(transactionRepository.findById(transactionId)
					.orElseThrow(() -> new NoSuchElementException("No record found with input transactionid:"+transactionId)));
			transactionList.add(record.get());
			response.setTransactioList(transactionList);
		}
		else {
			Pageable paging = PageRequest.of(page, size, Sort.by("transactionId").descending());
			Page transactionPage =  transactionRepository.findAllByTransactionDate(transactionDate, paging);
			if(transactionPage == null || transactionPage.isEmpty()) {
				throw new ApiRequestException("No record found with input transcationDate: "+transactionDate);
			}			
			transactionList = transactionPage.getContent();
			response.setCurrentPage(transactionPage.getNumber());
			response.setTotalPage(transactionPage.getTotalPages());
			response.setTotalElements(transactionPage.getNumberOfElements());	
			response.setTransactioList(transactionList);
		}
		return response;
	}

}
