package com.LHS.digitalimagebasics;

import java.awt.image.RGBImageFilter;

public class ColorHider extends RGBImageFilter {
	private int colorToHide;
	public ColorHider() {
		canFilterIndexColorModel = true;
	}
	@Override
	public int filterRGB(int x, int y, int rgb) {
		return ((rgb & (~colorToHide))
				| (rgb & colorToHide) >> 16 >> 16);
	}
	public void setColorToHide(int hidden) {
		colorToHide = hidden;
	}
}
