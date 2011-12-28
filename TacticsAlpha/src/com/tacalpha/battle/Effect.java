package com.tacalpha.battle;

import java.util.Collection;

import com.tacalpha.actor.Actor;
import com.tacalpha.grid.AreaOfEffect;
import com.tacalpha.grid.Grid;

public abstract class Effect {
	protected final Actor source;
	protected int mpCost = 0;

	protected Effect(Actor source, Grid grid) {
		this.source = source;
		this.setupTargeting(grid);
		grid.setAreaOfEffect(this.getAreaOfEffect());
	}

	public void applyEffect(Collection<Actor> targets) {
		if (this.source.spendMpIfAble(this.mpCost)) {
			this.doApply(targets);
		}
	}

	protected abstract void doApply(Collection<Actor> targets);

	protected abstract AreaOfEffect getAreaOfEffect();

	protected abstract void setupTargeting(Grid grid);
}
