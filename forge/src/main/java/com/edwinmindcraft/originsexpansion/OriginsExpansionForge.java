package com.edwinmindcraft.originsexpansion;

import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(OriginsExpansion.MODID)
public class OriginsExpansionForge {
	public OriginsExpansionForge() {
		EventBuses.registerModEventBus(OriginsExpansion.MODID, FMLJavaModLoadingContext.get().getModEventBus());
		OriginsExpansion.init();
		OriginsExpansionEventHandler.init();
		FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLCommonSetupEvent event) -> OriginsExpansion.setup());
	}
}
