package uk.ac.ed.inf.model;

import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;
import uk.ac.ed.inf.util.Constants;

/**
 * LngLatHandler class implements LngLatHandling and provides methods for handling LngLat objects.
 */
public class LngLatHandler implements LngLatHandling {

    /**
     * Calculates the Euclidean distance between two LngLat positions.
     *
     * @param startPosition The starting LngLat position.
     * @param endPosition   The ending LngLat position.
     * @return The Euclidean distance between the two positions.
     */
    @Override
    public double distanceTo(LngLat startPosition, LngLat endPosition) {
        // Implementation of Euclidean distance formula
        double startLat = startPosition.lat();
        double startLng = startPosition.lng();
        double endLat = endPosition.lat();
        double endLng = endPosition.lng();
        double latDiffSqr = Math.pow(endLat - startLat, 2);
        double lngDiffSqr = Math.pow(endLng - startLng, 2);

        return Math.sqrt(latDiffSqr + lngDiffSqr);
    }

    /**
     * Checks if a given LngLat position is close to another LngLat position.
     *
     * @param startPosition The starting LngLat position.
     * @param otherPosition  The target LngLat position.
     * @return True if the positions are close, false otherwise.
     */
    @Override
    public boolean isCloseTo(LngLat startPosition, LngLat otherPosition) {
        double distance = distanceTo(startPosition, otherPosition);
        // A threshold value for proximity, can be adjusted
        return distance < SystemConstants.DRONE_IS_CLOSE_DISTANCE;
    }

    /**
     * Checks if a given LngLat position is inside a named region.
     *
     * @param position The LngLat position to check.
     * @param region   The named region to check against.
     * @return True if the position is inside the region, false otherwise.
     */
    public boolean isInRegion(LngLat position, NamedRegion region) {
        int count = 0;
        LngLat[] vertices = region.vertices();
        for (int i = 0; i < vertices.length; i++) {
            LngLat currentVertex = vertices[i];
            LngLat nextVertex = vertices[(i + 1) % vertices.length];

            if (isPointOnEdge(position, currentVertex, nextVertex)) {
                return true;
            }

            // Checking if the point is on a vertex
            if (position.lng() == currentVertex.lng() && position.lat() == currentVertex.lat()) {
                return true;
            }

            boolean latitudeCondition = position.lat() <= Math.max(nextVertex.lat(), currentVertex.lat()) &&
                    position.lat() > Math.min(nextVertex.lat(), currentVertex.lat());
            double latitudeFraction = ((position.lat() - currentVertex.lat()) / (nextVertex.lat() - currentVertex.lat())) * (nextVertex.lng() - currentVertex.lng());
            boolean longitudeCondition = position.lng() < (currentVertex.lng() + latitudeFraction);

            if (longitudeCondition && latitudeCondition) {
                count++;
            }
        }
        return count % 2 == 1;
    }

    /**
     * Checks if a given point is on an edge defined by two vertices.
     *
     * @param position     The LngLat position to check.
     * @param currentVertex The starting vertex of the edge.
     * @param nextVertex    The ending vertex of the edge.
     * @return True if the point is on the edge, false otherwise.
     */
    private static boolean isPointOnEdge(LngLat position, LngLat currentVertex, LngLat nextVertex) {
        // Checking if the point is on edge
        boolean firstCondition = position.lng() > Math.min(currentVertex.lng(), nextVertex.lng());
        boolean secondCondition = position.lng() <= Math.max(currentVertex.lng(), nextVertex.lng());
        if (firstCondition && secondCondition) {
            // Calculating the slope of the line between current and endPoint
            double slope = (nextVertex.lat() - currentVertex.lat()) / (nextVertex.lng() - currentVertex.lng());
            double latitude = slope * (position.lng() - currentVertex.lng()) + currentVertex.lat();
            if (position.lat() == latitude) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the next LngLat position given a starting position and an angle.
     *
     * @param startPosition The starting LngLat position.
     * @param angle          The angle in degrees.
     * @return The next LngLat position.
     */
    @Override
    public LngLat nextPosition(LngLat startPosition, double angle) {
        if (!Constants.VALID_ANGLES.contains(angle)) {
            return startPosition;
        }

        double angleInRadians = Math.toRadians(angle);

        double startLat = startPosition.lat();
        double startLng = startPosition.lng();

        double endLat = startLat + SystemConstants.DRONE_MOVE_DISTANCE * (Math.sin(angleInRadians));
        double endLng = startLng + SystemConstants.DRONE_MOVE_DISTANCE * (Math.cos(angleInRadians));

        return new LngLat(endLng, endLat);
    }
}
