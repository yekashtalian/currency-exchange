package kpi.ye.currencyexchange.exchangerate;

import org.json.*;
import org.springframework.stereotype.Component;
import kpi.ye.currencyexchange.client.HttpClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * The {@code ExchangeRateClient} class contains a ALPHA_ADVANTAGE_URI
 * together with API_KEY, HISTORICAL_VALUES, REALTIME_VALUES parameters
 * and methods that allow to get realtime and historical exchange rates.
 *
 */
@Component
public class ExchangeRateClient {
    private static final String ALPHA_ADVANTAGE_URI = "https://www.alphavantage.co/query?function=";
    private static final String API_KEY = "F41YX3ZBH15DU6BK";
    private static final String HISTORICAL_VALUES = "FX_DAILY";
    private static final String REALTIME_VALUES = "CURRENCY_EXCHANGE_RATE";

    private final HttpClient httpClient;

    public ExchangeRateClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * The method uses {@link HttpClient} to obtain data from alphavantage.co
     * as a String, parses it into JSONObject and then checks for possible error
     * messages and if ok, creates an {@link ExchangeRate} object with current
     * exchange rate for specified currencies pair.
     *
     * @param fromCurrency a currency code in ISO 4217 format e.g: USD, EUR, PLN, GBP
     * @param toCurrency a currency code in ISO 4217 format e.g: USD, EUR, PLN, GBP
     * @return an {@code ExchangeRate} object.
     */
    ExchangeRate getRealTimeExchangeRate(String fromCurrency, String toCurrency) {

        String jsonString = httpClient.getStringFromUri(
                String.format("%s%s&from_currency=%s&to_currency=%s&apikey=%s",
                        ALPHA_ADVANTAGE_URI,
                        REALTIME_VALUES,
                        fromCurrency,
                        toCurrency,
                        API_KEY));

        JSONObject jsonObject = new JSONObject(jsonString);

        boolean note = jsonObject.has("Note");
        boolean error = jsonObject.has("Error Message");

        if (!note && !error) {
            BigDecimal exchangeRate = new BigDecimal(jsonObject
                    .getJSONObject("Realtime Currency Exchange Rate")
                    .getString("5. Exchange Rate"));

            LocalDate date = LocalDate.parse(jsonObject
                    .getJSONObject("Realtime Currency Exchange Rate")
                    .getString("6. Last Refreshed")
                    .substring(0, 10));

            return new ExchangeRate(exchangeRate, date, null);
        } else if (note) {
            return new ExchangeRate(null, null,
                    jsonObject.getString("Note"));
        }
        return new ExchangeRate(null, null,
                jsonObject.getString("Error Message"));
    }

    /**
     * The method uses {@link HttpClient} to obtain data from alphavantage.co
     * as a String, parses it into JSONObject and then checks for possible error
     * messages and if ok, creates an {@link ExchangeRate} object with current
     * exchange rate for specified currencies pair.
     *
     * @param fromCurrency a currency code in ISO 4217 format e.g: USD, EUR, PLN, GBP
     * @param toCurrency a currency code in ISO 4217 format e.g: USD, EUR, PLN, GBP
     * @param outputSize parameter can be a String value of "full" for rich history
     *                   and "compact" for currencies pair that don't have full history
     * @return a list of {@link ExchangeRate} objects
     */
    List<ExchangeRate> getHistoricalExchangeRates(String fromCurrency,
                                                                String toCurrency,
                                                                String outputSize) {
        List<ExchangeRate> exchangeRates = new ArrayList<>();

        String jsonString = httpClient.getStringFromUri(
                String.format("%s%s&from_symbol=%s&to_symbol=%s&outputsize=%s&apikey=%s",
                        ALPHA_ADVANTAGE_URI,
                        HISTORICAL_VALUES,
                        fromCurrency,
                        toCurrency,
                        outputSize,
                        API_KEY));

        JSONObject jsonObject = new JSONObject(jsonString);

        boolean note = jsonObject.has("Note");
        boolean error = jsonObject.has("Error Message");

        if (!note && !error) {
            Iterator<String> keys = jsonObject.getJSONObject("Time Series FX (Daily)").keys();

            while (keys.hasNext()) {
                String key = keys.next();

                if (!key.equals("") && !key.equals(" ")) {
                    LocalDate date = LocalDate.parse(key);
                    BigDecimal exchangeRate = new BigDecimal(jsonObject
                            .getJSONObject("Time Series FX (Daily)")
                            .getJSONObject(key)
                            .getString("4. close"));
                    exchangeRates.add(new ExchangeRate(exchangeRate, date, null));
                }
            }

            exchangeRates.sort(Comparator.comparing(ExchangeRate::getDate));
            return exchangeRates;

        } else if (note) {
            exchangeRates.add(new ExchangeRate(null, null,
                    jsonObject.getString("Note")));
            return exchangeRates;
        }
        exchangeRates.add(new ExchangeRate(null, null,
                jsonObject.getString("Error Message")));
        return exchangeRates;
    }
}
