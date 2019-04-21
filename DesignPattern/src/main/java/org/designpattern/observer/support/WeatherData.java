package org.designpattern.observer.support;

import java.util.ArrayList;

import org.designpattern.observer.Observer;
import org.designpattern.observer.Subject;

public class WeatherData implements Subject {
	private ArrayList<Observer> observers;
	private float temperature;
	private float humidity;
	private float pressure;

	public WeatherData() {
		observers = new ArrayList<Observer>();
	}

	@Override
	public void registerObserver(Observer observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		int index = observers.indexOf(observer);
		if (index > -1) {
			observers.remove(index);
		}
	}

	@Override
	public void notifyObservers() {
		for (int i = 0, l = observers.size(); i < l; i++) {
			Observer observer = observers.get(i);
			observer.update(temperature, humidity, pressure);
		}
	}

	public void measurementsChanged() {
		notifyObservers();
	}

	public void setMeasurements(float temperature, float humidity, float pressure) {
		this.pressure = pressure;
		this.humidity = humidity;
		this.temperature = temperature;
		measurementsChanged();
	}

}
