package com.emoldino.framework.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class ServerUtils {

	@Value("${http.port:0}")
	private int httpPort;

	private static ServerUtils instance;

	public ServerUtils() {
		instance = this;
	}

	public static String getName() {
		String name;
		try {
			name = InetAddress.getLocalHost().getHostName() + ":" + instance.httpPort;
		} catch (UnknownHostException e) {
			name = instance.httpPort + "";
		}
		return name;
	}

}
