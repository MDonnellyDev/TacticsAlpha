package com.tacalpha.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.List;

import com.tacalpha.Game;
import com.tacalpha.actor.Actor;
import com.tacalpha.grid.Grid;
import com.tacalpha.grid.GridPoint;
import com.tacalpha.grid.Tile;
import com.tacalpha.menu.Menu;

public class ScreenRenderer extends Component {
	private static final long serialVersionUID = -7203461939175986815L;

	// Generic
	private Graphics2D graphics;
	private BufferedImage image;

	// BATTLE Screen
	private int tileSize;
	private Rectangle gridSpace;
	private Rectangle textSpace;
	private Rectangle menuSpace;
	private Rectangle actorSpace;
	private Rectangle messageSpace;
	private final static int TEXT_SPACING = 5;
	private final static int TEXT_BORDERS = 5;

	// COLORS
	private final Color screenBackgroundColor = new Color(0, 0, 0);
	private final Color hpBackgroundColor = new Color(63, 0, 0);
	private final Color hpForegroundColor = new Color(255, 0, 0);
	private final Color mpBackgroundColor = new Color(0, 0, 63);
	private final Color mpForegroundColor = new Color(0, 0, 255);
	private final Color menuBackgroundColor = new Color(160, 160, 160);
	private final Color menuBorderColor = new Color(63, 63, 63);
	private final Color textColor = new Color(0, 0, 0);
	private final Color selectedTextColor = new Color(255, 255, 255);

	// TEMPORARY
	// TODO: These are currently using different colors, but should eventually
	// be textures or other effects.
	private final Color actorColor = new Color(255, 255, 0);
	private final Color genericTerrainColor = new Color(191, 191, 191);
	private final Color impassableTerrainColor = new Color(63, 63, 63);
	private final Color selectedTileColor = new Color(0, 255, 255);

	public ScreenRenderer() {

	}

	public void render(Game game, boolean hasFocus, int width, int height) {
		this.setUpRenderSpaces(width, height);

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
		this.fill(this.textSpace, this.screenBackgroundColor);
		this.renderMenu(menu);
		this.renderMessage(game.getMessage());
		this.renderActorInfo(grid.getSelectedTile().getOccupant());
	}

	private void setUpRenderSpaces(int width, int height) {
		this.setImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB));
		this.graphics = this.getImage().createGraphics();
		// The grid should take up the top 2/3 of the screen.
		this.gridSpace = new Rectangle(0, 0, width, height * 2 / 3);
		this.textSpace = new Rectangle(0, this.gridSpace.bottom + 1, width, height);
		this.menuSpace = new Rectangle(this.textSpace.left + ScreenRenderer.TEXT_SPACING, this.textSpace.top + ScreenRenderer.TEXT_SPACING,
				(this.textSpace.width / 4) - ScreenRenderer.TEXT_SPACING, this.textSpace.bottom - ScreenRenderer.TEXT_SPACING);
		this.messageSpace = new Rectangle(this.textSpace.left + (this.textSpace.width / 4) + ScreenRenderer.TEXT_SPACING, this.textSpace.top
				+ ScreenRenderer.TEXT_SPACING, this.textSpace.right - ScreenRenderer.TEXT_SPACING, this.textSpace.top + 50 - ScreenRenderer.TEXT_SPACING);
		this.actorSpace = new Rectangle(this.messageSpace.left, this.textSpace.top + 50 + ScreenRenderer.TEXT_SPACING, this.textSpace.right
				- ScreenRenderer.TEXT_SPACING, this.textSpace.bottom - ScreenRenderer.TEXT_SPACING);
		this.tileSize = 50;
	}

	private void renderGrid(Grid grid) {
		this.fill(this.gridSpace, this.screenBackgroundColor);
		Tile[][] tiles = grid.getTiles();
		GridPoint selectedLocation = grid.getSelectedLocation();
		for (int y = 0; y < grid.height; y++) {
			for (int x = 0; x < grid.width; x++) {
				Tile tile = tiles[y][x];
				Color color = tile.isImpassable() ? this.impassableTerrainColor : this.genericTerrainColor;
				if (selectedLocation.matches(x, y)) {
					color = this.selectedTileColor;
				}
				this.fill(this.getTileRectangle(x, y), color);
			}
		}
	}

	private void renderActors(Collection<Actor> actors) {
		for (Actor actor : actors) {
			GridPoint actorLocation = actor.getLocation();
			this.fill(this.getActorRectangle(actorLocation.getColumn(), actorLocation.getRow()), this.actorColor);
		}
	}

	private void renderMenu(Menu menu) {
		this.fill(this.menuSpace, this.menuBackgroundColor);
		this.border(this.menuSpace, this.menuBorderColor, ScreenRenderer.TEXT_BORDERS);

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
				this.graphics.setColor(this.selectedTextColor);
			} else {
				this.graphics.setColor(this.textColor);
			}
			this.graphics.drawString(option, left, this.menuSpace.top + ((i + 1) * fontHeight));
		}
	}

	private void renderMessage(String message) {
		this.fill(this.messageSpace, this.menuBackgroundColor);
		this.border(this.messageSpace, this.menuBorderColor, ScreenRenderer.TEXT_BORDERS);

		if (message == null) {
			return;
		}

		this.graphics.setColor(this.textColor);
		this.graphics.setFont(new Font("Arial", Font.PLAIN, 20));
		this.graphics.drawString(message, this.messageSpace.left + 15, this.messageSpace.top + this.graphics.getFontMetrics().getHeight());
	}

	private void renderActorInfo(Actor actor) {
		this.fill(this.actorSpace, this.menuBackgroundColor);
		this.border(this.actorSpace, this.menuBorderColor, ScreenRenderer.TEXT_BORDERS);

		if (actor == null) {
			return;
		}

		this.graphics.setColor(this.textColor);
		this.graphics.setFont(new Font("Courier New", Font.BOLD, 20));
		int fontHeight = this.graphics.getFontMetrics().getHeight();
		this.graphics.drawString("HP:", this.actorSpace.left + 15, this.actorSpace.top + fontHeight);
		this.graphics.drawString("MP:", this.actorSpace.left + 15, this.actorSpace.top + (fontHeight * 2));
		this.graphics.drawString(actor.getCurrentHealth() + " / " + actor.getMaxHealth(), this.actorSpace.getHorizontalCenter() + 5, this.actorSpace.top
				+ fontHeight);
		this.graphics.drawString(actor.getCurrentMana() + " / " + actor.getMaxMana(), this.actorSpace.getHorizontalCenter() + 5, this.actorSpace.top
				+ (fontHeight * 2));

		// TODO: Calculate how many pixels the HP: and MP: text takes up instead
		// of guessing.
		int textOffset = 50;
		int bottomBuffer = this.graphics.getFontMetrics().getAscent() + 1;
		int topBuffer = this.graphics.getFontMetrics().getDescent() + this.graphics.getFontMetrics().getLeading() + 1;
		// HP
		this.fill(new Rectangle(this.actorSpace.left + textOffset, this.actorSpace.top + topBuffer, this.actorSpace.getHorizontalCenter(), this.actorSpace.top
				+ (fontHeight * 2) - bottomBuffer), this.hpBackgroundColor);
		int barWidth = this.actorSpace.getHorizontalCenter() - (this.actorSpace.left + textOffset);
		int currentHpWidth = (int) (barWidth * ((float) actor.getCurrentHealth() / (float) actor.getMaxHealth()));
		this.fill(new Rectangle(this.actorSpace.left + textOffset, this.actorSpace.top + topBuffer, this.actorSpace.left + textOffset + currentHpWidth,
				this.actorSpace.top + (fontHeight * 2) - bottomBuffer), this.hpForegroundColor);
		// MP Background
		this.fill(new Rectangle(this.actorSpace.left + textOffset, this.actorSpace.top + fontHeight + topBuffer, this.actorSpace.getHorizontalCenter(),
				this.actorSpace.top + (fontHeight * 3) - bottomBuffer), this.mpBackgroundColor);
		int currentMpWidth = (int) (barWidth * ((float) actor.getCurrentMana() / (float) actor.getMaxMana()));
		this.fill(new Rectangle(this.actorSpace.left + textOffset, this.actorSpace.top + fontHeight + topBuffer, this.actorSpace.left + textOffset
				+ currentMpWidth, this.actorSpace.top + (fontHeight * 3) - bottomBuffer), this.mpForegroundColor);

	}

	private void fill(Rectangle rect, Color color) {
		this.graphics.setColor(color);
		this.graphics.fillRect(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
	}

	private void border(Rectangle rect, Color color, int thickness) {
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