package fr.utbm.gi.vi51.project.agent;

import fr.utbm.gi.vi51.project.environment.Construction;
import fr.utbm.gi.vi51.project.environment.FestivalMap;
import fr.utbm.gi.vi51.project.environment.PumpRoom;
import fr.utbm.gi.vi51.project.environment.Scene;
import fr.utbm.gi.vi51.project.environment.WaterClosed;
import fr.utbm.gi.vi51.project.environment.food.Food;
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
import org.janusproject.jaak.turtle.Turtle;

public class FestivalGoer extends FestivalEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5328824872764860436L;
        private static int i = 0;
        
        
        
        private long _jaugeDeFaimTotale; // Durée au bout de laquelle le joueur a faim en ms
        private long _jaugeDeFaimActuelle; // Quand >= _jaugeDeFaimTotale, j'ai faim
        
        private long _jaugeDeVessieTotale; // Durée au bout de laquelle le joueur a besoin de faire pipi en ms
        private long _jaugeDeVessieActuelle; // Quand >= jaugeVessieActuelle, j'ai besoin de faire pipi sur une barrière
        
        private Food _nourriture;
        
        private int _money;
        
        
        
	public FestivalGoer() {
		super();
               // this.getTurtleBody()
                
                _money = RandomUtils.getRand(0, 100);
                
                _currentState = WANDERING;
                
                _jaugeDeFaimTotale = RandomUtils.getRand(1*3*1000, 1*10*1000);
                _jaugeDeFaimActuelle = 0;
                
                _jaugeDeVessieTotale = RandomUtils.getRand(1*3*1000, 1*10*1000);
                _jaugeDeVessieActuelle = 0;
                
                _informationsTurtle = new TurtleSemantic(this);
                
                
               i++;
              /*if(true)//i == 50)//i%50 == 0)
              {
                 _currentDestination = new Point2i(10,60);
                // _currentState = MARCHE_VERS_DESTINATION;
              }*/
                 
            
	}
	
	
	
	@Override
	protected void turtleBehavior() {
            /**/
            Collection<Perceivable> perception = getPerception();
            
            JaakTimeManager jaakTimeManager = getJaakTimeManager();
              
            _jaugeDeFaimActuelle += jaakTimeManager.getWaitingDuration();
            _jaugeDeVessieActuelle += jaakTimeManager.getWaitingDuration();
            
            
            if(!( _currentState == MARCHE_VERS_NOURRITURE || _currentState == MARCHE_VERS_TOILETTES)) // Si je n'ai pas déjà un but de ce genre
            {
                if(_jaugeDeVessieActuelle >= _jaugeDeVessieTotale)
                {
                   System.out.println("Aaaahh pipiii !");
                   _currentState = MARCHE_VERS_TOILETTES;
                   _currentConstructDestination = _carteFestival.getRandomToilet();//_carteFestival.getNearestToilet();
                }
                else if(_jaugeDeFaimActuelle >= _jaugeDeFaimTotale) // Pipi > Faim dans la décision (voir pour mettre une proba de choix ?)
                {
                    if(_nourriture != null)
                    {
                        if(_nourriture.canEat())
                        {
                            _jaugeDeFaimActuelle -= _nourriture.eat();
                        }
                        else
                        {
                            // Décider si on jette par terre les déchets ou si on va vers une poubelle
                            dropOff(_nourriture);
                            _nourriture = null;
                        }
                    }
                    else
                    {
                        System.out.println("MON DIEU J'AI FAIIIIM");
                        _currentState = MARCHE_VERS_NOURRITURE;
                        _currentConstructDestination =  _carteFestival.getRandomFoodStand();//_carteFestival.getNearestFoodStand(); 
                    }
                    
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
                    //System.out.println("Relative point "+ relativePoint);
                    
                    if(casesInFrontOfMe.contains(relativePoint))
                    {
                        if(_currentConstructDestination instanceof WaterClosed)
                        {
                            _jaugeDeVessieActuelle = 0;
                            System.out.println("Ahhhh ça va mieux :)");
                            chooseToGoToARandomConcert();
                        }
                        else if(_currentConstructDestination instanceof PumpRoom)
                        {
                            Food tmpNourriture = ((PumpRoom)_currentConstructDestination).buyFood();
                            if(_money - tmpNourriture.getPrice() >= 0)
                            {
                                _nourriture = tmpNourriture;
                                _money -= _nourriture.getPrice();
                                 _jaugeDeFaimActuelle = 0;
                                System.out.println("Ahhhh j'ai bien mangé :)");
                                chooseToGoToARandomConcert();
                            }
                                
                                
                           
                        }
                    }
                    
                    applyPathfinding();
                    
                    
                    
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
                    
                   /* Collection<PerceivedTurtle> perceivedTurtles = getPerceivedTurtles();
                    for (PerceivedTurtle turtle : perceivedTurtles)
                    {
                        //System.out.println(turtle.getHeadingVector()+" "+turtle.getRelativePosition(getTurtleBody())+" "+((isInFrontOf(turtle.getRelativePosition(this.getTurtleBody())))?"true":"false"));
                        if(isInFrontOf(turtle.getRelativePosition(this.getTurtleBody())))
                        {
                            Direction sensTurtle = Direction.getSens(turtle.getHeadingVector());
                           // setHeading(sensTurtle.toFloat());//.getOpposite()
                        }
                        
                        
                    }*/
                    
                    
                    //System.out.println(Direction.getSens(getHeadingVector()).toString()+" "+getHeadingVector());
                    
                    
                    chooseToGoToARandomConcert();
                    
                    break;
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
        
           
        
        
        public boolean carryingFood() {
            return this._nourriture != null;
        }
        
        
        

}
