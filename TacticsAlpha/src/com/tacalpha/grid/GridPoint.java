package com.tacalpha.grid;

public class GridPoint {
	private int row;
	private int column;

	public GridPoint(int column, int row) {
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return this.row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return this.column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public boolean matches(int column, int row) {
		return this.column == column && this.row == row;
	}
}
