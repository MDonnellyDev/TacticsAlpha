package com.tacalpha.grid;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GridLoader {
	private GridLoader() {
	}

	public static Tile[][] getTiles(String filePath) {
		// Set up the input, which Java makes require a stupid number of
		// classes.
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Could not load map " + filePath + ". Could not find file.", e);
		}
		InputStreamReader streamReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(streamReader);

		// Read the file, saving the lines for future processing.
		List<String> lines = new ArrayList<String>();
		try {
			String currentLine = reader.readLine();
			while (currentLine != null) {
				lines.add(currentLine);
				currentLine = reader.readLine();
			}
		} catch (IOException e) {
			throw new RuntimeException("Could not load map " + filePath + ".", e);
		}

		// Make sure all the lines are the same size.
		int lineSize = lines.get(0).length();
		for (String line : lines) {
			if (line.length() != lineSize) {
				throw new IllegalStateException("Could not load map " + filePath + ". Grids must be rectangular.");
			}
		}

		// Add all the relevant Tiles and set up their initial state.
		Tile[][] ret = new Tile[lines.size()][lineSize];
		for (int y = 0; y < lines.size(); y++) {
			String line = lines.get(y);
			for (int x = 0; x < lineSize; x++) {
				ret[y][x] = new Tile();
				switch (line.charAt(x)) {
					case 'x':
						ret[y][x].setImpassable(true);
						break;
					default:
						break;
				}
			}
		}
		return ret;
	}
}
