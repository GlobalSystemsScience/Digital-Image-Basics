package com.LHS.digitalimagebasics;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import javax.swing.filechooser.FileNameExtensionFilter;

import sun.awt.image.ToolkitImage;

public abstract class InteractiveDisplay {
	private static final String CAPTION_NAME = "caption";
	private static final String IMAGE_NAME = "image";
	
	private Container body = new Container();
	private  GridBagLayout layout = new GridBagLayout();
	private String imageURL;
	private ImageIcon imageIcon;
	private JLabel imageLabel;
	private JLabel caption;
	private Container pixelValueContainer;
	private String[] answers;
	private int imageIndex;
	protected JLayeredPane imagePane;
	private BufferedImage image;
	private int resizeDimension = -1;
	
	protected JLabel redValue;
	protected JLabel greenValue;
	protected JLabel blueValue;
	private String[] captions;
	
	private JPanel imageContainer;
	
	protected MouseInputAdapter mouseClick = null;
	private GridBagConstraints imageConstraints;
	
	private BufferedImage usrImage = null;
	private boolean usingOwnImg = false;
	
	//Instructions at 0,0
	//Image list at 0,1
	//Specifics at 0,2
	//Image at 1,0 to 1,4
	//Caption at 1,4
	//Help text at 0,4
	//Pixel shit at 0,3
	
	
	public InteractiveDisplay() {
		imagePane = new JLayeredPane();
		
		body.setLayout(layout);
		body.setBounds(0, 0, 1000, 700);
		
	}
	protected void generateInstructions(String text) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridheight = 1;
		
		JLabel instructions = new JLabel(text);
		body.add(instructions, constraints);
	}
	protected void generateImageList(String[] labels, final String[] relURLS, final String[] captions, String[] answers) {
		generateImageList(labels, relURLS, captions);
		this.answers = answers;
	}
	protected void generateImageList(String[] labels, final String[] relURLS, final String[] captions) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridheight = 1;
		this.captions = captions;
		JComboBox<String> list = new JComboBox<String>(labels);
		final int specialIndex = list.getItemCount();
		list.addItem("Your Picture");
		System.out.println("CAPTIONS: " + captions.length);
		list.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JComboBox<?> target = (JComboBox<?>)arg0.getSource();
				
				String newRelURL = null;
				String newCaption = null;
				System.out.println("SPECIAL: " + specialIndex + "  " + target.getSelectedIndex());
				if (specialIndex == target.getSelectedIndex()) {	//Upload own picture
					JFileChooser fc = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Image (jpg, gif, png)", new String[] {"jpg", "jpeg", "gif", "png"});
					fc.setFileFilter(filter);
					fc.addChoosableFileFilter(filter);
					FileNameExtensionFilter filter2 = new FileNameExtensionFilter("JPG & JPEG", new String[] {"jpg", "jpeg"});
					fc.addChoosableFileFilter(filter2);
					FileNameExtensionFilter filter3 = new FileNameExtensionFilter("PNG", new String[] {"png"});
					fc.addChoosableFileFilter(filter3);
					FileNameExtensionFilter filter4 = new FileNameExtensionFilter("GIF", new String[] {"gif"});
					fc.addChoosableFileFilter(filter4);
					int returnVal = fc.showOpenDialog(target);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
		                File file = fc.getSelectedFile();
		                //FileWriter fw = new FileWriter(file);
		                String ext = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")+1).toLowerCase();
		                File destFile = new File("user." + ext);
		                
		                //System.out.println("Path and file: " + getClass().getClassLoader().getResource("/images/user." + ext) + " - " + destFile2);
		                //File destFile = new File("user." + ext);
		                try {
		                	//usrImage = ImageIO.read(file);
		                	usrImage = createResizedCopy(ImageIO.read(file), 512, 512, true);
		                			
							ImageIO.write(usrImage, ext, destFile);
							usingOwnImg = true;
							//ImageIO.write(usrImage, ext, new File("test."+ext));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
		                
		                
		                
		                newRelURL = "user." + ext;
		                newCaption = "Your Picture";
					} else {
						target.setSelectedIndex(InteractiveDisplay.this.imageIndex);
						return;
					}
				} else {
					newRelURL = relURLS[target.getSelectedIndex()];
					newCaption = captions[target.getSelectedIndex()];
					usingOwnImg = false;
				}
				InteractiveDisplay.this.imageIndex = target.getSelectedIndex();
				InteractiveDisplay.this.imageURL = newRelURL;
				//System.out.println("newRelURL " + newRelURL);
				changePicture(InteractiveDisplay.this.imageURL);
				changeCaption(newCaption);
			}
		});
		body.add(list, constraints);
		
	}
	private void changeCaption(String newCaption) {
		caption.setText(newCaption);
		
		body.repaint();
	}
	/**
	 * This should only be called once. It instantiates and adds the image to the display
	 * @param relURL relative URL of the image to add
	 */
	protected void addImage(String relURL) {
		
		this.imageURL = relURL;
		this.imageConstraints = new GridBagConstraints();
		imageConstraints.gridx = 1;
		imageConstraints.gridy = 0;
		imageConstraints.gridheight = 4;
		this.imageIndex = 0;
            try {
                imageIcon = new ImageIcon(ImageIO.read(getClass().getResource(imageURL)));
            } catch (IOException ex) {
                Logger.getLogger(InteractiveDisplay.class.getName()).log(Level.SEVERE, null, ex);
            }
		//imageIcon = new ImageIcon(getClass().getResource(imageURL));
		imageLabel = new JLabel(imageIcon);
		imageLabel.setName(IMAGE_NAME);

		
		if (mouseClick != null) {
			imageLabel.addMouseListener(mouseClick);
			imageLabel.addMouseMotionListener(mouseClick);
	
		} else {		
			imageLabel.addMouseMotionListener(new MouseMotionListener() {
				@Override
				public void mouseDragged(MouseEvent arg0) {
					//Nothing	
				}
				@Override
				public void mouseMoved(MouseEvent arg0) {
					Point target = arg0.getPoint();
					int pixel = getBufferedImage().getRGB(target.x, target.y);
					handlePixelValue(pixel);
				}
			});
		}
		System.out.println(imageIcon.getIconWidth()+", "+imageIcon.getIconHeight());
		imagePane.setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
		imageLabel.setMinimumSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
		imageLabel.setBounds(0, 0, imageIcon.getIconWidth(), imageIcon.getIconHeight());
                /*imageLabel.setBounds(0,0,512,512);
                imageLabel.setMinimumSize(new Dimension(512,512));
                imagePane.setPreferredSize(new Dimension(512, 512));*/
		if (this.imageIndex == this.captions.length-1) {
			System.out.println("RESIZING");
			imageLabel.setBounds(0,0,512,512);
		}
		body.add(imageLabel, imageConstraints);
		imagePane.add(imageLabel, new Integer(1));

		imagePane.moveToFront(imageLabel);

		body.add(imagePane, imageConstraints);		
	}
	protected void addCaption(String captionText) {
		GridBagConstraints consts = new GridBagConstraints();
		consts.gridx=1;
		consts.gridy=4;
		caption = new JLabel(captionText);
		caption.setName(CAPTION_NAME);
		
		body.add(caption, consts);
	}
	private BufferedImage getImage(String relURL) {
		BufferedImage img = null;
		try {
                	if(!usingOwnImg)
                		img = ImageIO.read(getClass().getResource(relURL));
                	else
                		img = usrImage;

                    /*Image i = Toolkit.getDefaultToolkit().getImage(getClass().getResource(relURL));
                    MediaTracker mt = new MediaTracker(imageContainer);
                    mt.addImage(i, 1);
                    mt.waitForAll();
                    img = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_RGB);
                    img.getGraphics().drawImage(i, 0, 0, null);*/
                    
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}
	protected void changePicture(String newRelURL) {
		BufferedImage img = getImage(newRelURL);
//		image = img;
		if (this.imageIndex == this.captions.length-1) {
			System.out.println("RESIZING");

		} else {
			//imageLabel.setBounds(0,0,imageIcon.getIconWidth(), imageIcon.getIconHeight());
		}
		if (resizeDimension != -1) {
			//System.out.println("Resize Dimension: " + resizeDimension);
			img = InteractiveDisplay.createResizedCopy(img, resizeDimension, resizeDimension, true);
		}
		ToolkitImage newImage = (ToolkitImage) Toolkit.getDefaultToolkit().createImage(img.getSource());
		imageIcon.setImage(newImage);
		body.repaint();
		this.changePictureHandler();

	}
	private static BufferedImage createResizedCopy(Image originalImage, 
            int scaledWidth, int scaledHeight, boolean preserveAlpha) {
	    System.out.println("resizing...");
	    int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
	    BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
	    Graphics2D g = scaledBI.createGraphics();
	    if (preserveAlpha) {
	            g.setComposite(AlphaComposite.Src);
	    }
	    g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
	    g.dispose();
	    return scaledBI;
	}
	protected void changePicture(Image newImage) {
		if (resizeDimension != -1) {
			newImage = InteractiveDisplay.createResizedCopy(newImage, resizeDimension, resizeDimension, true);
		}
		imageIcon.setImage(newImage);
		System.out.println("Changing: " + newImage);
		
//		try {
//			image = (BufferedImage) newImage;
//		} catch (Exception e) {
//			ToolkitImage img = (ToolkitImage)newImage;
//			System.out.println("MESSED UP: " + img);
//			System.out.println(img.getBufferedImage());
//		}
//		imageIcon.setImage(;)
//		System.out.println("Picture changed: " + image);
		this.revalidateContainer();
	}
	protected void addHelpText(boolean allowOriginalImage) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 4;
		
		Container temp = new Container();
		temp.setLayout(new GridLayout(2,1));
		
		JLabel label = new JLabel("<html>Intensities of colors range from 0%,<br>" +
				"meaning none of the color is present, to<br>" +
				"100%, when maximum color is present");
		temp.add(label);
		
		if (allowOriginalImage) {
			JButton originalImage = new JButton("Show Original Picture");
			originalImage.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					JFrame popup = new JFrame("Original Image");
					ImageIcon original;
					if(!usingOwnImg)
						original = new ImageIcon(getClass().getResource(imageURL));
					else
						original = new ImageIcon(InteractiveDisplay.this.usrImage);
					popup.setBounds(0,0, original.getIconWidth(), original.getIconHeight());
					JLabel originalLabel = new JLabel(original);
					popup.add(originalLabel);
					popup.setVisible(true);
				}
			});
			temp.add(originalImage);
		}
		
		body.add(temp, constraints);
		
	}
	protected void addToBody(Component comp, GridBagConstraints constraints) {
		body.add(comp, constraints);
	}
	public Container getContainer() {return body;}
	
	protected BufferedImage getBufferedImage() {
		try {
			return ((ToolkitImage)imageIcon.getImage()).getBufferedImage();
		} catch (Exception e) {
			return (BufferedImage)imageIcon.getImage();
		}
	}
	protected BufferedImage getOriginalBufferedImage() throws IOException {
        if(!usingOwnImg)    
        	return ImageIO.read(getClass().getResource(imageURL)); 
        else
        	return usrImage;
		
	}
	protected void setUpPixelValue(String label1, String label2, String label3) {
		pixelValueContainer = new Container();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridheight = 1;
		
		GridLayout layout = new GridLayout(4, 2);
		pixelValueContainer.setLayout(layout);
		
		JLabel header1 = new JLabel("Color");
		JLabel header2 = new JLabel("Displayed Intensity");
		
		JLabel red = new JLabel(label1);
		redValue = new JLabel("");
		
		JLabel green = new JLabel(label2);
		greenValue = new JLabel("");
		
		JLabel blue = new JLabel(label3);
		blueValue = new JLabel("");
		
		pixelValueContainer.add(header1);
		pixelValueContainer.add(header2);
		
		pixelValueContainer.add(red);
		pixelValueContainer.add(redValue);
		
		pixelValueContainer.add(green);
		pixelValueContainer.add(greenValue);
		
		pixelValueContainer.add(blue);
		pixelValueContainer.add(blueValue);
		
		body.add(pixelValueContainer, constraints);
	}
	protected void handlePixelValue(int rgb) {
		int red = (rgb >> 16) & 0x000000FF;
		int green = (rgb >>8 ) & 0x000000FF;
		int blue = (rgb) & 0x000000FF;
		red = (int)(((double)red / 255)*100);
		green = (int)(((double)green / 255)*100);
		blue = (int)(((double)blue / 255)*100);
		
		redValue.setText(red + "");
		greenValue.setText(green + "");
		blueValue.setText(blue + "");
		
		pixelValueContainer.repaint();
	}
	protected void repaintPixels() {
		pixelValueContainer.repaint();
	}
	protected void revalidateContainer() {
		this.body.validate();
		this.body.repaint();
	}
	protected String getRelURL() {
		return this.imageURL;
	}
	protected void displayAnswer() {
		this.changeCaption(answers[this.imageIndex]);
	}
	protected void hideAnswer() {
		this.changeCaption(this.captions[this.imageIndex]);
	}
	protected Rectangle getImageBounds() {
		System.out.println("IMAGE BOUNDS: " + image);
		return imageLabel.getBounds();
//		return new Rectangle(image.getMinX(), image.getMinY(), image.getWidth(), image.getHeight());
	}
	protected abstract void changePictureHandler();
	protected void addToLayeredPane(JComponent comp, Integer layer, int position) {
		imagePane.add(comp, layer, position);
	}
	protected void paintImage() {
		Graphics g = imageContainer.getGraphics();
		g.drawImage(image, 0, 0, null);
	}
	protected void drawImage(BufferedImage img) {
		System.out.println("Drawing image");
		this.imageIcon.setImage(img);
		this.revalidateContainer();
//		Graphics g = img.getGraphics();
//		g.drawImage(img, img.getMinX(), img.getMinY(), img.getWidth(), img.getHeight(), null);
		
	}
	protected void bringToFront(JComponent comp) {
		imagePane.moveToFront(comp);
	}
	
	protected void setPixelValue(int pixel) {
		this.redValue.setText(Math.round(((pixel & 0xFF0000) >> 16)/(double)255*100) + "");
		this.greenValue.setText(Math.round(((pixel & 0x00FF00) >> 8)/(double)255*100) + "");
		this.blueValue.setText(Math.round((pixel & 0x0000FF)/(double)255*100) + "");
		pixelValueContainer.repaint();
	}
	public void savePicture() {
		BufferedImage img = this.getBufferedImage();
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter fnef = new FileNameExtensionFilter("Images", "png");
		fc.setFileFilter(fnef);
		int returnVal = fc.showSaveDialog(body);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			int index = file.getAbsolutePath().lastIndexOf(".");
			if (index == -1 || !file.getAbsolutePath().substring(index+1).equals("png")) {
				file = new File(file.getAbsolutePath() + ".png"); 
			}
			try {
				ImageIO.write(img, "png", file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	protected void setResize(int num) {
		this.resizeDimension = num;
	}
	public void printPicture() {
		PrinterJob pj = PrinterJob.getPrinterJob();
	    pj.setPrintable(new Printable()
	     {
	       public int print(Graphics graphics, PageFormat pf, int pageIndex) throws PrinterException
	       {
	         if (pageIndex > 0) {
	           return 1;
	         }
	         Graphics2D g2d = (Graphics2D)graphics;
	         g2d.translate(pf.getImageableX(), pf.getImageableY());
	         g2d.drawImage(InteractiveDisplay.this.getBufferedImage(), 0, 0, null);
	         return 0;
	       }
	     });
	     if (pj.printDialog())
	       try {
	         pj.print();
	       } catch (PrinterException e1) {
	         e1.printStackTrace();
	       }
		/*if (pj.printDialog()) {
			try {
				PageFormat pf = new PageFormat();
				Paper p = new Paper();
				p.setImageableArea(this.imagePane.getX(), this.imagePane.getY(), this.imagePane.getWidth(), imagePane.getHeight());
				pf.setPaper(p);
				pj.pageDialog(pf);
				
				pj.print();
				System.out.println("Printed");
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Printed");
		}*/
		
	}
}
