package com.tacalpha.battle;

import java.util.Collection;

import com.tacalpha.actor.Actor;
import com.tacalpha.grid.AreaOfEffect;
import com.tacalpha.grid.Grid;
import com.tacalpha.grid.RadiusAOE;

public class Cure extends Effect {
	public Cure(Actor source, Grid grid) {
		super(source, grid);
		this.mpCost = 15;
	}

	@Override
	public void doApply(Collection<Actor> targets) {
		for (Actor target : targets) {
			target.heal(50);
		}
	}

	@Override
	public AreaOfEffect getAreaOfEffect() {
		return new RadiusAOE(3);
	}

	@Override
	public void setupTargeting(Grid grid) {
		grid.setTargetAdjacentSquares(this.source.getLocation(), 5);
	}

}
