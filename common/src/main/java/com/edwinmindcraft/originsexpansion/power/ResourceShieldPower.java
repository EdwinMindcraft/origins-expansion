package com.edwinmindcraft.originsexpansion.power;

import com.edwinmindcraft.originsexpansion.origins.OriginsAccess;
import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.VariableIntPower;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ResourceShieldPower extends Power {
	private final Function<Entity, Number> resourceFactor;
	private final PowerType<?> resource;
	private final Predicate<Tuple<DamageSource, Float>> shield;
	private Consumer<Entity> selfAction;
	private Consumer<Entity> selfActionOnBreak;
	private Consumer<Entity> attackerAction;
	private Consumer<Entity> attackerActionOnBreak;

	public ResourceShieldPower(PowerType<?> type, Player player, PowerType<?> resource, Function<Entity, Number> resourceFactor, Predicate<Tuple<DamageSource, Float>> shield) {
		super(type, player);
		this.resourceFactor = resourceFactor;
		this.resource = resource;
		this.shield = shield;
	}

	public boolean canApply(DamageSource damageSource, float amount) {
		return this.shield.test(new Tuple<>(damageSource, amount));
	}

	public float apply(LivingEntity self, DamageSource damageSource, float amount) {
		Power power = OriginsAccess.getComponent(self).getPower(this.resource);
		if (power instanceof VariableIntPower) {
			VariableIntPower vip = (VariableIntPower) power;
			float resourceFactor = this.resourceFactor.apply(self).floatValue();
			float maxDecr = (vip.getValue() - vip.getMin()) * resourceFactor;
			float remaining = Math.max(amount - maxDecr, 0);
			int decr = Mth.ceil((amount - remaining) / resourceFactor);
			vip.setValue(Math.max(vip.getMin(), vip.getValue() - decr));
			if (remaining > 0) {
				if (this.selfActionOnBreak != null)
					this.selfActionOnBreak.accept(self);
				if (this.attackerActionOnBreak != null && damageSource.getEntity() instanceof LivingEntity)
					this.attackerActionOnBreak.accept(damageSource.getEntity());
			}
			if (this.selfAction != null)
				this.selfAction.accept(self);
			if (this.attackerAction != null && damageSource.getEntity() instanceof LivingEntity)
				this.attackerAction.accept(damageSource.getEntity());
			return remaining;
		}
		return amount;
	}

	public void setSelfAction(Consumer<Entity> selfAction) {
		this.selfAction = selfAction;
	}

	public void setSelfActionOnBreak(Consumer<Entity> selfActionOnBreak) {
		this.selfActionOnBreak = selfActionOnBreak;
	}

	public void setAttackerAction(Consumer<Entity> attackerAction) {
		this.attackerAction = attackerAction;
	}

	public void setAttackerActionOnBreak(Consumer<Entity> attackerActionOnBreak) {
		this.attackerActionOnBreak = attackerActionOnBreak;
	}
}
