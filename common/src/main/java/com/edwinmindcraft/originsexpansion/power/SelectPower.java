package com.edwinmindcraft.originsexpansion.power;

import com.google.common.collect.ImmutableList;
import io.github.apace100.origins.power.Active;
import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SelectPower extends Power implements Active {
	private final List<String> candidates;
	private Key key;
	private int currentlySelected;

	public SelectPower(PowerType<?> type, Player player, List<String> candidates, @Nullable String defaultPower) {
		super(type, player);
		this.candidates = ImmutableList.copyOf(candidates);
		this.key = new Key();
		this.currentlySelected = defaultPower == null ? 0 : candidates.indexOf(defaultPower);
		if (this.currentlySelected < 0 || this.currentlySelected >= candidates.size())
			this.currentlySelected = 0;
	}

	@Override
	public void onUse() {
		++this.currentlySelected;
		if (this.currentlySelected >= this.candidates.size())
			this.currentlySelected = 0;
	}

	@Override
	public Key getKey() {
		return this.key;
	}

	@Override
	public void setKey(Key key) {
		this.key = key;
	}

	public String getSelected() {
		return this.currentlySelected < 0 || this.currentlySelected >= this.candidates.size() ? null : this.candidates.get(this.currentlySelected);
	}
}
