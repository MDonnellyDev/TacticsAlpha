package com.tacalpha.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AssetLoader {
	public static Graphics2D FONT = AssetLoader.loadBitmap("/ui/font.png");

	public static Graphics2D loadBitmap(String fileName) {
		try {
			BufferedImage img = ImageIO.read(AssetLoader.class.getResource(fileName));

			int w = img.getWidth();
			int h = img.getHeight();

			int[] rgb = img.getRGB(0, 0, w, h, new int[w * h], 0, w);
			for (int i = 0; i < rgb.length; i++) {
				int in = rgb[i];
				int col = (in & 0xf) >> 2;
				if (in == 0xffff00ff) {
					col = -1;
				}
				rgb[i] = col;
			}
			return img.createGraphics();
		} catch (IOException e) {
			return null;
		}
	}
}
