package com.edwinmindcraft.originsexpansion;

import com.edwinmindcraft.originsexpansion.power.ResourceShieldPower;
import io.github.apace100.origins.component.OriginComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class OriginExpansionEvents {
	public static float handleResourceShield(Entity entity, DamageSource source, float f) {
		if (entity instanceof Player) {
			for (ResourceShieldPower power : OriginComponent.getPowers(entity, ResourceShieldPower.class)) {
				if (f > 0 && power.canApply(source, f))
					f = power.apply((LivingEntity) entity, source, f);
				OriginComponent.sync((Player) entity);
			}
		}
		return f;
	}
}
