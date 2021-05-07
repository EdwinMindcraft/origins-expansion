package com.edwinmindcraft.originsexpansion.api.providers;

import com.edwinmindcraft.originsexpansion.api.NumberProvider;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.function.Function;

public class DivisionProvider<T> implements Function<T, Number> {
	public static <T> Codec<DivisionProvider<T>> codec(Codec<NumberProvider<T, ?>> codec) {
		return RecordCodecBuilder.create(instance -> instance.group(
				codec.fieldOf("numerator").forGetter(DivisionProvider::getNumerator),
				codec.fieldOf("denominator").forGetter(DivisionProvider::getDenominator)
		).apply(instance, DivisionProvider::new));
	}

	private final NumberProvider<T, ?> numerator;
	private final NumberProvider<T, ?> denominator;

	public DivisionProvider(NumberProvider<T, ?> numerator, NumberProvider<T, ?> denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}

	public NumberProvider<T, ?> getDenominator() {
		return this.denominator;
	}

	public NumberProvider<T, ?> getNumerator() {
		return this.numerator;
	}

	@Override
	public Number apply(T t) {
		return this.numerator.apply(t).doubleValue() / this.denominator.apply(t).doubleValue();
	}
}
