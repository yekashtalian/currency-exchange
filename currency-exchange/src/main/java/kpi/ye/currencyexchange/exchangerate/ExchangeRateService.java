package kpi.ye.currencyexchange.exchangerate;

import java.util.List;

/**
 * The {@code ExchangeRateService} interface provides {@code getRealTimeRate},
 * {@code getHistoricalRates} and {@code getHistoricalRatesInPeriods} methods
 * to be implemented.
 */
public interface ExchangeRateService {
    ExchangeRate getRealTimeRate(String fromCurrency, String toCurrency);

    List<ExchangeRate> getHistoricalRates(String fromCurrency, String toCurrency);

    List<List<ExchangeRate>> getHistoricalRatesInPeriods(List<ExchangeRate> exchangeRateList);
}
