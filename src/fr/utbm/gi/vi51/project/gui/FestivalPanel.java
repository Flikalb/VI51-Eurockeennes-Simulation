package fr.utbm.gi.vi51.project.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.janusproject.jaak.envinterface.channel.GridStateChannel;
import org.janusproject.jaak.envinterface.channel.GridStateChannelListener;

public class FestivalPanel extends JPanel implements GridStateChannelListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5761149270387609602L;

	public static final int CELL_SIZE = 10;

	private static final ImageIcon FESTIVAL_GOER_ICON;

	static {
		FESTIVAL_GOER_ICON = new ImageIcon("res/images/bonhomme.png");
	}

	private int width;
	private int height;

	private final GridStateChannel channel;


	public FestivalPanel(GridStateChannel channel) {
		setBackground(new Color(240, 231, 193));
		this.channel = channel;
		this.channel.addGridStateChannelListener(this);
		this.width = this.channel.getGridWidth();
		this.height=this.channel.getGridHeight();
	}

	public GridStateChannel getChannel() {
		return this.channel;
	}

	@Override
	public void gridStateChanged() {

		int i;
                System.out.println("nbAgents : "+this.channel.getTurtleCount());
               
                repaint();
	}

	@Override
	public void jaakEnd() {
		this.channel.removeGridStateChannelListener(this);
		this.width = this.height = 0;

		repaint();
	}

	@Override
	public void jaakStart() {
		repaint();
	}

	private static int simu2screen_x(int x) {
		return x * CELL_SIZE + 5;
	}

	private static int simu2screen_y(int y) {
		return y * CELL_SIZE + 5;
	}

	@Override
	public synchronized void paint(Graphics g) {
		// Standard drawing of the panel (mainly the background).
            System.out.println("repaint");
		super.paint(g);
                
                g.setColor(Color.BLUE);
                g.fillRect(0, 0, 200, 200);
                
                
                Image image = FESTIVAL_GOER_ICON.getImage();
                AffineTransform identity = new AffineTransform();
                Graphics2D g2d = (Graphics2D)g;
               
		for(int x=0; x<this.width; ++x) {
			for(int y=0; y<this.height; ++y) {

				if (this.channel.containsTurtle(x,y)) { // channel contient la grille de données
                                    // pour chaque agent, on dessine une image
                                    AffineTransform trans = new AffineTransform();
                                    trans.setTransform(identity);
                                    trans.translate(simu2screen_x(x), simu2screen_y(y)); // On la positionne au bon endroit x,y
                                    trans.rotate( this.channel.getOrientation(x, y)+Math.PI/2 ); // Et on l'oriente dans son sens de marche
                                    g2d.drawImage(image, trans, this);
				}
			}
		}
                

	
	}
}
