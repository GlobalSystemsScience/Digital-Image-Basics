package com.LHS.digitalimagebasics;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.event.MouseInputAdapter;

import sun.awt.image.ToolkitImage;

public class DataInImages extends InteractiveDisplay {
	
	private static final String[] filters = new String[] {"RGB Image(True Color)", "Shades of Red", "Red in Shades of Gray",
		"Shades of Green", "Green in Shades of Gray", "Shades of Blue", "Blue in Shades of Gray", "Red vs Green", "Red vs Blue",
		"Green vs Blue", "Red vs Green (normalized)",  "Red vs Blue (normalized)", "Green vs Blue (normalized)", "(Red+Green+Blue)/3"};
	private static final String[] tools = new String[] {"None", "Pixel", "Line", "Rectangle"};
	private static final String RED = "red";
	private static final String BLUE = "blue";
	
	
	private Container body = new Container();
	private Container toolContainer = new Container();
	private int displayedToolIndex = -1;
	
	private Cover cover = new Cover();
	
	private JLabel startXPosition = new JLabel("");
	private JLabel startYPosition = new JLabel("");
	private JLabel stopXPosition = new JLabel("");
	private JLabel stopYPosition = new JLabel("");
	private JLabel numPixels = new JLabel("");
	
	private Point oldStart;
	private Point oldEnd;
	private Point startPoint;
	private Point endPoint;
	
	public DataInImages() {
		super();
		generateInstructions("<html>Digital images contain a tremendous amount<br>" +
				"of data.<br><br>" +
				"Select an image, then use the visualization<br>" +
				"and analysis tools to explore the data in a" +
				"digital image</html>");
		generateImageList(new String[] {
				"Sample: Measure Leaf",
				"Sample: Color of Shadows"
			}, 
			new String[] {"/images/measure_leaf.png","/images/shadow_color.png"
			},
			new String[] {
				"How long is this leaf in inches?",
				"Do shadows have color?",
				"Your Picture"}
		);
		this.addHelpText(true);
		
		
		this.mouseClick = new AdvancedMouseListener(this, cover);
		addImage("/images/measure_leaf.png");
		addCover();
		addCaption("How long is this leaf in inches?");
		
		addSelectors();
		setUpPixelValue("Red", "Green", "Blue");
		
		body.add(toolContainer);
		setUpContainer();
	}
	private void addCover() {
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
	protected void updateDisplay() {
		System.out.println("HELLO");
		cover.repaint();
		if (displayedToolIndex >= 1) {
			
			
			this.startXPosition.setText("" + Math.round(cover.getStartPoint().getX()));
			this.startYPosition.setText("" + Math.round(cover.getStartPoint().getY()));
			if (displayedToolIndex > 1) {
				
				this.stopXPosition.setText("" + Math.round(cover.getEndPoint().getX()));
				this.stopYPosition.setText("" + Math.round(cover.getEndPoint().getY()));

				if (displayedToolIndex == 2) 
					this.numPixels.setText("" + Math.round(cover.getEndPoint().distance(cover.getStartPoint())));
				if (displayedToolIndex == 3)
					this.numPixels.setText("" + Math.round((Math.abs(cover.getEndPoint().getX()-cover.getStartPoint().getX()) * Math.abs(cover.getEndPoint().getY() - cover.getStartPoint().getY()))));
			}
			
		}
		this.handlePixelValue();
		this.bringToFront(cover);
	}

	protected void handlePixelValue() {
		System.out.println("PIXEL VALUE");
		BufferedImage img = this.getBufferedImage();
		
		int displayPixel = 0;
		switch (displayedToolIndex) {
		case 1:
			displayPixel = img.getRGB((int)cover.getStartPoint().getX(), (int)cover.getStartPoint().getY());
			break;
		case 2:
			int red=0, green=0, blue=0, total=0;
			int beginX, endX, beginY, endY;
			if (cover.getStartPoint().getX() > cover.getEndPoint().getX()) {
				beginX = (int) cover.getEndPoint().getX();
				endX = (int) cover.getStartPoint().getX();
				beginY = (int) cover.getEndPoint().getY();
				endY = (int) cover.getStartPoint().getY();
			} else {
				endX = (int) cover.getEndPoint().getX();
				beginX = (int) cover.getStartPoint().getX();
				beginY = (int) cover.getStartPoint().getY();
				endY = (int) cover.getEndPoint().getY();
			}			
			
			double deltaY = (endY - beginY);
			double deltaX = (endX-beginX);
			double slope = deltaY/deltaX;
			System.out.println("SLOPE: " + slope);
			for (int x = beginX; x < endX; x++) {
				System.out.println(x + ", " + beginY);
				int pixel = img.getRGB(x, beginY);
				red += (pixel & 0xFF0000) >> 16;
				green += (pixel & 0x00FF00) >> 8;
				blue += (pixel & 0x0000FF);
				beginY += slope;
				total++;
			}
			displayPixel = (((red/total) << 16) | ((green/total) << 8) | (blue/total));
			break;
		case 3: 
			int[] arr = new int[(int)(Math.abs(cover.getEndPoint().getX()-cover.getStartPoint().getX()) * 
					(int)Math.abs(cover.getEndPoint().getY()-cover.getStartPoint().getY()))];
			
			arr = img.getRGB((int)cover.getStartPoint().getX(), (int)cover.getStartPoint().getY(), 
					(int)Math.abs(cover.getEndPoint().getX()-cover.getStartPoint().getX()), 
					(int)Math.abs(cover.getEndPoint().getY()-cover.getStartPoint().getY()), 
					arr, 0, (int)Math.abs(cover.getEndPoint().getX()-cover.getStartPoint().getX()));

			displayPixel = averageValues(arr);
			break;
		}
		this.setPixelValue(displayPixel);
	}
	
	
	private int averageValues(int[] arr) {
		int red = 0;
		int green = 0;
		int blue = 0;
		int count = 0;
		for (int p : arr) {
			red += (p & 0xFF0000) >> 16;
			green += (p & 0x00FF00) >> 8;
			blue += (p & 0x0000FF);
			count++;
		}
		System.out.println("COUNT: " + count + " length: " + arr.length);
		int length = arr.length;
		if (arr.length == 0) {return 0;}
		return (((red/length) << 16) | ((green/length) << 8) | (blue/length));
	}
	
	private void setUpContainer() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy=2;
		body.setLayout(new GridLayout(2, 1));
		this.addToBody(body, constraints);
		GridBagLayout layout = new GridBagLayout();
		
		toolContainer.setLayout(layout);
		
	}
	private void addSelectors() {
		Container selectorArea = new Container();
		selectorArea.setLayout(new GridLayout(2, 2));
		selectorArea.setMaximumSize(new Dimension(300, 80));
		JLabel visualizations = new JLabel("Image Visualizations");
		JComboBox filterSelector = new JComboBox(filters);
		JLabel toolsLabel = new JLabel("Analysis Tools");
		JComboBox toolsSelector = new JComboBox(tools);
		
		filterSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JComboBox selector = (JComboBox) arg0.getSource();
				Image img = filter(selector.getSelectedIndex());

				BufferedImage tryImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
				Graphics2D g = tryImage.createGraphics();
				g.drawImage(img, null, null);
				
				DataInImages.this.changePicture(tryImage);
			}
		});
		toolsSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("CHANGING INDEX: " + ((JComboBox)arg0.getSource()).getSelectedIndex());
				cover.setDisplayedToolIndex(((JComboBox)arg0.getSource()).getSelectedIndex());
				changeAnalysisTools(((JComboBox)arg0.getSource()).getSelectedIndex());
				
			}
		});
		
		selectorArea.add(visualizations);
		selectorArea.add(filterSelector);
		selectorArea.add(toolsLabel);
		selectorArea.add(toolsSelector);
		
		
		body.add(selectorArea);
	}
	protected void changeAnalysisTools(int toolIndex) {
		if (displayedToolIndex == toolIndex) {	//we have nothing to refresh
			return;
		}
		displayedToolIndex = toolIndex;
		this.toolContainer.removeAll();
		switch (displayedToolIndex) {
		case 0:
			break;
		case 1:
			addHeader();
			addPixelLine("Start Point", startXPosition, startYPosition, 2);
			break;
		case 2:
			addHeader();
			addPixelLine("Start Point", startXPosition, startYPosition, 2);
			addPixelLine("Stop Point", stopXPosition, stopYPosition, 3);
			addNumPixels("Length of Line");
			break;
		case 3:
			addHeader();
			addPixelLine("Start Point", startXPosition, startYPosition, 2);
			addPixelLine("Stop Point", stopXPosition, stopYPosition, 3);
			addNumPixels("Pixels in Box");
			break;
		}
		this.revalidateContainer();
		
	}
	private void addNumPixels(String string) {
		JLabel label = new JLabel(string);
		toolContainer.add(label, getConstraints(0, 4, 1, 1));
		toolContainer.add(numPixels, getConstraints(1, 4, 1, 1));
		
	}
	private void addPixelLine(String labelText, JLabel first, JLabel second, int row) {
		JLabel label = new JLabel(labelText);
		toolContainer.add(label, getConstraints(0,row,1,1));
		
		toolContainer.add(first, getConstraints(1,row,1,1));
		toolContainer.add(second, getConstraints(2,row,1,1));
		if (row == 2)
			toolContainer.add(getAdjustment(DataInImages.RED), getConstraints(3, row, 1, 1));
		else
			toolContainer.add(getAdjustment(DataInImages.BLUE), getConstraints(3, row, 1, 1));
		
	}
	private void addHeader() {
		System.out.println("Adding Header");
		JLabel pixelPosition = new JLabel("Pixel Position");
		toolContainer.add(pixelPosition, getConstraints(1, 0, 2, 1));
		
		JLabel adjust = new JLabel("Adjust");
		toolContainer.add(adjust, getConstraints(3, 0, 1, 1));
		
		toolContainer.add(new JLabel("X"), getConstraints(1, 1, 1, 1));
		toolContainer.add(new JLabel("Y"), getConstraints(2, 1, 1, 1));
	}
	private static GridBagConstraints getConstraints(int x, int y, int width, int height) {
		GridBagConstraints ret = new GridBagConstraints();
		ret.gridx = x;
		ret.gridy = y;
		ret.gridheight = height;
		ret.gridwidth = width;
		return ret;
	}
	protected Image filter(int selectedIndex) {
		FilteredImageSource filtered = null;
		ImageFilter filter = null;
		switch (selectedIndex) {
		case 0:
			break;
		case 1:
		case 3:
		case 5:
			filter = new KeepOneColorFilter();
			if (selectedIndex == 1) {
				((KeepOneColorFilter) filter).setColor(0xFF0000);
			} else if (selectedIndex == 3) {
				((KeepOneColorFilter) filter).setColor(0x00FF00);
			} else {
				((KeepOneColorFilter) filter).setColor(0x0000FF);
			}
			
			break;
		case 2:
		case 4:
		case 6:
			filter = new GreyFilter();
			if (selectedIndex == 2) {
				System.out.println("RED");
				((GreyFilter) filter).setColor(0xFF0000);
			} else if (selectedIndex == 4) {
				System.out.println("GREEN");
				((GreyFilter) filter).setColor(0x00FF00);
			} else {
				System.out.println("BLUE");
				((GreyFilter) filter).setColor(0x0000FF);
			}
			break;
		case 7:
		case 10:
			filter = new ColorVsColor();
			((ColorVsColor)filter).setColors(0xFF0000, 0x00FF00, 16, 8);
			if (selectedIndex == 10) {
				((ColorVsColor)filter).setNormalization(true);
			}
			break;
		case 8:
		case 11:
			filter = new ColorVsColor();
			((ColorVsColor)filter).setColors(0xFF0000, 0x0000FF, 16, 0);
			if (selectedIndex == 11) {
				((ColorVsColor)filter).setNormalization(true);
			}
			break;
		case 9:
		case 12:
			filter = new ColorVsColor();
			((ColorVsColor)filter).setColors(0x00FF00, 0x0000FF, 8, 0);
			if (selectedIndex == 12) {
				((ColorVsColor)filter).setNormalization(true);
			}
			break;
		case 13:
			filter = new AverageColorFilter();
		}
		if (filter != null) {
			try {
				filtered = new FilteredImageSource(this.getOriginalBufferedImage().getSource(), filter);
				return Toolkit.getDefaultToolkit().createImage(filtered);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			return (ToolkitImage) Toolkit.getDefaultToolkit().createImage(this.getOriginalBufferedImage().getSource());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} 
		
	}
	private void addTools() {
		Container toolsArea = new Container();
		toolsArea.setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth=1;
		
		JLabel positionLabel = new JLabel("Pixel Position");
		toolsArea.add(positionLabel);
		
		JLabel adjust = new JLabel("Adjust");
	}	

	@Override
	protected void changePictureHandler() {
		// TODO Auto-generated method stub
		
	}
	private Container getAdjustment(String color) {
		Container container = new Container();
		container.setLayout(new BorderLayout());
		container.setBounds(0, 0, 50, 50);
		container.setPreferredSize(new Dimension(50, 50));
		System.out.println("images\\left_" + color + ".png");
		ImageIcon left = new ImageIcon(getClass().getResource("/images/left_" + color + ".png"));
                System.out.println("2");
		ImageIcon up = new ImageIcon(getClass().getResource("/images/up_" + color + ".png"));
		ImageIcon down = new ImageIcon(getClass().getResource("/images/down_" + color + ".png"));
		ImageIcon right = new ImageIcon(getClass().getResource("/images/right_" + color + ".png"));
		ImageIcon circle = new ImageIcon(getClass().getResource("/images/circle_" + color + ".png"));
		
		JLabel leftLabel = new JLabel(left);
		JLabel upLabel = new JLabel(up);
		JLabel downLabel = new JLabel(down);
		JLabel rightLabel = new JLabel(right);
		JLabel circleLabel = new JLabel(circle);
		
		String button;
		if (color.equals(RED)) {
			button = AdjustmentMouseListener.START;
		} else {
			button = AdjustmentMouseListener.END;
		}
		leftLabel.addMouseListener(new AdjustmentMouseListener(AdjustmentMouseListener.LEFT, button, cover, this));
		upLabel.addMouseListener(new AdjustmentMouseListener(AdjustmentMouseListener.UP, button, cover, this));
		downLabel.addMouseListener(new AdjustmentMouseListener(AdjustmentMouseListener.DOWN, button, cover, this));
		rightLabel.addMouseListener(new AdjustmentMouseListener(AdjustmentMouseListener.RIGHT, button, cover, this));
		
		
		container.add(leftLabel, BorderLayout.WEST);
		container.add(upLabel, BorderLayout.NORTH);
		container.add(downLabel, BorderLayout.SOUTH);
		container.add(rightLabel, BorderLayout.EAST);
		container.add(circleLabel, BorderLayout.CENTER);

		return container;
	}
	
	
	public int getDisplayedToolIndex() {
		return displayedToolIndex;
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
}
class AdvancedMouseListener extends MouseInputAdapter {
	DataInImages superInstance;
	Cover cover;
	boolean pressed = false;
	public AdvancedMouseListener(DataInImages source, Cover cover) {
		superInstance = source;
		this.cover = cover;
	}
	//
	public void mouseClicked(MouseEvent arg0) {
		super.mouseClicked(arg0);
		System.out.println(arg0.getPoint());
		if (superInstance.getDisplayedToolIndex() >= 0) {
			superInstance.setStartPoint(arg0.getPoint());
			cover.setStartPoint(arg0.getPoint());
			if (superInstance.getDisplayedToolIndex() >= 1) {
				superInstance.setEndPoint(arg0.getPoint());
				cover.setEndPoint(arg0.getPoint());
			}
			superInstance.updateDisplay();
		}
	}

	
	public void mouseEntered(MouseEvent arg0) {
		//Nothing
	}
	
	public void mouseExited(MouseEvent arg0) {
		pressed = false;
	}

	
	public void mousePressed(MouseEvent arg0) {
		super.mousePressed(arg0);
		System.out.println("Pressed");
		pressed = true;
		if (superInstance.getDisplayedToolIndex() >= 1) {
			superInstance.setStartPoint(arg0.getPoint());
			superInstance.setEndPoint(arg0.getPoint());
			cover.setStartPoint(arg0.getPoint());
			cover.setEndPoint(arg0.getPoint());
			superInstance.updateDisplay();
		} 
		
	}
	
	public void mouseDragged(MouseEvent arg0) {
		//super.mouseDragged(arg0);
		System.out.println(arg0.getPoint());
		if (pressed) {
			if (superInstance.getDisplayedToolIndex() == 1) {
				superInstance.setStartPoint(arg0.getPoint());
				cover.setStartPoint(arg0.getPoint());
//				superInstance.updateDisplay();
			} else if (superInstance.getDisplayedToolIndex() > 1) {
				superInstance.setEndPoint(arg0.getPoint());
				cover.setEndPoint(arg0.getPoint());
				
			}
			superInstance.updateDisplay();
		}
		
		
	}
	
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("Moving");
		
		
	}
	
	public void mouseReleased(MouseEvent arg0) {
		System.out.println("RELEASED");
		superInstance.updateDisplay();
		pressed = false;
		
	}
}
class AdjustmentMouseListener extends MouseInputAdapter {
	static final String UP = "up";
	static final String RIGHT = "right";
	static final String DOWN = "down";
	static final String LEFT = "left";
	
	static final String START = "start";
	static final String END = "end";
	
	String direction;
	String point;
	Cover cover;
	DataInImages superInstance;
	public AdjustmentMouseListener(String dir, String point, Cover cover, DataInImages superInst) {
		this.direction = dir;
		this.point = point;
		this.cover = cover;
		this.superInstance = superInst;
	}
	public void mouseClicked(MouseEvent arg0) {
		if (this.point.equals(START)) {
			moveDirection(cover.getStartPoint());
		} else {
			moveDirection(cover.getEndPoint());
		}
		superInstance.updateDisplay();
	}
	private void moveDirection(Point point) {
		if (direction.equals(UP)) {
			point.setLocation(point.getX(), point.getY()-1);
		} else if (direction.equals(RIGHT)) {
			point.setLocation(point.getX()+1, point.getY());
		} else if (direction.equals(DOWN)) {
			point.setLocation(point.getX(), point.getY()+1);
		} else {
			point.setLocation(point.getX()-1, point.getY());
		}
	}
}
