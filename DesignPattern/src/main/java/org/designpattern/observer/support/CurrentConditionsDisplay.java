package org.designpattern.observer.support;

import org.designpattern.observer.DisplayElement;
import org.designpattern.observer.Observer;
import org.designpattern.observer.Subject;

public class CurrentConditionsDisplay implements Observer, DisplayElement {
	private float temperature;
	private float humidity;
	private float pressure;
	private Subject wheaterData;

	public CurrentConditionsDisplay(Subject wheaterData) {
		this.wheaterData = wheaterData;
		this.wheaterData.registerObserver(this);
	}

	@Override
	public void display() {
		System.out.println("Current conditions: " + temperature + "F degree and " + humidity
				+ "% humidity and" + pressure + " pressure");
	}

	@Override
	public void update(float temp, float humidity, float pressure) {
		this.temperature = temp;
		this.humidity = humidity;
		this.pressure = pressure;
		display();
	}

}
