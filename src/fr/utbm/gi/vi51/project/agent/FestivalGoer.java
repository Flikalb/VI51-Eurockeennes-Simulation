package fr.utbm.gi.vi51.project.agent;

import fr.utbm.gi.vi51.project.environment.Construction;
import fr.utbm.gi.vi51.project.environment.FestivalMap;
import fr.utbm.gi.vi51.project.environment.PumpRoom;
import fr.utbm.gi.vi51.project.environment.Scene;
import fr.utbm.gi.vi51.project.environment.WaterClosed;
import org.janusproject.jaak.envinterface.body.TurtleBody;
import org.janusproject.jaak.envinterface.body.TurtleBodyFactory;

import fr.utbm.gi.vi51.project.utils.RandomUtils;
import java.util.ArrayList;
import java.util.Collection;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.discrete.object2d.Point2i;
import org.janusproject.jaak.envinterface.perception.Perceivable;
import org.janusproject.jaak.envinterface.perception.PerceivedTurtle;
import org.janusproject.jaak.envinterface.time.JaakTimeManager;

public class FestivalGoer extends FestivalAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5328824872764860436L;
        private static int i = 0;
        
        
        
        private long jaugeDeFaimTotale; // Durée au bout de laquelle le joueur a faim en ms
        private long jaugeDeFaimActuelle; // Quand >= jaugeDeFaimTotale, j'ai faim
        
        private long jaugeDeVessieTotale; // Durée au bout de laquelle le joueur a besoin de faire pipi en ms
        private long jaugeDeVessieActuelle; // Quand >= jaugeVessieActuelle, j'ai besoin de faire pipi sur une barrière
        
        // ### Différent états d'actions du festivalier
        
        private static final String WANDERING = "WANDERING";
       
        private static final String MARCHE_VERS_DESTINATION = "Марцхер верс ун концерт";
        private static final String MARCHE_VERS_CONCERT = "MARCHE_VERS_CONCERT";
        private static final String MARCHE_VERS_TOILETTES = "MARCHE_VERS_TOILETTES";
        private static final String MARCHE_VERS_NOURRITURE = "MARCHE_VERS_NOURRITURE";
        
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
                
                jaugeDeFaimTotale = RandomUtils.getRand(1*10*1000, 1*10*1000);
                jaugeDeFaimActuelle = 0;
                
                jaugeDeVessieTotale = RandomUtils.getRand(1*10*1000, 1*10*1000);
                jaugeDeVessieActuelle = 0;
                
                
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
            
            JaakTimeManager jaakTimeManager = getJaakTimeManager();
            jaugeDeFaimActuelle += jaakTimeManager.getWaitingDuration();
            jaugeDeVessieActuelle += jaakTimeManager.getWaitingDuration();
            
            
            if(!( _currentState == MARCHE_VERS_NOURRITURE || _currentState == MARCHE_VERS_TOILETTES)) // Si je n'ai pas déjà un but de ce genre
            {
                if(jaugeDeVessieActuelle >= jaugeDeVessieTotale)
                {
                   System.out.println("Aaaahh pipiii !");
                   _currentState = MARCHE_VERS_TOILETTES;
                   _currentConstructDestination = _carteFestival.getRandomToilet();//_carteFestival.getNearestToilet();
                }
                else if(jaugeDeFaimActuelle >= jaugeDeFaimTotale) // Pipi > Faim dans la décision (voir pour mettre une proba de choix ?)
                {
                    System.out.println("MON DIEU J'AI FAIIIIM");
                    _currentState = MARCHE_VERS_NOURRITURE;
                    _currentConstructDestination =  _carteFestival.getRandomFoodStand();//_carteFestival.getNearestFoodStand();
                }
            }
                
            
            
                
            
            switch(_currentState)
            {
                case EN_ATTENTE:
                    attendreSonTour(perception, _currentDestination);
                    break;
                    
                case MARCHE_VERS_CONCERT:
                    
                    //if(currentPosition has substance from _currentDestination Concert)
                    //{
                    //  _currentState = CHERCHE_PLACE_PROCHE_SCENE;
                    //  break;
                    // }
                    // Pas de break ici pour effectuer le cas applyPathfinding normal à la suite
                case MARCHE_VERS_DESTINATION:
                case MARCHE_VERS_TOILETTES:
                case MARCHE_VERS_NOURRITURE:
                    
                    ArrayList<Point2i> casesInFrontOfMe = getCasesInFrontOfMe();
                    Point2i relativePoint = new Point2i(_currentConstructDestination.getInteractCenter().getX() - getX(), _currentConstructDestination.getInteractCenter().getY() - getY());
                    System.out.println("Relative point "+ relativePoint);
                    
                    if(casesInFrontOfMe.contains(relativePoint))
                    {
                        if(_currentConstructDestination instanceof WaterClosed)
                        {
                            jaugeDeVessieActuelle = 0;
                            System.out.println("Ahhhh ça va mieux :)");
                            chooseToGoToARandomConcert();
                        }
                        else if(_currentConstructDestination instanceof PumpRoom)
                        {
                            jaugeDeFaimActuelle = 0;
                            System.out.println("Ahhhh j'ai bien mangé :)");
                            chooseToGoToARandomConcert();
                        }
                    }
                    
                    applyPathfinding(perception);
                    
                    
                    
                    break;
                    
                    
                case WANDERING:
                //default:
                    this.setHeading(this.getHeadingAngle()+RandomUtils.randomBinomial((float)Math.PI/4));
                   // this.setHeading(new Vector2f(1,-1));
                    moveForward(1);
                    //ArrayList<Point2i> casesInFrontOfMe = getCasesInFrontOfMe();
                   /* System.out.println("Position : "+Direction.getSens(getHeadingVector()));
                    for(Point2i point : casesInFrontOfMe)
                    {
                        System.out.println("Cases : "+point);
                    }*/
                    
                    Collection<PerceivedTurtle> perceivedTurtles = getPerceivedTurtles();
                    for (PerceivedTurtle turtle : perceivedTurtles)
                    {
                        //System.out.println(turtle.getHeadingVector()+" "+turtle.getRelativePosition(getTurtleBody())+" "+((isInFrontOf(turtle.getRelativePosition(this.getTurtleBody())))?"true":"false"));
                        if(isInFrontOf(turtle.getRelativePosition(this.getTurtleBody())))
                        {
                            Direction sensTurtle = Direction.getSens(turtle.getHeadingVector());
                           // setHeading(sensTurtle.toFloat());//.getOpposite()
                        }
                        
                        
                    }
                    
                    
                    //System.out.println(Direction.getSens(getHeadingVector()).toString()+" "+getHeadingVector());
                    
                    
                    chooseToGoToARandomConcert();
                    
                    break;
            }
	}

        private void applyPathfinding(Collection<Perceivable> perception) {
            
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

        private void attendreSonTour(Collection<Perceivable> perception, Point2i destination) {
            if(hasReachedDestination()){
                // Apply action
                // Return to wandering state
                _currentState = WANDERING;
                return;
            }
            
        }
        
        
        
        
        private void chooseToGoToARandomConcert() {
            _currentConstructDestination = _carteFestival.getRandomConcerts();//_carteFestival.getRandomDestination();
                    if(_currentConstructDestination instanceof Scene)
                        _currentState = MARCHE_VERS_CONCERT;
                    else
                        _currentState = MARCHE_VERS_DESTINATION;
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
