package kpi.ye.currencyexchange.client;

import com.jcabi.http.request.JdkRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
/**
 * The {@code HttpClient} class contains a method for downloading
 * data from the entered URI.
 */
@Component
public class HttpClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);

    /**
     * The {@code getStringFromUri} method uses {@link com.jcabi.http} client to
     * make an HTTP request and return an response content as a {@code String} value.
     * @param uri a string of characters that unambiguously identifies a particular resource
     *            e.g "http://www.nbp.pl/kursy/xml/c073z070413.xml"
     * @return a string that contains resource content
     */
    public String getStringFromUri(String uri) {
        String result = null;

        try {
            result = new String(new JdkRequest(uri).fetch().binary());
            if (result.equals("")){
                return null;
            }
        } catch (IOException e) {
            LOGGER.error("Error while connecting to external URI, check internet connection");
            e.printStackTrace();
        }
        return result;
    }
}
