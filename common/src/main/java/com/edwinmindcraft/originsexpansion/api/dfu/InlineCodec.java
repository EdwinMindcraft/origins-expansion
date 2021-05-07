package com.edwinmindcraft.originsexpansion.api.dfu;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;

import java.util.function.Function;

/**
 * If you are wondering what this is, for the sake of your sanity please don't.
 * This just allows codecs to be flattened.
 * @param <E> The serialized type
 * @param <A> The type providing the codec.
 */
public class InlineCodec<E, A> implements Codec<E> {

	private final String key;
	private final String value = "value";
	private final Codec<A> type;
	private final Function<? super E, ? extends A> from;
	private final Function<? super A, ? extends Codec<? extends E>> child;

	public InlineCodec(String key, Codec<A> type, final Function<? super E, ? extends A> from, final Function<? super A, ? extends Codec<? extends E>> child) {
		this.key = key;
		this.type = type;
		this.from = from;
		this.child = child;
	}

	@Override
	public <T> DataResult<Pair<E, T>> decode(DynamicOps<T> ops, T input) {
		DataResult<Pair<A, T>> key = ops.get(input, this.key).flatMap(x -> this.type.decode(ops, x));
		DataResult<? extends Codec<? extends E>> codec = key.map(Pair::getFirst).map(this.child);
		if (codec.error().isPresent())
			return DataResult.error(codec.error().get().message(), Pair.of(null, input));
		Codec<E> codecAccess = codec.result().map(x -> (Codec<E>) x).get();
		DataResult<Pair<E, T>> target = ops.get(input, this.value).flatMap(x -> codecAccess.decode(ops, x));
		if (target.error().isPresent() && ops instanceof InlineOps && !ops.compressMaps())
			target = codecAccess.decode(ops, input);
		return target;
	}

	@Override
	public <T> DataResult<T> encode(E input, DynamicOps<T> ops, T prefix) {
		A res = this.from.apply(input);
		Codec<E> apply = (Codec<E>) this.child.apply(res);
		RecordBuilder<T> builder = ops.mapBuilder();
		builder.add(this.key, this.type.encode(res, ops, prefix));
		if (ops instanceof InlineOps && !ops.compressMaps()) {
			DataResult<T> result = builder.build(prefix).flatMap(x -> apply.encode(input, ops, x));
			if (result.result().isPresent())
				return result;
		}
		builder.add(this.key, this.type.encode(res, ops, prefix));
		builder.add(this.value, apply.encode(input, ops, prefix));
		return builder.build(prefix);
	}
}