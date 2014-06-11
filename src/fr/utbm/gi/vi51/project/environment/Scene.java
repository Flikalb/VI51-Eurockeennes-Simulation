package fr.utbm.gi.vi51.project.environment;

import org.arakhne.afc.math.discrete.object2d.Point2i;
import org.janusproject.jaak.envinterface.perception.Obstacle;
import org.janusproject.jaak.envinterface.perception.Substance;
import org.janusproject.jaak.environment.solver.ActionApplier;

import fr.utbm.gi.vi51.project.agent.Direction;
import fr.utbm.gi.vi51.project.environment.obstacles.ObstacleScene;

public class Scene extends Construction {

	int RayonInfluence;
	Direction direction;
	boolean isplaying;
	
        
        private static final String CONCERT_JAZZ = "CONCERT_JAZZ";
        private static final String CONCERT_ROCK = "CONCERT_ROCK";
        
	
	public Scene(int Xsg, int Ysg, int Xid, int Yid, int Xci, int Yci, ActionApplier ap )
	{
		this.Xsg = Xsg ;
		this.Ysg = Ysg ;
		this.Xid = Xid ;
		this.Yid = Yid ;
		this.Xci = Xci ;
		this.Yci = Yci ;
	
		for(int xi = Xsg ; xi < Xid ; ++xi) 
		{ 
			for(int yi = Ysg ; yi < Yid ; ++yi) 
			{ 
				ap.putObject(xi, yi, new Obstacle()); 
			}
		}
	}
	

	
	public Scene(int Xsg, int Ysg, int Xid, int Yid, int Xci, int Yci, int RayonInfluence, ActionApplier ap, Direction direction, boolean play, int cat)
	{
		this.Xsg = Xsg ;
		this.Ysg = Ysg ;
		this.Xid = Xid ;
		this.Yid = Yid ;
		this.Xci = Xci ;
		this.Yci = Yci ;
		this.RayonInfluence = RayonInfluence ;
		this.direction=direction;
		isplaying=play;
		
		for(int xi = Xsg ; xi < Xid ; ++xi) 
		{ 
			for(int yi = Ysg ; yi < Yid ; ++yi) 
			{ 
				ap.putObject(xi, yi, new ObstacleScene(cat,this)); 
			}
		}
		
		for(int xi = Xci-RayonInfluence ; xi < Xci+RayonInfluence + 1 ; xi++) 
		{ 
			for(int yi = Yci-RayonInfluence ; yi < Yci+RayonInfluence + 1 ; yi++) 
			{ 
				int y =Yci-yi;
				int x =Xci-xi;
				
				if(direction==Direction.EAST)
				{
					if(xi>Xci)
					{
						if((Math.abs(x)+Math.abs(y))<=RayonInfluence)
						{
							ap.putObject(xi, yi, new SoundSubstance(400)); 
						}
					}
					
				}
				else if(direction==Direction.WEST)
				{
					if(xi<Xci)
					{
						if((Math.abs(x)+Math.abs(y))<=RayonInfluence)
						{
							ap.putObject(xi, yi, new SoundSubstance(400)); 
						}
					}
				}
			}
		}
		
	}
	
	public boolean getisplaying()
	{
		return isplaying;
	}
	
	public Point2i getEmissionPosition()
	{
		return new Point2i(Xci,Yci);
	}
	
}
