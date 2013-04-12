package com.LHS.digitalimagebasics;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ColorChecker {
	private Container container;
	
	public ColorChecker() {
		container = new Container();
		container.setBounds(0,0,1004,673);
		BorderLayout layout = new BorderLayout();
		layout.setHgap(0);
		layout.setVgap(0);
		container.setLayout(layout);

		JLabel label = new JLabel("<html><div style='text-align:center'>You should see 81 distinct color tiles in the image below-<br><br>" +
				"9 distinct rows and 9 disticnt columns<br></div></html>", JLabel.CENTER);
		
		ImageIcon colors = new ImageIcon(getClass().getResource("/images/colorchecker.png"));
		JLabel colorsLabel = new JLabel(colors);

		JLabel warning = new JLabel("<html>If not, the quality of your computer display is not capable of<br>displaying the range " +
				"of colors required by this software. SInce you<br> won't be able to see all of the colors of the activities, " +
				"the activities<br>may be misleading or confusing. Try to find a computer display that<br>has the needed color reproduction</html>", 
				JLabel.CENTER);
	
		container.add(label, BorderLayout.NORTH);
		container.add(colorsLabel, BorderLayout.CENTER);
		container.add(warning, BorderLayout.SOUTH);
		
		System.out.println(container.getBounds());
	}
	public Container getContainer() {
		return container;
	}

}
