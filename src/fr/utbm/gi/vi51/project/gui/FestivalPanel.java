package fr.utbm.gi.vi51.project.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.arakhne.afc.vmutil.Resources;
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

	private int width = 100;
	private int height = 100;
	
	private FestivalGoerLabel fgl;
	

	private final GridStateChannel channel;


	public FestivalPanel(GridStateChannel channel) {
		setBackground(Color.WHITE);
		this.channel = channel;
		this.channel.addGridStateChannelListener(this);
		fgl = new FestivalGoerLabel(FESTIVAL_GOER_ICON);
		fgl.setVisible(false);
		this.add(fgl);
		validate();
		repaint();
	}

	public GridStateChannel getChannel() {
		return this.channel;
	}

	@Override
	public void gridStateChanged() {
		for(int x=0; x<this.width; ++x) {
			for(int y=0; y<this.height; ++y) {

				if (this.channel.containsTurtle(x,y)) {
					fgl.setLocation(simu2screen_x(x),simu2screen_y(y));
					fgl.setAngle(this.channel.getOrientation(x, y));
					if(!fgl.isVisible()) fgl.setVisible(true);
					
				}
				
			}
		}
		if(!fgl.isVisible()) fgl.setVisible(true);
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
		super.paint(g);
	
	}
}
