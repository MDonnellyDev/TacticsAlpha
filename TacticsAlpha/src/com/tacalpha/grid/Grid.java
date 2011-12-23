package com.tacalpha.grid;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.tacalpha.actor.Actor;

public class Grid {
	private final Tile[][] tiles;
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

	public void moveSelectedLocation(Direction direction) {
		switch (direction) {
			case UP:
				this.currentYPosition--;
				if (this.currentYPosition < 0) {
					this.currentYPosition = this.height - 1;
				}
				break;
			case DOWN:
				this.currentYPosition++;
				if (this.currentYPosition >= this.height) {
					this.currentYPosition = 0;
				}
				break;
			case LEFT:
				this.currentXPosition--;
				if (this.currentXPosition < 0) {
					this.currentXPosition = this.width - 1;
				}
				break;
			case RIGHT:
				this.currentXPosition++;
				if (this.currentXPosition >= this.width) {
					this.currentXPosition = 0;
				}
				break;
			default:
				break;
		}
	}

	public GridPoint getSelectedLocation() {
		return new GridPoint(this.currentXPosition, this.currentYPosition);
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

	/**
	 * Search for all tiles that can be accessed starting at the centerPoint and
	 * moving radius or less.
	 */
	public Set<GridPoint> getMoveRadius(GridPoint centerPoint, int radius) {
		Set<GridPoint> results = new HashSet<GridPoint>();

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
			results.add(test);

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
						if (!results.contains(target)) {
							toVisit.add(new SearchInfo(target, movement - 1));
						}
					}
				}
				if (this.isInBounds(x + 1, y)) {
					if (!this.tiles[y][x + 1].isImpassable()) {
						GridPoint target = new GridPoint(x + 1, y);
						if (!results.contains(target)) {
							toVisit.add(new SearchInfo(target, movement - 1));
						}
					}
				}
				if (this.isInBounds(x, y - 1)) {
					if (!this.tiles[y - 1][x].isImpassable()) {
						GridPoint target = new GridPoint(x, y - 1);
						if (!results.contains(target)) {
							toVisit.add(new SearchInfo(target, movement - 1));
						}
					}
				}
				if (this.isInBounds(x, y + 1)) {
					if (!this.tiles[y + 1][x].isImpassable()) {
						GridPoint target = new GridPoint(x, y + 1);
						if (!results.contains(target)) {
							toVisit.add(new SearchInfo(target, movement - 1));
						}
					}
				}
			}
		}
		return results;
	}

	public Set<GridPoint> getAdjacentSquares(GridPoint location) {
		Set<GridPoint> results = new HashSet<GridPoint>();
		int x = location.getColumn();
		int y = location.getRow();
		if (this.isInBounds(x - 1, y)) {
			results.add(new GridPoint(x - 1, y));
		}
		if (this.isInBounds(x + 1, y)) {
			results.add(new GridPoint(x + 1, y));
		}
		if (this.isInBounds(x, y - 1)) {
			results.add(new GridPoint(x, y - 1));
		}
		if (this.isInBounds(x, y + 1)) {
			results.add(new GridPoint(x, y + 1));
		}
		return results;
	}

	private boolean isInBounds(int x, int y) {
		return !(x < 0 || y < 0 || x >= this.width || y >= this.height);
	}
}
