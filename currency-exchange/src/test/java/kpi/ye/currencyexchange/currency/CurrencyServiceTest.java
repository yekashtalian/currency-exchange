package kpi.ye.currencyexchange.currency;

import org.junit.Before;
import org.junit.Test;
import kpi.ye.currencyexchange.client.HttpClient;
import java.util.List;

import static org.junit.Assert.*;

public class CurrencyServiceTest {

private CurrencyService service;
private CurrencyClient currencyClient;

    @Before
    public void setUp() {
        currencyClient = new CurrencyClient(new HttpClient());
        service = new CurrencyServiceImpl(currencyClient);
    }

    @Test
    public void shouldContainSpecifiedCurrencyObject() {

        Currency expectedCurrency = new Currency("USD", "United States Dollar");

        List<Currency> returnedList = service.getAllCurrencies();

        assertTrue(returnedList.contains(expectedCurrency));
    }

}