package org.springframework.samples.hibernate.beans;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;


public class Village {

	protected String villageName;
	protected String cityName;
	
	@NotNull
	@Column(name = "VILLAGE_NAME")
	public String getVillageName() {
		return villageName;
	}
	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}
	@NotNull
	@Column(name = "CITY_NAME")
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
}
