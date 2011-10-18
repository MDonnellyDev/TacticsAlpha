package com.tacalpha.grid;

import com.tacalpha.actor.Actor;

public class Tile {
	private boolean impassable;
	private Actor occupant;

	public boolean isImpassable() {
		return this.impassable;
	}

	public void setImpassable(boolean impassable) {
		this.impassable = impassable;
	}

	public void setOccupant(Actor occupant) {
		this.occupant = occupant;
	}

	public Actor getOccupant() {
		return this.occupant;
	}
}
