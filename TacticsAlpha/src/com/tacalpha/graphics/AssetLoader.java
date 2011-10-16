package com.tacalpha.graphics;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class AssetLoader {
	public static Bitmap FONT = AssetLoader.loadBitmap("/ui/font.png");

	public static Bitmap loadBitmap(String fileName) {
		try {
			BufferedImage img = ImageIO.read(AssetLoader.class.getResource(fileName));

			int w = img.getWidth();
			int h = img.getHeight();

			Bitmap result = new Bitmap(w, h);
			img.getRGB(0, 0, w, h, result.pixels, 0, w);
			for (int i = 0; i < result.pixels.length; i++) {
				int in = result.pixels[i];
				int col = (in & 0xf) >> 2;
				if (in == 0xffff00ff) {
					col = -1;
				}
				result.pixels[i] = col;
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
