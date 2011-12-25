package com.tacalpha.grid;

public enum Direction {
	UP, DOWN, LEFT, RIGHT, NONE;

	public static GridPoint move(int x, int y, Direction d) {
		switch (d) {
			case UP:
				return new GridPoint(x, y - 1);
			case DOWN:
				return new GridPoint(x, y + 1);
			case LEFT:
				return new GridPoint(x - 1, y);
			case RIGHT:
				return new GridPoint(x + 1, y);
			default:
				return new GridPoint(x, y);
		}
	}
}
