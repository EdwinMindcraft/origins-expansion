package com.edwinmindcraft.originsexpansion.api;

import com.edwinmindcraft.originsexpansion.OriginsExpansion;
import com.edwinmindcraft.originsexpansion.api.dfu.InlineCodec;
import com.edwinmindcraft.originsexpansion.api.providers.ConstantProvider;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import me.shedaniel.architectury.registry.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.DoubleFunction;
import java.util.function.Function;

public class NumberProviderFactory<T> {

	public static <T> DoubleFunction<NumberProvider<T, ?>> constant(Registry<NumberProviderFactory<T>> registry) {
		return d -> new NumberProvider<>(registry.get(OriginsExpansion.get("constant")), new ConstantProvider<>(d));
	}

	public static <T> Codec<NumberProviderFactory<T>> codec(Registry<NumberProviderFactory<T>> registry) {
		return ResourceLocation.CODEC.xmap(registry::get, registry::getId);
	}

	public static <T> Codec<Either<Double, NumberProvider<T, ?>>> eitherCodec(Codec<NumberProviderFactory<T>> parent) {
		Codec<NumberProvider<T, ?>> inlineCodec = new InlineCodec<>("type", parent, NumberProvider::getFactory, NumberProviderFactory::getCodec);
		return Codec.either(Codec.DOUBLE, inlineCodec);
	}

	public static <T> Codec<NumberProvider<T, ?>> resultCodec(Codec<Either<Double, NumberProvider<T, ?>>> eitherCodec, Registry<NumberProviderFactory<T>> registry) {
		return eitherCodec.xmap(either -> either.map(l -> new NumberProvider<>(registry.get(OriginsExpansion.get("constant")), new ConstantProvider<>(l)), Function.identity()), Either::right);
	}

	private final Codec<NumberProvider<T, ?>> codec;

	@SuppressWarnings({"rawtypes", "unchecked"})
	public <F extends Function<T, Number>> NumberProviderFactory(Codec<F> codec) {
		this.codec = (Codec<NumberProvider<T, ?>>) (Codec) codec.xmap(f -> new NumberProvider<>(this, f), NumberProvider::getFunction);
	}

	public Codec<NumberProvider<T, ?>> getCodec() {
		return this.codec;
	}
}
