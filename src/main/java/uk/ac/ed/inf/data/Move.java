package uk.ac.ed.inf.data;

/**
 * Represents a move in a delivery system, including details such as order number, coordinates, and angle.
 */
public class Move {
    private String orderNo;
    private Double fromLongitude;
    private Double fromLatitude;
    private Double angle;
    private Double toLongitude;
    private Double toLatitude;

    /**
     * Constructs a Move object with the given parameters.
     *
     * @param orderNo       The order number associated with the move.
     * @param fromLongitude The longitude of the starting position.
     * @param fromLatitude  The latitude of the starting position.
     * @param angle         The angle of the move.
     * @param toLongitude   The longitude of the destination position.
     * @param toLatitude    The latitude of the destination position.
     */
    public Move(String orderNo, Double fromLongitude, Double fromLatitude, Double angle, Double toLongitude, Double toLatitude) {
        this.orderNo = orderNo;
        this.fromLongitude = fromLongitude;
        this.fromLatitude = fromLatitude;
        this.angle = angle;
        this.toLongitude = toLongitude;
        this.toLatitude = toLatitude;
    }

    public Move(){

    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Double getFromLongitude() {
        return fromLongitude;
    }

    public void setFromLongitude(Double fromLongitude) {
        this.fromLongitude = fromLongitude;
    }

    public Double getFromLatitude() {
        return fromLatitude;
    }

    public void setFromLatitude(Double fromLatitude) {
        this.fromLatitude = fromLatitude;
    }

    public Double getToLongitude() {
        return toLongitude;
    }

    public void setToLongitude(Double toLongitude) {
        this.toLongitude = toLongitude;
    }

    public Double getToLatitude() {
        return toLatitude;
    }

    public void setToLatitude(Double toLatitude) {
        this.toLatitude = toLatitude;
    }


    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }
}
