package com.edwinmindcraft.originsexpansion.registry;

import com.edwinmindcraft.originsexpansion.origins.utils.ArchitecturyWrappedRegistry;
import com.edwinmindcraft.originsexpansion.OriginsExpansion;
import com.edwinmindcraft.originsexpansion.api.NumberProviderFactory;
import me.shedaniel.architectury.registry.Registries;
import me.shedaniel.architectury.registry.Registry;
import net.minecraft.world.entity.Entity;

public class ExpansionRegistries {
	public static final Registry<NumberProviderFactory<Entity>> NUMBER_PROVIDER_ENTITY;

	static {
		Registries registries = Registries.get(OriginsExpansion.MODID);
		Registry<NPFEntity> entityRegistry = registries.<NPFEntity>builder(OriginsExpansion.get("entity_number_providers")).build();
		NUMBER_PROVIDER_ENTITY = new ArchitecturyWrappedRegistry<>(entityRegistry, NPFEntity::new, NPFEntity::get);
	}

	public static final class NPFEntity extends ArchitecturyWrappedRegistry.Wrapper<NumberProviderFactory<Entity>, NPFEntity> {
		public NPFEntity(NumberProviderFactory<Entity> value) {
			super(value);
		}
	}
}
