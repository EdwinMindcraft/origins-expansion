package com.edwinmindcraft.originsexpansion.origins.fabric;

import com.edwinmindcraft.originsexpansion.origins.utils.VanillaWrappedRegistry;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.power.factory.action.ActionType;
import io.github.apace100.origins.power.factory.condition.ConditionFactory;
import io.github.apace100.origins.power.factory.condition.ConditionType;
import io.github.apace100.origins.registry.ModComponents;
import me.shedaniel.architectury.registry.Registry;
import net.minecraft.world.entity.Entity;

public class OriginsAccessImpl {

	public static <T> ConditionType<T> defineCondition(String name, Registry<ConditionFactory<T>> archRegistry) {
		return new ConditionType<T>(name, VanillaWrappedRegistry.wrap(archRegistry));
	}

	public static <T> ActionType<T> defineAction(String name, Registry<ActionFactory<T>> archRegistry) {
		return new ActionType<>(name, VanillaWrappedRegistry.wrap(archRegistry));
	}

	public static OriginComponent getComponent(Entity entity) {
		return ModComponents.ORIGIN.get(entity);
	}
}
