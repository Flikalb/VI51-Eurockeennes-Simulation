package fr.utbm.gi.vi51.project.gui;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import fr.utbm.gi.vi51.project.utils.ImageUtils;


public class FestivalGoerLabel extends JLabel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 488012288432437477L;

	private ImageIcon image;
	private double angle;

	public FestivalGoerLabel(ImageIcon image) {
		super(image);
		this.image=image;
		this.angle=0.0f;
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		BufferedImage img = (BufferedImage) ImageUtils.rotateImage(image.getImage(), angle);
		g2d.drawImage(img, 0, 0, this);  	
		this.setSize(img.getWidth(), img.getHeight());
		g2d.dispose();

	}
	
	public void setAngle(double angle) {
		this.angle=angle+Math.PI/2;
		repaint();
	}
	

}
