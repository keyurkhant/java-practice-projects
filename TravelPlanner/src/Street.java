public class Street {
    private String streetId;
    private Point start;
    private Point end;
    private Point center;
    private Boolean startUTurn;

    private Boolean endUTurn;



    public Street(String streetId, Point start, Point end, Point center, Boolean startUTurn, Boolean endUTurn) {
        this.streetId = streetId;
        this.start = start;
        this.end = end;
        this.center = center;
        this.startUTurn = startUTurn;
        this.endUTurn = endUTurn;

//        this.distance = 1000000000;
    }

    public Point getCenter() {
        return center;
    }

    public String getStreetId() {
        return streetId;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public void setStreetId(String streetId) {
        this.streetId = streetId;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public Boolean getStartUTurn() {
        return startUTurn;
    }

    public void setStartUTurn(Boolean startUTurn) {
        this.startUTurn = startUTurn;
    }

    public Boolean getEndUTurn() {
        return endUTurn;
    }

    public void setEndUTurn(Boolean endUTurn) {
        this.endUTurn = endUTurn;
    }
}
