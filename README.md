# exchange-api

A simple foreign exchange application which is one of the most common services used in financial applications.

Functional Requirements:
1. Exchange Rate API<br/>
input: currency pair to retrieve the exchange rate<br/>
output: exchange rate<br/>
2. Conversion API:<br/>
input: source amount, source currency, target currency<br/>
output: amount in target currency, and transaction id.<br/>
3. Conversion List API<br/>
input: transaction id or transaction date (at least one of the inputs shall be provided for each call)<br/>
output: list of conversions filtered by the inputs and paging is required<br/>

- Exchange Rate API / getExchangeRate Method (GET)<br/>
input: String baseCurrency, String targetCurrency<br/>
output: Double rate<br/>
Url:<br/>
/rateapi?baseCurrency=USD&targetCurrency=TRY<br/>

- Conversion API / exchangeToNewCurrency (POST)<br/>
input: RequestForNewExchange / String baseCurrency; String targetCurrency; BigDecimal amount<br/>
output: ResponseForExchange / BigDecimal convertedAmount;  Long transactionId;<br/>
Url:<br/>
/conversionapi<br/>

- Conversion List API /bgetConversionList (GET)<br/>
input:Date transactionDate , Long transactionId, <br/>
@RequestParam(value = "page", required = false, defaultValue = "1") int page, <br/>
@RequestParam(value = "size", required = false, defaultValue = "3") int size)<br/><br/>
output:<br/>
ResponseConversionList /  List<Transaction> transactioList; int totalPage; int currentPage; int totalElements;<br/>
Url:<br/>
/conversionlist<br/>


Swagger link:<br/>
http://localhost:yourport/swagger-ui.html#/
