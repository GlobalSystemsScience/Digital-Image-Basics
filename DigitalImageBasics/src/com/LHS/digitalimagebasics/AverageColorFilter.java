package com.LHS.digitalimagebasics;

import java.awt.image.RGBImageFilter;

public class AverageColorFilter extends RGBImageFilter {
	public AverageColorFilter() {
		canFilterIndexColorModel = true;
	}
	@Override
	public int filterRGB(int x, int y, int rgb) {
		int average = (((rgb & 0xFF0000) >> 16) + ((rgb & 0x00FF00) >> 8) + ((rgb & 0x0000FF))) / 3;
		
		return 0xFF000000 | average << 16 | average << 8 | average;
	}

}
