package org.designpattern.decorator;

public abstract class Beverage {
	public static final String TALL = "TALL";
	public static final String GRANDE = "GRANDE";
	public static final String VENTI = "VENTI";

	protected String description = "Unknow Beverage";
	protected String size;

	public String getDescription() {
		return description;
	}

	public String getSize() {
		return size;
	}

	public abstract double coast();
}
