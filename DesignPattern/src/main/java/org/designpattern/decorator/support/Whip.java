package org.designpattern.decorator.support;

import org.designpattern.decorator.Beverage;
import org.designpattern.decorator.CondimentDecorator;

public class Whip extends CondimentDecorator {
	Beverage beverage;

	public Whip(Beverage beverage) {
		this.beverage = beverage;
		this.size = beverage.getSize();
	}

	@Override
	public String getDescription() {
		return beverage.getDescription() + ", Whip";
	}

	@Override
	public double coast() {
		double supercost = 0;
		switch (this.size) {
		case Beverage.TALL:
			supercost = .05;
		case Beverage.GRANDE:
			supercost = .15;
		case Beverage.VENTI:
			supercost = .25;
		}
		return supercost + beverage.coast();
	}

}
