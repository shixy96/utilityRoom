package org.designpattern.decorator.support;

import org.designpattern.decorator.Beverage;

public class Espresso extends Beverage {

	public Espresso(String size) {
		this.size = size;
		description = "Espresso " + this.size;
	}

	@Override
	public double coast() {
		switch (this.size) {
		case Beverage.TALL:
			return 1.89;
		case Beverage.GRANDE:
			return 1.99;
		case Beverage.VENTI:
			return 2.0;
		default:
			throw new IllegalArgumentException("must have size");
		}
	}

}
