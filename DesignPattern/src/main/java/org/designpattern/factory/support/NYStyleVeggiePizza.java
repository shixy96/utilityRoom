package org.designpattern.factory.support;

import org.designpattern.factory.Pizza;

public class NYStyleVeggiePizza extends Pizza {
	public NYStyleVeggiePizza() {
		name = "NY Style Sauce and Veggie Pizza";
		dough = "Thin Crust Dough";
		sauce = "Marinara Sauce";

		topping.add("Grated Veggie");
	}
}
