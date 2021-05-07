package com.edwinmindcraft.originsexpansion.api.dfu;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.DelegatingOps;
import net.minecraft.resources.RegistryReadOps;

public class InlineJsonOps extends DelegatingOps<JsonElement> implements InlineOps<JsonElement> {
	public static final InlineJsonOps INSTANCE = new InlineJsonOps();

	public InlineJsonOps() {
		super(JsonOps.INSTANCE);
	}
}
