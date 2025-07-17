package de.ok.conterra.weatherapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencagedata.jopencage.JOpenCageGeocoder;
import com.opencagedata.jopencage.model.JOpenCageForwardRequest;
import com.opencagedata.jopencage.model.JOpenCageLatLng;
import com.opencagedata.jopencage.model.JOpenCageResponse;
import com.opencagedata.jopencage.model.JOpenCageResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ForecastServiceTest {
    @Mock
    private JOpenCageGeocoder geocoder;
    @Mock
    private RestTemplate restTemplate;
    private ForecastService service;
    private final String openWeatherKey = "dummy";
    private final String openWeatherUrl = "http://api.test";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ForecastService(geocoder, restTemplate, openWeatherKey, openWeatherUrl);
    }

    @Test
    void getForecastByCoordinates_ReturnsDto() {
        ForecastDTO dto = service.getForecast(50.0, 8.0);
        assertNotNull(dto);
        assertTrue(dto.getTime() > 0);
        assertTrue(dto.getTemp() >= -5 && dto.getTemp() <= 40);
        assertTrue(dto.getFeelsLike() >= -3 && dto.getFeelsLike() <= 43);
        assertTrue(dto.getHumidity() >= 0 && dto.getHumidity() <= 100);
    }

    @Test
    void getForecastByCoordinatesWithInvalidLatLon_ThrowsException() {
        assertThrows(ResponseStatusException.class, () ->
                service.getForecast(1000, 20)
        );
        assertThrows(ResponseStatusException.class, () ->
                service.getForecast(10, 2000)
        );
        assertThrows(ResponseStatusException.class, () ->
                service.getForecast(1000, 2000)
        );
    }

    @Test
    void getForecastByAddress_ReturnsList() throws Exception {

        JOpenCageResponse geoResp = mock(JOpenCageResponse.class);
        when(geocoder.forward(any(JOpenCageForwardRequest.class))).thenReturn(geoResp);

        JOpenCageResult fakeResult = mock(JOpenCageResult.class);
        when(geoResp.getResults()).thenReturn(Collections.singletonList(fakeResult));


        JOpenCageLatLng pos = mock(JOpenCageLatLng.class);
        when(geoResp.getFirstPosition()).thenReturn(pos);
        when(pos.getLat()).thenReturn(10.0);
        when(pos.getLng()).thenReturn(20.0);


        String json = """
          { "list": [
            { "dt": 1000, "main": { "temp": 1.1, "feels_like": 2.2, "humidity": 10 }},
            { "dt": 2000, "main": { "temp": 3.3, "feels_like": 4.4, "humidity": 20 }}
          ]}
          """;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        when(restTemplate.getForObject(
                contains(openWeatherUrl),
                eq(JsonNode.class)))
                .thenReturn(root);

        List<ForecastDTO> list = service.getForecast("Germany", "Cologne", "Domkloster", 4);
        assertEquals(2, list.size());
        assertEquals(1000, list.get(0).getTime());
        assertEquals(1.1, list.get(0).getTemp());
        assertEquals(10, list.get(0).getHumidity());
    }

    @Test
    void getForecastByAddressWhenGeocoderReturnsNull_ThrowsNotFound() {
        when(geocoder.forward(any())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () ->
                service.getForecast("DE", "X", "Y", 1)
        );
    }

    @Test
    void getForecastByAddressWhenResultsEmpty_ThrowsNotFound() {
        JOpenCageResponse emptyResp = mock(JOpenCageResponse.class);
        when(geocoder.forward(any()))
                .thenReturn(emptyResp);
        when(emptyResp.getResults())
                .thenReturn(Collections.emptyList());

        assertThrows(ResponseStatusException.class, () ->
                service.getForecast("DE", "Germany", "Domkloster", 4)
        );
    }

    //TODO e.g list is empty, null parameter..
}