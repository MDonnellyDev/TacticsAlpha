package com.tacalpha.battle;

import java.util.Collection;

import com.tacalpha.actor.Actor;
import com.tacalpha.grid.AreaOfEffect;
import com.tacalpha.grid.Grid;

public abstract class Effect {
	protected final Actor source;

	protected Effect(Actor source) {
		this.source = source;
	}

	public abstract void applyEffect(Collection<Actor> targets);

	public abstract AreaOfEffect getAreaOfEffect();

	public abstract void setupTargeting(Grid grid);
}
