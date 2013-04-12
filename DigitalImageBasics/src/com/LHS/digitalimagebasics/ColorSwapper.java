package com.LHS.digitalimagebasics;

import java.awt.image.RGBImageFilter;

public class ColorSwapper extends RGBImageFilter {
	private static int RED = 0xFF0000;
	private static int GREEN = 0x00FF00;
	private static int BLUE = 0x0000FF;
	
	private int redDisplayColor;
	private int greenDisplayColor;
	private int blueDisplayColor;
	
	private int[] redColorShift;
	private int[] greenColorShift;
	private int[] blueColorShift;
	
	private boolean[] hideRed;
	private boolean[] hideGreen;
	private boolean[] hideBlue;
	
	private int count = 0;

	private boolean[] greenLeftShift;
	
	@Override
	public int filterRGB(int x, int y, int rgb) {
		int red=0, green = 0, blue=0;
		
			for (int i=0; i < greenColorShift.length; i++) {
				if (!hideGreen[i]) {
					if (greenColorShift[i] != -1) {
						if (greenLeftShift[i]) {
							green += (rgb & GREEN) << greenColorShift[i];
						} else {
							green += (rgb & GREEN) >> greenColorShift[i];
						}
					}
				}
			}
		
			for (int i=0; i < redColorShift.length; i++) {
				if (!hideRed[i]) {
					if (redColorShift[i] != -1)
						red += ((rgb & RED) >> redColorShift[i]);
				}
			}
		
			for (int i=0; i < blueColorShift.length; i++) {
				if (!hideBlue[i]) {
					if (blueColorShift[i] != -1)
						blue += ((rgb & BLUE) << blueColorShift[i]);
				}
			}
		if (count % 1000 == 0) {
			System.out.println(red + " " + green + " " + blue);
		}
		count++;
		return ( red | green | blue | (rgb & 0xFF000000));
	}
	
	public void setRedDisplayColor(int redDisplayColor) {
		this.redDisplayColor = redDisplayColor;
	}
	public void setGreenDisplayColor(int greenDisplayColor) {
		this.greenDisplayColor = greenDisplayColor;
	}
	public void setBlueDisplayColor(int blueDisplayColor) {
		this.blueDisplayColor = blueDisplayColor;
	}

	public void setRedColorShift(int[] redColorShift) {
		this.redColorShift = redColorShift;
	}

	public void setGreenColorShift(int[] greenColorShift) {
		this.greenColorShift = greenColorShift;
	}

	public void setBlueColorShift(int[] blueColorShift) {
		this.blueColorShift = blueColorShift;
	}

	public void setGreenLeftShift(boolean[] greenLeftShift) {
		this.greenLeftShift = greenLeftShift;
	}
	public void setHideRed(boolean[] hideRed) {
		this.hideRed = hideRed;
	}

	public void setHideGreen(boolean[] hideGreen) {
		this.hideGreen = hideGreen;
	}

	public void setHideBlue(boolean[] hideBlue) {
		this.hideBlue = hideBlue;
	}
	

}
