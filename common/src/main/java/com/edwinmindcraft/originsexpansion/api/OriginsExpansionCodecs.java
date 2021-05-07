package com.edwinmindcraft.originsexpansion.api;

import com.edwinmindcraft.originsexpansion.registry.ExpansionRegistries;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.PowerTypeReference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class OriginsExpansionCodecs {
	public static final Codec<PowerType<?>> POWER_TYPE = ResourceLocation.CODEC.xmap(PowerTypeReference::new, PowerType::getIdentifier);

	public static final Codec<NumberProviderFactory<Entity>> ENTITY_NUMBER_PROVIDER_FACTORY = NumberProviderFactory.codec(ExpansionRegistries.NUMBER_PROVIDER_ENTITY);
	public static final Codec<Either<Double, NumberProvider<Entity, ?>>> ENTITY_NUMBER_PROVIDER_FACTORY_OR_DOUBLE = NumberProviderFactory.eitherCodec(ENTITY_NUMBER_PROVIDER_FACTORY);
	public static final Codec<NumberProvider<Entity, ?>> ENTITY_NUMBER_PROVIDER = NumberProviderFactory.resultCodec(ENTITY_NUMBER_PROVIDER_FACTORY_OR_DOUBLE, ExpansionRegistries.NUMBER_PROVIDER_ENTITY);
	public static final Codec<List<NumberProvider<Entity, ?>>> ENTITY_NUMBER_PROVIDERS = ENTITY_NUMBER_PROVIDER.listOf();
}
