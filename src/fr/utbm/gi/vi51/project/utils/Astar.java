package fr.utbm.gi.vi51.project.utils;
 
import java.util.ArrayList;
 
import org.arakhne.afc.math.discrete.object2d.Point2i;
import org.janusproject.jaak.envinterface.channel.GridStateChannel;
import org.janusproject.jaak.envinterface.perception.EnvironmentalObject;
 
import fr.utbm.gi.vi51.project.environment.obstacles.ObstaclePumpRoom;
import fr.utbm.gi.vi51.project.environment.obstacles.ObstacleScene;
import fr.utbm.gi.vi51.project.environment.obstacles.ObstacleTree;
import fr.utbm.gi.vi51.project.environment.obstacles.ObstacleWater;
import fr.utbm.gi.vi51.project.environment.obstacles.ObstacleWaterClosed;
import org.janusproject.jaak.envinterface.perception.Obstacle;
 
public class Astar {
     
    private static ArrayList<NodeRecord> openList;
    private static ArrayList<NodeRecord> closedList;
    private static GridStateChannel channel;
    private static NodeRecord startRecord;
     
    public Astar(GridStateChannel c) {
        channel = c;
         
    }
     
    public static ArrayList<Point2i> findPath(Point2i startPosition, Point2i endPosition, int maxDepth) {
         
        
        /*if(!areCoordinatesAvailable(endPosition.x(),endPosition.y()))
        {
            System.out.println("Erreur endCoordonnées");
            return null;
        }*/
         
        NodeRecord currentNode=null;
         
        startRecord = new NodeRecord(startPosition);
        startRecord.setCostSoFar(0);
        startRecord.setEstimatedTotalCost(startRecord.estimateHeuristic(endPosition));
         
        openList = new ArrayList<NodeRecord>();
        closedList = new ArrayList<NodeRecord>();
         
        openList.add(startRecord);
         
        int depth=0;
        while(depth<maxDepth && !openList.isEmpty()) {
             
            currentNode = getSmallestElement(openList);
            if (currentNode.getCoordinates()==endPosition) break;
            else {
                ArrayList<Connection> connections = getConnections(currentNode);
                 
                for(Connection c : connections) {
                    Point2i endNode = c.getEndCoordinates();
                    double endNodeCost = currentNode.getCostSoFar() + c.getCost();
                    double endNodeHeuristic=0;
                    NodeRecord endNodeRecord;
                    if(closedListContains(endNode)) {
                        endNodeRecord = closedListFind(endNode);
                         
                        if(endNodeRecord.getCostSoFar()<=endNodeCost) continue;
                         
                        closedList.remove(endNodeRecord);
                        endNodeHeuristic = endNodeRecord.estimateHeuristic(endPosition);
                    }
                    else if(openListContains(endNode)) {
                        endNodeRecord = openListFind(endNode);
                         
                        if(endNodeRecord.getCostSoFar()<=endNodeCost) continue;
                         
                        endNodeHeuristic = endNodeRecord.estimateHeuristic(endPosition);
                         
                    }
                    else {
                        endNodeRecord = new NodeRecord(endNode);
                        endNodeHeuristic = endNodeRecord.estimateHeuristic(endPosition);
                    }
                     
                    endNodeRecord.setCostSoFar(endNodeCost);
                    endNodeRecord.setEstimatedTotalCost(endNodeCost+endNodeHeuristic);
                    endNodeRecord.setConnection(c);
                     
                    if(!openListContains(endNode)) {
                        openList.add(endNodeRecord);
                    }
                     
                }
                 
                openList.remove(currentNode);
                closedList.add(currentNode);
                depth++;
                 
            }
             
        }
         
        if(currentNode == null || currentNode.getConnection()== null || currentNode.getConnection().getFromNode()==null) {
            return new ArrayList<Point2i>();
        }
        else {
            ArrayList<Point2i> path = new ArrayList<Point2i>();
             
            while(!currentNode.getCoordinates().equals(startPosition)) {
                path.add(currentNode.getConnection().getFromNode().getCoordinates());
                currentNode = currentNode.getConnection().getFromNode();
            }
             
            return reverse(path);
        }
    }
 
    private static ArrayList<Point2i> reverse(ArrayList<Point2i> path) {
        ArrayList<Point2i> reverseArrayList = new ArrayList<Point2i>();
        for(int i = path.size()-1; i>=0;i--) {
            reverseArrayList.add(path.get(i));
        }
        return reverseArrayList;
    }
 
    private static NodeRecord openListFind(Point2i endNode) {
        for(NodeRecord node : openList) {
            if (node.getCoordinates().equals(endNode)) return node;
        }
        return null;
    }
 
    private static boolean openListContains(Point2i endNode) {
        for(NodeRecord node : openList) {
            if (node.getCoordinates().equals(endNode)) return true;
        }
        return false;
    }
 
    private static NodeRecord closedListFind(Point2i endNode) {
        for(NodeRecord node : closedList) {
            if (node.getCoordinates().equals(endNode)) return node;
        }
        return null;
    }
 
    private static boolean closedListContains(Point2i endNode) {
        for(NodeRecord node : closedList) {
            if (node.getCoordinates().equals(endNode)) return true;
        }
        return false;
    }
 
    private static NodeRecord getSmallestElement(ArrayList<NodeRecord> openList) {
         
        NodeRecord smallestNode = null;
        double smallestCost = Double.POSITIVE_INFINITY;
        for(NodeRecord n : openList) {
            if (n.getEstimatedTotalCost()<smallestCost) {
                smallestNode = n;
                smallestCost = n.getEstimatedTotalCost();
            }
        }
         
        return smallestNode;
    }
     
    private static ArrayList<Connection> getConnections(NodeRecord node) {
         
        Point2i coordinates = node.getCoordinates();
        ArrayList<Connection> connections = new ArrayList<Connection>();
         
        if(areCoordinatesAvailable(coordinates.x()-1,coordinates.y()-1)) {
            Point2i toNode = new Point2i(coordinates.x()-1,coordinates.y()-1);
            Connection connection = new Connection(node,toNode);
            connection.setCost(10);
            connections.add(connection);
        }
         
        if(areCoordinatesAvailable(coordinates.x()-1,coordinates.y())) {
            Point2i toNode = new Point2i(coordinates.x()-1,coordinates.y());
            Connection connection = new Connection(node,toNode);
            connection.setCost(10);
            connections.add(connection);
        }
         
        if(areCoordinatesAvailable(coordinates.x()-1,coordinates.y()+1)) {
            Point2i toNode = new Point2i(coordinates.x()-1,coordinates.y()+1);
            Connection connection = new Connection(node,toNode);
            connection.setCost(10);
            connections.add(connection);
        }
         
        if(areCoordinatesAvailable(coordinates.x(),coordinates.y()-1)) {
            Point2i toNode = new Point2i(coordinates.x(),coordinates.y()-1);
            Connection connection = new Connection(node,toNode);
            connection.setCost(10);
            connections.add(connection);
        }
         
        if(areCoordinatesAvailable(coordinates.x(),coordinates.y()+1)) {
            Point2i toNode = new Point2i(coordinates.x(),coordinates.y()+1);
            Connection connection = new Connection(node,toNode);
            connection.setCost(10);
            connections.add(connection);
        }
         
        if(areCoordinatesAvailable(coordinates.x()+1,coordinates.y()-1)) {
            Point2i toNode = new Point2i(coordinates.x()+1,coordinates.y()-1);
            Connection connection = new Connection(node,toNode);
            connection.setCost(10);
            connections.add(connection);
        }
         
        if(areCoordinatesAvailable(coordinates.x()+1,coordinates.y())) {
            Point2i toNode = new Point2i(coordinates.x()+1,coordinates.y());
            Connection connection = new Connection(node,toNode);
            connection.setCost(10);
            connections.add(connection);
        }
         
        if(areCoordinatesAvailable(coordinates.x()+1,coordinates.y()+1)) {
            Point2i toNode = new Point2i(coordinates.x()+1,coordinates.y()+1);
            Connection connection = new Connection(node,toNode);
            connection.setCost(10);
            connections.add(connection);
        }
         
        return connections;
         
    }
     
    private static boolean areCoordinatesAvailable(int x,int y) {
         
        if(x<0 || y<0
            || (x>channel.getGridWidth()-1) 
            || (y>channel.getGridHeight()-1) ) return false;
         
        for(EnvironmentalObject eo : channel.getEnvironmentalObjects(x, y)) {
            /*
                    || eo instanceof ObstacleScene 
                    || eo instanceof ObstacleTree 
                    || eo instanceof ObstacleWater
                    || eo instanceof ObstacleWaterClosed)*/
            if(eo instanceof Obstacle)
                return false;
        }
         
        if(channel.containsTurtle(x, y)) return false;
         
        return true;
    }
     
}