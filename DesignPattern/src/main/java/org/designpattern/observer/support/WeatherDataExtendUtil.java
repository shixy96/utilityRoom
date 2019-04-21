package org.designpattern.observer.support;

import java.util.Observable;

public class WeatherDataExtendUtil extends Observable {
	private float temperature;
	private float humidity;
	private float pressure;

	public void setMeasurements(float temperature, float humidity, float pressure) {
		this.pressure = pressure;
		this.humidity = humidity;
		this.temperature = temperature;
		measurementsChanged();
	}

	private void measurementsChanged() {
		setChanged();
		notifyObservers();
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public float getHumidity() {
		return humidity;
	}

	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}

	public float getPressure() {
		return pressure;
	}

	public void setPressure(float pressure) {
		this.pressure = pressure;
	}
}
