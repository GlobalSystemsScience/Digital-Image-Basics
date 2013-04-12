package com.LHS.digitalimagebasics;

import java.awt.image.RGBImageFilter;

public class BlueFilter extends RGBImageFilter {

	private int displayAs;
	private int untouched;
	private int shift;
	
	public BlueFilter() {
		canFilterIndexColorModel = true;
	}
	
	@Override
	public int filterRGB(int x, int y, int rgb) {
		
		return ((rgb & 0x0000FF) | ((rgb & 0x0000FF) << shift) | (rgb & untouched) | (rgb & 0xFF000000));
	}
	public void setDisplayAs(int color) {
		displayAs = color;
		if (displayAs == 0x00FF00) {
			shift = 8;
			untouched = 0xFF0000;
		} else {
			untouched = 0x00FF00;
			shift = 16;
		}
	}

}
