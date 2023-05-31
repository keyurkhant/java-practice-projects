/**
 * Identify a location on a map by the street name a side of the street
 */
public class Leg {
    private String streetId;
    private StreetSide streetSide;
    TurnDirection nextTurn;

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    private Double distance;

    public String getStreetId() {
        return streetId;
    }

    public void setStreetId(String streetId) {
        this.streetId = streetId;
    }

    public StreetSide getStreetSide() {
        return streetSide;
    }

    public void setStreetSide(StreetSide streetSide) {
        this.streetSide = streetSide;
    }

    public TurnDirection getNextTurn() {
        return nextTurn;
    }

    public void setNextTurn(TurnDirection nextTurn) {
        this.nextTurn = nextTurn;
    }

    public Leg(String streetId, StreetSide streetSide, TurnDirection nextTurn, Double distance) {
        this.streetId = streetId;
        this.streetSide = streetSide;
        this.nextTurn = nextTurn;

        this.distance = distance;
    }

    public Leg(String streetId, StreetSide streetSide, TurnDirection nextTurn) {
        this.streetId = streetId;
        this.streetSide = streetSide;
        this.nextTurn = nextTurn;

        this.distance = 0.0;
    }
}
