package uk.ac.ed.inf;

import uk.ac.ed.inf.api.RestClient;
import uk.ac.ed.inf.controller.PizzaDronzController;
import uk.ac.ed.inf.output.Logger;
import uk.ac.ed.inf.util.SuccessStatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * The main class that contains the entry point for the PizzaDronz application.
 */
public class App {
    /**
     * The main method of the program. It takes in two arguments, the date and the URL.
     * It then validates the arguments and creates a PizzaDronzController object to run the program.
     *
     * @param args The arguments passed in from the command line.
     */
    public static void main(String[] args) {
        // Create a logger for logging events to the console
        Logger logger = new Logger();

        // Check if there are exactly two command line arguments
        if (args.length != 2) {
            System.err.println("Error: Expected 2 arguments, but found " + args.length);
            System.exit(1);
        } else {
            String date = args[0];
            String url = args[1];

            // Check if date or URL is null
            if (date == null || url == null) {
                System.err.println("Expected: Date and URL cannot be null");
                System.exit(1);
            }

            // Validate the date and URL
            try {
                LocalDate localDate = parseDate(date);
                URL validUrl = validateUrl(url);

                RestClient restClient = new RestClient(url.toString());
                boolean isAlive = restClient.isAlive();
                if (!isAlive) {
                    System.err.println("Error: REST server is not alive");
                    System.exit(1);
                }
                // Get the HTTP response code and handle it
                int statusCode = handleResponseCode(validUrl, logger);

                if (statusCode == 200) {
                    // If the status code is 200, log success and run the PizzaDronzController
                    logger.logSuccess(SuccessStatus.HTTP_STATUS_CODE, ": " + String.valueOf(statusCode) + " OK");
                    PizzaDronzController deliveryController = new PizzaDronzController(validUrl.toString(), localDate, restClient);
                    deliveryController.run();
                } else {
                    // If the status code is not 200, print an error message
                    System.err.println("Error: Expected status code 200, but found " + statusCode);
                }
            } catch (IllegalArgumentException e) {
                // Handle exceptions related to invalid arguments
                System.err.println("Error: " + e.getMessage());
                System.exit(1);
            }
        }
    }

    /**
     * Parses a date string into a LocalDate object.
     *
     * @param date The date string in the format "YYYY-MM-DD".
     * @return A LocalDate object representing the parsed date.
     */
    private static LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            // Handle exceptions related to invalid date format
            System.err.println("Invalid date format. Expected: YYYY-MM-DD, Actual: " + date);
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    /**
     * Validates a URL string and returns a URL object.
     *
     * @param url The URL string to be validated.
     * @return A validated URL object.
     */
    private static URL validateUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            // Handle exceptions related to invalid URL format
            System.err.println("Invalid URL format. Expected: https://ilp-rest.azurewebsites.net, Actual: " + url);
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    /**
     * Handles the HTTP response code from a given URL and logs the connection status.
     *
     * @param url    The URL to connect to and check the response code.
     * @param logger The logger to log connection events.
     * @return The HTTP response code.
     */
    private static int handleResponseCode(URL url, Logger logger) {
        try {
            // Log the attempt to connect to the REST server
            logger.logSuccess(SuccessStatus.CONNECTING_TO_REST_SERVER, "...");


            // Open a connection to the URL and get the HTTP response code
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            int statusCode = http.getResponseCode();

            return statusCode;
        } catch (IOException e) {
            // Handle exceptions related to HTTP connection errors
            System.err.println("HTTP Connection Error: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
        }
        return -1;
    }
}
