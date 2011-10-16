package com.tacalpha.graphics;

import com.tacalpha.Game;
import com.tacalpha.GameRunner;
import com.tacalpha.grid.Grid;
import com.tacalpha.grid.GridPoint;
import com.tacalpha.grid.Tile;

public class ScreenRenderer extends Bitmap {
	public ScreenRenderer(int width, int height) {
		super(width, height);
	}

	public void render(Game game, boolean hasFocus) {
		this.renderGrid(game.getGrid());
	}

	public void renderGrid(Grid grid) {
		// TODO: Is this the best place to do this?
		// Scale the grid to fit on the screen and center it.
		// TODO: Add code to handle the case there tileSize is too small.
		// In that case, the grid should move to keep the selected tile centered
		// on the screen until the selected tile gets close to the edge.
		int tileSize = Math.min(GameRunner.HEIGHT / grid.height, GameRunner.WIDTH / grid.width);
		int origX = (GameRunner.WIDTH - (tileSize * grid.width)) / 2;
		int origY = (GameRunner.HEIGHT - (tileSize * grid.height)) / 2;

		this.fill(origX, origY, origX + (grid.width) * 50, origY + (grid.height) * 50, 0x000000);
		Tile[][] tiles = grid.getTiles();
		GridPoint selectedLocation = grid.getSelectedLocation();
		for (int y = 0; y < grid.height; y++) {
			for (int x = 0; x < grid.width; x++) {
				int color = tiles[y][x].isImpassable() ? 0x333333 : 0x00ffff;
				if (selectedLocation.matches(x, y)) {
					color = color & 0xaaaaaa;
				}
				this.fill(origX + (x * tileSize) + 1, origY + (y * tileSize) + 1, origX + (x * tileSize) + (tileSize - 1), origY + (y * tileSize)
						+ (tileSize - 1), color);
			}
		}
	}
}