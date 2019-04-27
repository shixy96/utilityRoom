package org.designpattern.singleton;

import static org.junit.Assert.*;

import org.junit.Test;

public class SingletonTest {

	@Test
	public void test() {
		Singleton singleton1 = Singleton.getInstance();
		Singleton singleton2 = Singleton.getInstance();
		assertEquals(singleton1, singleton2);
	}

}
