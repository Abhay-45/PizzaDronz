package uk.ac.ed.inf.model;

import uk.ac.ed.inf.output.Logger;
import uk.ac.ed.inf.data.Delivery;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.util.SuccessStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * OrderHandler class manages the validation and creation of delivery lists for orders.
 */
public class OrderHandler {
    private List<Order> orders;
    private Restaurant[] restaurants;
    private Logger logger;

    /**
     * Constructs an OrderHandler with a list of orders and an array of restaurants.
     *
     * @param orders      The list of orders to handle.
     * @param restaurants The array of restaurants to consider for validation.
     */
    public OrderHandler(List<Order> orders, Restaurant[] restaurants) {
        this.orders = orders;
        this.restaurants = restaurants;
        this.logger = new Logger();
    }

    /**
     * Validates the orders using an OrderValidator and logs success.
     *
     * @return The list of validated orders.
     */
    public List<Order> validateOrders() {
        OrderValidator orderValidator = new OrderValidator();
        for (Order order : orders) {
            orderValidator.validateOrder(order, restaurants);
        }
        logger.logSuccess(SuccessStatus.VALIDATED_ORDERS);
        return orders;
    }

    /**
     * Creates a list of deliveries based on the given list of orders.
     *
     * @param orders The list of orders to create deliveries from.
     * @return The list of created deliveries.
     */
    public List<Delivery> createDeliveryList(List<Order> orders) {
        List<Delivery> deliveries = new ArrayList<>();
        for (Order order : orders) {
            deliveries.add(new Delivery(
                    order.getOrderNo(),
                    order.getOrderStatus(),
                    order.getOrderValidationCode(),
                    order.getPriceTotalInPence()
            ));
        }
        return deliveries;
    }
}
