package kpi.ye.currencyexchange.exchangerate;

import org.junit.Before;
import org.junit.Test;
import kpi.ye.currencyexchange.client.HttpClient;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ExchangeRateClientTest {
private ExchangeRateClient exchangeRateClient;

    @Before
    public void setUp() {
        exchangeRateClient = new ExchangeRateClient(new HttpClient());
    }

    @Test
    public void shouldReturnExchangeRateNotNull() {

        ExchangeRate returnedExchangeRate = exchangeRateClient.getRealTimeExchangeRate("USD", "JPY");

        assertNotNull(returnedExchangeRate.getExchangeRate());
    }

    @Test
    public void shouldContainSpecifiedExchangeRateObject() {
        List<ExchangeRate> returnedExchangeRates = exchangeRateClient.getHistoricalExchangeRates("EUR", "USD", "full");

        ExchangeRate expectedExchangeRate = new ExchangeRate(
                new BigDecimal("1.1231"),
                LocalDate.parse("2019-05" +
                        "-13"), null);

        assertTrue(returnedExchangeRates.contains(expectedExchangeRate));
    }
}