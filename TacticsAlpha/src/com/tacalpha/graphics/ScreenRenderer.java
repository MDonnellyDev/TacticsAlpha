package com.tacalpha.graphics;

import com.tacalpha.Game;
import com.tacalpha.grid.Grid;
import com.tacalpha.grid.GridPoint;
import com.tacalpha.grid.Tile;

public class ScreenRenderer extends Bitmap {
	public ScreenRenderer(int width, int height) {
		super(width, height);
	}

	public void render(Game game, boolean hasFocus) {
		// TODO: Temporary testing code.
		this.renderGrid(game.getGrid(), 350, 200);
	}

	public void renderGrid(Grid grid, int origX, int origY) {
		this.fill(origX, origY, origX + (grid.width) * 50, origY + (grid.height) * 50, 0x000000);
		Tile[][] tiles = grid.getTiles();
		GridPoint selectedLocation = grid.getSelectedLocation();
		for (int y = 0; y < grid.height; y++) {
			for (int x = 0; x < grid.width; x++) {
				int color = tiles[y][x].isImpassable() ? 0x333333 : 0x00ffff;
				if (selectedLocation.matches(x, y)) {
					color = color & 0xaaaaaa;
				}
				this.fill(origX + (x * 50) + 1, origY + (y * 50) + 1, origX + (x * 50) + 49, origY + (y * 50) + 49, color);
			}
		}
	}
}