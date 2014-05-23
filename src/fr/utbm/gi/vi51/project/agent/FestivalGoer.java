package fr.utbm.gi.vi51.project.agent;

import org.janusproject.jaak.envinterface.body.TurtleBody;
import org.janusproject.jaak.envinterface.body.TurtleBodyFactory;

import fr.utbm.gi.vi51.project.utils.RandomUtils;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.discrete.object2d.Point2i;

public class FestivalGoer extends FestivalAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5328824872764860436L;
        private static int i = 1;
	public FestivalGoer() {
		super();
               // this.getTurtleBody()
	}
	
	@Override
	protected TurtleBody createTurtleBody(TurtleBodyFactory factory) {
            
            
		return factory.createTurtleBody(getAddress());
	}
	
	@Override
	protected void turtleBehavior() {
		/**/
            if(i%10 == 0)
            {
                Point2i seekPosition = new Point2i(60,60);
                Point2i position = this.getPosition();
                if(!seekPosition.equals(position)) {
                        Vector2f direction = new Vector2f();
                        direction.sub(seekPosition, position);
                        direction.normalize();
                        this.setHeading(direction);
                        moveForward(1);
                }
            }
            else{
                this.setHeading(this.getHeadingAngle()+RandomUtils.randomBinomial((float)Math.PI/4));
		moveForward(1);
            }
            i++;
	}

}
