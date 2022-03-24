package com.emoldino.framework.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClosureWrapper<T> {
	private String name;
	private Closure<T> closure;
}
