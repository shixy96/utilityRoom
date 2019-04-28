package org.designpattern.command.support;

public class Stereo {
	String place;
	int volume;

	public Stereo(String place) {
		this.place = place;
	}

	public void on() {
		System.out.println("[stereo on] Stereo is on");
	}

	public void off() {
		System.out.println("[stereo off] Stereo is off");
	}

	public void setCD() {
		System.out.println("[stereo on] Stereo has a CD now");
	}

	public void moveCD() {
		System.out.println("[stereo off] Stereo move the CD out");
	}

	public void setVolume(int volume) {
		this.volume = volume;
		System.out.println("[stereo on] set the volume to " + volume + " of the stereo");
	}
}
