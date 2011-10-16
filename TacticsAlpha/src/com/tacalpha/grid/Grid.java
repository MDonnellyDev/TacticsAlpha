package com.tacalpha.grid;

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
				this.currentYPosition = Math.max(0, this.currentYPosition - 1);
				break;
			case DOWN:
				this.currentYPosition = Math.min(this.height - 1, this.currentYPosition + 1);
				break;
			case LEFT:
				this.currentXPosition = Math.max(0, this.currentXPosition - 1);
				break;
			case RIGHT:
				this.currentXPosition = Math.min(this.width - 1, this.currentXPosition + 1);
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
}
