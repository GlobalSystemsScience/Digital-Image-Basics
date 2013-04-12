package com.LHS.digitalimagebasics;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JLabel;

public class Colors extends InteractiveDisplay {
	public static final int LEFT_SHIFT = 0;
	public static final int RIGHT_SHIFT = 1;
	public static final int MIDDLE = -1;
	private JComboBox[] selectors;
	private static final String[] labels = new String[] {"Red", "Green", "Blue", "None"};
	
	public Colors() {
		super();
		generateInstructions("<html>There are two  parts to a color picture<br>" +
				"displayed on a computer screen: the<br>" +
				"measured intensities of Reg, Green, and<br>" +
				"Blut recorded by a camera and the display<br>" +
				"of these values on the computer screen.<br><br>" +
				"Change which camera goes with<br>" +
				"which computer display color and<br>" +
				"watch the image respond immediately</html>");
		generateImageList(new String[] {"Sample:Color Circles 1",
				"Sample: Color Circles 2",
				"Sample: Sky Color",
				"Sample: Leaf Color",
				"Sample: RGB Color Space",
				"Sample: CMY Color Space"},
				new String[] {"/images/color_circles.png", "/images/color_circles2.png", "/images/sky_color.png", 
				"/images/leaf_color.png", "/images/RGB_color_space.png", "/images/CMY_color_space.png"},
				new String[] {
					"<html>Change the colors of this picture so one yellow<br>circle is displayed on a completely black background.<br> " +
					"How many different ways can this be done?</html>",
					"<html>Change the colors of this picture so one black<br>circle is displayed on a completely white background.<br>How " +
					"many different ways can this be done?</html>",
					"Okay, the sky is blue. But how much red and green are park of the color of a clear sky?",
					"Do green leaves reflect red and blue light too?",
					"Explore how the colors of the Red-Green-Blue color space may be manipulated",
					"Explore how the colors of the Cyan-Magenta-Yellow color space may be manipulated",
					"Your Picture"}
		);
		this.addHelpText(true);
		addImage("/images/color_circles.png");
		addCaption("<html>Change the colors of this picture so one yellow<br>circle is displayed on a completely black background.<br> " +
		"How many different ways can this be done?</html>");
		addChangeColors();
		setUpPixelValue("Red", "Green", "Blue");
		//this.setUpPixelValue();
	}
	/*
	@Override
	protected void handlePixelValue(int pixel) {
		
	}*/
	private void addChangeColors() {
		Container container = new Container();
		GridLayout layout = new GridLayout(4, 3);
		container.setLayout(layout);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridheight = 1;
		
		JLabel colorLabel = new JLabel("<html>Color Measured<br> by Camera</html>", JLabel.CENTER);
		JLabel empty = new JLabel("");
		JLabel displayLabel = new JLabel("Displayed Color", JLabel.CENTER);
		
		container.add(colorLabel);
		container.add(empty);
		container.add(displayLabel);
		
		
		selectors = new JComboBox[] {new JComboBox(labels), new JComboBox(labels), new JComboBox(labels)};
		
		ActionListener selectorListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				switch (Integer.parseInt(((JComboBox)arg0.getSource()).getName())) {
				case 0:				//Red label
					switchChannel(labels[0].toLowerCase(), (String)((JComboBox)arg0.getSource()).getSelectedItem());
					break;
				case 1:
					switchChannel(labels[1].toLowerCase(), (String)((JComboBox)arg0.getSource()).getSelectedItem());
					break;
				case 2:
					switchChannel(labels[2].toLowerCase(), (String)((JComboBox)arg0.getSource()).getSelectedItem());
					break;
				}
			}
		};

		System.out.println("HERE " + labels.length);
		JLabel[] separators = new JLabel[] {new JLabel("---"), new JLabel("---"), new JLabel("---")};
		
		for (int i = 0; i < labels.length-1; i++) {
			System.out.println("Adding new line");
			
			selectors[i].setSelectedIndex(i);
			selectors[i].setName("" + i);
			selectors[i].addActionListener(selectorListener);
			
			container.add(selectors[i]);
			container.add(separators[i]);
			container.add(new JLabel(labels[i]));
		}
		

		this.addToBody(container, constraints);
	}

	protected void switchChannel(String toReplace, String replacing) {
		if (toReplace.equals(replacing.toLowerCase())) { //Nothing to edit, reload the initial picture
			
			this.changePicture(this.getRelURL());
			this.revalidateContainer();
			reloadChanges();
			return;
		}
		
//		BufferedImage toEdit = getBufferedImage();
//		FilteredImageSource filteredSrc;
//		

//			ColorFilter filter = new ColorFilter();
//			
//			if (toReplace.toLowerCase().equals(labels[0].toLowerCase())) {
//				filter.setDisplayed(0xFF0000);
//				setOthers(filter, replacing, labels[1].toLowerCase(), 0x00FF00, 0x0000FF, 8, 16, LEFT_SHIFT);
//			} else if (toReplace.toLowerCase().equals(labels[1].toLowerCase())) {
//				filter.setDisplayed(0x00FF00);
//				setOthers(filter, replacing, labels[0].toLowerCase(), 0xFF0000, 0x0000FF, 8, 8, MIDDLE);
//			} else {
//				filter.setDisplayed(0x0000FF);
//				setOthers(filter, replacing, labels[0].toLowerCase(), 0xFF0000, 0x00FF00, 16, 8, RIGHT_SHIFT);
//			}
		BufferedImage toEdit = null;
		try {
			toEdit = getOriginalBufferedImage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FilteredImageSource filteredSrc = null;
		ColorSwapper filter = null;
//		if (replacing.toLowerCase().equals(labels[3].toLowerCase())) {
//			//filter = hideColor(toReplace);
//			//filteredSrc = new FilteredImageSource(toEdit.getSource(), filter);
//			
//		} else {
			filter = new ColorSwapper();
			int[] redShift = {-1, -1, -1}, greenShift = {-1, -1, -1}, blueShift = {-1, -1, -1};
			int redShiftIndex = 0, greenShiftIndex = 0, blueShiftIndex = 0;
			boolean[] greenLeftShift = new boolean[3];
			boolean[] hideRed = {false, false, false}, hideGreen = {false, false, false}, hideBlue = {false, false, false};
			for (int i =0; i < 3; i++ ) {
				if (((String)selectors[i].getSelectedItem()).toLowerCase().equals("red")) {
					
					redShift[redShiftIndex] = getShift("red", labels[i]);
					
					System.out.println("Adding to red shifts: " + redShift[redShiftIndex]);
					redShiftIndex++;
				} else if (((String)selectors[i].getSelectedItem()).toLowerCase().equals("green")) {
					greenShift[greenShiftIndex] = getShift("green", labels[i]);
					if (greenShift[greenShiftIndex] == -8) {
						greenLeftShift[greenShiftIndex] = true;
						greenShift[greenShiftIndex] = 8;
					}
					
					System.out.println("Adding to green shifts: " + greenShift[greenShiftIndex] + " " + greenLeftShift[greenShiftIndex]);
					greenShiftIndex++;

				} else if (((String)selectors[i].getSelectedItem()).toLowerCase().equals("blue")) {
					blueShift[blueShiftIndex] = getShift("blue", labels[i]);
					System.out.println("Adding to blue shifts: " + blueShift[blueShiftIndex]);
					blueShiftIndex++;
				} else {
					System.out.println("HIDING A COLOR");
					if (labels[i].equals("Red")) {
						hideRed[redShiftIndex] = true;
						redShiftIndex++;
					} else if (labels[i].equals("Green")) {
						hideGreen[greenShiftIndex]=true;
						greenShiftIndex++;
					} else {
						hideBlue[blueShiftIndex]=true;
						blueShiftIndex++;
					}
				}
			}
			filter.setBlueColorShift(blueShift);
			filter.setGreenColorShift(greenShift);
			filter.setRedColorShift(redShift);
			filter.setGreenLeftShift(greenLeftShift);
			filter.setHideRed(hideRed);
			filter.setHideGreen(hideGreen);
			filter.setHideBlue(hideBlue);

		filteredSrc = new FilteredImageSource(toEdit.getSource(), filter);
		
		
		Image img = Toolkit.getDefaultToolkit().createImage(filteredSrc);

		this.changePicture(img);
		
//		redShift = getShift("red", );
//		if (redShift == -1) {
//			filter.setHideRed(true);
//		} else {
//			filter.setRedColorShift(redShift);
//		}
//	greenShift = getShift("green", (String)selectors[1].getSelectedItem());
//	if (greenShift == -1) {
//		filter.setHideGreen(true);
//	} else {
//		if (greenShift < 0) {
//			filter.setGreenLeftShift(true);
//			filter.setGreenColorShift(-greenShift);
//		} else {
//			filter.setGreenColorShift(greenShift);
//		}
//	}
//	
//	blueShift = getShift("blue", (String)selectors[2].getSelectedItem());
//	if (blueShift == -1) {
//		filter.setHideBlue(true);
//	} else {
//		filter.setBlueColorShift(blueShift);
//	}
//	if (replacing.toLowerCase().equals(labels[0].toLowerCase())) {
//		System.out.println("REDFILTER");
//		filter = new RedFilter();
//		if (toReplace.equals(labels[1].toLowerCase())) {
//			
//			((RedFilter)filter).setDisplayAs(0x00FF00);
//		} else {
//			((RedFilter)filter).setDisplayAs(0x0000FF);
//		}
//	} else if (replacing.toLowerCase().equals(labels[1].toLowerCase())) {
//		filter = new GreenFilter();
//		if (toReplace.equals(labels[0].toLowerCase())) {
//			
//			((GreenFilter)filter).setDisplayAs(0xFF0000);
//		} else {
//			((GreenFilter)filter).setDisplayAs(0x0000FF);
//		}
//	} else {
//		filter = new BlueFilter();
//		if (toReplace.equals(labels[0].toLowerCase())) {
//			
//			((BlueFilter)filter).setDisplayAs(0xFF0000);
//		} else {
//			((BlueFilter)filter).setDisplayAs(0x00FF00);
//		}
//	}
	}

	private int getShift(String from, String selectedItem) {
		if (selectedItem.toLowerCase().equals("none")) {
			return -1;
		}
		if (from.toLowerCase().equals("red")) {
			if (selectedItem.toLowerCase().equals("red")) {
				return 0;
			} else if (selectedItem.toLowerCase().equals("green")) {
				return 8;
			}  else
				return 16;
		} else if (from.toLowerCase().equals("green")) {
			if (selectedItem.toLowerCase().equals("red")) {
				return -8;
			} else if (selectedItem.toLowerCase().equals("green")) {
				return 0;
			}  else
				return 8;
			
		} else {
			if (selectedItem.toLowerCase().equals("red")) {
				return 16;
			} else if (selectedItem.toLowerCase().equals("green")) {
				return 8;
			}  else
				return 0;
		}
	}
	private void reloadChanges() {
		for (int i = 0; i < selectors.length; i++) {
			if (!((String)selectors[i].getSelectedItem()).equals(labels[i])) {
				switchChannel(labels[i], (String)selectors[i].getSelectedItem());
			}
		}
		
	}

	private ImageFilter hideColor(String toReplace) {
		ColorHider filter = new ColorHider();
		int hide;
		if (toReplace.toLowerCase().equals(labels[0].toLowerCase())) {
			hide = 0xFF0000;
		} else if (toReplace.toLowerCase().equals(labels[1].toLowerCase())) {
			hide = 0x00FF00;
		} else {
			hide = 0x0000FF;
		}
	
		filter.setColorToHide(hide);
		return filter;
		
	}

	private void setOthers(ColorFilter filter, String replacing, String test, int testValue, int otherValue, int testGap, int otherGap, int shift) {

		boolean set = false;
		if (shift == LEFT_SHIFT) {
			filter.setShift("left");
		} else if (shift == RIGHT_SHIFT) {
			filter.setShift("right");
		} else {
			if (replacing.toLowerCase().equals(labels[2].toLowerCase())) {
				filter.setShift("left");
			} else {
				filter.setShift("right");
			}
		}
		if (replacing.toLowerCase().equals(test)) {
			filter.setMeasuresByCamera(testValue);
			filter.setKeeping(otherValue);
			filter.setGap(testGap);
		} else {
			filter.setMeasuresByCamera(otherValue);
			filter.setKeeping(testValue);
			filter.setGap(otherGap);
		}
		
	}
	@Override
	protected void changePictureHandler() {
		// TODO Auto-generated method stub
		
	}

}
