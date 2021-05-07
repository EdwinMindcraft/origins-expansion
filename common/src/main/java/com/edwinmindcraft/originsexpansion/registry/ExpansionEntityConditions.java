package com.edwinmindcraft.originsexpansion.registry;

import com.edwinmindcraft.originsexpansion.OriginsExpansion;
import com.edwinmindcraft.originsexpansion.origins.OriginsAccess;
import com.edwinmindcraft.originsexpansion.power.SelectPower;
import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.factory.condition.ConditionFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.LivingEntity;

import java.util.Objects;

public class ExpansionEntityConditions {

	public static void register() {
		register(new ConditionFactory<>(OriginsExpansion.get("in_combat"), new SerializableData().add("delay", SerializableDataType.INT, 80),
				(instance, livingEntity) -> {
					int delay = instance.getInt("delay");
					int timestamp = livingEntity.tickCount - delay;
					return livingEntity.getLastHurtByMobTimestamp() > timestamp || livingEntity.getLastHurtMobTimestamp() > timestamp;
				}));
		register(new ConditionFactory<>(OriginsExpansion.get("is_selected"), new SerializableData()
				.add("selector", SerializableDataType.POWER_TYPE)
				.add("value", SerializableDataType.STRING),
				(instance, livingEntity) -> {
					PowerType<?> type = instance.get("selector");
					String value = instance.get("value");
					Power power = OriginsAccess.getComponent(livingEntity).getPower(type);
					return power instanceof SelectPower && Objects.equals(value, ((SelectPower) power).getSelected());
				}));
	}

	private static void register(ConditionFactory<LivingEntity> factory) {
		Registry.register(ModRegistries.ENTITY_CONDITION, factory.getSerializerId(), factory);
	}
}
