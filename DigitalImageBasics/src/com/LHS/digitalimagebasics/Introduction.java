package com.LHS.digitalimagebasics;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Introduction {
	private Container container;
	public Introduction() {
		FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
		container = new Container();
		container.setLayout(layout);
		JLabel text = new JLabel();
		text.setText("<html>Digital images are valuable as art, memories,<br>" +
				"communications, documentation, and,<br>" +
				"something many may not realize, scientific data<br><br>" +
				"With a knowledge of color with light, familiarity<br>" +
				"with the basics of digital imagery, and software<br>" + 
				"tools, you can use the data in digital images for<br>" +
				"a variety of scientific explorations.<br><br>" +
				"The basics of digital imagery is divided into four parts:" + 
				"<ol><li>Pixel, or picture elements</li>" +
				"<li>Color</li>" +
				"<li>Data</li>" +
				"<li>False Color</li></ol><br><br>" +
				"At the right is an illustration of how a pixel relates<br>" +
				"to the color layers of a digital image.</html>");
		System.out.println(Introduction.class.getResource("/intro_layers.png"));
		ImageIcon icon = new ImageIcon(Introduction.class.getResource("/images/intro_layers.png"));
		JLabel label = new JLabel(icon);
		
		//Filler to align images on initial screen
		JLabel filler = new JLabel();
		filler.setPreferredSize(new Dimension(200,3));
		
		//System.out.println(getClass().getResource("/images/flower.png"));
		ImageIcon flower = new ImageIcon(getClass().getResource("/images/flower.png"));
		JLabel flowerLabel = new JLabel(flower);
		
		ImageIcon river = new ImageIcon(getClass().getResource("/images/river.png"));
		JLabel riverLabel = new JLabel(river);
		
		ImageIcon fish = new ImageIcon(getClass().getResource("/images/fish.png"));
		JLabel fishLabel = new JLabel(fish);
		
		container.add(text);
		container.add(label);
		container.add(filler);
		container.add(flowerLabel);
		container.add(riverLabel);
		container.add(fishLabel);
	}
	public Container getComponent() {
		return container;
	}

}
