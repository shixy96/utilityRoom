package org.designpattern.factory.support;

import org.designpattern.factory.Pizza;

public class ChicagoStyleCheesePizza extends Pizza {
	public ChicagoStyleCheesePizza() {
		name = "Chicago Style Deep Dish Cheese Pizza";
		dough = "Extra Thick Crust Dough";
		sauce = "Plum Tomato Sauce";

		topping.add("Sharedded Mozzarella Cheese");
	}

	@Override
	protected void cut() {
		System.out.println("Cutting the pizza into square slices");
	}
}
