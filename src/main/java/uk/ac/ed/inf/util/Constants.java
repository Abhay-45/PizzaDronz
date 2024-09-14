package uk.ac.ed.inf.util;

import uk.ac.ed.inf.ilp.data.LngLat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A utility class containing constants used throughout the application.
 */
public final class Constants {

    // URL constants
    public static final String RESTAURANT_URL = "restaurants";
    public static final String ORDER_URL = "orders";
    public static final String NOFLYZONE_URL = "noFlyZones";
    public static final String CENTRALAREA_URL = "centralArea";
    public static final String ALIVE_URL = "isAlive";

    // Geographical coordinates
    public static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);

    // Angle constant for hovering
    public static final Double HOVER_ANGLE = 999.0;

    // Valid angles for drone movement
    public static final List<Double> VALID_ANGLES = new ArrayList<>(Arrays.asList(
            0.0, 22.5, 45.0, 67.5, 90.0, 112.5, 135.0, 157.5,
            180.0, 202.5, 225.0, 247.5, 270.0, 292.5, 315.0, 337.5
    ));

    // Directory for result files
    public static final String RESULT_FILES_DIRECTORY = "resultfiles/";

    // Private constructor to prevent instantiation
    private Constants() {
    }
}
