package fr.utbm.gi.vi51.project.agent;

import org.janusproject.jaak.envinterface.body.TurtleBody;
import org.janusproject.jaak.envinterface.body.TurtleBodyFactory;
import org.janusproject.jaak.turtle.Turtle;

public abstract class FestivalSecurityAgent extends Turtle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1021603672862125311L;
	
	public FestivalSecurityAgent() {
		super(true);
	}
        
        @Override
	protected TurtleBody createTurtleBody(TurtleBodyFactory factory) {
            
            
		return factory.createTurtleBody(getAddress());
	}
	
	@Override
	protected void turtleBehavior() {
            
        }

}
