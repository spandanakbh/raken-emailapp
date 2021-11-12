package com.raken.email.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.raken.email.model.Rain;
import com.raken.email.model.WeatherForecast;
import com.raken.email.model.Wind;

/**
 * @author spandana k
 * Service class to call Weather service API
 */
@Service
public class WeatherServiceImpl implements WeatherService {
	
	private static final Logger LOG = LoggerFactory.getLogger(WeatherServiceImpl.class);

	@Override
	public String getWeatherForecast() {
		StringBuilder weatherForecast = new StringBuilder("Current Weather : ");
		String weatherAPIUrl = "http://api.openweathermap.org/data/2.5/weather?q=Carlsbad,06,1&units=imperial&appid=073beed2bea2d195aee00827badfa3be";
		
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<WeatherForecast> response = restTemplate
                .getForEntity(weatherAPIUrl, WeatherForecast.class);

		WeatherForecast responseBody = response.getBody();
		
		LOG.debug(responseBody.toString());
		
		weatherForecast.append(responseBody.getName()).append(" , ").append(responseBody.getMain().getTemp()).append("°F").
		append("</br>Humidity:").append(responseBody.getMain().getHumidity()).append("%");
		
		Rain rain = responseBody.getRain();
		
		if(rain != null) {
			weatherForecast.append("</br>Precipitation: ").
			append(responseBody.getRain().getPrecipitation()).append("mm");
		}
		
		Wind wind = responseBody.getWind();
		if(wind != null) {
			weatherForecast.append("</br>Wind: ").append(wind.getSpeed()).append("mph");
		}
		return weatherForecast.toString();
	}

}
