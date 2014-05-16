package fr.utbm.gi.vi51.project.agent;

public enum Direction {

	NORTH((float) (-Math.PI/2)),
	SOUTH((float) (Math.PI/2)),
	WEST((float)(Math.PI)),
	EAST((float) 0),
	NORTHEAST((float)(-Math.PI/4)),
	NORTHWEST((float)(-3*Math.PI/4)),
	SOUTHEAST((float)(Math.PI/4)),
	SOUTHWEST((float)(3*Math.PI/4));
	
	private float value;
	
	Direction(float value) {
		this.value=value;
	}
	
	public float toFloat() {
		return value;
	}
	
}
