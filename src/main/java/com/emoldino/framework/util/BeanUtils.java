package com.emoldino.framework.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BeanUtils {
	public static BeanUtils instance;

	@Autowired
	private ApplicationContext beans;

	public BeanUtils() {
		instance = this;
	}

	public static <T> T get(Class<T> clazz) {
		return instance.beans.getBean(clazz);
	}

	public static <T> T get(String name) {
		return (T) instance.beans.getBean(name);
	}
}
