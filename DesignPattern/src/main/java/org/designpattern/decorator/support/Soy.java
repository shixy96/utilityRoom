package org.designpattern.decorator.support;

import org.designpattern.decorator.Beverage;
import org.designpattern.decorator.CondimentDecorator;

public class Soy extends CondimentDecorator {
	Beverage beverage;

	public Soy(Beverage beverage) {
		this.beverage = beverage;
		this.size = beverage.getSize();
	}

	@Override
	public String getDescription() {
		return beverage.getDescription() + ", Soy";
	}

	@Override
	public double coast() {
		double supercost = 0;
		switch (this.size) {
		case Beverage.TALL:
			supercost = .15;
		case Beverage.GRANDE:
			supercost = .25;
		case Beverage.VENTI:
			supercost = .35;
		}
		return supercost + beverage.coast();
	}

}
