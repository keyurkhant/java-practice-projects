import java.util.*;

/**
 * Define a route to travel in the map.  It's a sequence of turns and streets in the city map.
 *
 * The first leg of a route is leg 1.
 */
public class Route {

    private MapPlanner mapPlanner;

    List<Leg> routeLocation ;

    public Route(MapPlanner mapPlanner){
        this.mapPlanner = mapPlanner;

        routeLocation = new ArrayList<>();
        routeLocation.add(new Leg(mapPlanner.getDepot().getStreetId(),
                mapPlanner.getDepot().getStreetSide(),
                null));
    }

    public Route(List<Leg> routeLocation, MapPlanner mapPlanner) {
        this.routeLocation = routeLocation;

        this.mapPlanner = mapPlanner;
    }

    /**
     * Grow a Route by adding one step (called a "leg") of the route at a time.  This method adds one more
     * leg to an existing route
     * @param turn -- from the current route, what kind of turn do you make onto the next leg
     * @param streetTurnedOnto -- the street id onto which the next leg of the route turns
     * @return -- true if the leg was added to the route.
     */
    public Boolean appendTurn( TurnDirection turn, String streetTurnedOnto ) {

        List<Edge> edges = mapPlanner.getAdjacentList().get(streetTurnedOnto);

        Street nextStreet = null;
        StreetSide nextStreetSide = null;

        Leg currentLocation = routeLocation.get(routeLocation.size() - 1);
        currentLocation.setNextTurn(turn);
        this.routeLocation.set(routeLocation.size() - 1, currentLocation);

        Edge edge;

        switch(turn) {
            case Left:
                edge = edges.get(currentLocation.getStreetSide() == StreetSide.Left ? 0 : 3);
                nextStreet = edge.getEnd();
                nextStreetSide = edge.getEndSide();
                break;
            case Right:
                edge = edges.get(currentLocation.getStreetSide() == StreetSide.Left ? 1 : 4);
                nextStreet = edge.getEnd();
                nextStreetSide = edge.getEndSide();
                break;
            case Straight:
                edge = edges.get(currentLocation.getStreetSide() == StreetSide.Left ? 2 : 5);
                nextStreet = edge.getEnd();
                nextStreetSide = edge.getEndSide();
                break;
            case UTurn:
                nextStreet = this.mapPlanner.getStreets().get(streetTurnedOnto);
                nextStreetSide = currentLocation.getStreetSide() == StreetSide.Left ? StreetSide.Right : StreetSide.Left;
        }

        if(nextStreet == null || nextStreetSide == null) return false;

        routeLocation.add(new Leg(nextStreet.getStreetId(), nextStreetSide, null));
        return true;
    }

    /**
     * Given a route, report whether the street of the given leg number of the route.
     *
     * Leg numbers begin with 1.
     * @param legNumber -- the leg number for which we want the next street.
     * @return -- the street id of the next leg, or null if there is an error.
     */
    public String turnOnto( int legNumber ) {

        try{
            return routeLocation.get(legNumber).getStreetId();
        }
        catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    /**
     * Given a route, report whether the type of turn that initiates the given leg number of the route.
     *
     * Leg numbers begin with 1.
     * @param legNumber -- the leg number for which we want the next turn.
     * @return -- the turn direction for the leg, or null if there is an error.
     */
    public TurnDirection turnDirection( int legNumber ) {
        try {
            Leg currentLocation = routeLocation.get(legNumber - 1);
            return currentLocation.getNextTurn();
        }
        catch(Exception e){
            return null;
        }

    }

    /**
     * Report how many legs exist in the current route
     * @return -- the number of legs in this route.
     */
    public int legs() {
        return routeLocation.size();
    }

    /**
     * Report the length of the current route.  Length is computed in metres by Euclidean distance.
     *
     * By assumption, the route always starts and ends at the middle of a road, so only half of the length
     * of the first and last leg roads contributes to the length of the route
     * @return -- the length of the current route.
     */
    public Double length() {
        return 0.0;
    }

    /**
     * Given a route, return all loops in the route.
     *
     * A loop in a route is a sequence of streets where we start and end at the same intersection.  A typical
     * example of a loop would be driving around the block in a city.  A loop does not need you to start and end
     * the loop going in the same direction.  It's just a point of driving through the same intersection again.
     *
     * A route may contain more than one loop.  Return the loops in order that they start along the route.
     *
     * If one loop is nested inside a larger loop then only report the larger loop.
     * @return -- a list of subroutes (starting and ending legs) of each loop.  The starting leg and the ending leg
     * share a common interesection.
     */
    public List<SubRoute> loops() {
        return null;
    }

    /**
     * Given a route, produce a new route with simplified instructions.  The simplification reports a route
     * that reports the turns in the route but does not report the points where you should keep going straight
     * along your current path.
     * @return -- the simplified route.
     */
    public Route simplify() {
        return null;
    }

    public List<Leg> getRouteLocation() {
        return routeLocation;
    }

    public void setRouteLocation(List<Leg> routeLocation) {
        this.routeLocation = routeLocation;
    }
}
