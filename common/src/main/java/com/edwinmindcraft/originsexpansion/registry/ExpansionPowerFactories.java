package com.edwinmindcraft.originsexpansion.registry;

import com.edwinmindcraft.originsexpansion.OriginsExpansion;
import com.edwinmindcraft.originsexpansion.api.OriginsExpansionDataTypes;
import com.edwinmindcraft.originsexpansion.power.ResourceShieldPower;
import com.edwinmindcraft.originsexpansion.power.SelectPower;
import io.github.apace100.origins.Origins;
import io.github.apace100.origins.power.Active;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import net.minecraft.core.Registry;
import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ExpansionPowerFactories {

	public static void register() {
		register(new PowerFactory<ResourceShieldPower>(OriginsExpansion.get("resource_shield"), new SerializableData()
				.add("resource", SerializableDataType.POWER_TYPE)
				.add("efficiency", OriginsExpansionDataTypes.ENTITY_NUMBER_PROVIDER)
				.add("damage_condition", SerializableDataType.DAMAGE_CONDITION, null)
				.add("self_action", SerializableDataType.ENTITY_ACTION, null)
				.add("self_action_on_break", SerializableDataType.ENTITY_ACTION, null)
				.add("attacker_action", SerializableDataType.ENTITY_ACTION, null)
				.add("attacker_action_on_break", SerializableDataType.ENTITY_ACTION, null),
				(data) -> {
					PowerType<?> resource = data.get("resource");
					Function<Entity, Number> eff = data.get("efficiency");
					return (type, player) -> {
						Predicate<Tuple<DamageSource, Float>> damage = data.isPresent("damage_condition") ? data.get("damage_condition") : x -> true;
						ResourceShieldPower power = new ResourceShieldPower(type, player, resource, eff, damage);
						if (data.isPresent("self_action"))
							power.setSelfAction(data.get("self_action"));
						if (data.isPresent("self_action_on_break"))
							power.setSelfActionOnBreak(data.get("self_action_on_break"));
						if (data.isPresent("attacker_action"))
							power.setAttackerAction(data.get("attacker_action"));
						if (data.isPresent("attacker_action_on_break"))
							power.setAttackerActionOnBreak(data.get("attacker_action_on_break"));
						return power;
					};
				}
		).allowCondition());
		register(new PowerFactory<SelectPower>(OriginsExpansion.get("select"), new SerializableData()
				.add("candidates", SerializableDataType.list(SerializableDataType.STRING))
				.add("default", SerializableDataType.STRING, "")
				.add("key", SerializableDataType.KEY, null),
				instance -> (selectPowerPowerType, player) -> {
					List<String> candidates = instance.get("candidates");
					String def = instance.get("default");
					Active.Key key = instance.get("key");
					SelectPower power = new SelectPower(selectPowerPowerType, player, candidates, def);
					if (key != null)
						power.setKey(key);
					return power;
				}
		));
	}

	private static void register(PowerFactory<?> actionFactory) {
		Registry.register(ModRegistries.POWER_FACTORY, actionFactory.getSerializerId(), actionFactory);
	}
}
