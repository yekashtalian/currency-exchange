package kpi.ye.currencyexchange.search;

import lombok.Data;
import kpi.ye.currencyexchange.exchangerate.ExchangeRate;

import java.util.List;

@Data
class SearchResponse {
    private String msg;
    private ExchangeRate realtimeRate;
    private List<List<ExchangeRate>> historicalRates;
    private String analysis;
}
