package com.tacalpha.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import com.tacalpha.Game;
import com.tacalpha.GameRunner;
import com.tacalpha.grid.Grid;
import com.tacalpha.grid.GridPoint;
import com.tacalpha.grid.Tile;

public class ScreenRenderer extends Component {
	private int tileSize = 50;
	private int gridX = 0;
	private int gridY = 0;
	private Graphics2D graphics2D;
	private BufferedImage image;

	public ScreenRenderer(int width, int height) {
		this.setImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB));
		this.graphics2D = this.getImage().createGraphics();
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
		this.graphics2D.setColor(Color.BLACK);
		this.graphics2D.fillRect(0, 0, this.gridX, this.gridY);
		Tile[][] tiles = grid.getTiles();
		GridPoint selectedLocation = grid.getSelectedLocation();
		for (int y = 0; y < grid.height; y++) {
			for (int x = 0; x < grid.width; x++) {
				Color color = tiles[y][x].isImpassable() ? Color.DARK_GRAY : Color.LIGHT_GRAY;
				if (selectedLocation.matches(x, y)) {
					color = Color.BLUE;
				}
				this.graphics2D.setColor(color);
				this.graphics2D.fill(new Rectangle2D.Double(this.getTileOffsetX(x), this.getTileOffsetY(y), this.tileSize, this.tileSize));
			}
		}
	}

	private Rectangle getTileLocation(int x, int y) {
		return new Rectangle(this.gridX + (x * this.tileSize) + 1, this.gridY + (y * this.tileSize) + 1,
				this.gridX + (x * this.tileSize) + (this.tileSize - 1), this.gridY + (y * this.tileSize) + (this.tileSize - 1));
	}

	private int getTileOffsetX(int x) {
		return this.gridX + (x * this.tileSize) + 1;
	}

	private int getTileOffsetY(int y) {
		return this.gridY + (y * this.tileSize) + 1;
	}

	private Rectangle getGridLocation(Grid grid) {
		return new Rectangle(this.gridX, this.gridY, this.gridX + (grid.width) * this.tileSize, this.gridY + (grid.height) * this.tileSize);
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return this.image;
	}
}