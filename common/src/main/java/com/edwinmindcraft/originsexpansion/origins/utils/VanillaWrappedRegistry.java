package com.edwinmindcraft.originsexpansion.origins.utils;

import com.mojang.serialization.Lifecycle;
import io.github.apace100.origins.util.ArchitecturyWrappedRegistry;
import me.shedaniel.architectury.registry.Registries;
import me.shedaniel.architectury.registry.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class VanillaWrappedRegistry<S> extends WritableRegistry<S> {

	private final Function<Registries, Registry<S>> toArchRegistry;
	private final Lifecycle lifecycle;
	private final Registry<S> archRegistry;
	private final Map<S, Lifecycle> entryToLifecycle;
	private final Map<String, Registry<S>> archRegistries;

	public static <T> VanillaWrappedRegistry<T> wrap(Registry<T> arch) {
		return wrap(arch, Lifecycle.stable());
	}

	@SuppressWarnings({"unchecked"})
	public static <T> VanillaWrappedRegistry<T> wrap(Registry<T> arch, Lifecycle lifecycle) {
		Function<Registries, Registry<T>> toArch = s -> s.get((ResourceKey<net.minecraft.core.Registry<T>>) arch.key());
		return new VanillaWrappedRegistry<>(arch, toArch, lifecycle);
	}

	public static <T, S> VanillaWrappedRegistry<T> wrap(Registry<T> arch, Function<T, S> to, Function<S, T> from) {
		return wrap(arch, to, from, Lifecycle.stable());
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T, S> VanillaWrappedRegistry<T> wrap(Registry<T> arch, Function<T, S> to, Function<S, T> from, Lifecycle lifecycle) {
		ResourceKey<net.minecraft.core.Registry<S>> key = (ResourceKey) arch.key();
		Function<Registries, Registry<T>> toArch = s -> new ArchitecturyWrappedRegistry<>(s.get(key), to, from);
		return new VanillaWrappedRegistry<>(arch, toArch, lifecycle);
	}

	public VanillaWrappedRegistry(Registry<S> archRegistry, Function<Registries, Registry<S>> toArchRegistry, Lifecycle lifecycle) {
		super(archRegistry.key(), lifecycle);
		this.archRegistry = archRegistry;
		this.toArchRegistry = toArchRegistry;
		this.lifecycle = lifecycle;
		this.entryToLifecycle = new HashMap<>();
		this.archRegistries = new HashMap<>();
	}

	@Nullable
	@Override
	public ResourceLocation getKey(S object) {
		return this.archRegistry.getId(object);
	}

	@Override
	public Optional<ResourceKey<S>> getResourceKey(S object) {
		return this.archRegistry.getKey(object);
	}

	@Override
	public int getId(@Nullable S object) {
		return this.archRegistry.getRawId(object);
	}

	@Nullable
	@Override
	public S byId(int i) {
		return this.archRegistry.byRawId(i);
	}

	@Nullable
	@Override
	public S get(@Nullable ResourceKey<S> ResourceKey) {
		return this.get(ResourceKey == null ? null : ResourceKey.location());
	}

	@Nullable
	@Override
	public S get(@Nullable ResourceLocation ResourceLocation) {
		return this.archRegistry.get(ResourceLocation);
	}

	@Override
	protected Lifecycle lifecycle(S object) {
		return this.entryToLifecycle.getOrDefault(object, Lifecycle.stable());
	}
	@Override
	public Lifecycle elementsLifecycle() {
		return this.lifecycle;
	}

	@Override
	public Set<ResourceLocation> keySet() {
		return this.archRegistry.getIds();
	}

	@Override
	public Set<Map.Entry<ResourceKey<S>, S>> entrySet() {
		return this.archRegistry.entrySet();
	}

	@Override
	public boolean containsKey(ResourceLocation ResourceLocation) {
		return this.archRegistry.contains(ResourceLocation);
	}

	@NotNull
	@Override
	public Iterator<S> iterator() {
		return this.archRegistry.iterator();
	}

	@Override
	public <V extends S> V registerMapping(int i, ResourceKey<S> ResourceKey, V object, Lifecycle lifecycle) {
		throw new UnsupportedOperationException("Set is unsupported on wrapped registries.");
	}

	@Override
	public <V extends S> V register(ResourceKey<S> ResourceKey, V object, Lifecycle lifecycle) {
		this.archRegistries.computeIfAbsent(ResourceKey.location().getNamespace(), x -> this.toArchRegistry.apply(Registries.get(x)))
				.registerSupplied(ResourceKey.location(), () -> object);
		this.entryToLifecycle.put(object, lifecycle);
		return object;
	}

	@Override
	public <V extends S> V registerOrOverride(OptionalInt optionalInt, ResourceKey<S> ResourceKey, V object, Lifecycle lifecycle) {
		throw new UnsupportedOperationException("Replace is unsupported on wrapped registries.");
	}
}
