/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utbm.gi.vi51.project.agent;

import fr.utbm.gi.vi51.project.environment.Construction;
import fr.utbm.gi.vi51.project.environment.FestivalMap;
import fr.utbm.gi.vi51.project.environment.Scene;
import fr.utbm.gi.vi51.project.environment.obstacles.ObstacleScene;
import fr.utbm.gi.vi51.project.utils.Astar;
import fr.utbm.gi.vi51.project.utils.RandomUtils;

import java.util.ArrayList;

import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.discrete.object2d.Point2i;
import org.janusproject.jaak.envinterface.body.TurtleBody;
import org.janusproject.jaak.envinterface.body.TurtleBodyFactory;
import org.janusproject.jaak.envinterface.frustum.SquareTurtleFrustum;
import org.janusproject.jaak.envinterface.influence.MotionInfluenceStatus;
import org.janusproject.jaak.envinterface.perception.EnvironmentalObject;
import org.janusproject.jaak.turtle.Turtle;

/**
 *
 * @author Benjamin
 */
public class FestivalEntity extends Turtle
{
    
    // ### Différent états d'actions du festivalier
    
    /**
     *
     */
    private static final long serialVersionUID = -1164713901331040192L;
    
    
    
    public static final String INIT = "INIT";
    
    public static final String WANDERING = "WANDERING";
    
    public static final String MARCHE_VERS_DESTINATION = "MARCHE_VERS_DESTINATION";
    public static final String MARCHE_VERS_CONCERT = "MARCHE_VERS_CONCERT";
    public static final String MARCHE_VERS_TOILETTES = "MARCHE_VERS_TOILETTES";
    public static final String MARCHE_VERS_NOURRITURE = "MARCHE_VERS_NOURRITURE";
    public static final String MARCHE_VERS_DECHET = "MARCHE_VERS_DECHET";
    
    
    public static final String FOLLOW_PATH = "FOLLOW_PATH";
    
    
    // On touche une substance
    public static final String CHERCHE_PLACE_PROCHE_SCENE = "CHERCHE_PLACE_PROCHE_SCENE";
    
    // On capte une file d'attente devant notre objectif
    public static final String REMONTE_FILE_ATTENTE = "REMONTE_FILE_ATTENTE";
    public static final String EN_ATTENTE = "EN_ATTENTE";
    // On effectue l'action
    public static final String EN_ACTION = "EN_ACTION"; //
    public static final String MANGER = "MANGER"; //
    public static final String FAIRE_SES_BESOINS = "FAIRE_SES_BESOINS"; //
    
    public static final String APPROCHER_SCENE = "APPROCHER_SCENE"; //
    public static final String ECOUTER_CONCERT = "ECOUTER_CONCERT"; //
    
    public static final String ARRIVED_ON_OBJECTIVE = "ARRIVED_ON_OBJECTIVE"; //
    
    
    public static final String RUN_AWAY = "RUN_AWAY";
    
    public static final String LEAVE_EUROCKS = "LEAVE_EUROCKS";
    // ### END
    
    
    private String _currentState = "";
    protected Point2i _currentDestination;
    protected Construction _currentConstructDestination;
    protected ArrayList<Point2i> _currentPath;
    
    protected Point2i _previousPosition;
    protected boolean _isOnSamePosition;
    
    protected FestivalMap _carteFestival;
    
    
    protected TurtleSemantic _informationsTurtle;
    
    protected SquareTurtleFrustum _frustum;
    
    
    protected int _nbFailMoves = 0;
    
    public FestivalEntity() {
        super();
        //System.out.println("suicide : "+canCommitSuicide());
        setCommitSuicide(true);
        _informationsTurtle = new TurtleSemantic(this);
        changeCurrentState(WANDERING);
        
    }
    
    @Override
    protected TurtleBody createTurtleBody(TurtleBodyFactory factory) {
        TurtleBody turtleBody = factory.createTurtleBody(getAddress(), _frustum);
        if(turtleBody != null && _informationsTurtle != null)
            turtleBody.setSemantic(_informationsTurtle);
        
        return turtleBody;
    }
    
    
    
    public void changeCurrentState(String state, boolean removeDestinations) {
       _currentState = state;
       if(removeDestinations)
       {
            _currentPath = null;
            _currentConstructDestination = null;
            _currentDestination = null; 
       }
    }
    public void changeCurrentState(String state) {
        changeCurrentState(state, true);
    }
    
    
     public String getCurrentState() {
        return _currentState;
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
    
    
    protected void moveToDestination() {
        
        
        Point2i seekPosition = (_currentConstructDestination == null)? _currentDestination:_currentConstructDestination.getInteractCenter();
        if(seekPosition == null) return;
        float distance = seekPosition.distance(getPosition());
        //System.out.println("rel : "+distance+" "+seekPosition+" "+getPosition());
        if(distance == 1.0) // Arrive sur l'objectif
        {
            Point2i relativePoint = new Point2i(seekPosition.getX() - this.getPosition().getX(),seekPosition.getY()-this.getPosition().getY());
            System.out.println("OK DISTANCE "+relativePoint);
            move(relativePoint.getX(), relativePoint.getY(), true);
            
            changeCurrentState(ARRIVED_ON_OBJECTIVE);
            
            return;
        }
        
        if(distance < 10.0)
        {
            _currentPath = null;
        }
        
        
        
        // Si on a déjà calculé un pathfinding
        if(_currentPath != null)
        {
            gotoNextNodeInPath(); // On le suit
            return;
        }
        
        applySeek(); // Sinon on seek jusqu'à ce que l'on soit bloqué
        
        if(_isOnSamePosition)
        {
            //System.out.println("On est bloqué :(");
            Point2i endPosition = seekPosition;
            _currentPath = Astar.findPath(this.getPosition(), endPosition, 100);
            if(_currentPath != null && _currentPath.size() > 0)
                _currentPath.remove(0);
            
        }
        
    }
    
    protected void applySeek() {
        
        Point2i seekPosition = (_currentConstructDestination == null)? _currentDestination:_currentConstructDestination.getInteractCenter();
        Point2i position = this.getPosition();
        
        if(seekPosition != null && !position.equals(seekPosition)) {
            Vector2f direction = new Vector2f();
            direction.sub(seekPosition, position);
            direction.normalize();
            this.setHeading(direction);
            moveForward(1);
        }
        
    }
    
    
    
    
    protected boolean checkFailMove()
    {
        if(_isOnSamePosition)
            _nbFailMoves++;
        else
            _nbFailMoves = 0;
        if(_nbFailMoves > 20)
        {
            goToAPlayingConcert();
            return true;
        }
        if(_nbFailMoves > 10)
        {
            Direction randomDir = Direction.getRandom();
            //System.out.println("Je suis bloqué :'("+randomDir.toString());
            setHeading(randomDir.toFloat());
            moveForward(1);
            return true;
        }
        return false;
    }
    
    
    
    
    protected void applyFlee() {
        
        Point2i fleePosition = (_currentConstructDestination == null)? _currentDestination:_currentConstructDestination.getInteractCenter();
        Point2i position = this.getPosition();
        if(fleePosition != null && !position.equals(fleePosition)) {
            Vector2f direction = new Vector2f();
            direction.sub(position, fleePosition);
            direction.normalize();
            this.setHeading(direction);
            moveForward(1);
        }
        
    }
    
    
    protected void runAway() {
        
        
        applyFlee();
        Vector2f direction = new Vector2f();
        if(_currentDestination == null)
        {
            //System.out.println("fail");
            leaveEurocks();
            return ;
        }
        direction.sub(_currentDestination,this.getPosition());
        if(direction.length()<10) {
            applyFlee();
        }
        else {
            leaveEurocks();
        }
        
    }
    
    
    protected void gotoNextNodeInPath() {
        if(_currentPath == null || _currentPath.size() == 0)
            return;
        Point2i p = _currentPath.remove(0);
        if(_currentPath.size() == 0)
            _currentPath = null;
        Vector2f direction = new Vector2f();
        direction.sub(p,this.getPosition());
        direction.normalize();
        this.setHeading(direction);
        moveForward(1);
    }
    
    
    /*protected void followPath(ArrayList<Point2i> path) {
     * for(Point2i p : path) {
     * Vector2f direction = new Vector2f();
     * direction.sub(p,this.getPosition());
     * direction.normalize();
     * this.setHeading(direction);
     * moveForward(1);
     * }
     * }*/
    
    
    protected boolean arrived() {
        Point2i seekPosition = (_currentConstructDestination == null)? _currentDestination:_currentConstructDestination.getInteractCenter();
        if(seekPosition == null) return false;
        return seekPosition.equals(getPosition());
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
    
    protected void goToAPlayingConcert() {
        _currentConstructDestination = _carteFestival.getPlayingConcerts();
        if(_currentConstructDestination != null)
            _currentState = MARCHE_VERS_CONCERT;
        else
            leaveEurocks();
    }
    
    protected void goTo(Point2i destination) {
        changeCurrentState(MARCHE_VERS_DESTINATION);
        _currentDestination = destination;
    }
    
    protected void leaveEurocks() {
        changeCurrentState(LEAVE_EUROCKS);
        _currentDestination = new Point2i(159,16);
    }
    
    
    public void setCarteFestival(FestivalMap _carteFestival) {
        this._carteFestival = _carteFestival;
    }
    
    
    public void kill() {
        killMe();
    }
    
}
