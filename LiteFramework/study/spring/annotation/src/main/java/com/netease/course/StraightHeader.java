package com.netease.course;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StraightHeader implements Header {
	@Value("${color}")
	private String color;
	@Value("${size}")
	private int size;

	@Override
	public void doWork() {
		System.out.println("Do some works with StraightHeader");
	}

	@Override
	public String getInfo() {
		return "StraightHeader: color-" + color + " size-" + size;
	}

}
