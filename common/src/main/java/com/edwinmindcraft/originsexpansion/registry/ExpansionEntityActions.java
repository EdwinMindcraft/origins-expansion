package com.edwinmindcraft.originsexpansion.registry;

import com.edwinmindcraft.originsexpansion.OriginsExpansion;
import com.edwinmindcraft.originsexpansion.api.OriginsExpansionDataTypes;
import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import net.minecraft.core.Registry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ExpansionEntityActions {

	public static void register() {
		register(new ActionFactory<>(OriginsExpansion.get("area_of_effect"), new SerializableData()
				.add("range", OriginsExpansionDataTypes.ENTITY_NUMBER_PROVIDER)
				.add("action", SerializableDataType.ENTITY_ACTION, null)
				.add("actions", SerializableDataType.ENTITY_ACTIONS, null)
				.add("condition", SerializableDataType.ENTITY_CONDITION, null)
				.add("include_self", SerializableDataType.BOOLEAN, false),
				(instance, entity) -> {
					Function<Entity, Number> rangeFunction = instance.get("range");
					List<Consumer<Entity>> actions = new ArrayList<>();
					if (instance.isPresent("action"))
						actions.add(instance.get("action"));
					if (instance.isPresent("actions"))
						actions.addAll(instance.get("actions"));
					Predicate<LivingEntity> predicate = instance.isPresent("condition") ? instance.get("condition") : x -> true;
					boolean includeSelf = instance.get("include_self");
					double range = rangeFunction.apply(entity).doubleValue();
					for (LivingEntity check : entity.level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(range))) {
						if (check == entity && !includeSelf)
							continue;
						if (predicate.test(check) && check.distanceToSqr(entity) < range * range)
							actions.forEach(x -> x.accept(check));
					}
				}));
		register(new ActionFactory<>(OriginsExpansion.get("damage"), new SerializableData()
		.add("amount", OriginsExpansionDataTypes.ENTITY_NUMBER_PROVIDER)
				.add("source", SerializableDataType.DAMAGE_SOURCE),
				((instance, entity) -> {
					Function<Entity, Number> provider = instance.get("amount");
					DamageSource damageSource = instance.get("source");
					entity.hurt(damageSource, provider.apply(entity).floatValue());
				})));
	}

	private static void register(ActionFactory<Entity> factory) {
		Registry.register(ModRegistries.ENTITY_ACTION, factory.getSerializerId(), factory);
	}
}
