package fr.utbm.gi.vi51.project.environment;

import fr.utbm.gi.vi51.project.environment.obstacles.ObstacleWaterClosed;
import org.janusproject.jaak.environment.solver.ActionApplier;

public class WaterClosed extends Construction {
	
	public WaterClosed(int Xsg, int Ysg, int Xid, int Yid, int Xci, int Yci, ActionApplier ap)
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
				ap.putObject(xi, yi, new ObstacleWaterClosed()); 
			}
		}
	}
}
