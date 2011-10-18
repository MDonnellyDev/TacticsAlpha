package com.tacalpha.graphics;

public class Rectangle {
	public final int left;
	public final int top;
	public final int right;
	public final int bottom;
	public final int width;
	public final int height;

	public Rectangle(int startX, int startY, int endX, int endY) {
		this.left = startX;
		this.top = startY;
		this.right = endX;
		this.bottom = endY;
		this.width = this.right - this.left;
		this.height = this.bottom - this.top;
	}
}
