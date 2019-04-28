package org.designpattern.factory;

import java.util.ArrayList;

public abstract class Pizza {
	protected String name;
	protected String dough;
	protected String sauce;
	protected ArrayList<String> topping = new ArrayList<String>();

	protected void prepare() {
		System.out.println("Preparing " + name);
		System.out.println("Tossing dough..");
		System.out.println("Add sauce");
		for (int i = 0, l = topping.size(); i < l; i++) {
			System.out.println("  " + topping.get(i));
		}
	}

	protected void bake() {
		System.out.println("Bake for 25 minutes at 350");
	}

	protected void cut() {
		System.out.println("Cutting the pizza into diagonal slices");
	}

	protected void box() {
		System.out.println("Place pizza in official PizzaStore box");
	}

	public String getName() {
		return name;
	}
}
