package com.tacalpha.menu;

import java.util.ArrayList;
import java.util.List;

public abstract class Menu {
	private List<String> menuOptions;
	private int currentOption;

	public Menu() {
		this.menuOptions = new ArrayList<String>();
		this.currentOption = 0;
	}

	// TODO: Make this return something more specific.
	public abstract Object choose();

	public void up() {
		this.currentOption--;
		if (this.currentOption < 0) {
			this.currentOption = this.menuOptions.size() - 1;
		}
	}

	public void down() {
		this.currentOption++;
		if (this.currentOption >= this.menuOptions.size()) {
			this.currentOption = 0;
		}
	}

	public int getCurrent() {
		return this.currentOption;
	}

	public List<String> getOptionTitles() {
		return this.menuOptions;
	}

	protected void addOption(String option) {
		this.menuOptions.add(option);
	}
}
