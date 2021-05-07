package com.edwinmindcraft.originsexpansion;

import net.fabricmc.api.ModInitializer;

public class OriginsExpansionFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		OriginsExpansion.init();
		OriginsExpansion.setup();
	}
}
