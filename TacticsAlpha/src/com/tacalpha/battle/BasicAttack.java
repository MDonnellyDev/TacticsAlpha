package com.tacalpha.battle;

import java.util.Collection;
import java.util.Random;

import com.tacalpha.actor.Actor;
import com.tacalpha.grid.AreaOfEffect;
import com.tacalpha.grid.Grid;
import com.tacalpha.grid.RadiusAOE;

public class BasicAttack extends Effect {
	private Random random = new Random();

	public BasicAttack(Actor source, Grid grid) {
		super(source, grid);
	}

	@Override
	public void doApply(Collection<Actor> targets) {
		for (Actor target : targets) {
			int attackerStrength = this.source.getStrength();
			int targetDefense = target.getDefense();
			double baseDamage = attackerStrength * (this.random.nextDouble() / 2.0 + 0.9);
			int damage = (int) (baseDamage * (100.0 / (100.0 + targetDefense)));
			target.damage(damage);
		}
	}

	@Override
	public AreaOfEffect getAreaOfEffect() {
		return new RadiusAOE(1);
	}

	@Override
	public void setupTargeting(Grid grid) {
		grid.setTargetAdjacentSquares(this.source.getLocation(), 2);
	}
}
