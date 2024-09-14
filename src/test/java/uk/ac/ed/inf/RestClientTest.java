package uk.ac.ed.inf;
import org.junit.Test;
import uk.ac.ed.inf.api.RestClient;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.util.List;

import static org.junit.Assert.*;

public class RestClientTest {
    @Test
    public void testConstructor(){
        RestClient restClient = new RestClient("https://ilp-rest.azurewebsites.net");
        assertNotNull(restClient);
    }

    @Test
    public void testFetchRestaurants() {
        RestClient restClient = new RestClient("https://ilp-rest.azurewebsites.net");
        Restaurant[] result = restClient.fetchRestaurants();
        for (Restaurant r : result) {
            System.out.println(r.name());
        }
    }

    @Test
    public void testFetchOrders(){
        RestClient restClient = new RestClient("https://ilp-rest.azurewebsites.net");
        List<Order> result = restClient.fetchOrders("2023-09-01");
        for (Order o : result) {
            System.out.println(o.getOrderDate());
        }
    }

    @Test
    public void testFetchNoFlyZones(){
        RestClient restClient = new RestClient("https://ilp-rest.azurewebsites.net");
        List<NamedRegion> result = restClient.fetchNoFlyZones();
        for (NamedRegion n : result) {
            System.out.println(n.name());
        }
    }

    @Test
    public void testFetchCentralArea(){
        RestClient restClient = new RestClient("https://ilp-rest.azurewebsites.net");
        NamedRegion result = restClient.fetchCentralArea();

        System.out.println(result.name());
        for (LngLat l : result.vertices()) {
             System.out.println(l.lng());
            System.out.println(l.lat());
        }
    }


}
