package uk.ac.ed.inf.model;

import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.*;
import uk.ac.ed.inf.ilp.constant.*;
import uk.ac.ed.inf.model.OrderValidator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import static org.junit.Assert.*;

public class PizzaInfoTest {
    private static Order orderSample;
    private static Pizza pizza1, pizza2, pizza3, pizza4, pizza5, pizza6, pizza7, pizza8;
    private static Restaurant[] restaurants;
    private static OrderValidator orderValidator = new OrderValidator();
    private static Restaurant[] restaurantsForNULL;


    @BeforeClass
    public static void setUpClass() {
        // Restaurant 1
        LngLat civerinosSliceLngLat = new LngLat(-3.1912869215011597,  55.945535152517735);
        DayOfWeek[] civerinosSliceOpenDay = {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY};
        Pizza[] civerinosMenu = {new Pizza("Margarita", 1000), new Pizza("Calzone", 1400)};

        // Restaurant 2
        LngLat soraLellaLngLat = new LngLat(-3.202541470527649, 55.943284737579376);
        DayOfWeek[] soraLellaOpenDay = {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY};
        Pizza[] soraLellaMenu = {new Pizza("Meat Lover=", 1400), new Pizza("Vegan Delight", 1100)};

        // Restaurant 3
        LngLat dominosLngLat = new LngLat(-3.1838572025299072, 55.94449876875712);
        DayOfWeek[] dominosOpenDay = {DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY};
        Pizza[] dominoMenu = {new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)};

        // Restaurant 4
        LngLat sodebergLngLat = new LngLat(-3.1940174102783203, 55.94390696616939);
        DayOfWeek[] sodebergOpenDay = {DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY};
        Pizza[] sodebergMenu = {new Pizza("Proper Pizza", 1400), new Pizza("Pineapple & Ham & Cheese", 900)};

        // Restaurant 5 NUll in Opening Days
        LngLat restaurant5LngLat = new LngLat(-3.1940174102783203, 55.94390696616939);
        DayOfWeek[] restaurant5OpenDay = {DayOfWeek.TUESDAY, null, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY};
        Pizza[] restaurant5Menu = {new Pizza("Proper Pizza", 1400), new Pizza("Pineapple & Ham & Cheese", 900)};

        restaurants = new Restaurant[4];
        restaurants[0] = new Restaurant("Civerinos Slice", civerinosSliceLngLat, civerinosSliceOpenDay, civerinosMenu);
        restaurants[1] = new Restaurant("Sora Lella Vegan Restaurant", soraLellaLngLat, soraLellaOpenDay, soraLellaMenu);
        restaurants[2] = new Restaurant("Domino's Pizza - Edinburgh - Southside", dominosLngLat, dominosOpenDay, dominoMenu);
        restaurants[3] = new Restaurant("Sodeberg Pavillion", sodebergLngLat, sodebergOpenDay, sodebergMenu);

        restaurantsForNULL = new Restaurant[1];
        restaurantsForNULL[0] = new Restaurant("Restaurant 5", restaurant5LngLat, restaurant5OpenDay, restaurant5Menu);

        // Required Setup for Testing OrderValidator
        orderValidator.setUpHashMaps(restaurants);

        // Generic Order
        orderSample = new Order(
                "32513460",
                LocalDate.parse("2023-10-01"),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                2400,
                new Pizza[]{},
                new CreditCardInformation("1234567891234567", "04/12", "123")
        );

        // Correct Pizza
        pizza1 = new Pizza("Super Cheese", 1400);
        pizza2 = new Pizza("All Shrooms", 900);
        pizza3 = new Pizza("Proper Pizza", 1400);
        pizza4 = new Pizza("Vegan Delight", 900);

        // Wrong Pizzas
        pizza5 = new Pizza("Pizza-Extra1", 1400);
        pizza6 = new Pizza("Pizza-Extra2", 900);
        pizza7 = new Pizza("Pizza-Extra3", 900);
        pizza8 = new Pizza("Pizza-Extra4", 1100);
    }


    // Max Pizza Test
    @Test
    public void shouldNotAccept5PizzaInOrder(){
        orderSample.setPizzasInOrder(new Pizza[]{pizza1, pizza2, pizza3, pizza4, pizza5});
        boolean result = orderValidator.maxPizzaCountExceeded(orderSample.getPizzasInOrder());
        assertTrue(result);
    }

    @Test
    public void shouldAccept4PizzaInOrder(){
        orderSample.setPizzasInOrder(new Pizza[]{pizza1, pizza2, pizza3, pizza4});
        boolean result = orderValidator.maxPizzaCountExceeded(orderSample.getPizzasInOrder());
        assertFalse(result);
    }

    @Test
    public void shouldAccept3PizzaInOrder(){
        orderSample.setPizzasInOrder(new Pizza[]{pizza1, pizza2, pizza3});
        boolean result = orderValidator.maxPizzaCountExceeded(orderSample.getPizzasInOrder());
        assertFalse(result);
    }

    // Pizza Defined Tests
    @Test
    public void shouldNotAcceptEmptyPizzaOrder(){
        boolean result = orderValidator.arePizzasDefined(orderSample.getPizzasInOrder());
        assertFalse(result);
    }

    @Test
    public void shouldNotAcceptNullPizzaOrder(){
        orderSample.setPizzasInOrder(null);
        boolean result = orderValidator.arePizzasDefined(orderSample.getPizzasInOrder());
        assertFalse(result);
    }

    @Test
    public void shouldNotAccept1WrongPizzas(){
        orderSample.setPizzasInOrder(new Pizza[]{pizza5});
        boolean result = orderValidator.arePizzasDefined(orderSample.getPizzasInOrder());
        assertFalse(result);
    }

    @Test
    public void shouldNotAccept2WrongPizzasInOrderFor4(){
        orderSample.setPizzasInOrder(new Pizza[]{pizza1, pizza2, pizza5, pizza6});
        boolean result = orderValidator.arePizzasDefined(orderSample.getPizzasInOrder());
        assertFalse(result);
    }

    @Test
    public void shouldNotAcceptOrderWithPizzaNameNull(){
        Pizza nullPizza = new Pizza(null, 1400);
        orderSample.setPizzasInOrder(new Pizza[]{nullPizza});
        boolean result = orderValidator.arePizzasDefined(orderSample.getPizzasInOrder());
        assertFalse(result);
    }

    // Pizza From Multiple Restaurants Validation
    @Test public void shouldNotAcceptOrderWithPizzasFromMultipleRestaurants(){
        orderSample.setPizzasInOrder(new Pizza[]{pizza1, pizza3});
        boolean result = orderValidator.arePizzasFromMultipleRestaurants(orderSample.getPizzasInOrder());
        assertTrue(result);
    }

    @Test public void shouldAcceptOrderWithPizzasFromSameRestaurant(){
        orderSample.setPizzasInOrder(new Pizza[]{pizza1, pizza2});
        boolean result = orderValidator.arePizzasFromMultipleRestaurants(orderSample.getPizzasInOrder());
        assertFalse(result);
    }

    // Restaurant Closed Validation
    @Test
    public void shouldNotAcceptOrderWithPizzasFromClosedRestaurant(){
        orderSample.setPizzasInOrder(new Pizza[]{pizza1, pizza2});
        orderSample.setOrderDate(LocalDate.parse("2023-09-25")); //MONDAY
        boolean result = orderValidator.isRestaurantClosed(orderSample.getPizzasInOrder(), orderSample.getOrderDate());
        assertTrue(result);
    }

    @Test
    public void shouldAcceptOrderWithPizzasFromOpenRestaurant(){
        orderSample.setPizzasInOrder(new Pizza[]{pizza1, pizza2});
        orderSample.setOrderDate(LocalDate.parse("2023-09-27")); //WEDNESDAY
        boolean result = orderValidator.isRestaurantClosed(orderSample.getPizzasInOrder(), orderSample.getOrderDate());
        assertFalse(result);
    }

    // Total Price Validation
    @Test
    public void shouldNotAcceptOrderWithIncorrectTotalPrice(){
        orderSample.setPizzasInOrder(new Pizza[]{pizza1, pizza2});
        orderSample.setPriceTotalInPence(1000);
        boolean result = orderValidator.isTotalPriceIncorrect(orderSample.getPizzasInOrder(), orderSample.getPriceTotalInPence());
        assertTrue(result);
    }

    @Test
    public void shouldAcceptOrderWithCorrectTotalPrice(){
        orderSample.setPizzasInOrder(new Pizza[]{pizza1, pizza2});
        orderSample.setPriceTotalInPence(2400); //+100 for delivery
        boolean result = orderValidator.isTotalPriceIncorrect(orderSample.getPizzasInOrder(), orderSample.getPriceTotalInPence());
        assertFalse(result);
    }

    @Test
    public void shouldIgnoreNullInOpeningDaysForRestaurant(){
        OrderValidator orderValidator = new OrderValidator();
        orderValidator.setUpHashMaps(restaurantsForNULL);
        Order newOrder = new Order(
                "32513460",
                LocalDate.parse("2023-10-01"),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                2400,
                new Pizza[]{},
                new CreditCardInformation("1234567891234567", "04/12", "123")
        );
        newOrder.setPizzasInOrder(new Pizza[]{pizza1, pizza2});
        newOrder.setOrderDate(LocalDate.parse("2023-09-27")); //WEDNESDAY
        boolean result = orderValidator.isRestaurantClosed(newOrder.getPizzasInOrder(), newOrder.getOrderDate());
        assertTrue(result);
    }




}
