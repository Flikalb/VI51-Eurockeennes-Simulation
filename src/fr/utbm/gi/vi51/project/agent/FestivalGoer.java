package fr.utbm.gi.vi51.project.agent;

import static fr.utbm.gi.vi51.project.agent.FestivalEntity.ECOUTER_CONCERT;
import static fr.utbm.gi.vi51.project.agent.FestivalEntity.LEAVE_EUROCKS;
import static fr.utbm.gi.vi51.project.agent.FestivalEntity.MARCHE_VERS_DESTINATION;
import static fr.utbm.gi.vi51.project.agent.FestivalEntity.WANDERING;
import fr.utbm.gi.vi51.project.environment.Construction;
import fr.utbm.gi.vi51.project.environment.FestivalMap;
import fr.utbm.gi.vi51.project.environment.PumpRoom;
import fr.utbm.gi.vi51.project.environment.Scene;
import fr.utbm.gi.vi51.project.environment.SoundSubstance;
import fr.utbm.gi.vi51.project.environment.WaterClosed;
import fr.utbm.gi.vi51.project.environment.food.Food;
import fr.utbm.gi.vi51.project.utils.Astar;

import org.janusproject.jaak.envinterface.body.TurtleBody;
import org.janusproject.jaak.envinterface.body.TurtleBodyFactory;
import org.janusproject.jaak.envinterface.frustum.SquareTurtleFrustum;

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
        
        _currentState = INIT;
        
        _jaugeDeFaimTotale = RandomUtils.getRand(1*10*1000, 1*100*1000);
        _jaugeDeFaimActuelle = 0;
        
        _jaugeDeVessieTotale = RandomUtils.getRand(1*20*1000, 1*100*1000);
        _jaugeDeVessieActuelle = 0;
        
        _informationsTurtle = new TurtleSemantic(this);
        
        _frustum = new SquareTurtleFrustum(5);
        
        i++;
        /*if(true)//i == 50)//i%50 == 0)
         * {
         * _currentDestination = new Point2i(10,60);
         * // _currentState = MARCHE_VERS_DESTINATION;
         * }*/
        
        
        
    }
    
    
    
    @Override
    protected void turtleBehavior() {
        /**/
        
        Collection<Perceivable> perception = getPerception();
        
        JaakTimeManager jaakTimeManager = getJaakTimeManager();
        
        
        if(_currentState == LEAVE_EUROCKS)
        {
            moveToDestination();
            if(getPosition().x() > 158)
                killMe();
            return;
        }
        
        
        
        _jaugeDeFaimActuelle += jaakTimeManager.getWaitingDuration();
        _jaugeDeVessieActuelle += jaakTimeManager.getWaitingDuration();
        
        /*if(RandomUtils.getRand(100) > 95)
         * dropOff(new Food(1,1,1));*/
        
        
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
        
        System.out.println(_currentState+" "+_currentConstructDestination+" "+_currentDestination+" "+getPosition()+" "+_currentPath);
        
        SoundSubstance soundSubs;
        
        
        switch(_currentState)
        {
            case INIT:
                goToAPlayingConcert();
                // goTo(new Point2i(120,60));
                break;
                
            case EN_ATTENTE:
                //attendreSonTour(perception, _currentDestination);
                break;
                
            case ECOUTER_CONCERT:
                
                if(_currentConstructDestination instanceof Scene)
                {
                    Direction direct = ((Scene)_currentConstructDestination).getEmissionDirection().getOpposite();
                    Point2i relativePoint = direct.getOpposite().toRelativePoint();
                    setHeading(new Vector2f(relativePoint.getX(), relativePoint.getY()));
                    moveForward(1);
                }
                
                soundSubs = touchUp(SoundSubstance.class);
                if(soundSubs == null)
                    goToAPlayingConcert();
                break;
                
                
            case MARCHE_VERS_CONCERT:
                
                
                soundSubs = touchUp(SoundSubstance.class);
                if(soundSubs != null && soundSubs.getScene().equals(_currentConstructDestination)) //touchUpWithSemantic
                {
                    // System.out.println("TOUCHE SUBSTANCE "+_currentState+" "+touchUp.getScene()+" "+_currentConstructDestination);
                    _currentState = ECOUTER_CONCERT;
                    _currentPath = null;
                }
                
                
                if(_currentConstructDestination instanceof Scene)
                {
                    if(!((Scene)_currentConstructDestination).getIsPlaying())
                        goToAPlayingConcert();
                }
                
                
                
                //if(currentPosition has substance from _currentDestination Concert)
                //{
                //  _currentState = CHERCHE_PLACE_PROCHE_SCENE;
                //  break;
                // }
                // Pas de break ici pour effectuer le cas applyPathfinding normal à la suite
            case MARCHE_VERS_DESTINATION:
            case MARCHE_VERS_TOILETTES:
            case MARCHE_VERS_NOURRITURE:
                
                /* ArrayList<Point2i> casesInFrontOfMe = getCasesInFrontOfMe();
                 * Point2i relativePoint = new Point2i(_currentConstructDestination.getInteractCenter().getX() - getX(), _currentConstructDestination.getInteractCenter().getY() - getY());
                 * 
                 * if(casesInFrontOfMe.contains(relativePoint))
                 * {*/
                
                Point2i seekPosition = (_currentConstructDestination == null)? _currentDestination:_currentConstructDestination.getInteractCenter();
                if(seekPosition == null) return;
                float distance = seekPosition.distance(getPosition());
                if(distance == 1.0)
                {
                    if(_currentConstructDestination instanceof WaterClosed)
                    {
                        _jaugeDeVessieActuelle = 0;
                        System.out.println("Ahhhh ça va mieux :)");
                        goToAPlayingConcert();
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
                            goToAPlayingConcert();
                        }
                        
                        
                        
                    }
                }
                
                
                
                moveToDestination();
                
                
                
                break;
                
                
                
                
            case WANDERING:
                this.setHeading(this.getHeadingAngle()+RandomUtils.randomBinomial((float)Math.PI/4));
                moveForward(1);
                
                break;
                
            default:
                System.out.println("position : "+getPosition());
                break;
        }
    }
    
    
    
    /* private void attendreSonTour(Collection<Perceivable> perception, Point2i destination) {
     * if(hasReachedDestination()){
     * // Apply action
     * // Return to wandering state
     * _currentState = WANDERING;
     * return;
     * }
     * 
     * }*/
    
    
    
    
    public boolean carryingFood() {
        return this._nourriture != null;
    }
    
    
    
    
}
