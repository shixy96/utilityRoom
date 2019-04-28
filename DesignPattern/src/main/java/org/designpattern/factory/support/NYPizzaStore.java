package org.designpattern.factory.support;

import org.designpattern.factory.Pizza;
import org.designpattern.factory.PizzaStore;

public class NYPizzaStore extends PizzaStore {

	@Override
	protected Pizza createPizza(String type) {
		if(type.equals("cheese")) {
			return new NYStyleCheesePizza();
		} else if(type.equals("veggie")) {
			return new NYStyleVeggiePizza();
		}
		return null;
	}
	
}
