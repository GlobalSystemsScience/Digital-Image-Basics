package com.LHS.digitalimagebasics;

import java.awt.image.RGBImageFilter;

public class GreenFilter extends RGBImageFilter {

	private int displayAs;
	private int untouched;
	private int shift;
	private boolean left;
	public GreenFilter() {
		canFilterIndexColorModel = true;
	}
	
	@Override
	public int filterRGB(int x, int y, int rgb) {
		if (left) {
			return ((rgb & 0x00FF00) | ((rgb & 0x00FF00) << shift) | (rgb & untouched) | (rgb & 0xFF000000));
		} else {
			return ((rgb & 0x00FF00) | ((rgb & 0x00FF00) >> shift) | (rgb & untouched) | (rgb & 0xFF000000));
		}
	}
	public void setDisplayAs(int color) {
		displayAs = color;
		if (displayAs == 0xFF0000) {
			shift = 8;
			left = true;
			untouched = 0x0000FF;
		} else {
			untouched = 0xFF0000;
			left = false;
			shift = 8;
		}
	}

}
