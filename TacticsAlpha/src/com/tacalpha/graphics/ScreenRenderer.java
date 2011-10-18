package com.tacalpha.graphics;

import com.tacalpha.Game;
import com.tacalpha.GameRunner;
import com.tacalpha.grid.Grid;
import com.tacalpha.grid.GridPoint;
import com.tacalpha.grid.Tile;

public class ScreenRenderer extends Bitmap {
	private int tileSize = 50;
	private int gridX = 0;
	private int gridY = 0;

	public ScreenRenderer(int width, int height) {
		super(width, height);
	}

	public void render(Game game, boolean hasFocus) {
		Grid grid = game.getGrid();

		// Scale the grid to fit on the screen and center it.
		// TODO: I don't like the class fields here. There has to be a better
		// way.
		this.tileSize = Math.min(GameRunner.HEIGHT / grid.height, GameRunner.WIDTH / grid.width);
		this.gridX = (GameRunner.WIDTH - (this.tileSize * grid.width)) / 2;
		this.gridY = (GameRunner.HEIGHT - (this.tileSize * grid.height)) / 2;

		this.renderGrid(grid);
	}

	private void renderGrid(Grid grid) {

		this.fill(this.getGridLocation(grid), 0x000000);
		Tile[][] tiles = grid.getTiles();
		GridPoint selectedLocation = grid.getSelectedLocation();
		for (int y = 0; y < grid.height; y++) {
			for (int x = 0; x < grid.width; x++) {
				int color = tiles[y][x].isImpassable() ? 0x333333 : 0x00ffff;
				if (selectedLocation.matches(x, y)) {
					color = color & 0xaaaaaa;
				}
				this.fill(this.getTileLocation(x, y), color);
			}
		}
	}

	private Rectangle getTileLocation(int x, int y) {
		return new Rectangle(this.gridX + (x * this.tileSize) + 1, this.gridY + (y * this.tileSize) + 1,
				this.gridX + (x * this.tileSize) + (this.tileSize - 1), this.gridY + (y * this.tileSize) + (this.tileSize - 1));
	}

	private Rectangle getGridLocation(Grid grid) {
		return new Rectangle(this.gridX, this.gridY, this.gridX + (grid.width) * this.tileSize, this.gridY + (grid.height) * this.tileSize);
	}
}