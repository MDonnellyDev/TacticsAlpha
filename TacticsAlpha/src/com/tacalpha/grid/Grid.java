package com.tacalpha.grid;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.tacalpha.actor.Actor;

public class Grid {
	private final Tile[][] tiles;
	private Set<GridPoint> targetableTiles;
	private AreaOfEffect currentEffectAOE;
	public final int width;
	public final int height;

	private int currentXPosition;
	private int currentYPosition;

	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		this.tiles = new Tile[height][width];
		this.currentXPosition = 0;
		this.currentYPosition = 0;
	}

	public Grid(String mapFile) {
		this.tiles = GridLoader.getTiles(mapFile);
		this.height = this.tiles.length;
		this.width = this.tiles[0].length;
	}

	public boolean moveSelectedLocation(Direction direction) {
		GridPoint destination = null;
		switch (direction) {
			case UP:
				destination = new GridPoint(this.currentXPosition, this.currentYPosition == 0 ? this.height - 1 : this.currentYPosition - 1);
				break;
			case DOWN:
				destination = new GridPoint(this.currentXPosition, this.currentYPosition == this.height - 1 ? 0 : this.currentYPosition + 1);
				break;
			case LEFT:
				destination = new GridPoint(this.currentXPosition == 0 ? this.width - 1 : this.currentXPosition - 1, this.currentYPosition);
				break;
			case RIGHT:
				destination = new GridPoint(this.currentXPosition == this.width - 1 ? 0 : this.currentXPosition + 1, this.currentYPosition);
				break;
			default:
				break;
		}
		if (this.targetableTiles == null || this.targetableTiles.contains(destination)) {
			this.setSelectedLocation(destination);
			return true;
		} else {
			return false;
		}
	}

	public GridPoint getSelectedLocation() {
		return new GridPoint(this.currentXPosition, this.currentYPosition);
	}

	private void setSelectedLocation(GridPoint destination) {
		this.currentXPosition = destination.getColumn();
		this.currentYPosition = destination.getRow();
	}

	public Tile getSelectedTile() {
		return this.tiles[this.currentYPosition][this.currentXPosition];
	}

	public Tile getTile(GridPoint location) {
		return this.tiles[location.getRow()][location.getColumn()];
	}

	public Tile[][] getTiles() {
		return this.tiles;
	}

	public boolean moveActorIfPossible(Actor actor, GridPoint destination) {
		int x = destination.getColumn();
		int y = destination.getRow();
		if (y < 0 || y >= this.height || x < 0 || x >= this.width) {
			return false;
		}
		Tile currentTile = this.tiles[y][x];
		if (currentTile.isImpassable()) {
			return false;
		}
		if (currentTile.getOccupant() == null) {
			GridPoint oldLocation = actor.getLocation();
			Tile oldTile = this.tiles[oldLocation.getRow()][oldLocation.getColumn()];
			oldTile.setOccupant(null);
			currentTile.setOccupant(actor);
			actor.updateLocation(x, y);
			return true;
		}
		return false;
	}

	public void addActorToGrid(Actor actor, GridPoint destination) {
		this.tiles[destination.getRow()][destination.getColumn()].setOccupant(actor);
	}

	public void removeSelectedActor() {
		this.tiles[this.currentYPosition][this.currentXPosition].setOccupant(null);
	}

	public Actor getActor(GridPoint location) {
		return this.tiles[location.getRow()][location.getColumn()].getOccupant();
	}

	public Set<GridPoint> getTargetTiles() {
		return this.targetableTiles;
	}

	public AreaOfEffect getCurrentAOE() {
		return this.currentEffectAOE;
	}

	/**
	 * Search for all tiles that can be accessed starting at the centerPoint and
	 * moving radius or less.
	 */
	public void setMoveRadius(GridPoint centerPoint, int radius) {
		this.targetableTiles = new HashSet<GridPoint>();

		class SearchInfo {
			public GridPoint location;
			public int movementLeft;

			public SearchInfo(GridPoint location, int movementLeft) {
				this.location = location;
				this.movementLeft = movementLeft;
			}
		}

		// Breadth First Search
		LinkedList<SearchInfo> toVisit = new LinkedList<SearchInfo>();
		toVisit.add(new SearchInfo(centerPoint, radius));
		while (!toVisit.isEmpty()) {
			// Get Required Variables
			SearchInfo info = toVisit.poll();
			GridPoint test = info.location;
			int y = test.getRow();
			int x = test.getColumn();
			int movement = info.movementLeft;

			// Add the current square to the acceptable squares
			this.targetableTiles.add(test);

			// If we have movement left
			if (movement > 0) {
				// Each of these if blocks does the same thing for a different
				// direction. First, check if the target is within the bounds of
				// the grid. Then, check whether the target is able to be
				// occupied by an actor. Finally, check whether we've already
				// seen that tile.
				if (this.isInBounds(x - 1, y)) {
					if (!this.tiles[y][x - 1].isImpassable()) {
						GridPoint target = new GridPoint(x - 1, y);
						if (!this.targetableTiles.contains(target)) {
							toVisit.add(new SearchInfo(target, movement - 1));
						}
					}
				}
				if (this.isInBounds(x + 1, y)) {
					if (!this.tiles[y][x + 1].isImpassable()) {
						GridPoint target = new GridPoint(x + 1, y);
						if (!this.targetableTiles.contains(target)) {
							toVisit.add(new SearchInfo(target, movement - 1));
						}
					}
				}
				if (this.isInBounds(x, y - 1)) {
					if (!this.tiles[y - 1][x].isImpassable()) {
						GridPoint target = new GridPoint(x, y - 1);
						if (!this.targetableTiles.contains(target)) {
							toVisit.add(new SearchInfo(target, movement - 1));
						}
					}
				}
				if (this.isInBounds(x, y + 1)) {
					if (!this.tiles[y + 1][x].isImpassable()) {
						GridPoint target = new GridPoint(x, y + 1);
						if (!this.targetableTiles.contains(target)) {
							toVisit.add(new SearchInfo(target, movement - 1));
						}
					}
				}
			}
		}
	}

	public void setTargetAdjacentSquares(GridPoint location, int radius) {
		this.targetableTiles = new HashSet<GridPoint>();
		radius = radius - 1;
		for (int x = this.currentXPosition - radius; x <= this.currentXPosition + radius; x++) {
			int leftover = radius - Math.abs(this.currentXPosition - x);
			for (int y = this.currentYPosition - leftover; y <= this.currentYPosition + leftover; y++) {
				if (this.isInBounds(x, y)) {
					this.targetableTiles.add(new GridPoint(x, y));
				}
			}
		}
	}

	public void setAreaOfEffect(AreaOfEffect areaOfEffect) {
		this.currentEffectAOE = areaOfEffect;
		this.currentEffectAOE.setGrid(this);
	}

	public Set<Actor> getTargetsOfCurrentEffect() {
		Set<Actor> results = new HashSet<Actor>();
		for (GridPoint point : this.currentEffectAOE.getAllAffectedLocations(this.getSelectedLocation())) {
			Tile tile = this.tiles[point.getRow()][point.getColumn()];
			if (tile.getOccupant() != null) {
				results.add(tile.getOccupant());
			}
		}
		return results;
	}

	public void clearTargetingLayer() {
		this.targetableTiles = null;
		this.currentEffectAOE = null;
	}

	public boolean isInBounds(int x, int y) {
		return !(x < 0 || y < 0 || x >= this.width || y >= this.height);
	}
}
