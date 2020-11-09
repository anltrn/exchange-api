package com.anil.turan.exchange.api.controller.response;

import java.math.BigDecimal;

public class ResponseExchangeTransaction {
		private BigDecimal convertedAmount;
		private Long transactionId;
		
		public BigDecimal getConvertedAmount() {
			return convertedAmount;
		}
		public void setConvertedAmount(BigDecimal convertedAmount) {
			this.convertedAmount = convertedAmount;
		}
		public Long getTransactionId() {
			return transactionId;
		}
		public void setTransactionId(Long transactionId) {
			this.transactionId = transactionId;
		}
		@Override
		public String toString() {
			return "ResponseForExchange [convertedAmount=" + convertedAmount + ", transactionId=" + transactionId + "]";
		}
}
