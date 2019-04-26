package org.designpattern.factory.support;

import org.designpattern.factory.Pizza;
import org.designpattern.factory.PizzaStore;

public class ChicagoPizzaStore extends PizzaStore {

	@Override
	protected Pizza createPizza(String type) {
		if (type.equals("cheese")) {
			return new ChicagoStyleCheesePizza();
		} else if (type.equals("veggie")) {
			return new ChicagoStyleVeggiePizza();
		}
		return null;
	}

}
