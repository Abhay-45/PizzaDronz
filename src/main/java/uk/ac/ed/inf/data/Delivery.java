package uk.ac.ed.inf.data;

import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;

/**
 * Represents a delivery with details such as order number, order status, order validation code, and cost.
 */
public class Delivery {
    private String orderNo;
    private OrderStatus orderStatus;
    private OrderValidationCode orderValidationCode;
    private int costInPence;

    /**
     * Constructs a Delivery object with the given parameters.
     *
     * @param orderNo             The order number associated with the delivery.
     * @param orderStatus         The status of the order.
     * @param orderValidationCode The validation code associated with the order.
     * @param costInPence         The cost of the delivery in pence.
     */
    public Delivery(String orderNo, OrderStatus orderStatus, OrderValidationCode orderValidationCode, int costInPence) {
        this.orderNo = orderNo;
        this.orderStatus = orderStatus;
        this.orderValidationCode = orderValidationCode;
        this.costInPence = costInPence;
    }

    public Delivery(){

    }

    public String getOrderNo() {
        return orderNo;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public OrderValidationCode getOrderValidationCode() {
        return orderValidationCode;
    }

    public int getCostInPence() {
        return costInPence;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderValidationCode(OrderValidationCode orderValidationCode) {
        this.orderValidationCode = orderValidationCode;
    }

    public void setCostInPence(int costInPence) {
        this.costInPence = costInPence;
    }




}
