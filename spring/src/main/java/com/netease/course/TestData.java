package com.netease.course;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestData {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("springmvc-config.xml");
		JdbcTemplateData guideManage = (JdbcTemplateData) context.getBean("jdbcTemplateData");
		//guideManage.creatTable();
		//guideManage.insertData();
		System.out.println(guideManage.count());
		((ConfigurableApplicationContext) context).close();
	}

}
