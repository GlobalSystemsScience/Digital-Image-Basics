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

public class FalseColor extends InteractiveDisplay {
	public static final int LEFT_SHIFT = 0;
	public static final int RIGHT_SHIFT = 1;
	public static final int MIDDLE = -1;
	private JComboBox[] selectors;
	private static final String[] labels = new String[] {"Infrared", "Red", "Green", "None"};
	
	public FalseColor() {
		super();
		generateInstructions("<html>Light invisible to  the human eye may be<br>" +
				"measured with modern sensors. To help us<br>" +
				"use the information, the data are displayed<br>" +
				"using the red, green, and blue color layers in a<br>" +
				"digital image.<br><br>" +
				"These false color images are used by<br>" +
				"scientists, engineers, and artists in many<br>" +
				"career fields");
		generateImageList(new String[] {
				"Sample: Satellite Image 1",
				"Sample: Satellite Image 2",
				"Sample: Satellite Image 3",
				"Sample: Satellite Image 4",
				"Sample: Satellite Image 5",
				"Sample: Satellite Image 6",
				"Sample: Cloud motion",
				"Sample: Before/After photo",
			},
			new String[] {"/images/satellite_1.png", "/images/satellite_2.png","/images/satellite_3.png", 
				"/images/satellite_4.png","/images/satellite_5.png", "/images/satellite_6.png",
				"/images/cloud_motion.png", "/images/before_after.png"
			},
			new String[] {"<html>This is how the world would look if you were taking a photo with a Landsat satellite. " +
					"Do leaves<br> reflect much near infrared light?</html>",
					"<html>This is how the world would look if you were taking a photo with a Landsat satellite. " +
					"Does water<br> reflect much near infrared light?</html>",
					"<html>This is how the world would look if you were taking a photo with a Landsat satellite. " +
					"Do cement<br> and blacktop reflect near infrared?</html>",
					"Is there a lake in this image?",
					"What is the relationship of viewing plants in near infrared and red light?",
					"To study Earth's landcover from satellite, do near infrared images provide all of the information you need?",
					"Which way are the clouds moving?",
					"<html>In this combined set of before and after images, objects that are green or magenta have been moved during<br> the time " +
					"to take the photos. >Use the utility tools in the AnalyzingDigitalImages software to create your own images<br> to see how " +
					"an area changes over time</html>",
					"Your Picture"}
		);
		this.addHelpText(true);
		addImage("/images/satellite_1.png");
		this.addCaption("This is how the world would look if you were taking a photo with a Landsat satellite. " +
				"Do leaves reflect much near infrared light?");
		addChangeColors();
		setUpPixelValue("Infrared", "Red", "Green");
	}

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
				case 0:				
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
		String[] colors = new String[] {"Red", "Green", "Blue"};
		for (int i = 0; i < labels.length-1; i++) {
			System.out.println("Adding new line");
			
			selectors[i].setSelectedIndex(i);
			selectors[i].setName("" + i);
			selectors[i].addActionListener(selectorListener);
			
			container.add(selectors[i]);
			container.add(separators[i]);
			container.add(new JLabel(colors[i]));
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
		
		BufferedImage toEdit = null;
		try {
			toEdit = getOriginalBufferedImage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FilteredImageSource filteredSrc = null;
		ColorSwapper filter = null;

			filter = new ColorSwapper();
			int[] redShift = {-1, -1, -1}, greenShift = {-1, -1, -1}, blueShift = {-1, -1, -1};
			int redShiftIndex = 0, greenShiftIndex = 0, blueShiftIndex = 0;
			boolean[] greenLeftShift = new boolean[3];
			boolean[] hideRed = {false, false, false}, hideGreen = {false, false, false}, hideBlue = {false, false, false};
			for (int i =0; i < 3; i++ ) {
				if (((String)selectors[i].getSelectedItem()).toLowerCase().equals("infrared")) {
					
					redShift[redShiftIndex] = getShift("infrared", labels[i]);
					
					System.out.println("Adding to red shifts: " + redShift[redShiftIndex]);
					redShiftIndex++;
				} else if (((String)selectors[i].getSelectedItem()).toLowerCase().equals("red")) {
					greenShift[greenShiftIndex] = getShift("red", labels[i]);
					if (greenShift[greenShiftIndex] == -8) {
						greenLeftShift[greenShiftIndex] = true;
						greenShift[greenShiftIndex] = 8;
					}
					
					System.out.println("Adding to green shifts: " + greenShift[greenShiftIndex] + " " + greenLeftShift[greenShiftIndex]);
					greenShiftIndex++;

				} else if (((String)selectors[i].getSelectedItem()).toLowerCase().equals("green")) {
					blueShift[blueShiftIndex] = getShift("green", labels[i]);
					System.out.println("Adding to blue shifts: " + blueShift[blueShiftIndex]);
					blueShiftIndex++;
				} else {
					System.out.println("HIDING A COLOR");
					if (labels[i].equals("Infrared")) {
						hideRed[redShiftIndex] = true;
						redShiftIndex++;
					} else if (labels[i].equals("Red")) {
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
		
	}

	private int getShift(String from, String selectedItem) {
		if (selectedItem.toLowerCase().equals("none")) {
			return -1;
		}
		if (from.toLowerCase().equals("infrared")) {
			if (selectedItem.toLowerCase().equals("infrared")) {
				return 0;
			} else if (selectedItem.toLowerCase().equals("red")) {
				return 8;
			}  else
				return 16;
		} else if (from.toLowerCase().equals("red")) {
			if (selectedItem.toLowerCase().equals("infrared")) {
				return -8;
			} else if (selectedItem.toLowerCase().equals("red")) {
				return 0;
			}  else
				return 8;
			
		} else {
			if (selectedItem.toLowerCase().equals("infrared")) {
				return 16;
			} else if (selectedItem.toLowerCase().equals("red")) {
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
