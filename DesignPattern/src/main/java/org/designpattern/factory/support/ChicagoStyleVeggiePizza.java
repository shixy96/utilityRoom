package org.designpattern.factory.support;

import org.designpattern.factory.Pizza;

public class ChicagoStyleVeggiePizza extends Pizza {
	public ChicagoStyleVeggiePizza() {
		name = "Chicago Style Sauce and Veggie Pizza";
		dough = "Extra Thick Crust Dough";
		sauce = "Plum Tomato Sauce";

		topping.add("Grated Veggie");
	}

	@Override
	protected void cut() {
		System.out.println("Cutting the pizza into square slices");
	}
}
