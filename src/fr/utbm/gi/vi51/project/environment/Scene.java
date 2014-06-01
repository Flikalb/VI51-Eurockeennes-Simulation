package fr.utbm.gi.vi51.project.environment;

import org.janusproject.jaak.envinterface.perception.Obstacle;
import org.janusproject.jaak.envinterface.perception.Substance;
import org.janusproject.jaak.environment.solver.ActionApplier;

import fr.utbm.gi.vi51.project.agent.Direction;

public class Scene extends Construction {

	int RayonInfluence;
	Direction direction;
	
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
	
	public Scene(int Xsg, int Ysg, int Xid, int Yid, int Xci, int Yci, int RayonInfluence, ActionApplier ap, Direction direction)
	{
		this.Xsg = Xsg ;
		this.Ysg = Ysg ;
		this.Xid = Xid ;
		this.Yid = Yid ;
		this.Xci = Xci ;
		this.Yci = Yci ;
		this.RayonInfluence = RayonInfluence ;
		this.direction=direction;
		
		for(int xi = Xsg ; xi < Xid ; ++xi) 
		{ 
			for(int yi = Ysg ; yi < Yid ; ++yi) 
			{ 
				ap.putObject(xi, yi, new Obstacle()); 
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
	
	
}
