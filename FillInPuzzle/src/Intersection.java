public class Intersection {

    /**
     * Intersection class will handle details and operation regarding intersection point between two word agent.
     * */
    private WordAgent first;
    private WordAgent second;
    private GridPoint intersectionPoint;

    /**
     * Intersection constructor used for initializing object.
     * */
    public Intersection(WordAgent first, WordAgent second, GridPoint intersectionPoint) {
        this.first = first;
        this.second = second;
        this.intersectionPoint = intersectionPoint;
    }

    public WordAgent getFirst() {
        return first;
    }

    public WordAgent getSecond() {
        return second;
    }

    public GridPoint getIntersectionPoint() {
        return intersectionPoint;
    }
}
