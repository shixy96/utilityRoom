package org.designpattern.decorator.support;

import org.designpattern.decorator.Beverage;

public class HouseBlend extends Beverage {

	public HouseBlend(String size) {
		this.size = size;
		description = "House Blend Coffee " + this.size;
	}

	@Override
	public double coast() {
		switch (this.size) {
			case Beverage.TALL:
				return .79;
			case Beverage.GRANDE:
				return .89;
			case Beverage.VENTI:
				return .99;
			default:
				throw new IllegalArgumentException("must have size");
		}
	}

}
