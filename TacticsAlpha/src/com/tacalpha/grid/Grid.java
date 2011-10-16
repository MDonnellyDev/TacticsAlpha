package com.tacalpha.grid;

public class Grid {
	private final Tile[][] tiles;
	public final int width;
	public final int height;

	private int selectedX;
	private int selectedY;

	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		this.tiles = new Tile[height][width];
		this.selectedX = 0;
		this.selectedY = 0;
	}

	public Grid(String mapFile) {
		this.tiles = GridLoader.getTiles(mapFile);
		this.height = this.tiles.length;
		this.width = this.tiles[0].length;
	}

	public void moveSelected(Direction direction) {
		switch (direction) {
			case UP:
				this.selectedY = Math.max(0, this.selectedY - 1);
				break;
			case DOWN:
				this.selectedY = Math.min(this.height - 1, this.selectedY + 1);
				break;
			case LEFT:
				this.selectedX = Math.max(0, this.selectedX - 1);
				break;
			case RIGHT:
				this.selectedX = Math.min(this.width - 1, this.selectedX + 1);
				break;
			default:
				break;
		}
	}

	public GridPoint getSelectedLocation() {
		return new GridPoint(this.selectedX, this.selectedY);
	}

	public Tile[][] getTiles() {
		return this.tiles;
	}
}
