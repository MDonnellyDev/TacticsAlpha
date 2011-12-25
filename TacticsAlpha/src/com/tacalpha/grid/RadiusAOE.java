package com.tacalpha.grid;

public class RadiusAOE extends AreaOfEffect {
	private int radius;

	public RadiusAOE(int radius) {
		this.radius = radius - 1;
	}

	@Override
	protected void populateLocations() {
		for (int x = -this.radius; x <= this.radius; x++) {
			int leftover = this.radius - Math.abs(x);
			for (int y = -leftover; y <= leftover; y++) {
				this.relativeLocations.add(new GridPoint(x, y));
			}
		}
	}
}
