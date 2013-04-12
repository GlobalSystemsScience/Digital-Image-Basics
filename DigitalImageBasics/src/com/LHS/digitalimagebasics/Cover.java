package com.LHS.digitalimagebasics;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Cover extends JPanel {
	private Point oldStart = new Point();
	private Point oldEnd = new Point();
	private Point startPoint = new Point();
	private Point endPoint = new Point();
	private JLabel imageIcon;
	private int displayedToolIndex;
	public Cover() {
		ImageIcon icon = new ImageIcon(getClass().getResource("/images/crosshair.png"));
		imageIcon = new JLabel(icon);
		imageIcon.setBounds(0,0,imageIcon.getWidth(),imageIcon.getHeight());
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
			switch (displayedToolIndex) {
			case 1:
				if (this.getComponentCount() == 0) {
					this.add(imageIcon);
				}
				imageIcon.setLocation(new Point((int)startPoint.getX()-12, (int)startPoint.getY()-12));
				break;
			case 2:
				if (this.getComponentCount() > 0) {
					this.remove(imageIcon);
				}
				g.drawLine((int)startPoint.getX(), (int)startPoint.getY(), (int)endPoint.getX(), (int)endPoint.getY());
				break;
			case 3:
				if (this.getComponentCount() > 0) {
					this.remove(imageIcon);
				}
				g.drawPolygon(
						new int[] {(int)startPoint.getX(), (int)endPoint.getX(),(int)endPoint.getX(),(int)startPoint.getX()}, 
						new int[] {(int)startPoint.getY(),(int)startPoint.getY(), (int)endPoint.getY(),(int)endPoint.getY()}, 
						4);
				break;
			}

	}

	public Point getOldStart() {
		return oldStart;
	}

	public void setOldStart(Point oldStart) {
		this.oldStart = oldStart;
	}

	public Point getOldEnd() {
		return oldEnd;
	}

	public void setOldEnd(Point oldEnd) {
		this.oldEnd = oldEnd;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

	public int getDisplayedToolIndex() {
		return displayedToolIndex;
	}

	public void setDisplayedToolIndex(int displayedToolIndex) {
		this.displayedToolIndex = displayedToolIndex;
	}

}
