package uk.ac.ed.inf;

import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.ed.inf.controller.PizzaDronzController;
import uk.ac.ed.inf.model.LngLatHandler;
import uk.ac.ed.inf.model.OrderHandler;
import uk.ac.ed.inf.api.RestClient;
import uk.ac.ed.inf.data.Move;
import uk.ac.ed.inf.data.Node;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.model.pathFinder.AStar;
import uk.ac.ed.inf.model.pathFinder.FlightPathGenerator;
import uk.ac.ed.inf.output.JsonWriter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AStarTest {

    LngLat appletonTower = new LngLat(-3.186874, 55.944494);
//    @Test
//    public void testAStar() {
//        RestClient restClient = new RestClient("https://ilp-rest.azurewebsites.net");
//        NamedRegion centralArea = restClient.fetchCentralArea();
//        List<NamedRegion> noFlyZone = restClient.fetchNoFlyZones();
//        System.out.println(centralArea.toString());
//
//        AStar astar = new AStar(appletonTower, new LngLat(-3.1912869215011597, 55.945535152517735), centralArea, noFlyZone);
//        Node result = astar.calculatePath();
//        System.out.println(result.toString());
//        List<LngLat> list = astar.reconstructPath(result);
//        for(int i = 0; i < list.size(); i++){
//            System.out.println(list.get(i).toString());
//        }
//        for (LngLat lngLat : list){
//            System.out.println(" ");
//            System.out.println("[\n" + lngLat.lng() + ", " + lngLat.lat() + "\n],");
//        }
//
//    }

    @Test
    public void testAStarForRestaurantSodeberg() {
        RestClient restClient = new RestClient("https://ilp-rest.azurewebsites.net");
        NamedRegion centralArea = restClient.fetchCentralArea();
        List<NamedRegion> noFlyZone = restClient.fetchNoFlyZones();
        System.out.println(centralArea.toString());

        AStar astar = new AStar( appletonTower, new LngLat(-3.1940174102783203, 55.94390696616939), centralArea, noFlyZone);
        Node result = astar.calculatePath();
        LngLatHandler lngLatHandler = new LngLatHandler();
        assertTrue(lngLatHandler.isCloseTo(result.getLngLat(), new LngLat(-3.1940174102783203, 55.94390696616939)));

    }

    @Test
    public void testAStarForRestaurantSoraLella() {
        RestClient restClient = new RestClient("https://ilp-rest.azurewebsites.net");
        NamedRegion centralArea = restClient.fetchCentralArea();
        List<NamedRegion> noFlyZone = restClient.fetchNoFlyZones();
        System.out.println(centralArea.toString());

        AStar astar = new AStar( appletonTower, new LngLat(-3.202541470527649, 55.943284737579376), centralArea, noFlyZone);
        Node result = astar.calculatePath();
        System.out.println(result.toString());
        LngLatHandler lngLatHandler = new LngLatHandler();
        assertTrue(lngLatHandler.isCloseTo(result.getLngLat(), new LngLat(-3.202541470527649, 55.943284737579376)));


    }

//    @Test
//    public void testFlightPathGeneration(){
//        RestClient restClient = new RestClient("https://ilp-rest.azurewebsites.net");
//        NamedRegion centralArea = restClient.fetchCentralArea();
//        List<NamedRegion> noFlyZone = restClient.fetchNoFlyZones();
//        Restaurant[] restaurants = restClient.fetchRestaurants();
//        List<Order> orders = restClient.fetchOrders(LocalDate.now().toString());
//
//        OrderHandler orderHandler = new OrderHandler(orders, restaurants);
//        List<Order> validOrders = new ArrayList<>();
//         validOrders = orderHandler.validateOrders();
//
//        FlightPathGenerator flightPathGenerator = new FlightPathGenerator(centralArea, noFlyZone);
////        System.out.println(orders.get(0));
//        List<Move> paths = flightPathGenerator.generateFlightPath(restaurants, Collections.singletonList(validOrders.get(0)));
////        AStar astar = new AStar( appletonTower, new LngLat(-3.202541470527649, 55.943284737579376), centralArea, noFlyZone);
////        Node result = astar.calculatePath();
////        List<Move> flightPaths = astar.constructFlightPath(result, "1");
//
//        JsonWriter jsonWriter = new JsonWriter();
//        String json = jsonWriter.writeFlightPathToJson(paths, LocalDate.now().toString());
//        System.out.println(json);
////        for(FlightPath flightPath : flightPaths){
////            System.out.println(flightPath.toString());
////        }
//
//    }



    @Test
    public void Astar(){
        LngLat destination = new LngLat( -3.202541470527649,55.943284737579376);
        LngLat start = new LngLat(-3.186874, 55.944494);
        RestClient restClient = new RestClient("https://ilp-rest.azurewebsites.net");
        NamedRegion centralArea = restClient.fetchCentralArea();
        LngLatHandler lngLatHandler = new LngLatHandler();
        List<NamedRegion> noFlyZone = restClient.fetchNoFlyZones();
        AStar astar = new AStar(start, destination, centralArea, noFlyZone);
        Node startNode = new Node(null, start, 0, lngLatHandler.distanceTo(start, destination), 999 );
//        List<Node> neighbours = astar.getNeighbours(startNode);
//        System.out.println(astar.getNeighbours(startNode).size());
    }

    @Test
    public void testOrderController(){
        RestClient restClient = new RestClient("https://ilp-rest.azurewebsites.net");
        Restaurant[] restaurants = restClient.fetchRestaurants();
        List<Order> orders = restClient.fetchOrders(LocalDate.now().toString());
        OrderHandler orderHandler = new OrderHandler(orders, restaurants);
        orders = orderHandler.validateOrders();
        for(Order order : orders){
            System.out.println(order.toString());
        }

    }

//    @Test
//    public void testCoreController(){
//        RestClient restClient = new RestClient("https://ilp-rest.azurewebsites.net");
//        PizzaDronzController pizzaDronzController = new PizzaDronzController("https://ilp-rest.azurewebsites.net", LocalDate.parse("2023-10-26"), restClient);
//        pizzaDronzController.run();
//    }
}
