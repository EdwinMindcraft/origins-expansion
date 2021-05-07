package com.edwinmindcraft.originsexpansion.api;

import com.edwinmindcraft.originsexpansion.OriginsExpansion;
import com.edwinmindcraft.originsexpansion.api.dfu.InlineJsonOps;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import io.github.apace100.origins.util.ClassUtil;
import io.github.apace100.origins.util.SerializableDataType;
import net.minecraft.world.entity.Entity;

import java.io.IOException;
import java.util.List;

public class OriginsExpansionDataTypes {

	public static <T> SerializableDataType<T> codec(Class<T> cls, Codec<T> codec) {
		return new SerializableDataType<>(cls, (buf, t) -> {
			try {
				buf.writeWithCodec(codec, t);
			} catch (IOException e) {
				OriginsExpansion.LOGGER.error("Failed to serialize type " + cls.getSimpleName() + "", e);
				throw new RuntimeException(e);
			}
		}, buf -> {
			try {
				return buf.readWithCodec(codec);
			} catch (IOException e) {
				OriginsExpansion.LOGGER.error("Failed to deserialize type " + cls.getSimpleName() + "", e);
				throw new RuntimeException(e);
			}
		}, i -> {
			DataResult<Pair<T, JsonElement>> decode = codec.decode(InlineJsonOps.INSTANCE, i);
			return decode.map(Pair::getFirst)
					.getOrThrow(false,
							s -> OriginsExpansion.LOGGER.error("JSON \"{}\" errored: {}", decode.map(Pair::getSecond).resultOrPartial(s2 -> {}).orElse(JsonNull.INSTANCE), s));
		});
	}

	public static SerializableDataType<NumberProvider<Entity, ?>> ENTITY_NUMBER_PROVIDER = codec(ClassUtil.castClass(NumberProvider.class), OriginsExpansionCodecs.ENTITY_NUMBER_PROVIDER);
	public static SerializableDataType<List<NumberProvider<Entity, ?>>> ENTITY_NUMBER_PROVIDERS = codec(ClassUtil.castClass(List.class), OriginsExpansionCodecs.ENTITY_NUMBER_PROVIDERS);

}
