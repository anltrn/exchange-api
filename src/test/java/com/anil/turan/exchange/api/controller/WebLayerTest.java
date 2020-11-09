package com.anil.turan.exchange.api.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.anil.turan.exchange.api.controller.request.RequestForNewExchange;
import com.anil.turan.exchange.api.controller.response.ResponseConversionList;
import com.anil.turan.exchange.api.controller.response.ResponseExchangeTransaction;
import com.anil.turan.exchange.api.model.dao.TransactionRepository;
import com.anil.turan.exchange.api.model.entity.Transaction;
import com.anil.turan.exchange.api.service.ExchangeRateService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ExchangeRateApiController.class)
public class WebLayerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ExchangeRateService service;
	
	@MockBean
	private TransactionRepository repo;
	
	@Test
	public void When_Same_Currency_Should_Return_Input_Given_Rate() throws Exception {
		when(service.getExchangeRate("USD", "USD")).thenReturn(Double.valueOf("8.0"));
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("baseCurrency", "USD");
		params.add("targetCurrency", "USD");
		this.mockMvc.perform(get("/rateapi").params(params)).andDo(print()).
		andExpect(status().isOk()).andExpect(content().string("8.0"));
	}
	
	@Test
	public void When_BaseAndTarget_CurrencyEmpty_Return_ErrorMessage() throws Exception {	
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("baseCurrency", "");
		params.add("targetCurrency", "");
		this.mockMvc.perform(get("/rateapi").params(params)).andDo(print()).
		andExpect(status().isBadRequest()).andExpect(content().string(containsString("TargetCurrency and BaseCurrency can't be empty")));
	}
	
	@Test
	public void When_Base_Currency_Empty_Return_ErrorMessage() throws Exception {	
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("baseCurrency", "");
		params.add("targetCurrency", "USD");
		this.mockMvc.perform(get("/rateapi").params(params)).andDo(print()).
		andExpect(status().isBadRequest()).andExpect(content().string(containsString("BaseCurrency can't be empty")));
	}
	
	@Test
	public void When_Target_Currency_Empty_Return_ErrorMessage() throws Exception {	
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("baseCurrency", "USD");
		params.add("targetCurrency", "");
		this.mockMvc.perform(get("/rateapi").params(params)).andDo(print()).
		andExpect(status().isBadRequest()).andExpect(content().string(containsString("TargetCurrency can't be empty")));
	}

	
	@Test
	public void When_Mock_Should_Return_TotalPage_Value_22() throws Exception {
		ResponseConversionList response = new ResponseConversionList();
		response.setCurrentPage(1);
		response.setTotalPage(22);
		response.setTransactioList(new ArrayList<Transaction>());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date   dateString       = format.parse ( "2020-11-09" );    
		when(service.getConversionList(dateString, 1L, 1, 10)).thenReturn(response);		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("transactionDate", "2020-11-09");
		params.add("transactionId", "1");
		params.add("page", "1");
		params.add("size", "10");
		mockMvc.perform(get("/conversionlist")
				.params(params)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.totalPage").value(22));
	}
	
	@Test
	public void When_TransactionDate_And_TransactionId_Empty_Should_Return_Error() throws Exception {			
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("transactionDate", "");
		params.add("transactionId", "");
		params.add("page", "1");
		params.add("size", "10");
		mockMvc.perform(get("/conversionlist")
				.params(params)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errors").value("Transaction date and transaction id can't be null"));
	}
	
	@Test
	public void When_TransactionDate_NotSuitable_Should_Return_Error() throws Exception {			
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("transactionDate", "78978978978");
		params.add("transactionId", "");
		params.add("page", "1");
		params.add("size", "10");
		mockMvc.perform(get("/conversionlist")
				.params(params)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Failed to convert value of type")));
	}
	
	@Test
	public void When_TransactionId_NotSuitable_Should_Return_Error() throws Exception {			
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("transactionDate", "");
		params.add("transactionId", "-5");
		params.add("page", "1");
		params.add("size", "10");
		mockMvc.perform(get("/conversionlist")
				.params(params)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errors").value("Transaction id can't be lower than 0(zero)"));
	}

	
	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
	@Test
	public void When_Mock_Should_Return_TransactionId_77() throws Exception {;
	RequestForNewExchange req = new RequestForNewExchange();
	req .setAmount(new BigDecimal(60));
	req.setBaseCurrency("USD");
	req.setTargetCurrency("TRY");
	ResponseExchangeTransaction resp = new ResponseExchangeTransaction();
	resp .setConvertedAmount(new BigDecimal(90));
	resp.setTransactionId(77L);
	when(service.exchangeToNewCurrency(req)).thenReturn(resp);
	mockMvc.perform(post("/conversionapi")
			.content(asJsonString(req))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("$.transactionId").value(77));
	}
	
	@Test
	public void When_BaseCurrency_Empty_Should_Return_ErrorMessage() throws Exception {;
	RequestForNewExchange req = new RequestForNewExchange();
	req .setAmount(new BigDecimal(60));
	req.setBaseCurrency("");
	req.setTargetCurrency("USD");
	mockMvc.perform(post("/conversionapi")
			.content(asJsonString(req))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("$.errors").value("BaseCurrency can't ben null or empty"));
	}
	
	@Test
	public void When_TargetCurrency_Empty_Should_Return_ErrorMessage() throws Exception {;
	RequestForNewExchange req = new RequestForNewExchange();
	req .setAmount(new BigDecimal(60));
	req.setBaseCurrency("USD");
	req.setTargetCurrency("");
	mockMvc.perform(post("/conversionapi")
			.content(asJsonString(req))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("$.errors").value("TargetCurrency can't ben null or empty"));
	}
	
	@Test
	public void When_Inputs_Empty_Should_Return_ErrorMessage() throws Exception {;
	RequestForNewExchange req = new RequestForNewExchange();
	req .setAmount(null);
	req.setBaseCurrency("");
	req.setTargetCurrency("");
	mockMvc.perform(post("/conversionapi")
			.content(asJsonString(req))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("$.errors").value("Targetcurrency & baseCurrency & amount can't not be null"));
	}

	@Test
	public void When_Amount_LowerThan_Zero_Should_Return_ErrorMessage() throws Exception {;
	RequestForNewExchange req = new RequestForNewExchange();
	req .setAmount(BigDecimal.ONE.negate());
	req.setBaseCurrency("USD");
	req.setTargetCurrency("GBP");
	mockMvc.perform(post("/conversionapi")
			.content(asJsonString(req))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("$.errors").value("Amount can't be null or smaller than zero"));
	}
}
