package org.designpattern.command.support;

public class CeilingFan {
	public static final int HIGH = 3;
	public static final int MEDIUM = 2;
	public static final int LOW = 1;
	public static final int OFF = 0;
	private String location;
	private int spead;

	public CeilingFan(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	public void high() {
		spead = HIGH;
	}

	public void medium() {
		spead = MEDIUM;
	}

	public void low() {
		spead = LOW;
	}

	public void off() {
		spead = OFF;
	}

	public int getSpead() {
		return spead;
	}
}
