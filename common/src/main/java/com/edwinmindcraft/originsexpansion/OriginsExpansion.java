package com.edwinmindcraft.originsexpansion;

import com.edwinmindcraft.originsexpansion.api.NumberProvider;
import com.edwinmindcraft.originsexpansion.api.NumberProviderFactory;
import com.edwinmindcraft.originsexpansion.api.OriginsExpansionCodecs;
import com.edwinmindcraft.originsexpansion.api.dfu.InlineJsonOps;
import com.edwinmindcraft.originsexpansion.api.providers.ConstantProvider;
import com.edwinmindcraft.originsexpansion.api.providers.StreamProvider;
import com.edwinmindcraft.originsexpansion.providers.BrightnessProvider;
import com.edwinmindcraft.originsexpansion.registry.*;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import io.github.apace100.origins.Origins;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.stream.DoubleStream;

public class OriginsExpansion {
	public static final String MODID = "origins-expansion";
	public static final Logger LOGGER = LogManager.getLogger("Origins Expansion");

	public static ResourceLocation get(String path) {
		return new ResourceLocation(MODID, path);
	}

	public static void init() {
		ExpansionEntityNumberProvider.register();
		ExpansionPowerFactories.register();
		ExpansionEntityConditions.register();
		ExpansionEntityActions.register();
	}

	public static void setup() {
		NumberProviderFactory<Entity> constant = ExpansionRegistries.NUMBER_PROVIDER_ENTITY.get(get("constant"));
		NumberProviderFactory<Entity> product = ExpansionRegistries.NUMBER_PROVIDER_ENTITY.get(get("product"));
		NumberProviderFactory<Entity> brightness = ExpansionRegistries.NUMBER_PROVIDER_ENTITY.get(get("brightness"));

		NumberProvider<Entity, ?> result = new NumberProvider<>(product, new StreamProvider<>(DoubleStream::sum, Arrays.asList(
				new NumberProvider<>(constant, new ConstantProvider<>(4)),
				new NumberProvider<>(brightness, new BrightnessProvider())
		)));

		DataResult<JsonElement> jsonElementDataResult = OriginsExpansionCodecs.ENTITY_NUMBER_PROVIDER.encodeStart(InlineJsonOps.INSTANCE, result);
		LOGGER.info(jsonElementDataResult.getOrThrow(false, s -> {}));
	}
}
