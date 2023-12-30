package kpi.ye.currencyexchange.exchangerate;

import org.junit.Before;
import org.junit.Test;
import kpi.ye.currencyexchange.client.HttpClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class ExchangeRateServiceTest {

    private ExchangeRateClient exchangeRateClient;
    private ExchangeRateService service;

    @Before
    public void setUp() {
        exchangeRateClient = new ExchangeRateClient(new HttpClient());
        service = new ExchangeRateServiceImpl(exchangeRateClient);
    }

    @Test
    public void shouldReturnExchangeRateObjectWithSpecifiedApiNoteAndDate() {
        //given
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setApiNote(null);
        exchangeRate.setDate(LocalDate.now());

        //when
        ExchangeRate realTimeRate = service.getRealTimeRate("EUR", "PLN");

        //then
        assertEquals(exchangeRate.getApiNote(), realTimeRate.getApiNote());
        assertEquals(exchangeRate.getDate(), realTimeRate.getDate());
    }

    @Test
    public void shouldContainSpecifiedObject() {
        //given
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setApiNote(null);
        exchangeRate.setDate(LocalDate.of(2018, 9, 27));
        exchangeRate.setExchangeRate(BigDecimal.valueOf(4.2684));

        //when
        List<ExchangeRate> exchangeRateList = service.getHistoricalRates("EUR", "PLN");

        //then
        assertTrue(exchangeRateList.contains(exchangeRate));
    }

}