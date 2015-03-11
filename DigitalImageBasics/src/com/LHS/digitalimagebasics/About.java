package com.LHS.digitalimagebasics;

import java.awt.Container;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class About {
	private Container body;
	
	public About() {
		body = new Container();
		body.setLayout(new GridLayout(1, 2));
		JLabel info = new JLabel("<html>" +
				"The original software components were created by John Pickle and Jacqueline Krtley,<br>" +
				"Museum of Science, Boston, MA in support of the Lawrence Hall of Science's Global<br> Systems " +
				"Science student series in 2002 with NASA funding<br>" +
				"Additional work on the Java version was done by Maxime Israel at the University of <br>Massachusetts in February 2015.<br><br>" +
				"This Revision was created by Ben Augarten, Lawrence Hall of Science, Berkeley, CA, converting the program from " +
				"Real Basic to Java to provide for a more modern, maintainable, and stable platform<br><br>" +
				"These revisions were created to support the NASA-funded project, Digital Earth Watch,<br>" +
				"originally named Measuring Vegetation Health - http://mvh.sr.unh.edu/. This educational<br>" +
				"project is a collaboration between seven institutions to develop learning activities,<br>" +
				"technologies, and software to measure environmental health by monitoring plants:<br>" +
				"<ul><li>Museum of Science, Boston, MA (lead institution) - www.mos.org</li>" +
				"<li>Global Systems Science, Lawrence Hall of Science, Berkeley, CA (co-lead)<br>" +
				"- www.globalsystemsscience.org<br></li>" +
				"<li>Forest Watch, University of New Hampshire, Durham, NH (co-lead)<br>" +
				"- www.forestwatch.sr.unh.edu/</li>" +
				"<li>EOS-WEbster, University of New Hampshire, Durham, NH<br>" +
				"- eos-webster.sr.unh.edu/</li>" +
				"<li>Remote Sensing and GIS Laboratory, Indiana State University, Terre Haute, IN<br>" +
				"- baby.indstate.edu/geo/rs/main.htm</li>" +
				"Blue Hill Observatory, Milton, MA - www.bluehill.org/</li>" +
				"<li>College of Education and Human Development, University of Southern Maine,<br>" +
				"Portland, ME - www.usm.maine.edu/cehd/</li></ul>" +
				"<br>" +
				"John Pickle programmed these revisions in the original Real Basic program, reflecting invaluable feedback and input from " +
				"the DEW team and years of working with teachers and informal science educators. Unless " +
				"otherwise credited, photos by John Pickle.<br><br>" +
				"This software may be freely copied and used for all educational applications." +
				"<br><br>Copyright 2003, 2009, 2011 by the Regents of the University of California<br>)" +
				"Copyright 2007, 2008 Museum of Science, Boston, MA<br>" +
				"Java revision; Version 1.0 created August 3, 2011</html>");
		
		ImageIcon ic = new ImageIcon(getClass().getResource("/images/logo.png"));

		System.out.println(ic.getIconHeight());
		JLabel labs = new JLabel(ic);

		body.add(info);
		body.add(labs);
	}
	
	public Container getContainer() {
		return body;
	}
}

