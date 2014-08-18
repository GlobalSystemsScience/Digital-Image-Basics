package com.LHS.digitalimagebasics;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Pixels extends InteractiveDisplay {
	private JLabel numPixels;
	private String[] pixelOptions = new String[] {"No Borders", "2 x 2", "4 x 4", "8 x 8", "16 x 16", "32 x 32", "64 x 64", "128 x 128", "256 x 256", "512 x 512"};
	private int[] resolutionSelections = new int[] {2, 4, 8, 16, 32, 64, 128, 256, 512};
	private int resolution = 0;				//default
	
	private final JButton decrease = new JButton("Decrease Resolution");
	private final JButton increase = new JButton("Increase Resolution");
	
	private GridCover cover;
	
	private int oldIndex = 0;
	private boolean answer = false;
	private boolean redo = false;
	public Pixels() {
		super();
		//this.setResize(512);
		generateInstructions("<html>A digital image is made up of tiny tiles of color called pixels<br><br>" +
				"Select a picture, and it will be displayed with 4 very large<br>pixels. Increase the resolution by using a greater number<br>" +
				"of smaller pixels<br><br>" +
				"Move the cursor across the image to see the color of<br>" +
				"highlighted pixels");
		generateImageList(new String[] {
				"Mystery Picture 1", 
				"Mystery Picture 2",
				"Mystery Picture 3",
				"Mystery Picture 4",
				"Mystery Picture 5",
				"Mystery Picture 6",
				"Mystery Picture 7",
				"Mystery Picture 8",
				"Mystery Picture 9",
//				"Mystery Picture 10"
			},
			new String[] {"/images/mystery_1.png", "/images/mystery_2.png","/images/mystery_3.png", 
//				"images//mystery_4.png",
				"/images/mystery_5.png", 
				"/images/mystery_6.png","/images/mystery_7.png", "/images/mystery_8.png","/images/mystery_9.png", "/images/mystery_10.png"},
			new String[] {
				"Home Sweet Home",
				"<html>Monument commemorating the first major battle <br>of the American Revolution. This IKONOS satellite image has a resolution<br>" +
				"of 1 meter, so this image covers 512 m x 512 m <br>of the Earth's surface. Coompare this detail to what a MODIS<br>" +
				"(256 m resolution) or Landsat (30 m resolution) satellite sees</html>",
				"Something only tall animals can do",
//				"<html>A fun place to explore science, technology, and engineering.<br> This IKONOS satellite image has a resolution of 1 meter,<br> " +
//				"so this image covers 512 m x 512 m of the Earth's surface. <br>Compare this detail to what a MODIS (256 m resolution) or <br>" +
//				"Landsat (30 m resolution) satellite sees</html>",
				"You'll never see a cold one fly",
				"The making of home sweet home",
				"Animals that took over a human-built environment for their home",
				"A bird that doesn't ten to inspire a lucky feeling",
				"A bird of prey that hunts at night",
				"Forests are home to so many plants and animals",
				"Your Picture"},
			new String[] {
				"<html>Earth photographed by astronaut Harrison Schmitt aboard the Apollo 17<br>spacecraft on December 9, 1972. This was the last " +
				"time humans traveled to the<br>moon. Photograph courtesy of NASA.</html>",
				"<html>Bunket Hill Monument, located in Charlestown, Massachusetts, is 221 tall. Image<br>provided by UNH's EOS-WEBSTER</html>",
				"<html>Not many animals are tall enough to stick their head out of a car sun roof. And<br>even fewe would enjoy the experience when " +
				"the car is moving!",
//				"<html>The Museum of Science in Boston, Massachusetts. The building actually extends<br>across the Charles River, so part of the building " +
//				"is in Cambridge, and part in<br>Boston. Image provided by UNH's EOS-WEBSTER</html>",
				"<html>Hundreds of hot air balloons take off near Albuquerque, New Mexico during Mass<br> Ascension held in early October</html>",
				"Construction site of a new home.",
				"<html>The sea lions of Fisherman's Wharf, Pier 39, San Francisco, California, moved in<br>following teh 1989 earthquake. " +
				"They spend roughly 10 months of the year<br> sleeping and sunning on the docks, and then head south to the Channel Islands<br>" +
				"near Santa Barbara to breed during the summer.",
				"<html>Turkey vultures earned their name because their red, featherless heads are<br>similar to turkeys. Turkey vultures are one " +
				"of the few North America birds with a<br>sense of smell, and they also have keen eyesight. Circling vultures may be<br>looking for food, " +
				"have spotted a recently killed animal, or are just playing!",
				"<html>The Eastern Screech Owl hunts a wide range of small animals from dusk to dawn,<br>with most activity being done during the " +
				"first four hours of the night</html>",
				"Enjoy the beauty of your local plant and wildlife!",
				"Your Picture"}
		);
		
		this.addHelpText(false);
		setUpPixelValue("Red", "Green", "Blue");
		
		
	}
	
	public void init() {
		addImage("/images/mystery_1.png");
		
		addCaption("Home Sweet Home");
		addResolutionOptions();
		
		this.changeResolution(this.resolutionSelections[resolution]);
		
		this.addCover();
	}
	private void addCover() {
		cover = new GridCover();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridheight = 4;
		cover.setPreferredSize(new Dimension(512, 512));
		cover.setBounds(getImageBounds());
		
		cover.setOpaque(false);
		this.addToLayeredPane(cover, new Integer(2), -1);
		this.bringToFront(cover);
		this.revalidateContainer();
	}
	
	private void addResolutionOptions() {
		Container resolutionOptions = new Container();
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridx = 0;
		constraints.gridy = 2;
		
		resolutionOptions.setLayout(new GridLayout(3, 2));
		
		decrease.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resolution--;
				changeResolution(resolutionSelections[resolution]);
			}
		});
		increase.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resolution++;
				changeResolution(resolutionSelections[resolution]);
			}
		});
		JLabel rowLabel = new JLabel("Number of Pixels");
		numPixels = new JLabel("");
		JLabel borders = new JLabel("Show Pixel Borders");
		
		JComboBox bordersSelector = new JComboBox(pixelOptions);
		
		bordersSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JComboBox border = (JComboBox) arg0.getSource();
				drawGrid(border.getSelectedIndex());
				if (redo) {
					drawGrid(border.getSelectedIndex());
					
				}
			}
		});
		
		resolutionOptions.add(decrease);
		resolutionOptions.add(increase);
		resolutionOptions.add(rowLabel);
		resolutionOptions.add(numPixels);
		resolutionOptions.add(borders);
		resolutionOptions.add(bordersSelector);
		
		
		this.addToBody(resolutionOptions, constraints);
	}
	private void drawGrid(int selectedIndex) {
		System.out.println("Drawing");
		if (selectedIndex == 0) {
			cover.setNumLines(-1);
		} else {
			selectedIndex--;
			cover.setNumLines(resolutionSelections[selectedIndex]);
		}
		
		cover.repaint();
	}

	private void changeResolution(int res) {
		if (resolution == resolutionSelections.length-1) {
			increase.setEnabled(false);
			decrease.setEnabled(true);
			answer = true;
		} else if (resolution == 0) {
			decrease.setEnabled(false);
			increase.setEnabled(true);
			answer = false;
		} else {
			increase.setEnabled(true);
			decrease.setEnabled(true);
			answer = false;
		}
		if (answer) {
			this.displayAnswer();
		} else {
			this.hideAnswer();
		}
		try {
			BufferedImage image = Pixels.this.getOriginalBufferedImage();
			ResolutionChanger resChange = new ResolutionChanger(image);
			
			Image img = resChange.decreaseResolution(resolutionSelections[resolution]);
			Image scaled = img.getScaledInstance(512, 512, Image.SCALE_SMOOTH);
			numPixels.setText(resolutionSelections[resolution] + "x" + resolutionSelections[resolution] + "=" + resolutionSelections[resolution]*resolutionSelections[resolution]);
			Pixels.this.changePicture(scaled);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	@Override
	protected void changePictureHandler() {
		this.resolution = 0;
		this.changeResolution(this.resolutionSelections[resolution]);
		
	}
}
