package org.litespring.bean.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.litespring.bean.service.PetStoreService;
import org.spring.beans.BeanDefinition;
import org.spring.beans.factory.BeanFactory;
import org.spring.beans.factory.support.DefaultBeanFactory;

public class BeanFactoryTest {

	@Test
	public void testGetBean() {
		BeanFactory factory = new DefaultBeanFactory("petstore.xml");

		BeanDefinition beanDefinition = factory.getBeanDefinition("petstore");

		assertEquals("org.litespring.bean.service.PetStoreService", beanDefinition.getBeanClassName());

		PetStoreService petStore = (PetStoreService) factory.getBean("petstore");

		assertNotNull(petStore);
	}

}
