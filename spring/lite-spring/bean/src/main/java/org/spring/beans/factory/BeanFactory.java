package org.spring.beans.factory;

import org.spring.beans.BeanDefinition;

public interface BeanFactory {

	BeanDefinition getBeanDefinition(String BeanId);

	Object getBean(String BeanName);

}
