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

    private static final SquareTurtleFrustum frus = new SquareTurtleFrustum(5000);
    
    public static final String INIT = "INIT";
    
    public static final String WANDERING = "WANDERING";
    
    public static final String MARCHE_VERS_DESTINATION = "MARCHE_VERS_DESTINATION";
    public static final String MARCHE_VERS_CONCERT = "MARCHE_VERS_CONCERT";
    public static final String MARCHE_VERS_TOILETTES = "MARCHE_VERS_TOILETTES";
    public static final String MARCHE_VERS_NOURRITURE = "MARCHE_VERS_NOURRITURE";
    public static final String MARCHE_VERS_DECHET = "MARCHE_VERS_DECHET";
    
    
    // On touche une substance
    public static final String CHERCHE_PLACE_PROCHE_SCENE = "CHERCHE_PLACE_PROCHE_SCENE";
    
    // On capte une file d'attente devant notre objectif
    public static final String REMONTE_FILE_ATTENTE = "REMONTE_FILE_ATTENTE";
    public static final String EN_ATTENTE = "EN_ATTENTE";
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
    
    
    private int _nbFailMoves = 0;
    
    public FestivalEntity() {
        super();
        
        _currentState = WANDERING;
        
    }
    
    @Override
    protected TurtleBody createTurtleBody(TurtleBodyFactory factory) {
        TurtleBody turtleBody = factory.createTurtleBody(getAddress(),frus);
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
        applySeek();
        return;
        // Lucie, implante ton A* ici :)
        
       /* Point2i endPosition = (_currentConstructDestination == null)? _currentDestination:_currentConstructDestination.getInteractCenter();
        System.out.println("endPosition "+endPosition+" "+this.getPosition());
        ArrayList<Point2i> path = Astar.findPath(this.getPosition(), endPosition);
        System.out.println(path);
        if(path != null)
            followPath(path);*/
        
        
    }
    
    protected void applySeek() {
    	
    	
    	/*Point2i seekPosition = new Point2i();
    	//for(EnvironmentalObject p : getPerceivedObjects()) {
    	for(EnvironmentalObject p : getPerceivedObjects()) 
    	{
    		
    			if(p instanceof ObstacleScene)
    			{ 
    				ObstacleScene temp= (ObstacleScene)p;
    				//System.out.print(p.getPosition());
    					// C'est ici qu'il faut mettre les arbres de d�cision relatifs aux d�cisions 
    					// concernant les concerts
    				
    				
    					
    						if(temp.getScene().getisplaying())
    						{
    							seekPosition=(temp.getScene().getEmissionPosition());
    							//System.out.println(seekPosition.toString()); debug
    							break;
    						}
    			}
		}
    	*/ 
    	
    	// COYOTE, VOICI LA METHODE PERMETTANT DE RECUPERER L ACTIVItE D UN CONCERT DONNE
    	// TU PEUX M EXPLIQUER A L OCASSE CE QUE T AS FAIT? J AI DU MAL A PERCUTER :)
    	// BISOUS

        Point2i seekPosition = (_currentConstructDestination == null)? _currentDestination:_currentConstructDestination.getInteractCenter();
        Point2i position = this.getPosition();
        
        System.out.println("seekPosition : "+seekPosition);
       
        
        
        
        // A VOIR ICI POUR TRUQUER LE SEEK POUR EVITER LES OBSTACLES
        // Le précédent seek a été un gros fail
        MotionInfluenceStatus lastMotionInfluenceStatus = getLastMotionInfluenceStatus();
        if(lastMotionInfluenceStatus != null && lastMotionInfluenceStatus.isFailure())
        {
            _nbFailMoves++;
            System.out.println("FAIL");
            
            if(_nbFailMoves < 2)
                seekPosition.setY(getPosition().y());
            else if(_nbFailMoves < 5)
                seekPosition.setX(getPosition().x());
            else if(_nbFailMoves < 10)
            {
                _currentState = WANDERING;
                _nbFailMoves = 0;
            }
            
        }
        else
            _nbFailMoves = 0;
        
       
    	if(!seekPosition.equals(position)) {
            Vector2f direction = new Vector2f();
            direction.sub(seekPosition, position);
            direction.normalize();
            this.setHeading(direction);
            moveForward(1);
        }
        
        
         
    }
    
    
    protected void applyFlee() {
        
       Point2i fleePosition = (_currentConstructDestination == null)? _currentDestination:_currentConstructDestination.getInteractCenter();
        Point2i position = this.getPosition();
        if(!fleePosition.equals(position)) {
            Vector2f direction = new Vector2f();
            direction.sub(position, fleePosition);
            direction.normalize();
            this.setHeading(direction);
            moveForward(1); 
        }
        
    }
    
    
    
    protected void followPath(ArrayList<Point2i> path) {
        for(Point2i p : path) {
            Vector2f direction = new Vector2f();
            direction.sub(p,this.getPosition());
            direction.normalize();
            this.setHeading(direction);
            moveForward(1);
        }
    }
    
    
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
    
    
    public void setCarteFestival(FestivalMap _carteFestival) {
        this._carteFestival = _carteFestival;
    }
    
}
