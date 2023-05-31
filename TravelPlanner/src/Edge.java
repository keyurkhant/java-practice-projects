public class Edge {
    private Street start;
    private Street end;
    private Double weight;

    private StreetSide startSide;

    private StreetSide endSide;

    Point point;

    public void setEnd(Street end) {
        this.end = end;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public StreetSide getStartSide() {
        return startSide;
    }

    public void setStartSide(StreetSide startSide) {
        this.startSide = startSide;
    }

    public void setStart(Street start) {
        this.start = start;
    }

    public StreetSide getEndSide() {
        return endSide;
    }

    public void setEndSide(StreetSide endSide) {
        this.endSide = endSide;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Edge(Street start, Street end, StreetSide startSide, StreetSide endSide) {
        this.start = start;
        this.end = end;
        this.weight = calculateTwoIntermediateLength(start, end);

        this.startSide = startSide;
        this.endSide = endSide;
    }

    public Double calculateTwoIntermediateLength(Street start, Street end) {
        if (start == null || end == null) return 0.0;
        Double firstHalf = start.getCenter().distanceTo(start.getEnd());
        Double secondHalf = end.getStart().distanceTo(end.getCenter());
        return  firstHalf + secondHalf;
    }

    public Street getStart() {
        return start;
    }

    public Street getEnd() {
        return end;
    }

    public Double getWeight() {
        return weight;
    }
}
