package org.spring.beans.factory.support;

import org.spring.beans.BeanDefinition;

public class GenericBeanDefinition implements BeanDefinition {
	private String id;
	private String beanClassName;
	private boolean singleton = true;
	private boolean prototype = false;
	private String scope = SCOPE_DEFAULT;

	public GenericBeanDefinition(String id, String beanClassName) {
		this.id = id;
		this.beanClassName = beanClassName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isSingleton() {
		return singleton;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	public boolean isPrototype() {
		return prototype;
	}

	public void setPrototype(boolean prototype) {
		this.prototype = prototype;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
		this.singleton = SCOPE_SINGLETON.equals(scope) || SCOPE_DEFAULT.equals(scope);
		this.prototype = SCOPE_PROTOTYPE.equals(scope);
	}

	public void setBeanClassName(String beanClassName) {
		this.beanClassName = beanClassName;
	}

	public String getBeanClassName() {
		return beanClassName;
	}

}
