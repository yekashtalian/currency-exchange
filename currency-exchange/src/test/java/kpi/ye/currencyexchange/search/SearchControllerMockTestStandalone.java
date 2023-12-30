package kpi.ye.currencyexchange.search;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import kpi.ye.currencyexchange.client.HttpClient;
import kpi.ye.currencyexchange.exchangerate.ExchangeRateClient;
import kpi.ye.currencyexchange.exchangerate.ExchangeRateServiceImpl;

import static org.hamcrest.Matchers.is;
import static org.springframework.test
        .web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework
        .test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


public class SearchControllerMockTestStandalone {
    private static SearchController searchController;

    @BeforeClass
    public static void setUp() {
        HttpClient httpClient = new HttpClient();
        ExchangeRateClient exchangeRateClient = new ExchangeRateClient(httpClient);
        ExchangeRateServiceImpl exchangeRateService = new ExchangeRateServiceImpl(exchangeRateClient);
        searchController = new SearchController(exchangeRateService);
    }


    @Test
    public void shouldReturnExpectedJSON() throws Exception {
        MockMvc mockMvc = standaloneSetup(searchController).build();

        mockMvc.perform(get("/search?fromCurrency=EUR&toCurrency=PLN")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.msg", is("Success")))
                .andDo(print());

    }


}