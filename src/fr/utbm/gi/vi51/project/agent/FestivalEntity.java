/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utbm.gi.vi51.project.agent;

import fr.utbm.gi.vi51.project.environment.Construction;
import fr.utbm.gi.vi51.project.environment.FestivalMap;
import fr.utbm.gi.vi51.project.environment.Scene;
import fr.utbm.gi.vi51.project.utils.RandomUtils;
import java.util.ArrayList;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.discrete.object2d.Point2i;
import org.janusproject.jaak.envinterface.body.TurtleBody;
import org.janusproject.jaak.envinterface.body.TurtleBodyFactory;
import org.janusproject.jaak.turtle.Turtle;

/**
 *
 * @author Benjamin
 */
public class FestivalEntity extends Turtle
{
    
    // ### Différent états d'actions du festivalier
        
        public static final String INIT = "INIT"; 
        
        public static final String WANDERING = "WANDERING";
       
        public static final String MARCHE_VERS_DESTINATION = "Марцхер верс ун концерт";
        public static final String MARCHE_VERS_CONCERT = "MARCHE_VERS_CONCERT";
        public static final String MARCHE_VERS_TOILETTES = "MARCHE_VERS_TOILETTES";
        public static final String MARCHE_VERS_NOURRITURE = "MARCHE_VERS_NOURRITURE";
        
            // On touche une substance
            public static final String CHERCHE_PLACE_PROCHE_SCENE = "цхерцхер уне место процхе де ла сцене";
        
            // On capte une file d'attente devant notre objectif
            public static final String REMONTE_FILE_ATTENTE = "REMONTE_FILE_ATTENTE";
            public static final String EN_ATTENTE = "ср_аттенте";
        // On effectue l'action
        public static final String EN_ACTION = "EN_ACTION"; // 
            public static final String MANGER = "MANGER"; // 
            public static final String FAIRE_SES_BESOINS = "FAIRE_SES_BESOINS"; // 
            public static final String ECOUTER_CONCERT = "ECOUTER_CONCERT"; // 
    // ### END
            
    
    protected String _currentState = "";
    protected Point2i _currentDestination;
    protected Construction _currentConstructDestination;
    
    protected FestivalMap _carteFestival;
    
    
    protected TurtleSemantic _informationsTurtle;
    
    public FestivalEntity() {
        super();
        
        _currentState = WANDERING;
        
    }
    
    @Override
    protected TurtleBody createTurtleBody(TurtleBodyFactory factory) {
        TurtleBody turtleBody = factory.createTurtleBody(getAddress());
        turtleBody.setSemantic(_informationsTurtle);
        
        return turtleBody;
    }
    protected boolean hasReachedDestination() {
        return this.getPosition().equals( _currentDestination);
    }
    
    
    protected boolean isInFrontOf(Point2i relativePoint) { // Si la pos relative de la tortue est en face de moi
        ArrayList<Point2i> casesInFrontOfMe = getCasesInFrontOfMe();
        return casesInFrontOfMe.contains(relativePoint);
    }
    
    
    
    protected ArrayList<Point2i> getCasesInFrontOfMe() { // Retourne les 3 cases sur ma droite
        ArrayList<Point2i> result = new ArrayList<Point2i>();
        Direction sensActuel = Direction.getSens(getHeadingVector());
        
        result.add(sensActuel.getPrevious().toRelativePoint());
        result.add(sensActuel.toRelativePoint());
        result.add(sensActuel.getNext().toRelativePoint());
        
        return result;
    }
    
    protected void applyPathfinding() {
            
            // Lucie, implante ton A* ici :)
            
            
            // System.out.println("Position : "+getPosition().getX()+" "+getPosition().getY());
           /* Collection<PerceivedTurtle> perceivedTurtles = getPerceivedTurtles();
            for (PerceivedTurtle turtle : perceivedTurtles)
            {
                System.out.println(turtle.getHeadingVector()+" "+turtle.getRelativePosition(getTurtleBody())+" "+((isInFrontOf(turtle.getRelativePosition(this.getTurtleBody())))?"true":"false"));
                //(TurtleBody)turtle.getSemantic()).getLastMotionInfluenceStatus();
               /*if(p.isTurtle())
                {
                    
                   // System.out.println(p.getSemantic());//turtle.getHeadingVector()+" "+this.getHeadingVector());
                   // if(turtle.getPosition())
                }
            }*/
            Point2i seekPosition = (_currentConstructDestination == null)? _currentDestination:_currentConstructDestination.getInteractCenter();//_currentDestination;
            Point2i position = this.getPosition();
            if(!seekPosition.equals(position)) {
                  Vector2f direction = new Vector2f();
                  direction.sub(seekPosition, position);
                  direction.normalize();
                  this.setHeading(direction);
                  moveForward(1);
            }
            moveForward(1);
            
        }
    
    
    protected void chooseToGoToARandomDestination() {
            _currentConstructDestination = _carteFestival.getRandomDestination();
                    if(_currentConstructDestination instanceof Scene)
                        _currentState = MARCHE_VERS_CONCERT;
                    else
                        _currentState = MARCHE_VERS_DESTINATION;
    }
    
    protected void chooseToGoToARandomConcert() {
            _currentConstructDestination = _carteFestival.getRandomConcerts();
                    if(_currentConstructDestination instanceof Scene)
                        _currentState = MARCHE_VERS_CONCERT;
                    else
                        _currentState = MARCHE_VERS_DESTINATION;
    }
    
    
    public void setCarteFestival(FestivalMap _carteFestival) {
        this._carteFestival = _carteFestival;
    }
    
}
