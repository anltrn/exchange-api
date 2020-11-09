package com.anil.turan.exchange.api.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.anil.turan.exchange.api.controller.request.RequestForNewExchange;
import com.anil.turan.exchange.api.controller.response.ResponseConversionList;
import com.anil.turan.exchange.api.service.ExchangeRateService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class MockWebTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private ExchangeRateService service;
	

	@Test
	public void shouldReturnOneRate() throws Exception {
		this.mockMvc.perform(get("/rateapi?baseCurrency=USD&targetCurrency=USD")).
		andDo(print()).andExpect(status().isOk()).
		andExpect(content().string(containsString("1.0")));
	}
	
	@Test
	public void shouldReturnGivenAmount() throws Exception {
		RequestForNewExchange request = new RequestForNewExchange();
		request.setAmount(BigDecimal.TEN);
		request.setTargetCurrency("USD");
		request.setBaseCurrency("USD");
		this.mockMvc.perform(post("/conversionapi").
		contentType(MediaType.APPLICATION_JSON).
		accept(MediaType.APPLICATION_JSON).
		content(asJsonString(request))).
		andDo(print()).andExpect(status().isOk()).
		andExpect(content().string(containsString("10")));
	}
	
	@Test
	public void shouldReturnNoRecord() throws Exception {
		ResponseConversionList response = new ResponseConversionList();
		response.setCurrentPage(1);
		response.setTotalPage(22);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("transactionDate", "2025-11-09");
		//params.add("transactionId", "1");
		params.add("page", "1");
		params.add("size", "10");
		this.mockMvc.perform(get("/conversionlist")
				.params(params)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("No record")));
	}
	
	
	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
}
