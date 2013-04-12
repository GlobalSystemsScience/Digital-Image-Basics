package com.LHS.digitalimagebasics;

import java.awt.image.RGBImageFilter;

public class ColorFilter extends RGBImageFilter {
	private int measured_by_camera;
	private int displayed;
	private int keeping;
	private int gap;
	private int count = 0;
	private int shift;
	public ColorFilter() {
		canFilterIndexColorModel = true;
	}
	
	@Override
	public int filterRGB(int x, int y, int rgb) {
		int shiftedValue = 0;
		if (shift == Colors.RIGHT_SHIFT) {
			shiftedValue = (rgb & measured_by_camera) >> gap;
		} else {
			shiftedValue = (rgb & measured_by_camera) << gap;
		}
		return (((rgb & keeping) + 0xFF000000)
				| (shiftedValue )
				| ((rgb & measured_by_camera))
				| (((rgb & displayed) >> 16) >> 16));
					
	}
	
	public void setMeasuresByCamera(int replacing) {
		this.measured_by_camera = replacing;
	}
	public void setDisplayed(int replaced) {
		System.out.println(replaced);
		System.out.println(0xFFFF0000);
		this.displayed = replaced;
	}
	public void setKeeping(int keeping) {
		this.keeping = keeping;
	}
	public void setGap(int gap) {
		this.gap = gap;
	}

	public void setShift(String shiftString) {
		if (shiftString.equals("right")) {
			shift = Colors.RIGHT_SHIFT;
		} else {
			shift = Colors.LEFT_SHIFT;
		}
	}

}
