package de.ok.conterra.weatherapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringJUnitConfig
@WebMvcTest(ForecastController.class)
public class ForecastControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private ForecastService service;

    @Test
    @WithMockUser(roles = "USER")
    public void getForecastByCoords_ReturnsJson() throws Exception {
        when(service.getForecast(51.96, 7.62))
                .thenReturn(new ForecastDTO(12345, 15.0, 13.0, 80));

        mvc.perform(get("/forecast?lat=51.96&lon=7.62"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.temp").value(15.0))
                .andExpect(jsonPath("$.feels_like").value(13.0))
                .andExpect(jsonPath("$.humidity").value(80));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getForecastByAddress_ReturnsList() throws Exception {
        List<ForecastDTO> forecastList = List.of(
                new ForecastDTO(12345, 22.3, 21.0, 65),
                new ForecastDTO(54321, 19.8, 18.1, 70)
        );
        when(service.getForecast("Germany", "Cologne", "Domkloster", 4))
                .thenReturn(forecastList);

        mvc.perform(get("/forecast")
                        .param("country", "Germany")
                        .param("city", "Cologne")
                        .param("street", "Domkloster")
                        .param("housenumber", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].temp").value(22.3))
                .andExpect(jsonPath("$[1].humidity").value(70));
    }

    //Security Tests
    @Test
    void requestWithoutAuthentication_ReturnsUnauthorized() throws Exception {
        mvc.perform(get("/forecast?lat=51.96&lon=7.62"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void requestWithAuthentication_ReturnsOkAndJson() throws Exception {
        when(service.getForecast(51.96, 7.62))
                .thenReturn(new ForecastDTO(12345, 15.0, 13.0, 80));

        mvc.perform(get("/forecast?lat=51.96&lon=7.62"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.temp").value(15.0))
                .andExpect(jsonPath("$.humidity").value(80));
    }
}


