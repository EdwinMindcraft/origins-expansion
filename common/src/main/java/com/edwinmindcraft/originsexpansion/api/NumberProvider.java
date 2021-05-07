package com.edwinmindcraft.originsexpansion.api;

import java.util.function.Function;

public class NumberProvider<T, F extends Function<T, Number>> implements Function<T, Number> {
	private final NumberProviderFactory<T> factory;
	private final F function;

	public NumberProvider(NumberProviderFactory<T> factory, F function) {
		this.factory = factory;
		this.function = function;
	}

	public F getFunction() {
		return this.function;
	}

	public NumberProviderFactory<T> getFactory() {
		return this.factory;
	}

	@Override
	public Number apply(T t) {
		return this.function.apply(t);
	}
}
