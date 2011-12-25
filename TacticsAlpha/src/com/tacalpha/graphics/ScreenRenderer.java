package com.tacalpha.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.tacalpha.Game;
import com.tacalpha.actor.Actor;
import com.tacalpha.equip.Equipment;
import com.tacalpha.equip.Slot;
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
	private int gridXOffset;
	private int gridYOffset;
	private Rectangle gridSpace;
	private Rectangle textSpace;
	private Rectangle menuSpace;
	private Rectangle actorSpace;
	private Rectangle messageSpace;
	private final static int MENU_SPACE_HEIGHT = 250;
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
	private final Color targetTileColor = new Color(127, 255, 127);

	public ScreenRenderer() {

	}

	public void render(Game game, boolean hasFocus, int width, int height) {
		this.setUpRenderSpaces(width, height);

		Grid grid = game.getGrid();
		Collection<Actor> actors = game.getActors();
		Menu menu = game.getActiveMenu();

		// TODO: I don't like the class fields here. There has to be a better
		// way.
		// this.tileSize = Math.min(this.gridSpace.height / grid.height,
		// this.gridSpace.width / grid.width);
		this.tileSize = game.getTileSize();

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
		// The grid should take up the top of the screen, leaving room for the
		// bottom stuff.
		this.gridSpace = new Rectangle(0, 0, width, height - ScreenRenderer.MENU_SPACE_HEIGHT);

		// Everything else should take up the bottom 250px of the screen
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
		this.calculateGridOffset(grid, selectedLocation);
		Set<GridPoint> targetLocations = grid.getTargetTiles();
		for (int y = 0; y < grid.height; y++) {
			for (int x = 0; x < grid.width; x++) {
				Tile tile = tiles[y][x];
				Color color = tile.isImpassable() ? this.impassableTerrainColor : this.genericTerrainColor;
				if (targetLocations != null && targetLocations.contains(new GridPoint(x, y))) {
					color = this.targetTileColor;
				}
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

		// Setup
		this.graphics.setColor(this.textColor);
		this.graphics.setFont(new Font("Courier New", Font.BOLD, 20));
		int fontHeight = this.graphics.getFontMetrics().getHeight();
		int leftFontPadding = this.actorSpace.left + 15;

		// Statistics
		this.graphics.drawString("HP:", leftFontPadding, this.actorSpace.top + fontHeight);
		this.graphics.drawString("MP:", leftFontPadding, this.actorSpace.top + (fontHeight * 2));
		this.graphics.drawString("Move Speed: " + actor.getMoveSpeed(), leftFontPadding, this.actorSpace.top + (fontHeight * 3));
		this.graphics.drawString("Strength: " + actor.getStrength(), leftFontPadding, this.actorSpace.top + (fontHeight * 4));
		this.graphics.drawString("Defense: " + actor.getDefense(), leftFontPadding, this.actorSpace.top + (fontHeight * 5));
		this.graphics.drawString("Agility: " + actor.getActionSpeed(), leftFontPadding, this.actorSpace.top + (fontHeight * 6));

		// Current/Total Numbers
		String tempStr = actor.getCurrentHealth() + "/" + actor.getMaxHealth();
		int barNumberAreaWidth = this.graphics.getFontMetrics().stringWidth(tempStr);
		this.graphics.drawString(tempStr, this.actorSpace.getHorizontalCenter() + 5, this.actorSpace.top + fontHeight);
		tempStr = actor.getCurrentMana() + "/" + actor.getMaxMana();
		barNumberAreaWidth = Math.max(barNumberAreaWidth, this.graphics.getFontMetrics().stringWidth(tempStr));
		this.graphics.drawString(tempStr, this.actorSpace.getHorizontalCenter() + 5, this.actorSpace.top + (fontHeight * 2));

		// Equipment
		int rightColumnPadding = this.actorSpace.getHorizontalCenter() + barNumberAreaWidth + 25;
		this.graphics.drawString("EQUIPMENT", rightColumnPadding, this.actorSpace.top + fontHeight);
		Equipment temp = actor.getEquipment(Slot.MAINHAND);
		this.graphics.drawString("RH: " + (temp == null ? "Empty" : temp.getName()), rightColumnPadding, this.actorSpace.top + (fontHeight * 2));
		temp = actor.getEquipment(Slot.OFFHAND);
		this.graphics.drawString("LH: " + (temp == null ? "Empty" : temp.getName()), rightColumnPadding, this.actorSpace.top + (fontHeight * 3));
		temp = actor.getEquipment(Slot.HEAD);
		this.graphics.drawString("HD: " + (temp == null ? "Empty" : temp.getName()), rightColumnPadding, this.actorSpace.top + (fontHeight * 4));
		temp = actor.getEquipment(Slot.BODY);
		this.graphics.drawString("BD: " + (temp == null ? "Empty" : temp.getName()), rightColumnPadding, this.actorSpace.top + (fontHeight * 5));
		temp = actor.getEquipment(Slot.FEET);
		this.graphics.drawString("FT: " + (temp == null ? "Empty" : temp.getName()), rightColumnPadding, this.actorSpace.top + (fontHeight * 6));
		temp = actor.getEquipment(Slot.OTHER);
		this.graphics.drawString("AC: " + (temp == null ? "Empty" : temp.getName()), rightColumnPadding, this.actorSpace.top + (fontHeight * 7));

		// Current/Total Bars
		int textOffset = 15 + this.graphics.getFontMetrics().stringWidth("HP:");
		int bottomBuffer = this.graphics.getFontMetrics().getAscent() + 1;
		int topBuffer = this.graphics.getFontMetrics().getDescent() + this.graphics.getFontMetrics().getLeading() + 1;
		// HP
		this.fill(new Rectangle(this.actorSpace.left + textOffset, this.actorSpace.top + topBuffer, this.actorSpace.getHorizontalCenter(), this.actorSpace.top
				+ (fontHeight * 2) - bottomBuffer), this.hpBackgroundColor);
		int barWidth = this.actorSpace.getHorizontalCenter() - (this.actorSpace.left + textOffset);
		int currentHpWidth = (int) (barWidth * ((float) actor.getCurrentHealth() / (float) actor.getMaxHealth()));
		this.fill(new Rectangle(this.actorSpace.left + textOffset, this.actorSpace.top + topBuffer, this.actorSpace.left + textOffset + currentHpWidth,
				this.actorSpace.top + (fontHeight * 2) - bottomBuffer), this.hpForegroundColor);
		// MP
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

	private void calculateGridOffset(Grid grid, GridPoint centerOn) {
		int gridWidthPx = grid.width * this.tileSize;
		int gridHeightPx = grid.height * this.tileSize;
		if (gridWidthPx <= this.gridSpace.width && gridHeightPx <= this.gridSpace.height) {
			// The grid fits in the space we have with room to spare. The
			// offsets will be used to center the grid in the space.
			this.gridXOffset = (this.gridSpace.width - gridWidthPx) / 2;
			this.gridYOffset = (this.gridSpace.height - gridHeightPx) / 2;
		} else {
			// The grid does not fit in the space we have. The offsets will be
			// used to keep the selected square centered in the view.
			this.gridXOffset = (this.gridSpace.width / 2) - (centerOn.getColumn() * this.tileSize) - (this.tileSize / 2);
			this.gridYOffset = (this.gridSpace.height / 2) - (centerOn.getRow() * this.tileSize) - (this.tileSize / 2);

			// Lock the edges of the grid to the edges of the screen when
			// selecting a square near the edge of the grid.
			if (this.gridXOffset > 0) {
				this.gridXOffset = 0;
			}
			if (this.gridYOffset > 0) {
				this.gridYOffset = 0;
			}
			if (this.gridXOffset + gridWidthPx < this.gridSpace.width) {
				this.gridXOffset = this.gridSpace.width - gridWidthPx;
			}
			if (this.gridYOffset + gridHeightPx < this.gridSpace.height) {
				this.gridYOffset = this.gridSpace.height - gridHeightPx;
			}

			// If the grid is bigger on one side but smaller in the other,
			// center on the smaller side.
			if (gridWidthPx <= this.gridSpace.width) {
				this.gridXOffset = (this.gridSpace.width - gridWidthPx) / 2;
			} else if (gridHeightPx <= this.gridSpace.height) {
				this.gridYOffset = (this.gridSpace.height - gridHeightPx) / 2;
			}
		}
	}

	private Rectangle getTileRectangle(int x, int y) {
		int tileSizeReduction = 1;
		return new Rectangle(this.gridSpace.left + (x * this.tileSize) + tileSizeReduction + this.gridXOffset, this.gridSpace.top + (y * this.tileSize)
				+ tileSizeReduction + this.gridYOffset, this.gridSpace.left + (x * this.tileSize) + (this.tileSize - tileSizeReduction) + this.gridXOffset,
				this.gridSpace.top + (y * this.tileSize) + (this.tileSize - tileSizeReduction) + this.gridYOffset);
	}

	private Rectangle getActorRectangle(int x, int y) {
		int actorSizeReduction = 5;
		while (this.tileSize - (actorSizeReduction * 2) < 0) {
			actorSizeReduction--;
		}
		return new Rectangle(this.gridSpace.left + (x * this.tileSize) + actorSizeReduction + this.gridXOffset, this.gridSpace.top + (y * this.tileSize)
				+ actorSizeReduction + this.gridYOffset, this.gridSpace.left + (x * this.tileSize) + (this.tileSize - actorSizeReduction) + this.gridXOffset,
				this.gridSpace.top + (y * this.tileSize) + (this.tileSize - actorSizeReduction) + this.gridYOffset);
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return this.image;
	}
}