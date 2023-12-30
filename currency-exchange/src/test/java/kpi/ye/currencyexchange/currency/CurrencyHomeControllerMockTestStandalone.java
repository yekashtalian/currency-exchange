package kpi.ye.currencyexchange.currency;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import kpi.ye.currencyexchange.client.HttpClient;

public class CurrencyHomeControllerMockTestStandalone {
    private static CurrencyHomeController homeController;
    private static String PAGE_VIEW_NAME = "home";

    @BeforeClass
    public static void setUp(){
        HttpClient httpClient = new HttpClient();
        CurrencyClient currencyClient = new CurrencyClient(httpClient);
        CurrencyServiceImpl currencyService = new CurrencyServiceImpl(currencyClient);
        homeController = new CurrencyHomeController(currencyService);
    }

    @Test
    public void shouldReturnHomePageViewAndCurrenciesAttribute() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.view().name(PAGE_VIEW_NAME))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("currencies"))
                .andDo(MockMvcResultHandlers.print());
    }
}