package uk.ac.ed.inf;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import uk.ac.ed.inf.model.OrderHandler;
import uk.ac.ed.inf.api.RestClient;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.time.LocalDate;
import java.util.List;

public class OrderControllerTest {

    @Test
    public void printOrdersAfterProcessing() {
        String baseUrl = "https://ilp-rest.azurewebsites.net";
        RestClient restClient = new RestClient(baseUrl);
        List<Order> orders = restClient.fetchOrders(LocalDate.now().toString());
        Restaurant[] restaurants = restClient.fetchRestaurants();
        OrderHandler orderHandler = new OrderHandler(orders, restaurants);
        List<Order> processedOrder = orderHandler.validateOrders();
        for (Order order : processedOrder) {
            System.out.println(order.getOrderValidationCode());
        }
    }


    @Test
    public void orderSequenceHasNotChanged() {
        String baseUrl = "https://ilp-rest.azurewebsites.net";
        RestClient restClient = new RestClient(baseUrl);
        List<Order> orders = restClient.fetchOrders(LocalDate.now().toString());
        Restaurant[] restaurants = restClient.fetchRestaurants();
        OrderHandler orderHandler = new OrderHandler(orders, restaurants);
        List<Order> processedOrder = orderHandler.validateOrders();

        for (int i = 0; i < orders.size(); i++) {
            assertTrue(orders.get(i).getOrderNo() == processedOrder.get(i).getOrderNo());
        }
    }
}
