package kpi.ye.currencyexchange.currency;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The {@code CurrencyHomeController} class provides "currencies"
 * model attribute and controller action under "/" endpoint which
 * generates "home" view to display.
 */
@Controller
public class CurrencyHomeController {
    private final CurrencyService currenciesService;

    public CurrencyHomeController(CurrencyService currenciesService) {
        this.currenciesService = currenciesService;
    }

    @ModelAttribute("currencies")
    public List<Currency> currenciesList() {
        return currenciesService.getAllCurrencies();
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

}
