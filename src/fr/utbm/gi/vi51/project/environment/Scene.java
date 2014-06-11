package fr.utbm.gi.vi51.project.environment;

import org.arakhne.afc.math.discrete.object2d.Point2i;
import org.janusproject.jaak.envinterface.perception.Obstacle;
import org.janusproject.jaak.envinterface.perception.Substance;
import org.janusproject.jaak.environment.solver.ActionApplier;

import fr.utbm.gi.vi51.project.agent.Direction;
import fr.utbm.gi.vi51.project.environment.obstacles.ObstacleScene;
import java.util.ArrayList;
import java.util.Collection;
import org.janusproject.jaak.envinterface.perception.EnvironmentalObject;

public class Scene extends Construction {
    
    private int RayonInfluence;
    private Direction direction;
    private boolean isplaying;
    
    private ActionApplier _ap;
    
    private boolean _substancesDrawed;
    private ArrayList<SoundSubstance> _soundSubs;
    
    
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
        this._ap = ap;
        
        
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
        this._ap = ap;
        this.RayonInfluence = RayonInfluence ;
        this.direction=direction;
        _soundSubs = new ArrayList<>();
        
        for(int xi = Xsg ; xi < Xid ; ++xi)
        {
            for(int yi = Ysg ; yi < Yid ; ++yi)
            {
                ap.putObject(xi, yi, new ObstacleScene(cat,this));
            }
        }
        setIsPlaying(play);
    }
    
    public void addSubstances()
    {
        if(_soundSubs.size() > 0) removeSubstances();
        System.out.println("addSubstances");
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
                            SoundSubstance soundSubstance = new SoundSubstance(400, this);
                            _ap.putObject(xi, yi, soundSubstance);
                            _soundSubs.add(soundSubstance);
                            
                        }
                    }
                    
                }
                else if(direction==Direction.WEST)
                {
                    if(xi<Xci)
                    {
                        if((Math.abs(x)+Math.abs(y))<=RayonInfluence)
                        {
                            SoundSubstance soundSubstance = new SoundSubstance(400, this);
                            _ap.putObject(xi, yi, soundSubstance);
                            _soundSubs.add(soundSubstance);
                        }
                    }
                }
            }
        }
        
        
       // removeSubstances();
    }
    
    public void removeSubstances()
    {
        System.out.println("removeSubstances");
        for(SoundSubstance sub : _soundSubs)
        {
            // _ap.removeObject((int)sub.getPosition().getX(), (int)sub.getPosition().getY(), sub);
            Collection<EnvironmentalObject> removeObjects = _ap.removeObjects((int)sub.getPosition().getX(), (int)sub.getPosition().getY());
           /* for(EnvironmentalObject obj : removeObjects)
            {
                System.out.println("obj : "+obj);
            }*/
        }
        _soundSubs = new ArrayList<>();
    }
    
    
    
    
    
    
    
    public boolean getIsPlaying()
    {
        return isplaying;
    }
    
    
    public void setIsPlaying(boolean value)
    {
        if(value == isplaying) return;
        isplaying = value;
        if(isplaying)addSubstances();
        else removeSubstances();
    }
    
    public Point2i getEmissionPosition()
    {
        return new Point2i(Xci,Yci);
    }
    
}
