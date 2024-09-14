package uk.ac.ed.inf.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import uk.ac.ed.inf.output.Logger;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.util.Constants;
import uk.ac.ed.inf.util.SuccessStatus;

/**
 * A client for interacting with the PizzaDronz REST API.
 */
public class RestClient {
    private String baseUrl;
    private Logger logger;

    private ObjectMapper mapper;

    /**
     * Constructs a RestClient with the specified base URL.
     *
     * @param url The base URL of the PizzaDronz REST API.
     */
    public RestClient(String url) {
        // Ensure that the URL ends with a slash
        if (!url.endsWith("/")) {
            url += "/";
        }
        baseUrl = url;
        logger = new Logger();
        mapper = new ObjectMapper();
    }

    /**
     * Check if the REST API is alive.
     *
     * @return True if the Rest Responds Alive, false otherwise.
     */
    public boolean isAlive(){
        try {
            URL url = new URL(baseUrl + Constants.ALIVE_URL);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(url, Boolean.class);
        } catch (Exception e) {
            System.err.println("Error connecting to isAlive endpoint");
            System.exit(1);
        }
        return false;
    }


    /**
     * Fetches the list of restaurants from the API.
     *
     * @return An array of Restaurant objects representing the fetched restaurants.
     */
    public Restaurant[] fetchRestaurants() {
        try {
            URL urlRestaurants = new URL(baseUrl + Constants.RESTAURANT_URL);
            Restaurant[] restaurants = mapper.readValue(urlRestaurants, Restaurant[].class);
            return restaurants;
        } catch (Exception e) {
            // Handle other exceptions
            System.out.println("Error in fetching restaurants");
            System.out.println("Error:" + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    /**
     * Fetches the list of orders from the API.
     *
     * @return A list of Order objects representing the fetched orders.
     */
    public List<Order> fetchOrders() {
        try {
            mapper.registerModule(new JavaTimeModule());
            URL urlOrders = new URL(baseUrl + Constants.ORDER_URL);
            Order[] orders = mapper.readValue(urlOrders, Order[].class);
            return Arrays.asList(orders);
        } catch (Exception e) {
            // Handle other exceptions
            System.out.println("Error in fetching orders");
            System.out.println("Error:" + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    /**
     * Fetches the list of orders for a specific date from the API.
     *
     * @param date The date for which orders should be fetched.
     * @return A list of Order objects representing the fetched orders for the specified date.
     */
    public List<Order> fetchOrders(String date) {
        try {
            mapper.registerModule(new JavaTimeModule());
            URL urlOrders = new URL(baseUrl + Constants.ORDER_URL + "/" + date);
            Order[] orders = mapper.readValue(urlOrders, Order[].class);
            return Arrays.asList(orders);
        } catch (Exception e) {
            // Handle other exceptions
            System.out.println("Error in fetching orders");
            System.out.println("Error:" + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    /**
     * Fetches the list of no-fly zones from the API.
     *
     * @return A list of NamedRegion objects representing the fetched no-fly zones.
     */
    public List<NamedRegion> fetchNoFlyZones() {
        try {
            URL urlNoFlyZones = new URL(baseUrl + Constants.NOFLYZONE_URL);
            NamedRegion[] noFlyZones = mapper.readValue(urlNoFlyZones, NamedRegion[].class);
            return Arrays.asList(noFlyZones);
        }
        catch (Exception e) {
            // Handle other exceptions
            System.out.println("Error in fetching no-fly zones");
            System.out.println("Error:" + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    /**
     * Fetches the central area information from the API.
     *
     * @return A NamedRegion object representing the fetched central area.
     */
    public NamedRegion fetchCentralArea() {
        try {
            URL urlCentralArea = new URL(baseUrl + Constants.CENTRALAREA_URL);
            NamedRegion centralArea = mapper.readValue(urlCentralArea, NamedRegion.class);
            return centralArea;
        }catch (Exception e) {
            // Handle other exceptions
            System.out.println("Error in fetching central area");
            System.out.println("Error:" + e.getMessage());
            System.exit(1);
        }
        return null;
    }
}
