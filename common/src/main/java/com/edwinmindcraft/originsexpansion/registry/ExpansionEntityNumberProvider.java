package com.edwinmindcraft.originsexpansion.registry;

import com.edwinmindcraft.originsexpansion.OriginsExpansion;
import com.edwinmindcraft.originsexpansion.api.NumberProviderFactory;
import com.edwinmindcraft.originsexpansion.api.OriginsExpansionHelper;
import com.edwinmindcraft.originsexpansion.providers.BrightnessProvider;
import com.edwinmindcraft.originsexpansion.providers.ResourceProvider;
import com.mojang.serialization.Codec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ExpansionEntityNumberProvider {

	public static void register() {
		OriginsExpansionHelper.defineMetaNumberProviders(ExpansionRegistries.NUMBER_PROVIDER_ENTITY);
		register("brightness", new NumberProviderFactory<>(BrightnessProvider.CODEC));
		register("health", new NumberProviderFactory<>(Codec.unit(x -> x instanceof LivingEntity ? ((LivingEntity) x).getHealth() : 0)));
		register("resource", new NumberProviderFactory<>(ResourceProvider.CODEC));
	}

	public static void register(String name, NumberProviderFactory<Entity> provider) {
		ExpansionRegistries.NUMBER_PROVIDER_ENTITY.register(OriginsExpansion.get(name), () -> provider);
	}
}
