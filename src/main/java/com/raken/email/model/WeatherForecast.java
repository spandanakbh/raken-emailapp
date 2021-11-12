package com.raken.email.model;

/**
 * @author spandana k
 *
 */
public class WeatherForecast {
	
	private String name;
	
	private Main main;
	
	private Wind wind;
	
	private Rain rain;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Main getMain() {
		return main;
	}

	public void setMain(Main main) {
		this.main = main;
	}

	public Wind getWind() {
		return wind;
	}

	public void setWind(Wind wind) {
		this.wind = wind;
	}

	public Rain getRain() {
		return rain;
	}

	public void setRain(Rain rain) {
		this.rain = rain;
	}
}
