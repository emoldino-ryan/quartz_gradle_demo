package com.emoldino.framework.util;

import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class HttpUtils {
	public static HttpServletRequest getRequest() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest req = attrs == null ? null : attrs.getRequest();
		return req;
	}

	public static String getReferer(HttpServletRequest req) {
		if (req == null) {
			return null;
		}
		String referer = req.getHeader("Referer");
		return referer;
	}

	public static String getRequestUrl(HttpServletRequest req) {
		StringBuilder buf = new StringBuilder();
		buf.append(req.getMethod()).append(" ");
		String target = req.getRequestURL().toString();
		buf.append(target);
		return buf.toString();
	}

	public static String getParamsStr(HttpServletRequest req) {
		if (req == null || req.getParameterMap() == null) {
			return null;
		}
		StringBuilder buf = new StringBuilder();
		int[] i = { 0 };
		req.getParameterMap().forEach((k, v) -> {
			if (ObjectUtils.isEmpty(v)) {
				return;
			}
			for (String str : v) {
				if (ObjectUtils.isEmpty(str)) {
					continue;
				}
				buf.append(i[0]++ == 0 ? "" : "\r\n&").append(k).append("=").append(str);
			}
		});
		return buf.toString();
	}

}
