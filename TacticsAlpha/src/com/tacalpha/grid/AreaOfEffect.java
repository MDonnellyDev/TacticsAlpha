package com.tacalpha.grid;

import java.util.HashSet;
import java.util.Set;

public abstract class AreaOfEffect {
	protected Set<GridPoint> relativeLocations = new HashSet<GridPoint>();
	private Grid grid;

	public final boolean contains(GridPoint referencePoint, GridPoint targetPoint) {
		if (this.relativeLocations.isEmpty()) {
			this.populateLocations();
		}
		return this.relativeLocations.contains(new GridPoint(targetPoint.getColumn() - referencePoint.getColumn(), targetPoint.getRow()
				- referencePoint.getRow()));
	}

	public final Set<GridPoint> getAllAffectedLocations(GridPoint referencePoint) {
		Set<GridPoint> results = new HashSet<GridPoint>();
		for (GridPoint relativeLocation : this.relativeLocations) {
			int x = referencePoint.getColumn() + relativeLocation.getColumn();
			int y = referencePoint.getRow() + relativeLocation.getRow();
			if (this.grid.isInBounds(x, y)) {
				results.add(new GridPoint(x, y));
			}
		}
		return results;
	}

	void setGrid(Grid grid) {
		this.grid = grid;
	}

	protected abstract void populateLocations();
}
