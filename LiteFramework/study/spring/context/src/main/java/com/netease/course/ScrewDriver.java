package com.netease.course;

public class ScrewDriver {
	private String color = "red";

	public void use() {
		System.out.println("use " + color + " screwdirver");
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void init() {
		System.out.println("init screwDriver");
	}

	public void cleanUp() {
		System.out.println("cleanUp screwDriver");
	}
}
