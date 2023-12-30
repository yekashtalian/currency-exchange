package kpi.ye.currencyexchange.currencyAnalyses;

import kpi.ye.currencyexchange.currency.Currency;
import kpi.ye.currencyexchange.currency.CurrencyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyAnalysisService {

    private final CurrencyClient currencyClient;
    private final Gpt4Service gpt4Service;

    @Autowired
    public CurrencyAnalysisService(CurrencyClient currencyClient, Gpt4Service gpt4Service) {
        this.currencyClient = currencyClient;
        this.gpt4Service = gpt4Service;
    }

    public String getCurrencyAnalysis(String from, String to) throws Exception {
        String prompt = createPrompt(from, to);
        String analysis = gpt4Service.createCompletion(prompt);
        return analysis;
    }

    private String createPrompt(String from, String to) {
        if (from.length() < 3 || to.length() < 3){
            return "Please select two currencies";
        }
        return "The following is a two currencies: " + from + " and " + to +
                ". Provide a detailed analysis of provided currencies and their trends and provide wide description.";
    }
}