package com.LHS.digitalimagebasics;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class ResolutionChanger {
	private int oldWidth;
	private BufferedImage image;
	public ResolutionChanger(BufferedImage img) {
		image = img;
		oldWidth = image.getWidth();
	}
	
	public Image decreaseResolution(int newNumPixels) {
		int[] fPixels = new int[newNumPixels*newNumPixels*3];
		int sizeBox = oldWidth/newNumPixels;
		int index = 0;
		Raster rast = image.getData();
		System.out.println(oldWidth + " " + sizeBox);
		for (int c = 0; c < oldWidth; c+= sizeBox) {
			for (int r = 0; r < oldWidth; r+= sizeBox) {
				int[] pixels = new int[sizeBox*sizeBox*3];
				pixels = rast.getPixels(r, c, sizeBox, sizeBox, pixels);
				int[] thisPixel = new int[3];
				thisPixel = getAveragePixelValue(pixels);
				fPixels[index] = thisPixel[0];
				index++;
				fPixels[index] = thisPixel[1];
				index++;
				fPixels[index] = thisPixel[2];
				index++;
			}
		}

		return getImageFromArray(fPixels, newNumPixels, newNumPixels);
	}
	private static int[] getAveragePixelValue(int[] pixels) {
		int totalRed = 0, totalGreen = 0, totalBlue = 0;
		int totalPixels = pixels.length/3;
		for (int k = 0; k < pixels.length; k += 3) {
			totalRed += pixels[k];
			totalGreen += pixels[k+1];
			totalBlue += pixels[k+2];
		}
		return new int[] {totalRed/totalPixels, totalGreen/totalPixels, totalBlue/totalPixels};
	}
	public static Image getImageFromArray(int[] pixels, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = (WritableRaster) image.getData();

        raster.setPixels(0,0,width,height,pixels);
        image.setData(raster);
        return image;
    }
}
