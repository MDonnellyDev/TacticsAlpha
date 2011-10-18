package com.tacalpha.grid;

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

	public Actor getActor(GridPoint location) {
		return this.tiles[location.getRow()][location.getColumn()].getOccupant();
	}
}
