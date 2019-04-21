package org.designpattern.observer.support;

import java.util.Observable;
import java.util.Observer;

import org.designpattern.observer.DisplayElement;

public class CurrentConditionsDisplayImplementUtil implements Observer, DisplayElement {
	private float temperature;
	private float humidity;
	private float pressure;
	Observable observable;

	public CurrentConditionsDisplayImplementUtil(Observable observable) {
		this.observable = observable;
		observable.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof WeatherDataExtendUtil) {
			WeatherDataExtendUtil wExtendUtil = (WeatherDataExtendUtil) o;
			this.temperature = wExtendUtil.getTemperature();
			this.humidity = wExtendUtil.getHumidity();
			this.pressure = wExtendUtil.getPressure();
			display();
		}
	}

	@Override
	public void display() {
		System.out.println("Current conditions(implement Observer in Util): " + temperature + "F degree and " + humidity + "% humidity and"
				+ pressure + " pressure");
	}

}
