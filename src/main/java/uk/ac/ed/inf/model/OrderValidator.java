package uk.ac.ed.inf.model;
import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Pizza;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.ilp.interfaces.OrderValidation;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.constant.OrderStatus;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.util.HashMap;

/**
 * OrderValidator class provides methods to validate orders based on various criteria.
 */
public class OrderValidator implements OrderValidation {
    HashMap<String, String> pizzaRestaurantNameMap = new HashMap<String, String>();
    HashMap<String, DayOfWeek[]> restaurantOpenDaysMap = new HashMap<String , DayOfWeek[]>();
    HashMap<String, Integer> pizzaPriceMap = new HashMap<String, Integer>();

    /**
     * Validates the given order based on different criteria.
     *
     * @param orderToValidate    The order to be validated.
     * @param definedRestaurants An array of defined restaurants to use in validation.
     * @return The validated order.
     */
    @Override
    public Order validateOrder(Order orderToValidate, Restaurant[] definedRestaurants) {
        // Initial Setup of HashMaps for Faster Validation
        setUpHashMaps(definedRestaurants);

        // Card Number Validation
        String creditCardNumber = orderToValidate.getCreditCardInformation().getCreditCardNumber();
        if (!validateCreditCardNumber(creditCardNumber)) {
            orderToValidate.setOrderValidationCode(OrderValidationCode.CARD_NUMBER_INVALID);
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            return orderToValidate;
        }

        // CVV Validation
        String cvv = orderToValidate.getCreditCardInformation().getCvv();
        if (!validateCVV(cvv)) {
            orderToValidate.setOrderValidationCode(OrderValidationCode.CVV_INVALID);
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            return orderToValidate;
        }

        // Expiration Date Validation
        LocalDate orderDate = orderToValidate.getOrderDate();
        String expirationDate = orderToValidate.getCreditCardInformation().getCreditCardExpiry();
        if (!validateExpirationDate(expirationDate, orderDate)) {
            orderToValidate.setOrderValidationCode(OrderValidationCode.EXPIRY_DATE_INVALID);
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            return orderToValidate;
        }

        // Max Pizza Count Validation
        Pizza[] pizzasInOrder = orderToValidate.getPizzasInOrder();
        if(maxPizzaCountExceeded(pizzasInOrder)){
            orderToValidate.setOrderValidationCode(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            return orderToValidate;
        }

        // Pizza Defined Validation
        if(!arePizzasDefined(pizzasInOrder)){
            orderToValidate.setOrderValidationCode(OrderValidationCode.PIZZA_NOT_DEFINED);
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            return orderToValidate;
        }

        // Pizza From Multiple Restaurants Validation
        if(arePizzasFromMultipleRestaurants(pizzasInOrder)){
            orderToValidate.setOrderValidationCode(OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS);
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            return orderToValidate;
        }

        // Restaurant Closed Validation
        LocalDate date = orderToValidate.getOrderDate();
        if(isRestaurantClosed(pizzasInOrder, date)){
            orderToValidate.setOrderValidationCode(OrderValidationCode.RESTAURANT_CLOSED);
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            return orderToValidate;
        }

        // Total Price Validation
        int priceTotalInPence = orderToValidate.getPriceTotalInPence();
        if(isTotalPriceIncorrect(pizzasInOrder, priceTotalInPence)){
            orderToValidate.setOrderValidationCode(OrderValidationCode.TOTAL_INCORRECT);
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            return orderToValidate;
        }

        // If all the validations are passed, the order is valid
        orderToValidate.setOrderValidationCode(OrderValidationCode.NO_ERROR);
        orderToValidate.setOrderStatus(OrderStatus.VALID_BUT_NOT_DELIVERED);

        return orderToValidate;
    }

    /**
     * Validates the credit card number for length and numeric values.
     *
     * @param creditCardNumber The credit card number to validate.
     * @return True if the credit card number is valid; otherwise, false.
     */
    protected boolean validateCreditCardNumber(String creditCardNumber) {
        // check if the credit card number is 16 digits
        if (creditCardNumber == null || creditCardNumber.length() != 16) return false;

        // check if the credit card number is numeric
        try {
            Long.parseLong(creditCardNumber);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Validates the CVV for length and numeric values.
     *
     * @param cvv The CVV to validate.
     * @return True if the CVV is valid; otherwise, false.
     */
    protected boolean validateCVV(String cvv) {
        // check if the CVV is 3 digits
        if (cvv == null || cvv.length() != 3) return false;

        // check if the CVV is numeric
        try {
            Integer.parseInt(cvv);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Validates the expiration date by parsing and comparing with the order date.
     *
     * @param expirationDate The expiration date to validate.
     * @param orderDate      The order date for comparison.
     * @return True if the expiration date is valid; otherwise, false.
     */
    protected boolean validateExpirationDate(String expirationDate, LocalDate orderDate) {
        if (expirationDate == null) {
            return false;
        }
        // Parse the expiry date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        YearMonth expiryDate;
        try {
            expiryDate = YearMonth.parse(expirationDate, formatter);
        } catch (Exception e) {
            return false;
        }
        // Compare the expiry year and month with the order's year and month
        return orderDate.isBefore(expiryDate.atEndOfMonth()) || orderDate.isEqual(expiryDate.atEndOfMonth());
    }

    /**
     * Sets up HashMaps for faster validation based on defined restaurants.
     *
     * @param definedRestaurants An array of defined restaurants.
     */
    protected void setUpHashMaps(Restaurant[] definedRestaurants){
        for(Restaurant restaurant : definedRestaurants){
            restaurantOpenDaysMap.put(restaurant.name(), restaurant.openingDays());
            for(Pizza pizza : restaurant.menu()){
                pizzaRestaurantNameMap.put(pizza.name(), restaurant.name());
                pizzaPriceMap.put(pizza.name(), pizza.priceInPence());
            }
        }
    }

    /**
     * Checks if the maximum pizza count has been exceeded.
     *
     * @param pizzasInOrder The array of pizzas in the order.
     * @return True if the maximum pizza count is exceeded; otherwise, false.
     */
    protected boolean maxPizzaCountExceeded(Pizza[] pizzasInOrder){
        return pizzasInOrder.length > SystemConstants.MAX_PIZZAS_PER_ORDER;
    }

    /**
     * Checks if all pizzas in the order are defined.
     *
     * @param pizzasInOrder The array of pizzas in the order.
     * @return True if all pizzas are defined; otherwise, false.
     */
    protected boolean arePizzasDefined(Pizza[] pizzasInOrder){
        if(pizzasInOrder == null || pizzasInOrder.length == 0) {
            return false;
        }
        for(Pizza pizza : pizzasInOrder) {
            if(pizza == null || pizza.name() == null) {
                return false;
            }
            if(pizzaRestaurantNameMap.get(pizza.name()) == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if pizzas in the order are from multiple restaurants.
     *
     * @param pizzasInOrder The array of pizzas in the order.
     * @return True if pizzas are from multiple restaurants; otherwise, false.
     */
    protected boolean arePizzasFromMultipleRestaurants(Pizza[] pizzasInOrder) {
        String restaurantName = pizzaRestaurantNameMap.get(pizzasInOrder[0].name());
        for (Pizza pizza : pizzasInOrder) {
            if (!pizzaRestaurantNameMap.get(pizza.name()).equals(restaurantName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the restaurant is closed on the given date.
     *
     * @param pizzasInOrder The array of pizzas in the order.
     * @param date          The date for checking restaurant closure.
     * @return True if the restaurant is closed; otherwise, false.
     */
    protected boolean isRestaurantClosed(Pizza[] pizzasInOrder, LocalDate date) {
        // Get the day of the week
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        String restaurantName = pizzaRestaurantNameMap.get(pizzasInOrder[0].name());

        // Check if the restaurant is not open on any day
        if(restaurantOpenDaysMap.get(restaurantName) == null) {
            return true;
        }

        // Check if the restaurant is open on the given day
        for (DayOfWeek openDay : restaurantOpenDaysMap.get(restaurantName)) {
            if (dayOfWeek == openDay) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the total price of pizzas in the order is correct.
     *
     * @param pizzasInOrder       The array of pizzas in the order.
     * @param priceTotalInPence The total price in pence for validation.
     * @return True if the total price is correct; otherwise, false.
     */
    protected boolean isTotalPriceIncorrect(Pizza[] pizzasInOrder, int priceTotalInPence) {
        int totalPrice = 0;
        for (Pizza pizza : pizzasInOrder) {
            totalPrice += pizzaPriceMap.get(pizza.name());
        }
        totalPrice+=SystemConstants.ORDER_CHARGE_IN_PENCE;
        return totalPrice != priceTotalInPence;
    }


}
