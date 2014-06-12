package fr.utbm.gi.vi51.project.agent;

import static fr.utbm.gi.vi51.project.agent.FestivalEntity.APPROCHER_SCENE;
import static fr.utbm.gi.vi51.project.agent.FestivalEntity.ECOUTER_CONCERT;
import static fr.utbm.gi.vi51.project.agent.FestivalEntity.LEAVE_EUROCKS;
import static fr.utbm.gi.vi51.project.agent.FestivalEntity.MARCHE_VERS_DESTINATION;
import static fr.utbm.gi.vi51.project.agent.FestivalEntity.RUN_AWAY;
import static fr.utbm.gi.vi51.project.agent.FestivalEntity.WANDERING;
import fr.utbm.gi.vi51.project.environment.Construction;
import fr.utbm.gi.vi51.project.environment.FestivalMap;
import fr.utbm.gi.vi51.project.environment.FireSubstance;
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
import java.awt.List;

import java.util.ArrayList;
import java.util.Collection;

import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.discrete.object2d.Point2i;
import org.janusproject.jaak.envinterface.perception.Perceivable;
import org.janusproject.jaak.envinterface.perception.PerceivedTurtle;
import org.janusproject.jaak.envinterface.time.JaakTimeManager;
import org.janusproject.jaak.turtle.Turtle;
import org.janusproject.kernel.message.Message;
import org.janusproject.kernel.message.ObjectMessage;

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
    
    
    private int _listeningState;
    
    
    
    public FestivalGoer() {
        super();
        // this.getTurtleBody()
        
        _money = RandomUtils.getRand(0, 100);
        
        changeCurrentState(INIT);
        
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
        
        
        
        _isOnSamePosition = getPosition().equals(_previousPosition);
        _previousPosition = getPosition();
        if(getCurrentState() != ECOUTER_CONCERT)
            if(checkFailMove()) return;
        
        //System.out.println("currentState "+getCurrentState()+" "+getPosition()+" "+_isOnSamePosition);
        
        if(getPosition().x() > 157)
        {
            System.out.println("kill me");
            killMe();
        }
        
        Message m = this.getMessage(); // Voit quelqu'un qui fuit
        if(m instanceof ObjectMessage) {
            if(((ObjectMessage)m).getContent() instanceof Point2i) {
                changeCurrentState(RUN_AWAY);
                this._currentDestination=((Point2i)((ObjectMessage)m).getContent());
                
            }
        }
        
        for(Perceivable p : perception) {
            if(p.isSubstance()) {
                if(p instanceof FireSubstance) {
                    changeCurrentState(RUN_AWAY);
                    this._currentDestination=p.getPosition();
                    
                }
            }
            
        }
        
        if(getCurrentState() == RUN_AWAY) {
            for(Perceivable p : perception) {
                //on vérifie que l'on ne détecte pas d'autres feux
                if(p instanceof FireSubstance) {
                    _currentDestination = p.getPosition();
                }
                //on prévient les agents que l'on fuit
                if (p.isTurtle()) {
                    TurtleSemantic t = (TurtleSemantic)p.getSemantic();
                    this.forwardMessage(new ObjectMessage(this._currentDestination),t.getOwner().getAddress());
                }
            }
            
            runAway();
            return;
        }
        
        
        if(getCurrentState() == LEAVE_EUROCKS) // Action la plus importante
        {
            System.out.println("LEAVE "+_currentDestination);
            moveToDestination();
            return;
        }
        
        
        
        
        
        
        
        
        
        _jaugeDeFaimActuelle += jaakTimeManager.getWaitingDuration();
        _jaugeDeVessieActuelle += jaakTimeManager.getWaitingDuration();
        
        
        
        if(!( getCurrentState() == MARCHE_VERS_NOURRITURE || getCurrentState() == MARCHE_VERS_TOILETTES)) // Si je n'ai pas déjà un but de ce genre
        {
            if(_jaugeDeVessieActuelle >= _jaugeDeVessieTotale)
            {
                System.out.println("Aaaahh pipiii !");
                changeCurrentState(MARCHE_VERS_TOILETTES);
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
                    changeCurrentState(MARCHE_VERS_NOURRITURE);
                    _currentConstructDestination =  _carteFestival.getRandomFoodStand();//_carteFestival.getNearestFoodStand();
                }
                
            }
        }
        
        
        /*if(RandomUtils.getRand(100) > 95)
         * dropOff(new Food(1,1,1));*/
        
        
        //System.out.println(_currentState+" "+_currentConstructDestination+" "+_currentDestination+" "+getPosition()+" "+_currentPath);
        
        SoundSubstance soundSubs;
        
        
        switch(getCurrentState())
        {
            case INIT: // State initial
                goToAPlayingConcert();
                // goTo(new Point2i(120,60));
                break;
                
            case APPROCHER_SCENE:
                
                if(_isOnSamePosition)
                {
                    // On a atteint une place correcte
                    changeCurrentState(ECOUTER_CONCERT);
                    
                    break;
                }
                else if(_currentConstructDestination instanceof Scene)
                {
                    /*Direction direct = ((Scene)_currentConstructDestination).getEmissionDirection().getOpposite();
                     * 
                     * 
                     * Collection <PerceivedTurtle> turtles = this.getPerceivedTurtles();
                     * 
                     * 
                     * ArrayList<Point2i> pointsPossiblesRelatifs = new ArrayList<Point2i>();
                     * pointsPossiblesRelatifs.add(direct.getPrevious().getOpposite().toRelativePoint());
                     * pointsPossiblesRelatifs.add(direct.getNext().getOpposite().toRelativePoint());
                     * pointsPossiblesRelatifs.add(direct.getOpposite().toRelativePoint());
                     * 
                     * ArrayList<Point2i> pointsPossibles = new ArrayList<Point2i>();
                     * 
                     * Point2i pt1= new Point2i(this.getPosition());
                     * Point2i pt2= new Point2i(this.getPosition());
                     * Point2i pt3= new Point2i(this.getPosition());
                     * pt1.add(pointsPossiblesRelatifs.get(0).x(), pointsPossiblesRelatifs.get(0).y());
                     * pt2.add(pointsPossiblesRelatifs.get(1).x(), pointsPossiblesRelatifs.get(1).y());
                     * pt3.add(pointsPossiblesRelatifs.get(2).x(), pointsPossiblesRelatifs.get(2).y());
                     * 
                     * 
                     * pointsPossibles.add(pt1);
                     * pointsPossibles.add(pt2);
                     * pointsPossibles.add(pt3);
                     * 
                     * for( PerceivedTurtle turtle : turtles )
                     * {
                     * int indice = pointsPossibles.indexOf(turtle.getPosition());
                     * if(indice != -1)
                     * {
                     * pointsPossibles.remove(indice);
                     * pointsPossiblesRelatifs.remove(indice);
                     * }
                     * }
                     * System.out.println(pointsPossibles.size());
                     * if(pointsPossibles.size()>0)
                     * {
                     * Point2i resultat = pointsPossiblesRelatifs.get(RandomUtils.getRand(pointsPossiblesRelatifs.size() - 1));
                     * System.out.println(resultat);
                     * setHeading(new Vector2f(resultat.getX(), resultat.getY()));
                     * moveForward(1);
                     * return;
                     * }*/
                    
                    Direction direct = ((Scene)_currentConstructDestination).getEmissionDirection().getOpposite();
                    Point2i relativePoint = direct.getOpposite().toRelativePoint();
                    setHeading(new Vector2f(relativePoint.getX(), relativePoint.getY()));
                    moveForward(1);
                }
                // Plus de son, on se casse voir ailleurs
                soundSubs = touchUp(SoundSubstance.class);
                if(soundSubs == null)
                    goToAPlayingConcert();
                break;
                
            case ECOUTER_CONCERT:
                
                _listeningState++;
                if(_listeningState > 1)
                    _listeningState = 0;
                
                soundSubs = touchUp(SoundSubstance.class);
                if(soundSubs == null)
                    goToAPlayingConcert();
                break;
                
                
            case MARCHE_VERS_CONCERT:
                
                // Si on entends le son d'un concert, on se met en état de recherche d'une place plus proche
                soundSubs = touchUp(SoundSubstance.class);
                if(soundSubs != null && soundSubs.getScene().equals(_currentConstructDestination)) //touchUpWithSemantic
                {
                    changeCurrentState(APPROCHER_SCENE, false);
                }
                
                
                if(_currentConstructDestination instanceof Scene)
                {
                    // Si notre scene objectif vient de voir son concert se terminer, on va ailleurs
                    if(!((Scene)_currentConstructDestination).getIsPlaying())
                        goToAPlayingConcert();
                }
                
                moveToDestination();
                break;
                
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
                
            case ARRIVED_ON_OBJECTIVE:
                
                goToAPlayingConcert();
                
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
    
    public boolean listeningConcert() {
        return getCurrentState() == ECOUTER_CONCERT;
    }
    
    public int listeningState() { // pour créer une animation des bras
        return _listeningState;
    }
    
    
    
    
}
