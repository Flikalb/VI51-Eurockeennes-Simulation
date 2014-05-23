package fr.utbm.gi.vi51.project.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.janusproject.jaak.envinterface.channel.GridStateChannel;
import org.janusproject.jaak.envinterface.channel.GridStateChannelListener;
import org.janusproject.jaak.envinterface.perception.EnvironmentalObject;
import org.janusproject.jaak.envinterface.perception.Obstacle;

public class FestivalPanel extends JPanel implements GridStateChannelListener 
{
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
	
	private ArrayList<FestivalGoerLabel> goers;

	private final GridStateChannel channel;


	public FestivalPanel(GridStateChannel channel) {
		setBackground(new Color(240, 231, 193));
		this.channel = channel;
		this.channel.addGridStateChannelListener(this);
		this.width = this.channel.getGridWidth();
		this.height=this.channel.getGridHeight();
		this.goers = new ArrayList<FestivalGoerLabel>();
		
		
		
		
	}

	public GridStateChannel getChannel() {
		return this.channel;
	}

	@Override
	public void gridStateChanged() {

		int i;
		for(i=this.goers.size();i<this.channel.getTurtleCount();i++) {
			this.goers.add(new FestivalGoerLabel(FESTIVAL_GOER_ICON));
			this.add(this.goers.get(i));
			validate();
		}
		i = 0;
		for(int x=0; x<this.width; ++x) {
			for(int y=0; y<this.height; ++y) {

				if (this.channel.containsTurtle(x,y)) {
					assert(i<this.channel.getTurtleCount());
					
					this.goers.get(i).setLocation(simu2screen_x(x),simu2screen_y(y));		
					this.goers.get(i).setAngle(this.channel.getOrientation(x, y));
					this.goers.get(i).setVisible(true);
					i++;
					
				}
				
			}
		}
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
	public synchronized void paint(Graphics g) 
	{
		// Standard drawing of the panel (mainly the background).
		
		super.paint(g);
		
		for(int x=0; x<this.channel.getGridWidth(); ++x) 
		{
            for(int y=0; y<this.channel.getGridHeight(); ++y) 
            {
		
            	for(EnvironmentalObject eo : this.channel.getEnvironmentalObjects(x, y)) 
				{
				    if(eo instanceof Obstacle) 
				    {
				              g.setColor(Color.BLACK);
				              g.fillRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
				    }
				}
            }
       }
		
		
	
	}
}
            

