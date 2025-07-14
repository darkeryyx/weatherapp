package de.ok.conterra.weatherapp;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Generates mock weather forecasts with realistic random values.
 */
@Service
public class ForecastService {

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
