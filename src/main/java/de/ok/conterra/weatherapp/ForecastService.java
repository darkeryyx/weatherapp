package de.ok.conterra.weatherapp;


import com.fasterxml.jackson.databind.JsonNode;
import com.opencagedata.jopencage.JOpenCageGeocoder;
import com.opencagedata.jopencage.model.JOpenCageForwardRequest;
import com.opencagedata.jopencage.model.JOpenCageLatLng;
import com.opencagedata.jopencage.model.JOpenCageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


/**
 * Service for retrieving weather forecast data.
 * Supports both coordinate-based and address-based requests.
 */
@Service
public class ForecastService {

    private RestTemplate restTemplate;
    private String openWeatherKey;
    private String openWeatherUrl;
    private JOpenCageGeocoder geocoder;

    public ForecastService(JOpenCageGeocoder geocoder, RestTemplate restTemplate, @Value("${openweathermap.api.key}") String openWeatherKey, @Value("${openweathermap.url}") String openWeatherUrl) {
        this.geocoder = geocoder;
        this.restTemplate = restTemplate;
        this.openWeatherKey = openWeatherKey;
        this.openWeatherUrl = openWeatherUrl;
    }

    /**
     * Generates a 5-day weather forecast in 3-hour intervals for the given address.
     * adress → Geocoder → coordinates
     * coordinates → OpenWeatherMap → list of forecasts
     *
     * @param country     Country code or name (e.g., "DE")
     * @param city        City name (e.g., "Münster")
     * @param street      Street name (e.g., "Domplatz")
     * @param housenumber House number (e.g., 1)
     * @return a list of ForecastDTO objects containing:
     *         - time (Unix timestamp in UTC),
     *         - temp (temperature in °C),
     *         - feels_like (felt temperature in °C),
     *         - humidity (percentage)
     * @throws ResponseStatusException with HTTP 404 if the address cannot be geocoded,
     *         or HTTP 503 if weather data cannot be retrieved
     */
    public List<ForecastDTO> getForecast(String country, String city, String street, int housenumber) {
        if (country == null || country.isBlank() ||
                city    == null || city.isBlank()    ||
                street  == null || street.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Die Parameter country, city und street dürfen nicht leer sein."
            );
        }
        if (housenumber <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Der Parameter housenumber muss größer 0 sein."
            );
        }

        String address = String.format("%s %d, %s, %s",
                street, housenumber, city, country);

        // requesting adress & forward geocoding
        JOpenCageForwardRequest geoReq = new JOpenCageForwardRequest(address);
        JOpenCageResponse geoResp = geocoder.forward(geoReq);

        if (geoResp == null || geoResp.getResults().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Adresse nicht gefunden: " + address
            );
        }

        // getFirstPosition() = gives first adress of list of matches
        JOpenCageLatLng pos = geoResp.getFirstPosition();
        double lat = pos.getLat();
        double lon = pos.getLng();

        // get weather data for coordinates (5 day forecast in 3-hour steps)
        String url = String.format(
                "%s?lat=%f&lon=%f&units=metric&appid=%s",
                openWeatherUrl, lat, lon, openWeatherKey
        );

        //send request to api and get JsonNode back
        JsonNode root = restTemplate.getForObject(url, JsonNode.class);

        if (root == null || !root.has("list")) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Keine Wetterdaten verfügbar"
            );
        }

        JsonNode list = root.path("list");
        if (!list.isArray() || list.size() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Keine Wetterdaten im 3-Stunden-Takt verfügbar"
            );
        }

        List<ForecastDTO> result = new ArrayList<>();
        for (JsonNode block : list) {
            long timestamp   = block.path("dt").asLong();
            JsonNode main    = block.path("main");
            double temp      = main.path("temp").asDouble();
            double feelsLike = main.path("feels_like").asDouble();
            int humidity     = main.path("humidity").asInt();

            result.add(new ForecastDTO(timestamp, temp, feelsLike, humidity));
        }

        return result;
    }

    /**
     * @param lat Latitude
     * @param lon Longitude
     * @return ForecastDTO containing:
     *         - time (Unix timestamp in UTC),
     *         - temp (temperature),
     *         - feels_like (felt temperature),
     *         - humidity (percentage)
     */
    public ForecastDTO getForecast(double lat, double lon) {

        if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Latitude must be between -90 and 90, longitude between -180 and 180"
            );
        }

        long time = Instant.now().getEpochSecond();
        double temp = (Math.round(ThreadLocalRandom.current().nextDouble(-5, 40)*100)/100.0);
        double feels_like = (Math.round((temp + ThreadLocalRandom.current().nextDouble(-2, 3))*100)/100.0);
        int humidity = ThreadLocalRandom.current().nextInt(0, 100);

        return new ForecastDTO(time, temp, feels_like, humidity);
    }
}
