package fr.utbm.gi.vi51.project.agent;

import org.janusproject.jaak.envinterface.body.TurtleBody;
import org.janusproject.jaak.envinterface.body.TurtleBodyFactory;

import fr.utbm.gi.vi51.project.utils.RandomUtils;
import java.util.Collection;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.discrete.object2d.Point2i;
import org.janusproject.jaak.envinterface.perception.Perceivable;

public class FestivalGoer extends FestivalAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5328824872764860436L;
        private static int i = 1;
        
        private static final String EN_ATTENTE = "ср_аттенте";
        private static final String MARCHE_VERS_DESTINATION = "Марцхер верс ун концерт";
        private static final String CHERCHE_PLACE_PROCHE_SCENE = "цхерцхер уне место процхе де ла сцене";
        private static final String WANDERING = "WANDERING";
        
        
        private String _currentState = "";
        private Point2i _currentDestination;
        
        
	public FestivalGoer() {
		super();
               // this.getTurtleBody()
               i++;
              if(i%50 == 0)
              {
                 _currentDestination = new Point2i(60,60);
                 _currentState = MARCHE_VERS_DESTINATION;
              }
                 
            
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
                    applyPathfinding();
                    break;
                    
                    
                case WANDERING:
                    this.setHeading(this.getHeadingAngle()+RandomUtils.randomBinomial((float)Math.PI/4));
                    moveForward(1);
                    break;
            }
	}

        private void applyPathfinding() {
            Point2i seekPosition = _currentDestination;
            Point2i position = this.getPosition();
            if(!seekPosition.equals(position)) {
                  Vector2f direction = new Vector2f();
                  direction.sub(seekPosition, position);
                  direction.normalize();
                  this.setHeading(direction);
                  moveForward(1);
            }
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

}
