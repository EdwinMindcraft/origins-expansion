package com.edwinmindcraft.originsexpansion.providers;

import com.edwinmindcraft.originsexpansion.api.OriginsExpansionCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.origins.power.PowerType;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;

public class ResourceProvider implements Function<Entity, Number> {
	public static final Codec<ResourceProvider> CODEC = RecordCodecBuilder.create(instance -> instance.group(OriginsExpansionCodecs.POWER_TYPE.fieldOf("resource").forGetter(ResourceProvider::getResource)).apply(instance, ResourceProvider::new));

	private final PowerType<?> resource;

	public ResourceProvider(PowerType<?> resource) {
		this.resource = resource;
	}

	@Override
	public Number apply(Entity entity) {
		return null;
	}

	public PowerType<?> getResource() {
		return resource;
	}
}
