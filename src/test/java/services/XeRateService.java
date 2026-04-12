package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import utils.ConfigReader;
import utils.EnvReader;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Real XE API integration for fetching reference exchange rates.
 *
 * Tasks:
 * - Call XE Currency Data API
 * - Parse JSON response
 * - Return expected converted amount
 *
 * Note:
 * Requires valid XE_ACCOUNT_ID and XE_API_KEY.
 * Free trial always returns a fix exchange rates = 1.2345 if currency pairs are different
 */

public class XeRateService implements ExchangeRateService {

    private final HttpClient client;
    private final ObjectMapper mapper;
    private final String baseUrl;
    private final String apiId;
    private final String apiKey;

    public XeRateService() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
        this.baseUrl = ConfigReader.get("xe.base.url");
        this.apiId = EnvReader.get("XE_API_ID");
        this.apiKey = EnvReader.get("XE_API_KEY");
       }

    @Override
    public double getExpectedConvertedAmount(String from, String to, double amount) {
        if (apiId == null || apiKey == null || apiId.isBlank() || apiKey.isBlank()) {
            throw new RuntimeException("XE credentials not set. Please set XE_ACCOUNT_ID and XE_API_KEY.");
        }

        try {
            String url = String.format(
                    "%s/v1/convert_from.json/?from=%s&to=%s&amount=%s",
                    baseUrl, from, to, amount
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", basicAuthHeader(apiId, apiKey))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("XE API error: " + response.statusCode() + " " + response.body());
            }

            JsonNode root = mapper.readTree(response.body());
            JsonNode toArray = root.path("to");

            if (!toArray.isArray() || toArray.isEmpty()) {
                throw new RuntimeException("No conversion data returned from XE: " + response.body());
            }

            return toArray.get(0).path("mid").asDouble();

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch XE conversion result", e);
        }
    }

    private String basicAuthHeader(String username, String password) {
        String raw = username + ":" + password;
        String encoded = Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
        return "Basic " + encoded;
    }
}