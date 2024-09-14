package uk.ac.ed.inf.output;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONObject;
import uk.ac.ed.inf.data.Delivery;
import uk.ac.ed.inf.data.Move;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import uk.ac.ed.inf.util.Constants;

/**
 * JsonWriter class provides methods to write deliveries, flight paths, and drone paths to JSON and GeoJSON files.
 */
public class JsonWriter{
    private File directory;

    /**
     * Constructor to initialize the directory for storing result files.
     */
    public JsonWriter(){
        this.directory = createDirectory(Constants.RESULT_FILES_DIRECTORY);
    }

    /**
     * Writes deliveries to a JSON file with the specified date in the file name.
     *
     * @param deliveries The list of deliveries to be written to the file.
     * @param date       The date used in the file name.
     * @return The JSON string representing the deliveries.
     */
    public String writeDeliveriesToJson(List<Delivery> deliveries, String date) {
        String fileName = "deliveries-" + date + ".json";
        return writeJsonToFile(deliveries, fileName);
    }

    /**
     * Writes flight paths to a JSON file with the specified date in the file name.
     *
     * @param flightPath The list of moves representing the flight path to be written to the file.
     * @param date       The date used in the file name.
     * @return The JSON string representing the flight path.
     */
    public String writeFlightPathToJson(List<Move> flightPath, String date) {
        String fileName = "flightpath-" + date + ".json";
        return writeJsonToFile(flightPath, fileName);
    }

    /**
     * Writes drone paths to a GeoJSON file with the specified date in the file name.
     *
     * @param flightPath The list of moves representing the drone path to be written to the file.
     * @param date       The date used in the file name.
     * @return The GeoJSON string representing the drone path.
     */
    public String writeDronePathToGeoJson(List<Move> flightPath, String date) {
        LinkedHashMap<String, Object> featureCollection = getJsonObject(flightPath);
        String fileName = "drone-" + date + ".geojson";
        return writeToFile(featureCollection, fileName, directory);
    }

    /**
     * Writes JSON data to a file.
     *
     * @param data     The data to be written to the file.
     * @param fileName The name of the file.
     * @return The JSON string representing the data.
     */
    private <T> String writeJsonToFile(List<T> data, String fileName) {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            File outputFile = new File(directory, fileName);

            // Delete the existing file if it exists
            if (outputFile.exists()) {
                outputFile.delete();
            }

            // Write JSON to the file
            writer.writeValue(outputFile, data);

            return writer.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return "";
    }

    /**
     * Writes GeoJSON data to a file.
     *
     * @param data     The GeoJSON data to be written to the file.
     * @param fileName The name of the file.
     * @param directory The directory where the file is stored.
     * @return The GeoJSON string representing the data.
     */
    private String writeToFile(LinkedHashMap<String, Object> data, String fileName, File directory) {
        try (FileWriter fileWriter = new FileWriter(new File(directory, fileName))) {
            JSONObject jsonFeatureCollection = new JSONObject(data);
            fileWriter.write(jsonFeatureCollection.toString(2));
            return jsonFeatureCollection.toString();
        } catch (IOException e) {
            System.err.println("Error in writing drone path to GeoJson file");
        }
        return "";
    }

    /**
     * Converts the flight path to a GeoJSON object.
     *
     * @param flightPath The list of moves representing the flight path.
     * @return The GeoJSON object representing the flight path.
     */
    private LinkedHashMap<String, Object> getJsonObject(List<Move> flightPath) {
        LinkedHashMap<String, Object> featureCollection = new LinkedHashMap<>();
        LinkedList<LinkedHashMap<String, Object>> features = new LinkedList<>();
        LinkedHashMap<String, Object> feature = new LinkedHashMap<>();
        LinkedHashMap<String, Object> geometry = new LinkedHashMap<>();
        LinkedList<LinkedList<Double>> coordinates = new LinkedList<>();

        for (Move move : flightPath) {
            LinkedList<Double> coordinate = new LinkedList<>();
            coordinate.add(move.getFromLongitude());
            coordinate.add(move.getFromLatitude());
            coordinates.add(coordinate);
        }

        feature.put("type", "Feature");
        feature.put("geometry", geometry);
        feature.put("properties", new LinkedHashMap<>());
        features.add(feature);
        featureCollection.put("type", "FeatureCollection");
        featureCollection.put("features", features);
        geometry.put("type", "LineString");
        geometry.put("coordinates", coordinates);

        return featureCollection;
    }

    /**
     * Creates a directory if it does not exist.
     *
     * @param directoryPath The path of the directory to be created.
     * @return The created directory.
     */
    private File createDirectory(String directoryPath) {
        File directory = new File(directoryPath);

        // Create the directory and any necessary parent directories
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }
}
