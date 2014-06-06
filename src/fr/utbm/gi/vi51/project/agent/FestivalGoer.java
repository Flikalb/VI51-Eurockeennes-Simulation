package fr.utbm.gi.vi51.project.agent;

import fr.utbm.gi.vi51.project.environment.Construction;
import fr.utbm.gi.vi51.project.environment.FestivalMap;
import org.janusproject.jaak.envinterface.body.TurtleBody;
import org.janusproject.jaak.envinterface.body.TurtleBodyFactory;

import fr.utbm.gi.vi51.project.utils.RandomUtils;
import java.util.ArrayList;
import java.util.Collection;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.discrete.object2d.Point2i;
import org.janusproject.jaak.envinterface.perception.Perceivable;
import org.janusproject.jaak.envinterface.perception.PerceivedTurtle;

public class FestivalGoer extends FestivalAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5328824872764860436L;
        private static int i = 0;
        
        
        // ### Différent états du festivalier
        
        private static final String WANDERING = "WANDERING";
       
        private static final String MARCHE_VERS_DESTINATION = "Марцхер верс ун концерт";
        
            // On touche une substance
            private static final String CHERCHE_PLACE_PROCHE_SCENE = "цхерцхер уне место процхе де ла сцене";
        
            // On capte une file d'attente devant notre objectif
            private static final String REMONTE_FILE_ATTENTE = "REMONTE_FILE_ATTENTE";
            private static final String EN_ATTENTE = "ср_аттенте";
        // On effectue l'action
        private static final String EN_ACTION = "EN_ACTION"; // 
            private static final String MANGER = "MANGER"; // 
            private static final String FAIRE_SES_BESOINS = "FAIRE_SES_BESOINS"; // 
            private static final String ECOUTER_CONCERT = "ECOUTER_CONCERT"; // 
        // ### END
        
        
        private String _currentState = "";
        private Point2i _currentDestination;
        private Construction _currentConstructDestination;
        
        private FestivalMap _carteFestival;

    
        
        
        
        
	public FestivalGoer() {
		super();
               // this.getTurtleBody()
                
                _currentState = WANDERING;
                
                
               i++;
              /*if(true)//i == 50)//i%50 == 0)
              {
                 _currentDestination = new Point2i(10,60);
                // _currentState = MARCHE_VERS_DESTINATION;
              }*/
                 
            
	}
	
	@Override
	protected TurtleBody createTurtleBody(TurtleBodyFactory factory) {
            
            
		return factory.createTurtleBody(getAddress());
	}
	
	@Override
	protected void turtleBehavior() {
            /**/
            Collection<Perceivable> perception = getPerception();
            
            
            
            
            switch(_currentState)
            {
                case EN_ATTENTE:
                    attendreSonTour(perception, _currentDestination);
                    break;
                    
                case MARCHE_VERS_DESTINATION:
                    applyPathfinding(perception);
                    break;
                    
                    
                case WANDERING:
                //default:
                    this.setHeading(this.getHeadingAngle()+RandomUtils.randomBinomial((float)Math.PI/4));
                   // this.setHeading(new Vector2f(1,-1));
                    moveForward(1);
                    ArrayList<Point2i> casesInFrontOfMe = getCasesInFrontOfMe();
                    System.out.println("Position : "+Direction.getSens(getHeadingVector()));
                    for(Point2i point : casesInFrontOfMe)
                    {
                        System.out.println("Cases : "+point);
                    }
                    
                    Collection<PerceivedTurtle> perceivedTurtles = getPerceivedTurtles();
                    for (PerceivedTurtle turtle : perceivedTurtles)
                    {
                        System.out.println(turtle.getHeadingVector()+" "+turtle.getRelativePosition(getTurtleBody())+" "+((isInFrontOf(turtle.getRelativePosition(this.getTurtleBody())))?"true":"false"));
                        if(isInFrontOf(turtle.getRelativePosition(this.getTurtleBody())))
                        {
                            Direction sensTurtle = Direction.getSens(turtle.getHeadingVector());
                           // setHeading(sensTurtle.toFloat());//.getOpposite()
                        }
                        
                        
                    }
                    
                    
                    System.out.println(Direction.getSens(getHeadingVector()).toString()+" "+getHeadingVector());
                    
                    
                    _currentConstructDestination = _carteFestival.getRandomDestination();
                    _currentState = MARCHE_VERS_DESTINATION;
                    
                    break;
            }
	}

        private void applyPathfinding(Collection<Perceivable> perception) {
            // System.out.println("Position : "+getPosition().getX()+" "+getPosition().getY());
            Collection<PerceivedTurtle> perceivedTurtles = getPerceivedTurtles();
            for (PerceivedTurtle turtle : perceivedTurtles)
            {
                System.out.println(turtle.getHeadingVector()+" "+turtle.getRelativePosition(getTurtleBody())+" "+((isInFrontOf(turtle.getRelativePosition(this.getTurtleBody())))?"true":"false"));
                //(TurtleBody)turtle.getSemantic()).getLastMotionInfluenceStatus();
               /*if(p.isTurtle())
                {
                    
                   // System.out.println(p.getSemantic());//turtle.getHeadingVector()+" "+this.getHeadingVector());
                   // if(turtle.getPosition())
                }*/
            }
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

        private void attendreSonTour(Collection<Perceivable> perception, Point2i destination) {
            if(hasReachedDestination()){
                // Apply action
                // Return to wandering state
                _currentState = WANDERING;
                return;
            }
            
        }
        
        
        private boolean hasReachedDestination() {
            return this.getPosition().equals( _currentDestination);
        }
        
        
        private boolean isInFrontOf(Point2i relativePoint) { // Si la pos relative de la tortue est en face de moi
            ArrayList<Point2i> casesInFrontOfMe = getCasesInFrontOfMe();
            return casesInFrontOfMe.contains(relativePoint);
        }
        
        
        
        private ArrayList<Point2i> getCasesInFrontOfMe() { // Retourne les 3 cases sur ma droite
           ArrayList<Point2i> result = new ArrayList<Point2i>();
           Direction sensActuel = Direction.getSens(getHeadingVector());
           
           result.add(sensActuel.getPrevious().toRelativePoint());
           result.add(sensActuel.toRelativePoint());
           result.add(sensActuel.getNext().toRelativePoint());
           
           return result;
        }
        
        
        
        public void setCarteFestival(FestivalMap _carteFestival) {
            this._carteFestival = _carteFestival;
        }

}
