package com.tacalpha.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.List;

import com.tacalpha.Game;
import com.tacalpha.GameRunner;
import com.tacalpha.actor.Actor;
import com.tacalpha.grid.Grid;
import com.tacalpha.grid.GridPoint;
import com.tacalpha.grid.Tile;
import com.tacalpha.menu.Menu;

public class ScreenRenderer extends Component {
	// Generic
	private Graphics2D graphics;
	private BufferedImage image;

	// BATTLE Screen
	private int tileSize;
	private Rectangle gridSpace;
	private Rectangle menuSpace;

	public ScreenRenderer(int width, int height) {
		this.setImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB));
		this.graphics = this.getImage().createGraphics();
		// The grid should take up the top 2/3 of the screen.
		this.gridSpace = new Rectangle(0, 0, GameRunner.WIDTH, GameRunner.HEIGHT * 2 / 3);
		this.menuSpace = new Rectangle(0, this.gridSpace.bottom + 1, GameRunner.WIDTH, GameRunner.HEIGHT);
		this.tileSize = 50;
	}

	public void render(Game game, boolean hasFocus) {
		Grid grid = game.getGrid();
		Collection<Actor> actors = game.getActors();
		Menu menu = game.getActiveMenu();

		// TODO: I don't like the class fields here. There has to be a better
		// way.
		this.tileSize = Math.min(this.gridSpace.height / grid.height, this.gridSpace.width / grid.width);

		this.renderGrid(grid);
		// TODO: Make this take some actors.
		this.renderActors(actors);
		this.renderMenu(menu);
	}

	private void renderGrid(Grid grid) {
		this.fill(this.gridSpace, Color.BLACK);
		Tile[][] tiles = grid.getTiles();
		GridPoint selectedLocation = grid.getSelectedLocation();
		for (int y = 0; y < grid.height; y++) {
			for (int x = 0; x < grid.width; x++) {
				Tile tile = tiles[y][x];
				Color color = tile.isImpassable() ? Color.DARK_GRAY : Color.LIGHT_GRAY;
				if (selectedLocation.matches(x, y)) {
					color = Color.BLUE;
				}
				this.fill(this.getTileRectangle(x, y), color);
			}
		}
	}

	private void renderActors(Collection<Actor> actors) {
		for (Actor actor : actors) {
			GridPoint actorLocation = actor.getLocation();
			this.fill(this.getActorRectangle(actorLocation.getColumn(), actorLocation.getRow()), Color.YELLOW);
		}
	}

	private void renderMenu(Menu menu) {
		this.fill(this.menuSpace, Color.GRAY);

		if (menu == null) {
			return;
		}

		this.graphics.setFont(new Font("Arial", Font.PLAIN, 20));

		List<String> options = menu.getOptionTitles();

		// Leave some space before the words.
		int left = this.menuSpace.left + 15;
		int fontHeight = this.graphics.getFontMetrics().getHeight();
		int selectedIndex = menu.getCurrent();

		for (int i = 0; i < options.size(); i++) {
			String option = options.get(i);
			if (i == selectedIndex) {
				this.graphics.setColor(Color.WHITE);
			} else {
				this.graphics.setColor(Color.BLACK);
			}
			this.graphics.drawString(option, left, this.menuSpace.top + ((i + 1) * fontHeight));
		}
	}

	private void fill(Rectangle rect, Color color) {
		this.graphics.setColor(color);
		this.graphics.fillRect(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
	}

	private Rectangle getTileRectangle(int x, int y) {
		int tileSizeReduction = 1;
		return new Rectangle(this.gridSpace.left + (x * this.tileSize) + tileSizeReduction, this.gridSpace.top + (y * this.tileSize) + tileSizeReduction,
				this.gridSpace.left + (x * this.tileSize) + (this.tileSize - tileSizeReduction), this.gridSpace.top + (y * this.tileSize)
						+ (this.tileSize - tileSizeReduction));
	}

	private Rectangle getActorRectangle(int x, int y) {
		int actorSizeReduction = 5;
		while (this.tileSize - (actorSizeReduction * 2) < 0) {
			actorSizeReduction--;
		}
		return new Rectangle(this.gridSpace.left + (x * this.tileSize) + actorSizeReduction, this.gridSpace.top + (y * this.tileSize) + actorSizeReduction,
				this.gridSpace.left + (x * this.tileSize) + (this.tileSize - actorSizeReduction), this.gridSpace.top + (y * this.tileSize)
						+ (this.tileSize - actorSizeReduction));
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return this.image;
	}
}