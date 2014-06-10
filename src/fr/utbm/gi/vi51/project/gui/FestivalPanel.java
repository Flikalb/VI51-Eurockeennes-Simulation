package fr.utbm.gi.vi51.project.gui;

import fr.utbm.gi.vi51.project.agent.FestivalGoer;
import fr.utbm.gi.vi51.project.agent.FestivalTrashMan;
import fr.utbm.gi.vi51.project.agent.TurtleSemantic;
import fr.utbm.gi.vi51.project.environment.SoundSubstance;
import fr.utbm.gi.vi51.project.environment.food.Food;
import fr.utbm.gi.vi51.project.environment.obstacles.ObstaclePumpRoom;
import fr.utbm.gi.vi51.project.environment.obstacles.ObstacleScene;
import fr.utbm.gi.vi51.project.environment.obstacles.ObstacleTrash;
import fr.utbm.gi.vi51.project.environment.obstacles.ObstacleTree;
import fr.utbm.gi.vi51.project.environment.obstacles.ObstacleWater;
import fr.utbm.gi.vi51.project.environment.obstacles.ObstacleWaterClosed;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.janusproject.jaak.envinterface.body.TurtleBody;

import org.janusproject.jaak.envinterface.channel.GridStateChannel;
import org.janusproject.jaak.envinterface.channel.GridStateChannelListener;
import org.janusproject.jaak.envinterface.perception.EnvironmentalObject;
import org.janusproject.jaak.envinterface.perception.JaakObject;
import org.janusproject.jaak.envinterface.perception.Obstacle;
import org.janusproject.jaak.environment.model.JaakEnvironment;
import org.janusproject.jaak.environment.model.RealTurtleBody;
import org.janusproject.jaak.turtle.Turtle;

public class FestivalPanel extends JPanel implements GridStateChannelListener 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5761149270387609602L;

	public static final int CELL_SIZE = 10;

	private static final ImageIcon FESTIVAL_GOER_ICON;
        private static final ImageIcon FESTIVAL_GOER_WITH_FOOD_ICON;
        private static final ImageIcon SECURITY_AGENT_ICON;
        private static final ImageIcon TRASH_MAN_ICON;
        

	static {
		FESTIVAL_GOER_ICON = new ImageIcon("res/images/bonhomme.png");
		FESTIVAL_GOER_WITH_FOOD_ICON = new ImageIcon("res/images/bonhommeAvecVerre.png");
                SECURITY_AGENT_ICON = new ImageIcon("res/images/agentSecu.png");
                TRASH_MAN_ICON = new ImageIcon("res/images/trashMan.png");
	}

	private int width;
	private int height;

	private final GridStateChannel channel;
        private final JaakEnvironment environment;


	public FestivalPanel(GridStateChannel channel, JaakEnvironment environment) {
		setBackground(new Color(240, 231, 193));
		this.channel = channel;
		this.channel.addGridStateChannelListener(this);
		this.width = this.channel.getGridWidth();
		this.height=this.channel.getGridHeight();
        this.environment = environment;
	}

	public GridStateChannel getChannel() {
		return this.channel;
	}

	@Override
	public void gridStateChanged() {
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
                //System.out.println("repaint");
		super.paint(g);
                
               // g.setColor(Color.BLUE);
               // g.fillRect(0, 0, 200, 200);
                
                
                Image image = FESTIVAL_GOER_ICON.getImage();
                AffineTransform identity = new AffineTransform();
                Graphics2D g2d = (Graphics2D)g;
               
		for(int x=0; x<this.width; ++x) {
			for(int y=0; y<this.height; ++y) {
                                
                                for(EnvironmentalObject eo : this.channel.getEnvironmentalObjects(x, y)) 
                                {
                                	
                                    if(eo instanceof SoundSubstance) 
				    {
				              g.setColor(Color.YELLOW);
				              g.fillRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
				    }
                    
                                    if(eo instanceof ObstacleScene) 
				    {
				              g.setColor(Color.BLACK);
				              g.fillRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
				    }
				    else if(eo instanceof ObstaclePumpRoom) 
				    {
				              g.setColor(Color.RED);
				              g.fillRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
                                    }
                                    else if(eo instanceof ObstacleWater) 
				    {
				              g.setColor(Color.CYAN);
				              g.fillRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
                                    }
				    else if(eo instanceof ObstacleTree) 
				    {
				              g.setColor(Color.GREEN);
				              g.fillRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
				    }
				    else if(eo instanceof ObstacleWaterClosed) 
				    {
				              g.setColor(Color.ORANGE);
				              g.fillRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
				    }
                                    
                                    else if(eo instanceof ObstacleTrash) 
				    {
				              g.setColor(new Color(33,99,66));
				              g.fillRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
				    }
                                    
                                    else if(eo instanceof Food) 
				    {
				              g.setColor(new Color(255,50,50));
				              g.fillRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
				    }
                                    
                                    else if(eo instanceof Obstacle) 
				    {
				              g.setColor(Color.BLACK);
				              g.fillRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
				    }
                                    
                                }
                                
				if (this.channel.containsTurtle(x,y)) { // channel contient la grille de données
                                    // pour chaque agent, on dessine une image
                                    /*BufferedImage bufferedImage = ImageUtils.toBufferedImage(image);
                                    //bufferedImage = ImageUtils.changeColor(bufferedImage, 15);
                                    image =  Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());*/
                                    
                                    AffineTransform trans = new AffineTransform();
                                    trans.setTransform(identity);
                                    trans.translate(simu2screen_x(x), simu2screen_y(y)); // On la positionne au bon endroit x,y
                                    trans.rotate( this.channel.getOrientation(x, y)+Math.PI/2 ); // Et on l'oriente dans son sens de marche
                                    
                                    Collection<TurtleBody> turtles = environment.getTurtles(x, y);
                                    for(TurtleBody tmpBody : turtles)
                                    {
                                        TurtleSemantic tmpData = (TurtleSemantic)tmpBody.getSemantic();
                                        if(tmpData != null)
                                        {
                                            if(tmpData.getOwner() instanceof FestivalGoer)
                                            {
                                                FestivalGoer festivalier = (FestivalGoer)tmpData.getOwner();
                                                g2d.drawImage(festivalier.carryingFood()?FESTIVAL_GOER_WITH_FOOD_ICON.getImage(): FESTIVAL_GOER_ICON.getImage(), trans, this);
                                            }
                                            else if(tmpData.getOwner() instanceof FestivalTrashMan)
                                            {
                                                FestivalTrashMan trashMan = (FestivalTrashMan)tmpData.getOwner();
                                                g2d.drawImage(TRASH_MAN_ICON.getImage(), trans, this);
                                            }
                                        }
                                    }
                                                        /*Collection<JaakObject> allObjects = this.channel.getAllObjects(x, y);
                                                        for(JaakObject tmp : allObjects)
                                                        {
                                                            System.out.println(tmp);
                                                            if(tmp instanceof RealTurtleBody)
                                                            {
                                                                ((RealTurtleBody)tmp)
                                                            }
                                                        }*/
                                    
                                    
                                   
                                    
                                   // g2d.drawImage(FESTIVAL_GOER_WITH_FOOD_ICON.getImage(), trans, this);
				}      
                                
			}
		}

	}
        
        
        
}
            

