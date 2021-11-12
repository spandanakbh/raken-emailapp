package com.raken.email.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author spandana k
 *
 */
public class Rain {
	
	@JsonProperty(value = "3h")
	private int precipitation;

	public int getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(int precipitation) {
		this.precipitation = precipitation;
	}
}
