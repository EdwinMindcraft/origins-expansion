package com.edwinmindcraft.originsexpansion.origins;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.power.factory.action.ActionType;
import io.github.apace100.origins.power.factory.condition.ConditionFactory;
import io.github.apace100.origins.power.factory.condition.ConditionType;
import me.shedaniel.architectury.registry.Registry;
import net.minecraft.world.entity.Entity;

public class OriginsAccess {
	//FIXME: For compat reasons, there should be a MC Registry defined constructor, as well as the architectury one.
	@ExpectPlatform
	public static <T> ConditionType<T> defineCondition(String name, Registry<ConditionFactory<T>> archRegistry) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static <T>ActionType<T> defineAction(String name, Registry<ActionFactory<T>> archRegistry) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static OriginComponent getComponent(Entity entity) {
		throw new AssertionError();
	}
}
