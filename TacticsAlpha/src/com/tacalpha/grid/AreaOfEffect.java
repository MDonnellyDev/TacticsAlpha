package com.tacalpha.grid;

import java.util.Set;

public abstract class AreaOfEffect {
	protected Grid grid;

	public abstract boolean contains(GridPoint referencePoint, GridPoint targetPoint);

	public abstract Set<GridPoint> getAllAffectedLocations(GridPoint referencePoint);

	void setGrid(Grid grid) {
		this.grid = grid;
	}
}
