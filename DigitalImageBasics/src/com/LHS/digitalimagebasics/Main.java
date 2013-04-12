package com.LHS.digitalimagebasics;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Main {
	public enum Mode {INTRODUCTION, PIXELS, COLORS, DATA_IN_IMAGES, FALSE_COLOR, CHECK_COLOR, ABOUT};
	public static final int x=0, y=0, width=1020, height=760;
	
	private static final String[] fileLabels = new String[] {"Save Picture"}; //, "Print Picture"};
	private Mode mode;
	
	private JFrame mainDisplay;
	private InteractiveDisplay variableDisplay;
	private Container mContainer;

	private static final String[] helpLabels = new String[] {"Open GSS Website"};
	
	public Main() {
		mode = Mode.INTRODUCTION;
		mainDisplay = new JFrame();
		mainDisplay.setBounds(x, y, width, height);
		mainDisplay.setTitle("Introduction to the Basics of Digital Images");
		mainDisplay.setLayout(new BorderLayout());
		this.addContextMenu();
		this.addNavigationMenu();
		mContainer = new Container();
		mContainer.setLayout(new BorderLayout());
		mainDisplay.add((mContainer), BorderLayout.CENTER);
                mainDisplay.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String[] args) {
		Main m = new Main();
		m.show();
		m.generateDisplay();
		
	}
	
	private void show() {
		mainDisplay.setVisible(true);
	}
	/**
	 * This adds the variable display object based on the current mode
	 */
	private void generateDisplay() {
		switch(mode) {
			case INTRODUCTION:
				
				mContainer.add(new Introduction().getComponent(), BorderLayout.CENTER);
				break;
			case PIXELS:
				variableDisplay = new Pixels();
				
				mContainer.add(variableDisplay.getContainer(), BorderLayout.CENTER);
				((Pixels)variableDisplay).init();
				break;
			case COLORS:
				variableDisplay =new Colors(); 
				mContainer.add(variableDisplay.getContainer(), BorderLayout.CENTER);
				break;
			case DATA_IN_IMAGES:
				variableDisplay = new DataInImages();
				mContainer.add(variableDisplay.getContainer(), BorderLayout.CENTER);
				break;
			case FALSE_COLOR:
				variableDisplay = new FalseColor();
				mContainer.add(variableDisplay.getContainer(), BorderLayout.CENTER);
				break;
			case CHECK_COLOR:
				mContainer.add(new ColorChecker().getContainer(), BorderLayout.CENTER);
				break;
			case ABOUT:
				mContainer.add(new About().getContainer(), BorderLayout.CENTER);
				break;
		}
		mainDisplay.repaint();
		mainDisplay.getContentPane().validate();
	}
	private void addContextMenu() {
		JMenuBar menuBar;
		JMenu file, help;
		
		menuBar = new JMenuBar();
		file = new JMenu("File");
		help = new JMenu("Help");
		
		String[] fileLabelList = this.getFileMenu();
		String[] helpLabelList = this.getHelpMenu();
		
		addStringsToFileMenu(file, fileLabelList);
		addStringsToFileMenu(help, helpLabelList);
		
		menuBar.add(file);
		menuBar.add(help);
		mainDisplay.setJMenuBar(menuBar);
		
	}
	private void addStringsToFileMenu(JMenu menu, String[] labelList) {
		for (int i = 0; i < labelList.length; i++) {
			JMenuItem item = new JMenuItem(labelList[i]);
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String label = ((JMenuItem)arg0.getSource()).getText();
					if (label.equals(fileLabels[0])) {	//savePicture
						variableDisplay.savePicture();
					} 
					else if (label.equals(helpLabels[0])) {
						BrowserControl.displayURL("http://www.globalsystemsscience.org");
					}
//					else if (label.equals(fileLabels[1])) {	//Print picture
//						variableDisplay.printPicture();
//					}

				}
			});
			menu.add(item);
		}
	}
	/**
	 * Not yet implemented
	 * @return an array of strings to add as labels for the file menu
	 */
	private String[] getFileMenu() {
		return fileLabels;
	}
	/**
	 * Not yet implemented
	 * @return an array of strings to add as labels for the help menu
	 */
	private String[] getHelpMenu() {
		return helpLabels;
	}
	
	
	private void addNavigationMenu() {
		String[] buttonLabels = {"Intro", "Pixels", "Colors", "Data in Images", "False Color", "Check Color", "About"};
		
		int width = mainDisplay.getWidth();
		int buttonWidth = 120;
		int buttonHeight = 40;
		int padding = (width-(buttonLabels.length*buttonWidth))/(buttonLabels.length+1);
		
		GridLayout layout = new GridLayout(1, buttonLabels.length);
		System.out.println(padding);
		layout.setHgap(padding);
		
		Container north = new Container();
		north.setLayout(layout);
		
		for (int i=0; i < buttonLabels.length; i++) {
			JButton btn = new JButton(buttonLabels[i]);
			btn.setSize(buttonWidth, buttonHeight);
			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					mode = getMode(((JButton)arg0.getSource()).getText());
					redisplay();
				}
				private Mode getMode(String text) {
					if (text.equals("Intro"))
						return Mode.INTRODUCTION;
					 else if (text.equals("Pixels")) 
						 return Mode.PIXELS;
					 else if (text.equals("Colors"))
						 return Mode.COLORS;
					 else if (text.equals("Data in Images"))
						 return Mode.DATA_IN_IMAGES;
					 else if (text.equals("False Color"))
						 return Mode.FALSE_COLOR;
					 else if (text.equals("Check Color"))
						 return Mode.CHECK_COLOR;
					 else if (text.equals("About"))
						 return Mode.ABOUT;
					 else 
						 return null;
				}
			});
			north.add(btn);
		}
		mainDisplay.add(north, BorderLayout.NORTH);
	}
	/**
	 * This is a public wrapper to redisplay the graph based on a change in mode
	 */
	public void redisplay() {
		if (mContainer != null) {
			System.out.println(Arrays.toString(mContainer.getComponents()));
			while (mContainer.getComponentCount() > 0) {
				System.out.println("Removing component");
				mContainer.remove(0);
			}
			System.out.println(Arrays.toString(mContainer.getComponents()));
		}
		System.out.println(mode);
		
		this.generateDisplay();
	}
	/**
	 * Checks to see if a container contains a given component
	 * @param superContainer the container that may have the component
	 * @param container the component to check if added
	 * @return true if found, false if not
	 */
	private boolean contains(Container superContainer, Component container) {
		Component[] lst = superContainer.getComponents();
		for (int i = 0; i < lst.length; i++) {
			System.out.println(lst[i].toString());
			if (lst[i].equals(container)) {
				return true;
			}
		}
		return false;
	}
}
