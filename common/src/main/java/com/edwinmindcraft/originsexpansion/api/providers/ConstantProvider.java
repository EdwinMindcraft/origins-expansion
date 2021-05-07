package com.edwinmindcraft.originsexpansion.api.providers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.function.Function;

public class ConstantProvider<T> implements Function<T, Number> {

	public static <T> Codec<ConstantProvider<T>> codec() {
		return Codec.DOUBLE.xmap(ConstantProvider::new, ConstantProvider::getValue);//RecordCodecBuilder.create(instance -> instance.group(Codec.DOUBLE.fieldOf("value").forGetter(ConstantProvider::getValue)).apply(instance, ConstantProvider::new));
	}

	private final double value;

	public ConstantProvider(double value) {
		this.value = value;
	}

	public double getValue() {
		return this.value;
	}

	@Override
	public Number apply(T t) {
		return this.value;
	}
}
