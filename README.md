# exchange-api

A simple foreign exchange application which is one of the most common services used in financial applications.

Functional Requirements:
1. Exchange Rate API
○ input: currency pair to retrieve the exchange rate
○ output: exchange rate
2. Conversion API:
○ input: source amount, source currency, target currency
○ output: amount in target currency, and transaction id.
3. Conversion List API
○ input: transaction id or transaction date (at least one of the inputs shall be provided for each call)
○ output: list of conversions filtered by the inputs and paging is required

1. getExchangeRate Method (GET)
input: String baseCurrency, String targetCurrency
output: Double rate

Url:
/rateapi?baseCurrency=USD&targetCurrency=TRY

2. exchangeToNewCurrency (POST)
input: RequestForNewExchange / String baseCurrency; String targetCurrency; BigDecimal amount
output: ResponseForExchange / BigDecimal convertedAmount;  Long transactionId;

Url:
/conversionapi

3. getConversionList (GET)
input:
Date transactionDate , 
Long transactionId, 
@RequestParam(value = "page", required = false, defaultValue = "1") int page, 
@RequestParam(value = "size", required = false, defaultValue = "3") int size)
output:
ResponseConversionList /  List<Transaction> transactioList; int totalPage; int currentPage; int totalElements;

Url:
/conversionlist



Swagger link:
http://localhost:yourport/swagger-ui.html#/
