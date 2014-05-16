package fr.utbm.gi.vi51.project.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.janusproject.jaak.envinterface.channel.GridStateChannel;
import org.janusproject.jaak.envinterface.channel.GridStateChannelListener;

public class FestivalPanel extends JPanel implements GridStateChannelListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5761149270387609602L;

	public static final int CELL_SIZE = 4;

	private static final Color FESTIVAL_GOER_COLOR = Color.BLUE;
	private static final Color FESTIVAL_EMPTY_COLOR = Color.GRAY;

	private Color[][] grid = null;
	private int width = 200;
	private int height = 200;

	private final GridStateChannel channel;


	public FestivalPanel(GridStateChannel channel) {
		setBackground(Color.WHITE);
		this.channel = channel;
		this.channel.addGridStateChannelListener(this);
	}

	public GridStateChannel getChannel() {
		return this.channel;
	}

	@Override
	public void gridStateChanged() {

		// If it is the first grid-change notification,
		// initialize the attributes of the panel.
		if (this.grid==null) {
			
			this.width = this.channel.getGridWidth();
			this.height = this.channel.getGridHeight();

			Dimension dim = new Dimension(
					this.width*CELL_SIZE + 10,
					this.height*CELL_SIZE + 10);
			
			setPreferredSize(dim);
			revalidate(); 
			
			this.grid = new Color[this.width][this.height];
		}

		// Update the graphical representation of the cells
		// according to the current state of the Jaak grid.
		Color c = null;	 
		// Loop on all the cells.
		for(int x=0; x<this.width; ++x) {
			for(int y=0; y<this.height; ++y) {

				if (this.channel.containsTurtle(x,y)) {
					c = FESTIVAL_GOER_COLOR;
				}
				else {
					c = FESTIVAL_EMPTY_COLOR;
				}
				this.grid[x][y] = c;
			}
		}

		repaint();
	}

	@Override
	public void jaakEnd() {
		this.channel.removeGridStateChannelListener(this);

		this.grid = null;
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

	public synchronized void paint(Graphics g) {
		// Standard drawing of the panel (mainly the background).
		super.paint(g);

		// Loop on the graphical representation of the grid,
		// and draw the small rectangles.		
		if (this.grid!=null) {
			for(int x=0; x<this.width; ++x) {
				for(int y=0; y<this.height; ++y) {
					if (this.grid[x][y]!=null) {
						g.setColor(this.grid[x][y]);
						g.fillRect(simu2screen_x(x),simu2screen_y(y),CELL_SIZE, CELL_SIZE);
					}
				}
			}
		}
	}

}
