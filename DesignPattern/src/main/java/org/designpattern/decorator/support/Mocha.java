package org.designpattern.decorator.support;

import org.designpattern.decorator.Beverage;
import org.designpattern.decorator.CondimentDecorator;

public class Mocha extends CondimentDecorator {
	Beverage beverage;

	public Mocha(Beverage beverage) {
		this.beverage = beverage;
		this.size = beverage.getSize();
	}

	@Override
	public String getDescription() {
		return beverage.getDescription() + ", Mocha";
	}

	@Override
	public double coast() {
		double supercost = 0;
		switch (this.size) {
		case Beverage.TALL:
			supercost = .10;
		case Beverage.GRANDE:
			supercost = .20;
		case Beverage.VENTI:
			supercost = .30;
		}
		return supercost + beverage.coast();
	}

}
