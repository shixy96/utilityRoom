package org.designpattern.decorator;

import org.designpattern.decorator.support.Espresso;
import org.designpattern.decorator.support.HouseBlend;
import org.designpattern.decorator.support.Mocha;
import org.designpattern.decorator.support.Soy;
import org.designpattern.decorator.support.Whip;

public class StarbuzzCoffee {
	public static void main(String args[]) {
		Beverage beverage = new Espresso(Beverage.TALL);
		System.out.println(beverage.getDescription() + " $" + beverage.coast());

		Beverage beverage2 = new HouseBlend(Beverage.GRANDE);
		beverage2 = new Soy(beverage2);
		beverage2 = new Mocha(beverage2);
		beverage2 = new Whip(beverage2);
		System.out.println(beverage2.getDescription() + " $" + beverage2.coast());
	}
}
