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
	private Rectangle textSpace;
	private Rectangle menuSpace;
	private Rectangle logSpace;
	private Rectangle messageSpace;
	private final static int TEXT_SPACING = 5;
	private final static int TEXT_BORDERS = 5;

	public ScreenRenderer(int width, int height) {
		this.setImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB));
		this.graphics = this.getImage().createGraphics();
		// The grid should take up the top 2/3 of the screen.
		this.gridSpace = new Rectangle(0, 0, GameRunner.WIDTH, GameRunner.HEIGHT * 2 / 3);
		this.textSpace = new Rectangle(0, this.gridSpace.bottom + 1, GameRunner.WIDTH, GameRunner.HEIGHT);
		this.menuSpace = new Rectangle(this.textSpace.left + ScreenRenderer.TEXT_SPACING, this.textSpace.top + ScreenRenderer.TEXT_SPACING,
				(this.textSpace.width / 4) - ScreenRenderer.TEXT_SPACING, this.textSpace.bottom - ScreenRenderer.TEXT_SPACING);
		this.messageSpace = new Rectangle(this.textSpace.left + (this.textSpace.width / 4) + ScreenRenderer.TEXT_SPACING, this.textSpace.top
				+ ScreenRenderer.TEXT_SPACING, this.textSpace.right - ScreenRenderer.TEXT_SPACING, this.textSpace.top + 50 - ScreenRenderer.TEXT_SPACING);
		this.logSpace = new Rectangle(this.messageSpace.left, this.textSpace.top + 50 + ScreenRenderer.TEXT_SPACING, this.textSpace.right
				- ScreenRenderer.TEXT_SPACING, this.textSpace.bottom - ScreenRenderer.TEXT_SPACING);
		this.tileSize = 50;
	}

	public void render(Game game, boolean hasFocus) {
		Grid grid = game.getGrid();
		Collection<Actor> actors = game.getActors();
		Menu menu = game.getActiveMenu();

		// TODO: I don't like the class fields here. There has to be a better
		// way.
		this.tileSize = Math.min(this.gridSpace.height / grid.height, this.gridSpace.width / grid.width);

		// Grid
		this.renderGrid(grid);
		this.renderActors(actors);

		// Menu and Info
		this.fill(this.textSpace, Color.BLACK);
		this.renderMenu(menu);
		// TODO: These 2 need to be fleshed out.
		this.renderMessage("This message will change!");
		this.renderLog();
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
		this.border(this.menuSpace, Color.DARK_GRAY, ScreenRenderer.TEXT_BORDERS);

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

	private void renderMessage(String message) {
		this.fill(this.messageSpace, Color.GRAY);
		this.border(this.messageSpace, Color.DARK_GRAY, ScreenRenderer.TEXT_BORDERS);

		if (message == null) {
			return;
		}

		this.graphics.setColor(Color.BLACK);
		this.graphics.setFont(new Font("Arial", Font.PLAIN, 20));
		this.graphics.drawString(message, this.messageSpace.left + 15, this.messageSpace.top + this.graphics.getFontMetrics().getHeight());
	}

	private void renderLog() {
		// TODO: Make this take an argument.
		this.fill(this.logSpace, Color.GRAY);
		this.border(this.logSpace, Color.DARK_GRAY, ScreenRenderer.TEXT_BORDERS);
	}

	private void fill(Rectangle rect, Color color) {
		this.graphics.setColor(color);
		this.graphics.fillRect(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
	}

	private void border(Rectangle rect, Color color, int thickness) {
		// int outer = thickness / 2;
		int outer = thickness / 2;
		int inner = thickness - outer;
		this.graphics.setColor(color);
		this.graphics.fillRect(rect.left - outer, rect.top - outer, rect.width + (outer * 2), thickness);
		this.graphics.fillRect(rect.left - outer, rect.top - outer, thickness, rect.height + (outer * 2));
		this.graphics.fillRect(rect.right - inner, rect.top - outer, thickness, rect.height + (outer * 2));
		this.graphics.fillRect(rect.left - outer, rect.bottom - inner, rect.width + (outer * 2), thickness);
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