package com.LHS.digitalimagebasics;

import java.awt.image.RGBImageFilter;

public class ColorVsColor extends RGBImageFilter {
	private int color1;
	private int color2;
	private int shift1;
	private int shift2;
	private boolean normalize = false;
	
	public ColorVsColor() {
		canFilterIndexColorModel = true;
	}
	@Override
	public int filterRGB(int x, int y, int rgb) {
		int diff = ((rgb & color1) >> shift1) - ((rgb & color2) >> shift2);
		
		if (normalize) {
			int divisor =((rgb & color1) >> shift1) + ((rgb & color2) >> shift2); 

			double temp;
			temp = (double)diff / divisor; 
			diff = (int)(temp * 255);
		}
		if (diff > 0) {
			return (0xFF000000+ (diff << shift1)); 
		} else {
			diff = Math.abs(diff);

			return (0xFF000000+ (diff << shift2));
		}
		
	}
	public void setColors(int one, int two, int shift1, int shift2) {
		color1 = one;
		color2 = two;
		this.shift1 = shift1;
		this.shift2 = shift2;
	}
	public void setNormalization(boolean value) {
		normalize = value;
	}

}
