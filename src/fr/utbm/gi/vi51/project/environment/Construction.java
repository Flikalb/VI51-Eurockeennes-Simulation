package fr.utbm.gi.vi51.project.environment;

import java.util.ArrayList;
import org.arakhne.afc.math.discrete.object2d.Point2i;

import org.janusproject.jaak.envinterface.perception.Obstacle;

public abstract class Construction {
	
	protected int Xsg;
	protected int Ysg;
	
	protected int Xid;
	protected int Yid;
	
	protected int Xci; // centre d'interaction (entree buvette, chanteur pour scene, etc)
	protected int Yci;
	
	protected ArrayList<Obstacle> o;
        
        public Point2i getInteractCenter()
        {
            return new Point2i(Xci, Yci);
        }
	
}
