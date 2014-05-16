package fr.utbm.gi.vi51.project.agent;

import org.janusproject.jaak.envinterface.body.TurtleBody;
import org.janusproject.jaak.envinterface.body.TurtleBodyFactory;

public class FestivalGoer extends FestivalAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5328824872764860436L;

	public FestivalGoer() {
		super();
	}
	
	@Override
	protected TurtleBody createTurtleBody(TurtleBodyFactory factory) {
		return factory.createTurtleBody(getAddress());
	}
	
	@Override
	protected void turtleBehavior() {
		moveForward(2);
	}

}
