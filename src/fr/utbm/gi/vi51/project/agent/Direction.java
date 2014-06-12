package fr.utbm.gi.vi51.project.agent;

import fr.utbm.gi.vi51.project.utils.RandomUtils;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.discrete.object2d.Point2i;

public enum Direction {
    
    NORTH((float) (-Math.PI/2), "NORTH", new Point2i(0,-1)),
    NORTHWEST((float)(-3*Math.PI/4), "NORTHWEST", new Point2i(1,-1)),
    WEST((float)(Math.PI), "WEST", new Point2i(1,0)),
    SOUTHWEST((float)(3*Math.PI/4), "SOUTHWEST", new Point2i(1,1)),
    SOUTH((float) (Math.PI/2), "SOUTH", new Point2i(0,1)),
    SOUTHEAST((float)(Math.PI/4), "SOUTHEAST", new Point2i(-1,1)),
    EAST((float) 0, "EAST", new Point2i(-1,0)),
    NORTHEAST((float)(-Math.PI/4), "NORTHEAST", new Point2i(-1,-1));
    
    
    
    private float value;
    private String name;
    private Point2i relative;
    
    Direction(float value, String name, Point2i relative) {
        this.value=value;
        this.name = name;
        this.relative = relative;
    }
    
    public float toFloat() {
        return value;
    }
    
    public String toString() {
        return name;
    }
    public Point2i toRelativePoint() {
        return relative;
    }
    
    public Direction getPrevious() {
        if(ordinal()-1 < 0)
            return values()[values().length-1];
        return values()[(ordinal()-1) % values().length];
    }
    public Direction getNext() {
        
        return values()[(ordinal()+1) % values().length];
    }
    
    
    public Direction getOpposite() {
        
        return values()[(ordinal()+4) % values().length];
    }
    
    public static Direction getRandom(){
        return values()[RandomUtils.getRand(values().length - 1)];
    }
    
    
    public static Direction getSens(Point2i relativePoint)
    {
        for(Direction d : values())
        {
            if(d.toRelativePoint().getX() == relativePoint.getX() && d.toRelativePoint().getY() == relativePoint.getY())
                return d;
        }
        return null;
    }
    
    public static Direction getSens(Vector2f headingVector)
    {
        if(headingVector.getX() >= -0.5 &&  headingVector.getX() <= 0.5 && headingVector.getY() > 0)
            return SOUTH;
        
        if(headingVector.getX() >= -0.5 &&  headingVector.getX() <= 0.5 && headingVector.getY() < 0)
            return NORTH;
        
        
        
        if(headingVector.getX() > 0 && headingVector.getY() < 0)
            return NORTHWEST;
        if(headingVector.getX() > 0 && headingVector.getY() >= -0.5 &&  headingVector.getY() <= 0.5)
            return WEST;
        if(headingVector.getX() > 0 && headingVector.getY() > 0)
            return SOUTHWEST;
        
        if(headingVector.getX() < 0 && headingVector.getY() < 0)
            return NORTHEAST;
        if(headingVector.getX() < 0 && headingVector.getY() >= -0.5 &&  headingVector.getY() <= 0.5)
            return EAST;
        if(headingVector.getX() < 0 && headingVector.getY() > 0)
            return SOUTHEAST;
        
        
        
        return null;
    }
    
}
