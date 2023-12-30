package kpi.ye.currencyexchange.currency;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import kpi.ye.currencyexchange.client.HttpClient;

import java.util.*;

/**
 * The {@code CurrencyClient} class contains a CURRENCIES URI and
 * a method that allow to get actual currencies codes and full names.
 */
@Component
public class CurrencyClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(CurrencyClient.class);
    private static final String CURRENCIES_URI = "https://openexchangerates.org/api/currencies.json";

    private final HttpClient httpClient;

    public CurrencyClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * The method uses {@link HttpClient} to obtain data from
     * openexchangerates.org as a String, parses it into a JSONObject
     * and then using downloaded data creates a list of {@code Currency} objects.
     *
     * @return a list od {@code Currency} objects with currency code and name.
     */
    public List<Currency> getCurrencies() {
        List<Currency> currencyList = new ArrayList<>();
        String jsonString = httpClient.getStringFromUri(CURRENCIES_URI);

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator<String> keys = jsonObject.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                if (!key.equals("") && !key.equals(" "))
                    currencyList.add(new Currency(key, jsonObject.getString(key)));
            }

            currencyList.sort(Comparator.comparing(Currency::getCode));
        } catch (NullPointerException e) {
            LOGGER.error("Null value found while parsing jsonString to jsonObject");
            e.printStackTrace();
        }
        return currencyList;
    }
}
