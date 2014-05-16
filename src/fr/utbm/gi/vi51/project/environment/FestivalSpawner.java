package fr.utbm.gi.vi51.project.environment;

import org.janusproject.jaak.envinterface.body.TurtleBody;
import org.janusproject.jaak.spawner.JaakAreaSpawner;
import org.janusproject.jaak.turtle.Turtle;
import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.time.KernelTimeManager;

import fr.utbm.gi.vi51.project.agent.Direction;
import fr.utbm.gi.vi51.project.agent.FestivalGoer;

public class FestivalSpawner extends JaakAreaSpawner {

	

	private int budget;
	
	public FestivalSpawner(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public FestivalSpawner(int budget,int x, int y, int width, int height) {
		super(x, y, width, height);
		this.budget=budget;
	}

	@Override
	protected float computeSpawnedTurtleOrientation(KernelTimeManager time) {
		return 0.0f;
	}

	@Override
	protected Turtle createTurtle(KernelTimeManager time) {
		return new FestivalGoer();
	}

	@Override
	protected Object[] getTurtleActivationParameters(Turtle turtle,
													 KernelTimeManager time) {
		// Nothing special to pass to the turtle's activate function.
	    return null;
	}

	@Override
	protected boolean isSpawnable(KernelTimeManager time) {
		return this.budget>0;
	}

	@Override
	protected void turtleSpawned(Turtle turtle, TurtleBody body,
								 KernelTimeManager time) {
		--this.budget;
	}

	@Override
	protected void turtleSpawned(AgentAddress adr, TurtleBody body,
								 KernelTimeManager time) {
		--this.budget;
	}

}
