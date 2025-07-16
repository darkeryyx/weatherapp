package de.ok.conterra.weatherapp;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/forecast")
public class ForecastController {

    private ForecastService forecastService;

    public ForecastController(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @GetMapping(params = {"country","city","street","housenumber"})
    public List<ForecastDTO> getForecastByAddress(@RequestParam("country") String country, @RequestParam("city") String city, @RequestParam("street") String street, @RequestParam("housenumber") int housenumber ) {
        return forecastService.getForecast(country, city, street, housenumber);
    }

    @GetMapping(params = {"lat","lon"})
    public ForecastDTO getForecastByCoordinates(@RequestParam("lat") double lat, @RequestParam("lon") double lon) {
        return forecastService.getForecast(lat, lon);
    }

}