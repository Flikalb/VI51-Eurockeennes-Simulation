package fr.utbm.gi.vi51.project.agent;

import org.janusproject.jaak.envinterface.body.TurtleBody;
import org.janusproject.jaak.envinterface.body.TurtleBodyFactory;

import fr.utbm.gi.vi51.project.utils.RandomUtils;

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
		this.setHeading(this.getHeadingAngle()+RandomUtils.randomBinomial((float)Math.PI/4));
		//System.out.println(this.getHeadingAngle());	
		moveForward(1);
	}

}
