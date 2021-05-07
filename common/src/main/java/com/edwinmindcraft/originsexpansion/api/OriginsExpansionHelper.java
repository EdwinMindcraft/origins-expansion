package com.edwinmindcraft.originsexpansion.api;

import com.edwinmindcraft.originsexpansion.OriginsExpansion;
import com.edwinmindcraft.originsexpansion.api.providers.*;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import me.shedaniel.architectury.registry.Registry;

import java.util.List;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;

public class OriginsExpansionHelper {

	public static <T> void defineMetaNumberProviders(Registry<NumberProviderFactory<T>> registry) {
		Codec<NumberProviderFactory<T>> factoryCodec = NumberProviderFactory.codec(registry);
		Codec<Either<Double, NumberProvider<T, ?>>> eitherCodec = NumberProviderFactory.eitherCodec(factoryCodec);
		Codec<NumberProvider<T, ?>> instanceCodec = NumberProviderFactory.resultCodec(eitherCodec, registry);
		Codec<List<NumberProvider<T, ?>>> instanceListCodec = instanceCodec.listOf();
		DoubleFunction<NumberProvider<T, ?>> constant = NumberProviderFactory.constant(registry);

		registry.register(OriginsExpansion.get("constant"), () -> new NumberProviderFactory<>(ConstantProvider.codec()));
		registry.register(OriginsExpansion.get("sum"), () -> new NumberProviderFactory<>(StreamProvider.codec(instanceListCodec, DoubleStream::sum)));
		registry.register(OriginsExpansion.get("product"), () -> new NumberProviderFactory<>(StreamProvider.codec(instanceListCodec, x -> x.reduce(1, (left, right) -> left * right))));
		registry.register(OriginsExpansion.get("division"), () -> new NumberProviderFactory<>(DivisionProvider.codec(instanceCodec)));
		registry.register(OriginsExpansion.get("subtraction"), () -> new NumberProviderFactory<>(SubtractionProvider.codec(instanceCodec)));
		registry.register(OriginsExpansion.get("clamp"), () -> new NumberProviderFactory<>(ClampProvider.codec(instanceCodec, constant)));
		registry.register(OriginsExpansion.get("min"), () -> new NumberProviderFactory<>(StreamProvider.codec(instanceListCodec, x -> x.min().orElse(Double.NEGATIVE_INFINITY))));
		registry.register(OriginsExpansion.get("max"), () -> new NumberProviderFactory<>(StreamProvider.codec(instanceListCodec, x -> x.max().orElse(Double.POSITIVE_INFINITY))));
	}
}
