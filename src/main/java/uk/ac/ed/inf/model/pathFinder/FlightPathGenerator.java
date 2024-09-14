package uk.ac.ed.inf.model.pathFinder;

import uk.ac.ed.inf.output.Logger;
import uk.ac.ed.inf.util.SuccessStatus;
import uk.ac.ed.inf.data.Move;
import uk.ac.ed.inf.data.Node;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.data.*;
import uk.ac.ed.inf.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * FlightPathGenerator class is responsible for generating flight paths for pizza deliveries using A* algorithm.
 */
public class FlightPathGenerator {
    private final HashMap<String, Restaurant> pizzaRestaurantNameMap = new HashMap<>();
    private final NamedRegion centralArea;
    private final List<NamedRegion> noFlyZone;
    private Logger logger;

    /**
     * Constructs a FlightPathGenerator object with the specified central area and list of no-fly zones.
     *
     * @param centralArea The central area constraint (NamedRegion).
     * @param noFlyZone   The list of no-fly zones (NamedRegion).
     */
    public FlightPathGenerator(NamedRegion centralArea, List<NamedRegion> noFlyZone) {
        this.centralArea = centralArea;
        this.noFlyZone = noFlyZone;
        this.logger = new Logger();
    }

    /**
     * Generates flight paths for a given set of restaurants and orders.
     *
     * @param definedRestaurants An array of defined restaurants.
     * @param orders             A list of orders.
     * @return A list of moves representing the flight paths.
     */
    public List<Move> generateFlightPath(Restaurant[] definedRestaurants, List<Order> orders) {
        mapPizzaAndRestaurant(definedRestaurants);
        List<Move> allMoves = new ArrayList<>();
        logger.logSuccess(SuccessStatus.GENERATING_FLIGHT_PATHS, "...");
        for (Order order : orders) {
            if (order.getOrderStatus() == OrderStatus.VALID_BUT_NOT_DELIVERED) {
                calculateFlightPathForOrder(order, allMoves);
            }
        }
        logger.logSuccess(SuccessStatus.SUCCESSFULLY_GENERATED_FLIGHT_PATHS);
        return allMoves;
    }

    /**
     * Maps pizzas to their corresponding restaurants.
     *
     * @param definedRestaurants An array of defined restaurants.
     */
    private void mapPizzaAndRestaurant(Restaurant[] definedRestaurants) {
        for (Restaurant restaurant : definedRestaurants) {
            for (Pizza pizza : restaurant.menu()) {
                pizzaRestaurantNameMap.put(pizza.name(), restaurant);
            }
        }
    }

    /**
     * Calculates the flight path for a specific order and adds the moves to the overall list.
     *
     * @param order     The order for which the flight path is calculated.
     * @param allMoves  The list of all moves representing flight paths.
     */
    private void calculateFlightPathForOrder(Order order, List<Move> allMoves) {
        Pizza pizza = order.getPizzasInOrder()[0];
        Restaurant restaurant = pizzaRestaurantNameMap.get(pizza.name());
        LngLat restaurantLocation = restaurant.location();

        AStar astar = new AStar(Constants.APPLETON_TOWER, restaurantLocation, centralArea, noFlyZone);
        Node lastNode = astar.calculatePath();

        if (lastNode == null) {
            order.setOrderStatus(OrderStatus.VALID_BUT_NOT_DELIVERED);
            return;
        }

        List<Move> currMoves = constructFlightPath(lastNode, order.getOrderNo());
        List<Move> resultMovesToAdd = generateCompleteMoveList(currMoves, lastNode, order.getOrderNo());

        allMoves.addAll(resultMovesToAdd);
        order.setOrderStatus(OrderStatus.DELIVERED);
    }

    /**
     * Generates the complete move list including hover moves, reverse moves, etc.
     *
     * @param currMoves    The current list of moves.
     * @param lastNode     The last node in the flight path.
     * @param orderNo      The order number.
     * @return The complete list of moves.
     */
    private List<Move> generateCompleteMoveList(List<Move> currMoves, Node lastNode, String orderNo) {
        List<Move> reverseMoves = generateReverseMoves(currMoves);
        List<Move> resultMovesToAdd = new ArrayList<>();

        currMoves.add(createHoverMove(lastNode, orderNo, Constants.HOVER_ANGLE));

        resultMovesToAdd.addAll(currMoves);
        resultMovesToAdd.addAll(reverseMoves);
        return resultMovesToAdd;
    }


    /**
     * Constructs the flight path for a given node.
     *
     * @param node    The node for which the flight path is constructed.
     * @param orderNo The order number.
     * @return The list of moves representing the flight path.
     */
    private List<Move> constructFlightPath(Node node, String orderNo){
        List<Move> flightPath = new ArrayList<>();
        while(node.getParent() != null){

            flightPath.add(new Move(
                    orderNo,
                    node.getParent().getLngLat().lng(),
                    node.getParent().getLngLat().lat(),
                    node.getAngle(),
                    node.getLngLat().lng(),
                    node.getLngLat().lat()
            ));
            node = node.getParent();
        }
        Collections.reverse(flightPath);
        return flightPath;
    }

    /**
     * Generates the reverse moves for a given list of moves.
     *
     * @param currMoves The current list of moves.
     * @return The list of reverse moves.
     */
    private List<Move> generateReverseMoves(List<Move> currMoves) {
        List<Move> reverseMoves = new ArrayList<>();
        for (int i = currMoves.size() - 1; i >= 0; i--) {
            reverseMoves.add(createReverseMove(currMoves.get(i)));
        }
        reverseMoves.add(createHoverMoveForReversePath(currMoves.get(0)));
        return reverseMoves;
    }

    /**
     * Creates a reverse move for a given move.
     *
     * @param move The original move.
     * @return The reverse move.
     */
    private Move createReverseMove(Move move) {
        return new Move(
                move.getOrderNo(),
                move.getToLongitude(),
                move.getToLatitude(),
                getReverseAngle(move.getAngle()),
                move.getFromLongitude(),
                move.getFromLatitude()
        );
    }

    /**
     * Gets the reverse angle of a given angle.
     *
     * @param angle The angle for which the reverse angle is calculated.
     * @return The reverse angle.
     */
    private Double getReverseAngle(Double angle){
        return (angle + 180) % 360;
    }

    /**
     * Creates a hover move for the reverse path.
     *
     * @param move The original move.
     * @return The hover move for the reverse path.
     */
    private Move createHoverMoveForReversePath(Move move) {
        return new Move(
                move.getOrderNo(),
                move.getFromLongitude(),
                move.getFromLatitude(),
                Constants.HOVER_ANGLE,
                move.getFromLongitude(),
                move.getFromLatitude()
        );
    }

    /**
     * Creates a hover move for a given node.
     *
     * @param node     The node for which the hover move is created.
     * @param orderNo  The order number.
     * @param hoverAngle The hover angle.
     * @return The hover move.
     */
    private Move createHoverMove(Node node, String orderNo, double hoverAngle) {
        return new Move(
                orderNo,
                node.getLngLat().lng(),
                node.getLngLat().lat(),
                hoverAngle,
                node.getLngLat().lng(),
                node.getLngLat().lat()
        );
    }
}
