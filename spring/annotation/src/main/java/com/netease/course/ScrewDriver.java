package com.netease.course;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

@Controller
public class ScrewDriver {
	@Resource
	Header header;

	public void use() {
		System.out.println("ScrewDriver use " + header.getInfo() + "");
	}

	@PostConstruct
	public void init() {
		System.out.println("init ScrewDriver");
	}

	@PreDestroy
	public void destroy() {
		System.out.println("destroy ScrewDriver");
	}
}
