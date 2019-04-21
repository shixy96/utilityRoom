package org.designpattern.observer;

import org.designpattern.observer.support.CurrentConditionsDisplay;
import org.designpattern.observer.support.CurrentConditionsDisplayImplementUtil;
import org.designpattern.observer.support.WeatherData;
import org.designpattern.observer.support.WeatherDataExtendUtil;

public class WeatherStation {
	public static void main(String[] args) {
		WeatherData weatherData = new WeatherData();
		CurrentConditionsDisplay currentConditionsDisplay = new CurrentConditionsDisplay(weatherData);
		weatherData.setMeasurements(80, 65, 30.4f);

		System.out.println();

		WeatherDataExtendUtil wDataExtendUtil = new WeatherDataExtendUtil();
		CurrentConditionsDisplayImplementUtil cDisplayImplementUtil = new CurrentConditionsDisplayImplementUtil(
				wDataExtendUtil);
		wDataExtendUtil.setMeasurements(80, 65, 30.4f);

	}
}
