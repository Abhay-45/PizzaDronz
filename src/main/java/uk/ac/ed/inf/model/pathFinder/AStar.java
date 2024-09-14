package uk.ac.ed.inf.model.pathFinder;

import uk.ac.ed.inf.model.LngLatHandler;
import uk.ac.ed.inf.data.Node;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.util.Constants;

import java.util.*;

/**
 * AStar class implements the A* algorithm to find the optimal path between two points on a map,
 * considering no-fly-zones and a central area constraint.
 */
public class AStar {
    private final LngLat start;
    private final LngLat end;
    private final NamedRegion centralArea;
    private final List<NamedRegion> noFlyZones;

    /**
     * Constructs an AStar object with the given start and end points, central area, and list of no-fly zones.
     *
     * @param start       The starting point (LngLat) of the path.
     * @param end         The ending point (LngLat) of the path.
     * @param centralArea The central area constraint (NamedRegion).
     * @param noFlyZones  The list of no-fly zones (NamedRegion).
     */
    public AStar(LngLat start, LngLat end, NamedRegion centralArea, List<NamedRegion> noFlyZones) {
        this.start = start;
        this.end = end;
        this.centralArea = centralArea;
        this.noFlyZones = noFlyZones;
    }

    /**
     * Calculates the optimal path using the A* algorithm.
     *
     * @return The final node representing the optimal path.
     */
    public Node calculatePath() {
        LngLatHandler lngLatHandler = new LngLatHandler();
        NodePriorityComparator comparator = new NodePriorityComparator();
        PriorityQueue<Node> openSet = new PriorityQueue<>(comparator);
        HashMap<LngLat, Node> visited = new HashMap<>();

        Node startNode = new Node(
                null,
                start,
                0,
                lngLatHandler.distanceTo(start, end),
                Constants.HOVER_ANGLE
        );
        openSet.add(startNode);

        Node currentNode;

        while (!openSet.isEmpty()) {
            currentNode = openSet.poll();
            if (lngLatHandler.isCloseTo(currentNode.getLngLat(), end)) {
                return currentNode;
            }
            List<Node> neighbours = getNeighbours(currentNode, lngLatHandler);
            updateNeighbours(openSet, visited, currentNode, neighbours, lngLatHandler);
        }
        return null;
    }

    /**
     * Updates the neighbors of the current node in the A* algorithm.
     *
     * @param openSet       The priority queue of open nodes.
     * @param visited       The map of visited nodes.
     * @param currentNode   The current node being processed.
     * @param neighbours    The list of neighboring nodes.
     * @param lngLatHandler The handler for LngLat operations.
     */
    private void updateNeighbours(PriorityQueue<Node> openSet, HashMap<LngLat, Node> visited, Node currentNode, List<Node> neighbours, LngLatHandler lngLatHandler) {
        for (Node neighbour : neighbours) {
            if (visited.containsKey(neighbour.getLngLat())) {
                if (neighbour.getF() < visited.get(neighbour.getLngLat()).getF()) {
                    visited.put(neighbour.getLngLat(), neighbour);
                } else {
                    continue;
                }
            }

            Node node = new Node(
                    currentNode,
                    neighbour.getLngLat(),
                    currentNode.getG() + lngLatHandler.distanceTo(currentNode.getLngLat(), neighbour.getLngLat()),
                    lngLatHandler.distanceTo(neighbour.getLngLat(), end),
                    neighbour.getAngle()
            );
            openSet.add(node);
            visited.put(node.getLngLat(), node);
        }
    }

    /**
     * Gets the neighboring nodes of the current node.
     *
     * @param currNode      The current node.
     * @param lngLatHandler The handler for LngLat operations.
     * @return The list of neighboring nodes.
     */
    private List<Node> getNeighbours(Node currNode, LngLatHandler lngLatHandler) {
        List<Node> neighbours = new ArrayList<>();
        boolean currInCentral = lngLatHandler.isInRegion(currNode.getLngLat(), centralArea);

        for (Double angle : Constants.VALID_ANGLES) {
            LngLat nextPosition = lngLatHandler.nextPosition(currNode.getLngLat(), angle);
            boolean isNextPosInCentral = lngLatHandler.isInRegion(nextPosition, centralArea);
            boolean isValid = checkValidity(currInCentral, nextPosition, isNextPosInCentral, lngLatHandler);

            if (isValid) {
                Node nextNode = new Node(currNode, nextPosition, currNode.getG() + lngLatHandler.distanceTo(currNode.getLngLat(), nextPosition),
                        lngLatHandler.distanceTo(nextPosition, end), angle);
                neighbours.add(nextNode);
            }
        }

        return neighbours;
    }

    /**
     * Checks the validity of the next position for the A* algorithm.
     *
     * @param currInCentral     Whether the current position is in the central area.
     * @param nextPosition      The next position to be checked.
     * @param isNextPosInCentral Whether the next position is in the central area.
     * @param lngLatHandler     The handler for LngLat operations.
     * @return True if the next position is valid, false otherwise.
     */
    private boolean checkValidity(boolean currInCentral, LngLat nextPosition, boolean isNextPosInCentral, LngLatHandler lngLatHandler) {
        if (!currInCentral && isNextPosInCentral) {
            return false;
        }
        for (NamedRegion noFlyZone : noFlyZones) {
            if (lngLatHandler.isInRegion(nextPosition, noFlyZone)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Comparator for comparing nodes based on priority in the A* algorithm.
     */
    public class NodePriorityComparator implements Comparator<Node> {
        @Override
        public int compare(Node node1, Node node2) {
            return Double.compare(node1.getF(), node2.getF());
        }
    }
}
