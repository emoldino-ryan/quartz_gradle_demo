package com.emoldino.framework.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class CacheUtils {

	public static <K, V> V get(final Map<K, V> cache, final K key, final Closure<V> closure) {
		Assert.notNull(cache, "cache is required!!");

		boolean debug = log.isDebugEnabled();

		if (cache.containsKey(key)) {
			if (debug) {
				log.debug("get data from map cache by key: " + key);
			}
			return cache.get(key);
		}

		if (debug) {
			log.debug("get data by executing closure.");
		}
		V value = closure.execute();

		if (value != null) {
			if (debug) {
				log.debug("put data to map cache by key: " + key);
			}
			cache.put(key, value);
		}

		return value;
	}

	public static <K, V> V getNullable(final Map<K, Optional<V>> cache, final K key, final Closure<V> closure) {
		Assert.notNull(cache, "cache is required!!");

		boolean debug = log.isDebugEnabled();

		if (cache.containsKey(key)) {
			if (debug) {
				log.debug("get data from map cache by key: " + key);
			}
			return cache.get(key).orElse(null);
		}

		if (debug) {
			log.debug("get data by executing closure.");
		}
		V value = closure.execute();

		if (debug) {
			log.debug("put data to map cache by key: " + key);
		}
		cache.put(key, Optional.ofNullable(value));

		return value;
	}

}
