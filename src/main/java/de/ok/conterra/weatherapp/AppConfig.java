package de.ok.conterra.weatherapp;

import com.opencagedata.jopencage.JOpenCageGeocoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    private String apiKey;

    public AppConfig(@Value("${opencage.api.key}") String apiKey){
        this.apiKey = apiKey;
    }

    @Bean
    public JOpenCageGeocoder jOpenCageGeocoder(){
        return new JOpenCageGeocoder(apiKey);
    }

    // HTTP-Client
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder b) {
        return b.build();
    }
}
