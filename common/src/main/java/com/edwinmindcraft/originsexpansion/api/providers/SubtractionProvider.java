package com.edwinmindcraft.originsexpansion.api.providers;

import com.edwinmindcraft.originsexpansion.api.NumberProvider;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.function.Function;

public class SubtractionProvider<T> implements Function<T, Number> {
	public static <T> Codec<SubtractionProvider<T>> codec(Codec<NumberProvider<T, ?>> codec) {
		return RecordCodecBuilder.create(instance -> instance.group(
				codec.fieldOf("first").forGetter(SubtractionProvider::getFirst),
				codec.fieldOf("second").forGetter(SubtractionProvider::getSecond)
		).apply(instance, SubtractionProvider::new));
	}

	private final NumberProvider<T, ?> first;
	private final NumberProvider<T, ?> second;

	public SubtractionProvider(NumberProvider<T, ?> first, NumberProvider<T, ?> second) {
		this.first = first;
		this.second = second;
	}

	public NumberProvider<T, ?> getSecond() {
		return this.second;
	}

	public NumberProvider<T, ?> getFirst() {
		return this.first;
	}

	@Override
	public Number apply(T t) {
		return this.first.apply(t).doubleValue() - this.second.apply(t).doubleValue();
	}
}
