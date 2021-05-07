package com.edwinmindcraft.originsexpansion.api.providers;

import com.edwinmindcraft.originsexpansion.api.NumberProvider;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;

import java.util.function.DoubleFunction;
import java.util.function.Function;

public class ClampProvider<T> implements Function<T, Number> {

	public static <T> Codec<ClampProvider<T>> codec(Codec<NumberProvider<T,?>> codec, DoubleFunction<NumberProvider<T, ?>> constant) {
		return RecordCodecBuilder.create(instance -> instance.group(
				codec.fieldOf("input").forGetter(ClampProvider::getInput),
				codec.fieldOf("min").orElseGet(() -> constant.apply(0.0D)).forGetter(ClampProvider::getMin),
				codec.fieldOf("max").orElseGet(() -> constant.apply(1.0D)).forGetter(ClampProvider::getMax)
		).apply(instance, ClampProvider::new));
	}

	private final NumberProvider<T, ?> input;
	private final NumberProvider<T, ?> min;
	private final NumberProvider<T, ?> max;

	public ClampProvider(NumberProvider<T, ?> input, NumberProvider<T, ?> min, NumberProvider<T, ?> max) {
		this.input = input;
		this.min = min;
		this.max = max;
	}

	public NumberProvider<T, ?> getInput() {
		return this.input;
	}

	public NumberProvider<T, ?> getMin() {
		return this.min;
	}

	public NumberProvider<T, ?> getMax() {
		return this.max;
	}

	@Override
	public Number apply(T t) {
		return Mth.clamp(this.input.apply(t).doubleValue(), this.min.apply(t).doubleValue(), this.max.apply(t).doubleValue());
	}
}
