package com.tacalpha.grid;

import java.util.HashSet;
import java.util.Set;

import com.tacalpha.actor.Actor;

public class LineAOE extends AreaOfEffect {
	private Actor source;
	private int distance;

	public LineAOE(Actor source, int distance) {
		this.source = source;
		this.distance = distance;
	}

	@Override
	public boolean contains(GridPoint referencePoint, GridPoint targetPoint) {
		Direction lineDirection = this.calculateLineDirection(referencePoint);
		GridPoint temp = referencePoint;
		int i = 0;
		do {
			if (temp.equals(targetPoint)) {
				return true;
			}
			temp = Direction.move(temp.getColumn(), temp.getRow(), lineDirection);
			if (!this.grid.isInBounds(temp.getColumn(), temp.getRow())) {
				return false;
			}
			i++;
		} while (i < this.distance);
		return false;
	}

	@Override
	public Set<GridPoint> getAllAffectedLocations(GridPoint referencePoint) {
		Direction lineDirection = this.calculateLineDirection(referencePoint);
		GridPoint temp = referencePoint;
		Set<GridPoint> results = new HashSet<GridPoint>();
		int i = 0;
		do {
			results.add(temp);
			temp = Direction.move(temp.getColumn(), temp.getRow(), lineDirection);
			if (!this.grid.isInBounds(temp.getColumn(), temp.getRow())) {
				break;
			}
			i++;
		} while (i < this.distance);
		return results;
	}

	private Direction calculateLineDirection(GridPoint referencePoint) {
		GridPoint sourceLocation = this.source.getLocation();
		if (sourceLocation.equals(referencePoint)) {
			return Direction.NONE;
		}

		int xDiff = referencePoint.getColumn() - sourceLocation.getColumn();
		int yDiff = referencePoint.getRow() - sourceLocation.getRow();
		if (Math.abs(xDiff) > Math.abs(yDiff)) {
			return xDiff > 0 ? Direction.RIGHT : Direction.LEFT;
		} else {
			return yDiff > 0 ? Direction.DOWN : Direction.UP;
		}
	}
}
