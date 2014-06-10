/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utbm.gi.vi51.project.environment;

import fr.utbm.gi.vi51.project.environment.obstacles.ObstacleTrash;
import org.janusproject.jaak.environment.solver.ActionApplier;

/**
 *
 * @author Benjamin
 */
public class Trash extends Construction
{
    public Trash(int Xsg, int Ysg, int Xid, int Yid, int Xci, int Yci, ActionApplier ap)
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
				ap.putObject(xi, yi, new ObstacleTrash()); 
			}
		}
	}
}
