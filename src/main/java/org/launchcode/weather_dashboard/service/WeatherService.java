package org.launchcode.weather_dashboard.service;


import org.launchcode.weather_dashboard.dto.GeocodeResult;
import org.launchcode.weather_dashboard.model.WeatherData;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.beans.factory.annotation.Value;

@Service
public class WeatherService {

    // TODO #4A: Create a new instance of RestTemplate
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${weather.api.key}")
    private String apiKey;

    private static final String GEO_URL =
            "https://api.openweathermap.org/geo/1.0/direct?q={city}&limit=1&appid={key}";

    private static final String WEATHER_URL =
            "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={key}&units=imperial";

    public WeatherData getCurrentWeather(String city) {
        try {


            // TODO #4B
            GeocodeResult[] geoResults = restTemplate.getForObject(
                    GEO_URL,
                    GeocodeResult[].class,
                    city,
                    apiKey
            );

            if (geoResults == null || geoResults.length == 0) {
                throw new RuntimeException("City not found: " + city);
            }

            double lat = geoResults[0].lat();
            double lon = geoResults[0].lon();


            // TODO #4C
            WeatherData weather = restTemplate.getForObject(
                    WEATHER_URL,
                    WeatherData.class,
                    lat,
                    lon,
                    apiKey
            );

            if (weather == null) {
                throw new RuntimeException("No weather data received for coordinates (" + lat + ", " + lon + ")");
            }

            return weather;

        } catch (HttpClientErrorException e) {

            if (e.getStatusCode().value() == 404) {
                throw new RuntimeException("Location or weather resource not found: " + city);
            } else {
                throw new RuntimeException("API Error: " + e.getMessage());
            }

        } catch (RuntimeException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException("Failed to get weather data: " + e.getMessage());
        }
    }
}
