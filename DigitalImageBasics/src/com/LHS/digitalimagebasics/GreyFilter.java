package com.LHS.digitalimagebasics;

import java.awt.image.RGBImageFilter;

public class GreyFilter extends RGBImageFilter {
	private int color;
	private int gap1;
	private int gap2;
	public GreyFilter() {
		canFilterIndexColorModel = true;
	}
	@Override
	public int filterRGB(int x, int y, int rgb) {
		//return ((this.color << 16) | this.color << 8 | this.color);
		int second, third;
		if (gap1 < 0) 
			second = (rgb & color) << (-gap1);
		else 
			second = (rgb & color) >> gap1;
		if (gap2 < 0)
			third = (rgb & color) << (-gap2);
		else
			third = (rgb & color) >> gap2;
		
		return (rgb & (color+0xFF000000)) | second | third;

		//return ((rgb & 0xFFFF0000) | (rgb & 0xFF0000) >> 8 | (rgb & 0xFF0000) >> 16);
	}
	public void setColor(int color) {
		
		this.color = color;
		if (this.color == 0xFF0000) {
			gap1 = 8;
			gap2 = 16;
		}
		else if (this.color == 0x00FF00) {
			gap1 = -8;
			gap2 = 8;
		}
		else {
			gap1 = -16;
			gap2 = -8;
		}
		/*while (this.color > 255) {
			this.color = this.color >> 8;
		}*/
		
	}
	

}
