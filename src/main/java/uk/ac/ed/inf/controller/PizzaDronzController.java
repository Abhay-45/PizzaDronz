package uk.ac.ed.inf.controller;

import uk.ac.ed.inf.output.Logger;
import uk.ac.ed.inf.api.RestClient;
import uk.ac.ed.inf.data.Delivery;
import uk.ac.ed.inf.model.OrderHandler;
import uk.ac.ed.inf.model.pathFinder.FlightPathGenerator;
import uk.ac.ed.inf.output.JsonWriter;
import uk.ac.ed.inf.data.Move;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.util.SuccessStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller class for the PizzaDronz application. Manages the flow of data and controls the main execution.
 */
public class PizzaDronzController {
    private String baseUrl;
    private LocalDate date;
    private Restaurant[] restaurants;
    private List<Order> orders;
    private List<NamedRegion> noFlyZones;
    private NamedRegion centralArea;
    private RestClient restClient;

    /**
     * Constructs a PizzaDronzController with the specified base URL and date.
     *
     * @param baseUrl The base URL of the PizzaDronz API.
     * @param date    The date for which the controller is operating.
     */
    public PizzaDronzController(String baseUrl, LocalDate date, RestClient restClient) {
        this.baseUrl = baseUrl;
        this.date = date;
        this.restClient = restClient;
    }

    /**
     * Runs the PizzaDronzController, fetching data from the API, processing orders, generating flight paths,
     * creating delivery lists, and writing results to JSON and GeoJSON files.
     */
    public void run() {
        Logger logger = new Logger();
        logger.logSuccess(SuccessStatus.RUNNING_PIZZADRONZ_CONTROLLER, "...");

        // Fetch data from the server
        this.restaurants = restClient.fetchRestaurants();
        this.orders = restClient.fetchOrders(date.toString());
        this.noFlyZones = restClient.fetchNoFlyZones();
        this.centralArea = restClient.fetchCentralArea();
        logger.logSuccess(SuccessStatus.SUCCESSFULLY_FETCHED_DATA);


        // Process the orders
        OrderHandler orderHandler = new OrderHandler(orders, restaurants);
        this.orders = orderHandler.validateOrders();

        // Generate the flight path
        FlightPathGenerator flightPathGenerator = new FlightPathGenerator(centralArea, noFlyZones);
        List<Move> paths = flightPathGenerator.generateFlightPath(restaurants, orders);

        // Create the delivery list
        List<Delivery> deliveries = orderHandler.createDeliveryList(orders);

        // Write deliveries to JSON file
        JsonWriter jsonWriter = new JsonWriter();
        jsonWriter.writeDeliveriesToJson(deliveries, date.toString());

        // Write flight path to JSON file
        jsonWriter.writeFlightPathToJson(paths, date.toString());

        // Write drone path to GeoJSON file
        jsonWriter.writeDronePathToGeoJson(paths, date.toString());

        logger.logSuccess(SuccessStatus.SUCCESSFULLY_CREATED_ALL_FILES);

        logger.logSuccess(SuccessStatus.FINISHED, "!");
    }

}
