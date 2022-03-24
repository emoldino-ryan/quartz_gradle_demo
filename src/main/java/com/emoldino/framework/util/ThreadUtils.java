package com.emoldino.framework.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Slf4j
public class ThreadUtils {
	private static ThreadLocal<Map<String, Object>> tprops = new ThreadLocal<>();

	public static boolean isInitialized() {
		return tprops.get() != null;
	}

	private static void initProps() {
		if (log.isDebugEnabled()) {
			log.debug("Init Thread Props");
		}

		tprops.set(new HashMap<>());

		Stack<String> stack = new Stack<>();
		setProp("ThreadUtils.stack", stack);
	}

	private static void removeProps() {
		tprops.remove();

		if (log.isDebugEnabled()) {
			log.debug("Clear Thread Props (GapTime: " + procGapTime().getGapTimeMillis() + ")");
		}
	}

	public static <V> V doScopeSupports(String name, Closure<V> closure) {
		if (isInitialized()) {
			return closure.execute();
		}
		return doScope(name, closure);
	}

	public static void doScopeSupports(String name, ClosureNoReturn closure) {
		if (isInitialized()) {
			closure.execute();
		} else {
			doScope(name, closure);
		}
	}

	public static void doScope(String name, ClosureNoReturn closure) {
		doScope(name, () -> {
			closure.execute();
			return null;
		});
	}

	public static <V> V doScope(String name, Closure<V> closure) {
		boolean firstStack = !isInitialized();
		if (firstStack) {
			initProps();
			setStartTimeMillis(System.currentTimeMillis());
		}

		Stack<String> stack = null;
		boolean stackAdded = false;
		try {
			stack = getStack();
			stack.push(ObjectUtils.isEmpty(name) ? "null" : name);
			stackAdded = true;

			V resp = closure.execute();

			if (firstStack) {
				getAfter().forEach((k, item) -> {
					try {
						item.execute();
					} catch (Exception e1) {
						log.warn(e1.getMessage(), e1);
					}
				});
			}

			return resp;

		} catch (RuntimeException e) {
			if (firstStack) {
				getThrows().forEach((k, item) -> {
					try {
						item.execute();
					} catch (Exception e1) {
						log.warn(e1.getMessage(), e1);
					}
				});
			}
			throw e;
		} finally {
			if (firstStack) {
				getFinally().forEach((k, item) -> {
					try {
						item.execute();
					} catch (Exception e1) {
						log.warn(e1.getMessage(), e1);
					}
				});
			}

			if (stackAdded) {
				stack.pop();
			}
			if (firstStack) {
				removeProps();
			}
		}
	}

	public static boolean isFirstStack() {
		Stack<String> stack = getStack();
		return stack != null && stack.size() <= 1;
	}

	@SuppressWarnings("unchecked")
	private static Stack<String> getStack() {
		return (Stack<String>) getProp("ThreadUtils.stack");
	}

	private static Map<String, Closure<?>> getAfter() {
		return Optional.ofNullable(_getAfter()).orElseGet(() -> Collections.emptyMap());
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Closure<?>> _getAfter() {
		return (Map<String, Closure<?>>) ThreadUtils.getProp("ThreadUtils.after");
	}

	public static void putAfter(String key, Closure<?> closure) {
		Map<String, Closure<?>> map = _getAfter();
		if (map == null) {
			map = new LinkedHashMap<>();
			ThreadUtils.setProp("ThreadUtils.after", map);
		}
		map.put(key, closure);
	}

	public static void addAfter(Closure<?> closure) {
		Map<String, Closure<?>> map = _getAfter();
		if (map == null) {
			map = new LinkedHashMap<>();
			ThreadUtils.setProp("ThreadUtils.after", map);
		}
		map.put(UUID.randomUUID().toString(), closure);
	}

	private static Map<String, Closure<?>> getThrows() {
		return Optional.ofNullable(_getThrows()).orElseGet(() -> Collections.emptyMap());
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Closure<?>> _getThrows() {
		return (Map<String, Closure<?>>) ThreadUtils.getProp("ThreadUtils.throws");
	}

	public static void putThrows(String key, Closure<?> closure) {
		Map<String, Closure<?>> map = _getThrows();
		if (map == null) {
			map = new LinkedHashMap<>();
			ThreadUtils.setProp("ThreadUtils.throws", map);
		}
		map.put(key, closure);
	}

	public static void addThrows(Closure<?> closure) {
		Map<String, Closure<?>> map = _getThrows();
		if (map == null) {
			map = new LinkedHashMap<>();
			ThreadUtils.setProp("ThreadUtils.throws", map);
		}
		map.put(UUID.randomUUID().toString(), closure);
	}

	private static Map<String, Closure<?>> getFinally() {
		return Optional.ofNullable(_getFinally()).orElseGet(() -> Collections.emptyMap());
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Closure<?>> _getFinally() {
		return (Map<String, Closure<?>>) ThreadUtils.getProp("ThreadUtils.finally");
	}

	public static void putFinally(String key, Closure<?> closure) {
		Map<String, Closure<?>> map = _getFinally();
		if (map == null) {
			map = new LinkedHashMap<>();
			ThreadUtils.setProp("ThreadUtils.finally", map);
		}
		map.put(key, closure);
	}

	public static void addFinally(Closure<?> closure) {
		Map<String, Closure<?>> map = _getFinally();
		if (map == null) {
			map = new LinkedHashMap<>();
			ThreadUtils.setProp("ThreadUtils.finally", map);
		}
		map.put(UUID.randomUUID().toString(), closure);
	}

	public static long getStartTimeMillis() {
		Long startTimeMillis = (Long) ThreadUtils.getProp("ThreadUtils.startTimeMillis");
		return startTimeMillis == null ? 0 : startTimeMillis;
	}

	private static void setStartTimeMillis(long startTimeMillis) {
		ThreadUtils.setProp("ThreadUtils.startTimeMillis", startTimeMillis);
	}

	public static long getPrevTimeMillis() {
		Long prevTimeMillis = (Long) ThreadUtils.getProp("ThreadUtils.prevTimeMillis");
		if (prevTimeMillis != null) {
			return prevTimeMillis;
		}
		return getStartTimeMillis();
	}

	private static void setPrevTimeMillis(long prevTimeMillis) {
		ThreadUtils.setProp("ThreadUtils.prevTimeMillis", prevTimeMillis);
	}

	public static ExecutionTime getExecutionTime() {
		long startTimeMillis = getStartTimeMillis();
		if (startTimeMillis == 0) {
			return new ExecutionTime(0, 0, 0);
		}
		long prevTimeMillis = getPrevTimeMillis();
		long currentTimeMillis = System.currentTimeMillis();

		ExecutionTime time = new ExecutionTime(startTimeMillis, prevTimeMillis, currentTimeMillis);
		time.setPrevTimeMillis(prevTimeMillis);
		time.setCurrentTimeMillis(currentTimeMillis);
		return time;
	}

	public static ExecutionTime procGapTime() {
		ExecutionTime time = getExecutionTime();
		if (time.getCurrentTimeMillis() != 0) {
			setPrevTimeMillis(time.getCurrentTimeMillis());
		}
		return time;
	}

	@Data
	@AllArgsConstructor
	public static class ExecutionTime {
		private long startTimeMillis;
		private long prevTimeMillis;
		private long currentTimeMillis;

		public long getGapTimeMillis() {
			return currentTimeMillis - prevTimeMillis;
		}

		public long getElapsedTimeMillis() {
			return currentTimeMillis - startTimeMillis;
		}
	}

	public static <T> T getProp(String name, Closure<T> closure) {
		@SuppressWarnings("unchecked")
		T value = (T) ThreadUtils.getProp(name);
		if (value != null) {
			return value;
		}
		value = closure.execute();
		if (value == null) {
			return null;
		}
		ThreadUtils.setProp(name, value);
		return value;
	}

	public static Object getProp(String name) {
		Map<String, Object> map = tprops.get();
		if (map == null) {
			return null;
		}
		Object value = map.get(name);
		return value;
	}

	public static void setProp(String name, Object value) {
		Map<String, Object> map = tprops.get();
		if (map == null) {
			return;
		}
		map.put(name, value);
	}

	public static Map<String, Object> getProps() {
		Map<String, Object> map = tprops.get();
		if (map == null) {
			return Collections.emptyMap();
		}
		return new HashMap<String, Object>(map);
	}

	public static void putProps(Map<String, Object> props) {
		if (props == null) {
			return;
		}
		Map<String, Object> map = tprops.get();
		if (map == null) {
			return;
		}
		map.putAll(props);
	}

	public static void setPropApiName(String name) {
		ThreadUtils.setProp("ThreadUtils.apiName", name);
	}

	public static String getPropApiName() {
		String name = (String) ThreadUtils.getProp("ThreadUtils.apiName");
		return name;
	}

}
