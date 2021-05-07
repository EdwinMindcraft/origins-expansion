package com.edwinmindcraft.originsexpansion.api.providers;

import com.edwinmindcraft.originsexpansion.api.NumberProvider;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.DoubleStream;

public class StreamProvider<T> implements Function<T, Number> {

	public static <T> Codec<StreamProvider<T>> codec(Codec<List<NumberProvider<T, ?>>> codec, ToDoubleFunction<DoubleStream> function) {
		return RecordCodecBuilder.create(instance -> instance.group(
				codec.fieldOf("values").forGetter(StreamProvider::getChildren)
		).apply(instance, s -> new StreamProvider<>(function, s)));
	}

	private final ToDoubleFunction<DoubleStream> function;
	private final List<NumberProvider<T, ?>> children;

	public StreamProvider(ToDoubleFunction<DoubleStream> function, List<NumberProvider<T, ?>> children) {
		this.function = function;
		this.children = children;
	}

	public List<NumberProvider<T, ?>> getChildren() {
		return this.children;
	}

	@Override
	public Number apply(T t) {
		return this.function.applyAsDouble(this.children.stream().mapToDouble(x -> x.apply(t).doubleValue()));
	}
}
