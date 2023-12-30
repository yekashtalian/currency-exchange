package kpi.ye.currencyexchange.currency;

import org.junit.Before;
import org.junit.Test;
import kpi.ye.currencyexchange.client.HttpClient;

import java.util.List;
import static org.junit.Assert.*;

public class CurrencyClientTest {
    private CurrencyClient currencyClient;

    @Before
    public void setUp() {
        currencyClient = new CurrencyClient(new HttpClient());
    }

    @Test
    public void shouldContainSpecifiedCurrencyObject() {
        List<Currency> returnedCurrencies = currencyClient.getCurrencies();

        Currency expectedCurrency = new Currency("USD", "United States Dollar");

        assertTrue(returnedCurrencies.contains(expectedCurrency));
    }

}