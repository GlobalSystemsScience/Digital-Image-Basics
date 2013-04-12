package com.LHS.digitalimagebasics;

import java.awt.image.RGBImageFilter;

public class KeepOneColorFilter extends RGBImageFilter {
	private int colorKeep;
	public KeepOneColorFilter() {
		canFilterIndexColorModel = true;
	}
	@Override
	public int filterRGB(int x, int y, int rgb) {
		return rgb & (colorKeep + 0xFF000000);
	}
	public void setColor(int color) {
		this.colorKeep = color;
	}
}
