package fr.utbm.gi.vi51.project.environment.obstacles;

import org.janusproject.jaak.envinterface.perception.Obstacle;

import fr.utbm.gi.vi51.project.environment.Scene;

public class ObstacleScene extends Obstacle {
	
	private int cat;
	private Scene sc;
	
	public ObstacleScene(int number, Scene scene)
	{
		cat=number;
		sc=scene;
		
	}
	
	public int getCat()
	{
		return cat;
	}
	
	public Scene getScene()
	{
		return sc;
	}
	
}
