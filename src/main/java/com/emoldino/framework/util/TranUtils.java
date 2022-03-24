package com.emoldino.framework.util;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TranUtils {

	public static <T> T doTran(Closure<T> closure) {
		return BeanUtils.get(TranUtils.class)._doTran(closure);
	}

	@Transactional
	public <T> T _doTran(Closure<T> closure) {
		return closure.execute();
	}

	public static <T> T doNewTran(Closure<T> closure) {
		return BeanUtils.get(TranUtils.class)._doNewTran(closure);
	}

	public static void doNewTran(ClosureNoReturn closure) {
		BeanUtils.get(TranUtils.class)._doNewTran(() -> {
			closure.execute();
			return null;
		});
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public <T> T _doNewTran(Closure<T> closure) {
		return closure.execute();
	}

}
