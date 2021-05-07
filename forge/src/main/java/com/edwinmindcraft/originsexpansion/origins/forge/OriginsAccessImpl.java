package com.edwinmindcraft.originsexpansion.origins.forge;

import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.power.factory.action.ActionType;
import io.github.apace100.origins.power.factory.condition.ConditionFactory;
import io.github.apace100.origins.power.factory.condition.ConditionType;
import io.github.apace100.origins.registry.ModComponentsArchitectury;
import me.shedaniel.architectury.registry.Registry;
import net.minecraft.world.entity.Entity;

public class OriginsAccessImpl {
	public static <T> ConditionType<T> defineCondition(String name, Registry<ConditionFactory<T>> archRegistry) {
		return new ConditionType<>(name, archRegistry);
	}

	public static <T> ActionType<T> defineAction(String name, Registry<ActionFactory<T>> archRegistry) {
		return new ActionType<>(name, archRegistry);
	}

	public static OriginComponent getComponent(Entity entity) {
		return ModComponentsArchitectury.getOriginComponent(entity);
	}
}
