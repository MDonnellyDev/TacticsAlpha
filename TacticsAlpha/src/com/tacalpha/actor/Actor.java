package com.tacalpha.actor;

import com.tacalpha.grid.GridPoint;

public class Actor {
	private int xLocation;
	private int yLocation;

	public Actor(int x, int y) {
		this.xLocation = x;
		this.yLocation = y;
	}

	public GridPoint getLocation() {
		return new GridPoint(this.xLocation, this.yLocation);
	}

	public void updateLocation(int x, int y) {
		this.xLocation = x;
		this.yLocation = y;
	}

	// public boolean move(Grid grid, Direction direction) {
	// GridPoint destination = Direction.move(this.xLocation, this.yLocation,
	// direction);
	// return grid.moveActorIfPossible(this, destination);
	// }
}
