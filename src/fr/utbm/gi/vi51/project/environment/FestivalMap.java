/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utbm.gi.vi51.project.environment;

import fr.utbm.gi.vi51.project.utils.RandomUtils;
import java.util.ArrayList;

/**
 * Plan du festival, distribué à chaque agent pour qu'il connaisse les différents lieux
 * Initialisé dans FestivalSystem et distribué à l'agent dans son lieu de spawn
 * @author Benjamin
 */
public class FestivalMap 
{
    private ArrayList<Construction> _scenes;
    private ArrayList<Construction> _foodStands;
    private ArrayList<Construction> _toilets;
    
    private ArrayList<Construction> _all;
    
    public FestivalMap() {
       _scenes = new ArrayList<>();
       _foodStands = new ArrayList<>();
       _toilets = new ArrayList<>();
       
       _all = new ArrayList<>();
    }
    
    
    public void addConstruction(Construction construction) {
        if(construction instanceof Scene)
            _scenes.add(construction);
        else if(construction instanceof PumpRoom)
            _foodStands.add(construction);
        else if(construction instanceof WaterClosed)
            _toilets.add(construction);
        
        _all.add(construction);
    }
    
    
    // TODO ::
    
    public Construction getPlayingConcertByType(String type) // Scene.CONCERT_[TYPE]
    {
        return null;
    }
    public ArrayList<Construction> getAllPlayingConcerts()
    {
        return null;
    }
    
    
    public Construction getRandomConcerts()
    {
        return _scenes.get(RandomUtils.getRand(_scenes.size() - 1));
    }
    
    
    public Construction getPlayingConcerts()
    {
        ArrayList<Construction> result = new ArrayList<>();
        for(Construction sc : _scenes)
        {
            Scene scene = (Scene)sc;
            if(scene.getIsPlaying())
                result.add(sc);
        }
        if(result.size() < 1)
            return null;
        return result.get(RandomUtils.getRand(result.size() - 1));
    }
    
    public Construction getNearestFoodStand()
    {
        return null;
    }
    
    public ArrayList<Construction> getAllFoodStands()
    {
        return _foodStands;
    }
    
    public Construction getRandomFoodStand()
    {
        return _foodStands.get(RandomUtils.getRand(_foodStands.size() - 1));
    }
    
    
    
    public Construction getNearestToilet()
    {
        return null;
    }
    
    public ArrayList<Construction> getAllToilets()
    {
        return _toilets;
    }
    
    public Construction getRandomToilet()
    {
        return _toilets.get(RandomUtils.getRand(_toilets.size() - 1));
    }
    
    public Construction getRandomDestination()
    {
        return _all.get(RandomUtils.getRand(_all.size() - 1));
    }
    
    
    public ArrayList<Construction> getAllDestinations()
    {
        return _all;
    }

    
    
    
    
    
    
}
