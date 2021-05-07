package com.edwinmindcraft.originsexpansion.mixin;

import com.edwinmindcraft.originsexpansion.OriginExpansionEvents;
import com.edwinmindcraft.originsexpansion.power.ResourceShieldPower;
import io.github.apace100.origins.component.OriginComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@ModifyVariable(method = "actuallyHurt", at = @At("HEAD"), ordinal = 0)
	public float reduce(float f, DamageSource source) {
		return OriginExpansionEvents.handleResourceShield(this, source, f);
	}
}
