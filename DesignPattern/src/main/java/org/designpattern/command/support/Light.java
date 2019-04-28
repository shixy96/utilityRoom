package org.designpattern.command.support;

public class Light {
	String place;

	public Light(String place) {
		this.place = place;
	}

	public void on() {
		System.out.println("[light on]" + place + " Light is on");
	}

	public void off() {
		System.out.println("[light off]" + place + " Light is off");
	}
}
