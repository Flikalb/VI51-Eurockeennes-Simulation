package fr.utbm.gi.vi51.project.environment;

import java.util.ArrayList;

import org.janusproject.jaak.envinterface.perception.Obstacle;

public abstract class Construction {
	
	protected int Xsg;
	protected int Ysg;
	
	protected int Xid;
	protected int Yid;
	
	protected int Xci; // centre d'interaction (entr�e buvette, chanteur pour sc�ne, etc)
	protected int Yci;
	
	protected ArrayList<Obstacle> o;
	
}
