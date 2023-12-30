package kpi.ye.currencyexchange.client;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HttpClientTest {
private HttpClient httpClient;

    @Before
    public void setUp() {
        httpClient = new HttpClient();
    }

    @Test
    public void shouldReturnExpectedString() {
        String uri = "https://support.oneskyapp.com/hc/en-us/article_attachments/202761627/example_1.json";
        String returnedString = httpClient.getStringFromUri(uri);

        String expectedString = "{\n" +
                "    \"fruit\": \"Apple\",\n" +
                "    \"size\": \"Large\",\n" +
                "    \"color\": \"Red\"\n" +
                "}";

        assertThat(returnedString, Matchers.is(expectedString));
    }

    @Test
    public void shouldReturnNullWhenNoItemFound() {
        String uri = "https://support.oneskyapp.com/hc/en-us/article_attachments/202761627/example_2.json";

        String returnedString = httpClient.getStringFromUri(uri);

        assertNull(returnedString);
    }

}