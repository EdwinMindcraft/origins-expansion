package com.edwinmindcraft.originsexpansion.mixin;

import com.edwinmindcraft.originsexpansion.OriginExpansionEvents;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends LivingEntity {
	protected LocalPlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}
	
	@ModifyVariable(method = "actuallyHurt", at = @At("HEAD"), ordinal = 0)
	public float reduce(float f, DamageSource source) {
		return OriginExpansionEvents.handleResourceShield(this, source, f);
	}
}
