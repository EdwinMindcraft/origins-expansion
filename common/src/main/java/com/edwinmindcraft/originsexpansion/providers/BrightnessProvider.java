package com.edwinmindcraft.originsexpansion.providers;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;

public class BrightnessProvider implements Function<Entity, Number> {
	public static final Codec<BrightnessProvider> CODEC = Codec.unit(BrightnessProvider::new);

	@Override
	public Number apply(Entity entity) {
		return entity.getBrightness();
	}
}
