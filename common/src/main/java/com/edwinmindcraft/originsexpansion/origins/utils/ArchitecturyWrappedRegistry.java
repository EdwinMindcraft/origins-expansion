package com.edwinmindcraft.originsexpansion.origins.utils;

import me.shedaniel.architectury.core.RegistryEntry;
import me.shedaniel.architectury.registry.Registry;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Represents a converting registry for the purposes of bypassing forge's unique class restriction.
 *
 * @param <T> The type of the wrapped registry.
 * @param <S> The underlying type of the source registry.
 */
public class ArchitecturyWrappedRegistry<T, S> implements Registry<T> {

	private final Registry<S> sourceRegistry;
	private final Function<T, S> to;
	private final Function<S, T> from;

	public ArchitecturyWrappedRegistry(Registry<S> sourceRegistry, Function<T, S> to, Function<S, T> from) {
		this.sourceRegistry = sourceRegistry;
		this.to = to;
		this.from = from;
	}

	@Override
	public @NotNull RegistrySupplier<T> delegateSupplied(ResourceLocation id) {
		return new WrappedRegistrySupplier(this.sourceRegistry.delegateSupplied(id));
	}

	@Override
	public @NotNull RegistrySupplier<T> registerSupplied(ResourceLocation id, Supplier<T> supplier) {
		return new WrappedRegistrySupplier(this.sourceRegistry.registerSupplied(id, () -> this.to.apply(supplier.get())));
	}

	@Override
	public @Nullable ResourceLocation getId(T obj) {
		return this.sourceRegistry.getId(this.to.apply(obj));
	}

	@Override
	public int getRawId(T obj) {
		return this.sourceRegistry.getRawId(this.to.apply(obj));
	}

	@Override
	public Optional<ResourceKey<T>> getKey(T obj) {
		return this.sourceRegistry.getKey(this.to.apply(obj)).map(this::convert);
	}

	@Override
	public @Nullable T get(ResourceLocation id) {
		return this.from.apply(this.sourceRegistry.get(id));
	}

	@Override
	public @Nullable T byRawId(int rawId) {
		return this.from.apply(this.sourceRegistry.byRawId(rawId));
	}

	@Override
	public boolean contains(ResourceLocation id) {
		return this.sourceRegistry.contains(id);
	}

	@Override
	public boolean containsValue(T obj) {
		return this.sourceRegistry.containsValue(this.to.apply(obj));
	}

	@Override
	public Set<ResourceLocation> getIds() {
		return this.sourceRegistry.getIds();
	}

	@Override
	public Set<Map.Entry<ResourceKey<T>, T>> entrySet() {
		return this.sourceRegistry.entrySet().stream().map(x -> new Map.Entry<ResourceKey<T>, T>() {
			@Override
			public ResourceKey<T> getKey() {
				return ArchitecturyWrappedRegistry.this.convert(x.getKey());
			}

			@Override
			public T getValue() {
				return ArchitecturyWrappedRegistry.this.from.apply(x.getValue());
			}

			@Override
			public T setValue(T t) {
				return ArchitecturyWrappedRegistry.this.from.apply(x.setValue(ArchitecturyWrappedRegistry.this.to.apply(t)));
			}
		}).collect(Collectors.toSet());
	}



	@Override
	@SuppressWarnings("unchecked")
	public ResourceKey<? extends net.minecraft.core.Registry<T>> key() {
		return (ResourceKey<? extends net.minecraft.core.Registry<T>>) this.sourceRegistry.key();
	}

	@SuppressWarnings("unchecked")
	private ResourceKey<T> convert(ResourceKey<S> key) {
		//FIXME this implementation is meh, because technically keys ARE the same, but it might no be the best way to do this.
		return (ResourceKey<T>) key;
	}

	@NotNull
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			final Iterator<S> prev = ArchitecturyWrappedRegistry.this.sourceRegistry.iterator();

			@Override
			public boolean hasNext() {
				return this.prev.hasNext();
			}

			@Override
			public T next() {
				return ArchitecturyWrappedRegistry.this.from.apply(this.prev.next());
			}

			@Override
			public void remove() {
				this.prev.remove();
			}

			@Override
			public void forEachRemaining(Consumer<? super T> action) {
				this.prev.forEachRemaining(x -> action.accept(ArchitecturyWrappedRegistry.this.from.apply(x)));
			}
		};
	}

	public static class Wrapper<T, W extends Wrapper<T, W>> extends RegistryEntry<W> {
		private final T value;

		public Wrapper(T value) {
			this.value = value;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || this.getClass() != o.getClass()) return false;
			Wrapper<?, ?> wrapper = (Wrapper<?, ?>) o;
			return Objects.equals(this.value, wrapper.value);
		}

		public T get() {
			return this.value;
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.value);
		}

		@Override
		public String toString() {
			return "W:" + this.getClass().getSimpleName() + ":{" + this.value.toString() + "}";
		}
	}

	public class WrappedRegistrySupplier implements RegistrySupplier<T> {

		private final RegistrySupplier<S> source;

		public WrappedRegistrySupplier(RegistrySupplier<S> source) {
			this.source = source;
		}

		@Override
		public @NotNull ResourceLocation getRegistryId() {
			return this.source.getRegistryId();
		}

		@Override
		public @NotNull ResourceLocation getId() {
			return this.source.getId();
		}

		@Override
		public boolean isPresent() {
			return this.source.isPresent();
		}

		@Override
		public T get() {
			return ArchitecturyWrappedRegistry.this.from.apply(this.source.get());
		}
	}
}
