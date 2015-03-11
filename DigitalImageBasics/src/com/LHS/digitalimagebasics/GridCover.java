package com.LHS.digitalimagebasics;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GridCover extends JPanel {
	private static final int WIDTH = 512;
	
	private int numLines = -1;
	private int currentNumLines;
	
	public GridCover() {
	}
	@Override
	protected void paintComponent(Graphics g) {
		if (numLines != -1) {
			System.out.println("Painting");
			
			if (numLines < currentNumLines) {	//We have to clear the grid
				g.clearRect(0, 0, WIDTH, WIDTH);
			}
			int gap = 512/numLines;
			g.setColor(Color.white);
			for (int i=0; i < 512; i+= gap) {
				//System.out.println("Making lines");
				g.drawLine(i, 0, i, 512);
				g.drawLine(0, i, 512, i);
			}
		}
	
	}
	public void setNumLines(int num) {
		this.numLines = num;
	}
	

}
