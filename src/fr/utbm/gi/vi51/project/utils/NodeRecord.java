package fr.utbm.gi.vi51.project.utils;
 
import org.arakhne.afc.math.discrete.object2d.Point2i;
 
public class NodeRecord {
 
    Point2i coordinates;
    Connection connection = null;
    double costSoFar;
    double estimatedTotalCost;
     
    public NodeRecord(Point2i point) {
        this.coordinates = point;
        this.costSoFar = 0.0 ;
    }
 
    public Point2i getCoordinates() {
        return coordinates;
    }
 
    public void setCoordinates(Point2i coordinates) {
        this.coordinates = coordinates;
    }
 
    public Connection getConnection() {
        return connection;
    }
 
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
 
    public double getCostSoFar() {
        return costSoFar;
    }
 
    public void setCostSoFar(double costSoFar) {
        this.costSoFar = costSoFar;
    }
 
    public double getEstimatedTotalCost() {
        return estimatedTotalCost;
    }
 
    public void setEstimatedTotalCost(double estimatedTotalCost) {
        this.estimatedTotalCost = estimatedTotalCost;
    }
     
    public double estimateHeuristic(Point2i end) {
        //vol d'oiseau
        return Math.sqrt((end.getX()-this.coordinates.getX())*(end.getX()-this.coordinates.getX())
                +(end.getY()-this.coordinates.getY())*(end.getY()-this.coordinates.getY()));
    }
     
}