package org.designpattern.factory.support;

import org.designpattern.factory.Pizza;

public class NYStyleCheesePizza extends Pizza {
	public NYStyleCheesePizza() {
		name = "NY Style Sauce and Cheese Pizza";
		dough = "Thin Crust Dough";
		sauce = "Marinara Sauce";

		topping.add("Grated Reggiano Cheese");
	}
}
