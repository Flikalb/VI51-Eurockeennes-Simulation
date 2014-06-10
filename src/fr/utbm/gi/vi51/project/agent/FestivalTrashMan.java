/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utbm.gi.vi51.project.agent;

import static fr.utbm.gi.vi51.project.agent.FestivalEntity.EN_ATTENTE;
import static fr.utbm.gi.vi51.project.agent.FestivalEntity.MARCHE_VERS_CONCERT;
import static fr.utbm.gi.vi51.project.agent.FestivalEntity.MARCHE_VERS_DESTINATION;
import static fr.utbm.gi.vi51.project.agent.FestivalEntity.MARCHE_VERS_NOURRITURE;
import static fr.utbm.gi.vi51.project.agent.FestivalEntity.MARCHE_VERS_TOILETTES;
import static fr.utbm.gi.vi51.project.agent.FestivalEntity.WANDERING;
import fr.utbm.gi.vi51.project.environment.Scene;
import fr.utbm.gi.vi51.project.environment.WaterClosed;
import fr.utbm.gi.vi51.project.environment.food.Food;
import fr.utbm.gi.vi51.project.utils.RandomUtils;
import java.util.ArrayList;
import java.util.Collection;
import org.arakhne.afc.math.discrete.object2d.Point2i;
import org.janusproject.jaak.envinterface.body.TurtleBody;
import org.janusproject.jaak.envinterface.body.TurtleBodyFactory;
import org.janusproject.jaak.envinterface.perception.Perceivable;
import org.janusproject.jaak.envinterface.time.JaakTimeManager;
import org.janusproject.jaak.turtle.Turtle;

/**
 *
 * @author Benjamin
 */
public class FestivalTrashMan extends FestivalEntity {
    
    /**
     *
     */
    private static final long serialVersionUID = 1021603672862125311L;
    
    
    private TurtleSemantic _informationsTurtle;
    
    private Food _trashFounded;
    
    private long _timeSincePreviousAction;
    
    
    
    public FestivalTrashMan() {
        super();
        _informationsTurtle = new TurtleSemantic(this);
        _currentState = INIT;
    }
    
    @Override
    protected TurtleBody createTurtleBody(TurtleBodyFactory factory) {
        
        TurtleBody turtleBody = factory.createTurtleBody(getAddress());
        turtleBody.setSemantic(_informationsTurtle);
        
        return turtleBody;
    }
    
    @Override
    protected void turtleBehavior() {
        JaakTimeManager jaakTimeManager = getJaakTimeManager();
        
        _timeSincePreviousAction += jaakTimeManager.getWaitingDuration();
        
        Collection<Perceivable> perception = getPerception();
        for(Perceivable tmpObj : perception)
        {
            //System.out.println("tmpObj : "+tmpObj);
            if(tmpObj instanceof Food)
            {
                System.out.println("FOOOOD  "+tmpObj);
                _trashFounded = (Food)tmpObj;
                _currentState = MARCHE_VERS_DESTINATION;
                _currentDestination = _trashFounded.getPosition();
            }
        }
        
        
        if(_trashFounded != null)
        {
            if(getPosition().equals(_trashFounded.getPosition()))
            {
                pickUp(_trashFounded);
                _trashFounded = null;
            }
        }
        
        switch(_currentState)
        {
            case INIT:
                chooseToGoToARandomDestination();
                break;
                
            case MARCHE_VERS_CONCERT:
            case MARCHE_VERS_TOILETTES:
            case MARCHE_VERS_NOURRITURE:
                
                if(_timeSincePreviousAction > 10*1000)
                {
                    _timeSincePreviousAction = 0;
                    _currentState = WANDERING;
                }
                
              case MARCHE_VERS_DESTINATION:   
                applyPathfinding();
                
                
                break;
            default:
                this.setHeading(this.getHeadingAngle()+RandomUtils.randomBinomial((float)Math.PI/4));
                moveForward(1);
                if(_timeSincePreviousAction > 10*1000)
                {
                    _timeSincePreviousAction = 0;
                    chooseToGoToARandomDestination();
                }
        }
        
        
        
        
        
        
        
    }
}