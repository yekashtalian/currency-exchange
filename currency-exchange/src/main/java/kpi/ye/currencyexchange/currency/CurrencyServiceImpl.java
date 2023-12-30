package kpi.ye.currencyexchange.currency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The {@code CurrencyServiceImpl} implements {@link CurrencyService} interface.
 */
@Service
public class CurrencyServiceImpl implements CurrencyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyServiceImpl.class);
    private final CurrencyClient currencyClient;

    public CurrencyServiceImpl(CurrencyClient currencyClient) {
        this.currencyClient = currencyClient;
    }

    /**
     * The {@code getAllCurrencies} method uses {@link CurrencyClient} to
     * obtain all currencies from openexchangerates.org website.
     *
     * @return a list of {@link Currency} objects
     */
    @Override
    public List<Currency> getAllCurrencies() {
        List<Currency> currencies = currencyClient.getCurrencies();
        if (currencies == null || currencies.isEmpty()) {
            LOGGER.error("Couldn't download currencies from https://openexchangerates.org/api/currencies.json");
            return null;
        }
        return currencies;
    }

}
