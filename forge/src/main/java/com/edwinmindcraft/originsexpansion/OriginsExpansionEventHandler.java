package com.edwinmindcraft.originsexpansion;

import com.edwinmindcraft.originsexpansion.power.ResourceShieldPower;
import io.github.apace100.origins.component.OriginComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OriginsExpansion.MODID)
public class OriginsExpansionEventHandler {
	public static void init() {}
	@SubscribeEvent
	public static void applyShield(LivingHurtEvent event) {
		event.setAmount(OriginExpansionEvents.handleResourceShield(event.getEntityLiving(), event.getSource(), event.getAmount()));
	}
}
