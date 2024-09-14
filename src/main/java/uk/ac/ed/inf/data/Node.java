package uk.ac.ed.inf.data;

import uk.ac.ed.inf.ilp.data.LngLat;

/**
 * Represents a node in a pathfinding algorithm, containing information about its parent, location, and cost values.
 */
public class Node {
    private Node parent;
    private LngLat lngLat;
    private double g;
    private double h;
    private double f;
    private double angle;

    /**
     * Constructs a Node object with the given parameters.
     *
     * @param parent The parent node in the path.
     * @param lngLat The geographical coordinates of the node.
     * @param g      The cost from the start node to this node.
     * @param h      The heuristic cost from this node to the goal node.
     * @param angle  The angle associated with the move to this node.
     */
    public Node(Node parent, LngLat lngLat, double g, double h, double angle) {
        this.parent = parent;
        this.lngLat = lngLat;
        this.g = g;
        this.h = h;
        this.f = g + h;
        this.angle = angle;
    }

    public Node getParent() {
        return parent;
    }

    public LngLat getLngLat() {
        return lngLat;
    }

    public double getG() {
        return g;
    }

    public double getH() {
        return h;
    }

    public double getF() {
        return f;
    }

    public double getAngle() {
        return angle;
    }

    public void setParent(Node parent){
        this.parent = parent;
    }

    public void setG(double g){
        this.g = g;
    }

    public void setAngle(double angle){
        this.angle = angle;
    }

}

