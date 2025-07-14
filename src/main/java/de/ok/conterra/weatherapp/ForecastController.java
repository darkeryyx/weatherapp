package de.ok.conterra.weatherapp;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/forecast")
public class ForecastController {

    private ForecastService forecastService;

    public ForecastController(ForecastService forecastService) {
        this.forecastService = forecastService;
    }
    @GetMapping
    public ForecastDTO getForecast(@RequestParam("lat") double lat, @RequestParam("lon") double lon) {
        return forecastService.getForecast(lat, lon);
    }

}
