package kpi.ye.currencyexchange.exchangerate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRateServiceImpl.class);
    private final ExchangeRateClient exchangeRateClient;

    public ExchangeRateServiceImpl(ExchangeRateClient exchangeRateClient) {
        this.exchangeRateClient = exchangeRateClient;
    }

    @Override
    public ExchangeRate getRealTimeRate(String fromCurrency, String toCurrency) {
        ExchangeRate realTimeRate = exchangeRateClient.getRealTimeExchangeRate(fromCurrency, toCurrency);
        if (realTimeRate.getApiNote() != null) {
            LOGGER.error(fromCurrency + " " + toCurrency + " " + realTimeRate.getApiNote());
            return null;
        }
        return realTimeRate;
    }

    @Override
    public List<ExchangeRate> getHistoricalRates(String fromCurrency, String toCurrency) {
        List<ExchangeRate> historicalRates = exchangeRateClient.getHistoricalExchangeRates(fromCurrency, toCurrency, "full");

        if (historicalRates.get(0).getApiNote() != null) {

            if (historicalRates.get(0).getApiNote().contains("Invalid API call")) {

                List<ExchangeRate> historicalRatesCompact = exchangeRateClient.getHistoricalExchangeRates(fromCurrency, toCurrency, "compact");

                assert historicalRatesCompact != null;
                if (historicalRatesCompact.get(0).getApiNote().contains("Invalid API call")) {
                    LOGGER.error(fromCurrency + " " + toCurrency + " " + historicalRatesCompact.get(0).getApiNote());
                    return historicalRatesCompact;
                }
                LOGGER.error(fromCurrency + " " + toCurrency + " " + historicalRatesCompact.get(0).getApiNote());
                return historicalRatesCompact;

            }
            LOGGER.error(fromCurrency + " " + toCurrency + " " + historicalRates.get(0).getApiNote());
            return historicalRates;
        }
        return historicalRates;
    }

    @Override
    public List<List<ExchangeRate>> getHistoricalRatesInPeriods(List<ExchangeRate> exchangeRateList) {
        List<List<ExchangeRate>> result = new ArrayList<>();
        TimePeriod timePeriod = new TimePeriod();

        List<ExchangeRate> oneWeek = filterRatesByTimePeriod(exchangeRateList, timePeriod.getOneWeek());
        List<ExchangeRate> twoWeeks = filterRatesByTimePeriod(exchangeRateList, timePeriod.getTwoWeeks());
        List<ExchangeRate> oneMonth = filterRatesByTimePeriod(exchangeRateList, timePeriod.getOneMonth());
        List<ExchangeRate> twoMonths = filterRatesByTimePeriod(exchangeRateList, timePeriod.getTwoMonths());
        List<ExchangeRate> sixMonths = filterRatesByTimePeriod(exchangeRateList, timePeriod.getSixMonths());
        List<ExchangeRate> oneYear = filterRatesByTimePeriod(exchangeRateList, timePeriod.getOneYear());
        List<ExchangeRate> twoYears = filterRatesByTimePeriod(exchangeRateList, timePeriod.getTwoYears());
        List<ExchangeRate> fiveYears = filterRatesByTimePeriod(exchangeRateList, timePeriod.getFiveYears());
        List<ExchangeRate> tenYears = filterRatesByTimePeriod(exchangeRateList, timePeriod.getFiveYears());

        result.add(oneWeek);
        checkSize(result, oneWeek, twoWeeks);
        checkSize(result, twoWeeks, oneMonth);
        checkSize(result, oneMonth, twoMonths);
        checkSize(result, twoMonths, sixMonths);
        checkSize(result, sixMonths, oneYear);
        checkSize(result, oneYear, twoYears);
        checkSize(result, twoYears, fiveYears);
        checkSize(result, fiveYears, tenYears);
        checkSize(result, tenYears, exchangeRateList);

        return result;
    }

    private List<ExchangeRate> filterRatesByTimePeriod(List<ExchangeRate> exchangeRateList, LocalDate date) {
        List<ExchangeRate> result = new ArrayList<>();
        try {
            result = exchangeRateList.stream()
                    .filter(e -> e.getDate().isEqual(date)
                            || e.getDate().isAfter(date))
                    .sorted(Comparator.comparing(ExchangeRate::getDate))
                    .collect(Collectors.toList());
        } catch (NullPointerException e) {
            e.printStackTrace();
            LOGGER.error("Found null value in historical exchange rates data" + exchangeRateList.toString());
        }
        return result;
    }

    private void checkSize(List<List<ExchangeRate>> result, List<ExchangeRate> first, List<ExchangeRate> second) {
        if (first.size() < second.size()) {
            result.add(second);
        }
    }
}

