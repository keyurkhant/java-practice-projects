import com.sun.xml.internal.ws.api.ha.StickyFeature;

import java.util.*;

public class MapPlanner {
    private int degrees;
    private Location depot;
    private Map<String, Street> streets = new HashMap<>();
    private Map<String, List<Edge>> adjacentList = new HashMap<>();

    /**
     * Create the Map Planner object.  The degrees provided tell us how much deviation from straight-forward
     * is needed to identify an actual turn in a route rather than a straight-on driving.
     * @param degrees
     */
    public MapPlanner( int degrees ) {
        this.degrees = degrees % 360;
    }

    /**
     * Identify the location of the depot.  That location is used as the starting point of any route request
     * to a destination
     * @param depot -- the street ID and side of the street (left or right) where we find the depot
     * @return -- true if the depot was set.  False if there was a problem in setting the depot location.
     */
    public Boolean depotLocation( Location depot ) {
        if (!streets.containsKey(depot.getStreetId())) {
            return false;
        }
        this.depot = depot;
        return true;
    }

    /**
     * Add a street to our map of the city.  The street is identified by the unique street id.
     * Although the parameters indicate a start and an end to the street, the street is bi-directional.
     * The start and end are just relevant when identifying the side of the street for some location.
     *
     * Street coordinates are in metres.
     *
     * Streets that share coordinates of endpoints meet at an intersection and you can drive from one street to the
     * other at that intersection.
     * @param streetId -- unique identifier for the street.
     * @param start -- coordinates of the starting intersection for the street
     * @param end -- coordinates of the ending entersection for the street
     * @return -- true if the street could be added.  False if the street isn't available in the map.
     */
    public Boolean addStreet( String streetId, Point start, Point end ) {
        if (start.equals(end)) {
            return false;
        }
        Street newStreet = new Street(streetId, start, end, getCenterPoint(start, end), true, true);
        List<Edge> edges = new ArrayList<>(6);

        for(int i = 0; i < 6; i++){
            edges.add(null);
        }

        if (streets.isEmpty()) {
            streets.put(streetId, newStreet);
            adjacentList.put(streetId, edges);
        } else {
            if(streets.containsKey(streetId)) return false;
            // first loop for same street
            for(String strId: streets.keySet()) {
                Street existingStreet = streets.get(strId);
                if(isSameStreet(existingStreet, newStreet)) return false;
            }

            // Mapping on each street map and handling adjacency list
            for(Map.Entry<String, Street> street: streets.entrySet()) {
                Street existingStreet = street.getValue();

                if((existingStreet.getEnd().getX() == newStreet.getStart().getX() && existingStreet.getEnd().getY() == newStreet.getStart().getY())){
                    TurnDirection firstDirection = newStreet.getEnd().turnType(newStreet.getStart(), existingStreet.getStart(), this.degrees);

                    existingStreet.setEndUTurn(false);
                    newStreet.setStartUTurn(false);
                    switch (firstDirection) {
                        case Left:
                            edges.set(0, new Edge(newStreet, existingStreet, StreetSide.Right, StreetSide.Right));
                            adjacentList.get(existingStreet.getStreetId()).set(4, new Edge(existingStreet, newStreet, StreetSide.Left, StreetSide.Left));
                            break;
                        case Right:
                            edges.set(1, new Edge(newStreet, existingStreet, StreetSide.Right, StreetSide.Right));
                            adjacentList.get(existingStreet.getStreetId()).set(3, new Edge(existingStreet, newStreet, StreetSide.Left, StreetSide.Left));
                            break;
                        case Straight:
                            edges.set(2, new Edge(newStreet, existingStreet, StreetSide.Right, StreetSide.Right));
                            adjacentList.get(existingStreet.getStreetId()).set(5, new Edge(existingStreet, newStreet, StreetSide.Left, StreetSide.Left));
                            break;
                    }
                }
                else if((existingStreet.getStart().getX() == newStreet.getEnd().getX() && existingStreet.getStart().getY() == newStreet.getEnd().getY())){
                    TurnDirection firstDirection = newStreet.getStart().turnType(newStreet.getEnd(), existingStreet.getEnd(), this.degrees);

                    existingStreet.setStartUTurn(false);
                    newStreet.setEndUTurn(false);
                    switch (firstDirection) {
                        case Left:
                            edges.set(3, new Edge(newStreet, existingStreet, StreetSide.Left, StreetSide.Left));
                            adjacentList.get(existingStreet.getStreetId()).set(1, new Edge(existingStreet, newStreet, StreetSide.Right, StreetSide.Right));
                            break;
                        case Right:
                            edges.set(4, new Edge(newStreet, existingStreet, StreetSide.Left, StreetSide.Left));
                            adjacentList.get(existingStreet.getStreetId()).set(0, new Edge(existingStreet, newStreet, StreetSide.Right, StreetSide.Right));
                            break;
                        case Straight:
                            edges.set(5, new Edge(newStreet, existingStreet, StreetSide.Left, StreetSide.Left));
                            adjacentList.get(existingStreet.getStreetId()).set(2, new Edge(existingStreet, newStreet, StreetSide.Right, StreetSide.Right));
                            break;
                    }
                }
                else if(existingStreet.getStart().getX() == newStreet.getStart().getX() && existingStreet.getStart().getY() == newStreet.getStart().getY()){
                    TurnDirection firstDirection = newStreet.getEnd().turnType(newStreet.getStart(), existingStreet.getEnd(), this.degrees);

                    existingStreet.setStartUTurn(false);
                    newStreet.setStartUTurn(false);
                    switch (firstDirection) {
                        case Left:
                            edges.set(0, new Edge(newStreet, existingStreet, StreetSide.Right, StreetSide.Left));
                            adjacentList.get(existingStreet.getStreetId()).set(1, new Edge(existingStreet, newStreet, StreetSide.Left, StreetSide.Right));
                            break;
                        case Right:
                            edges.set(1, new Edge(newStreet, existingStreet, StreetSide.Right, StreetSide.Left));
                            adjacentList.get(existingStreet.getStreetId()).set(0, new Edge(existingStreet, newStreet, StreetSide.Left, StreetSide.Right));
                            break;
                        case Straight:
                            edges.set(2, new Edge(newStreet, existingStreet, StreetSide.Right, StreetSide.Left));
                            adjacentList.get(existingStreet.getStreetId()).set(2, new Edge(existingStreet, newStreet, StreetSide.Left, StreetSide.Right));
                            break;
                    }
                }
                else if(existingStreet.getEnd().getX() == newStreet.getEnd().getX() && existingStreet.getEnd().getY() == newStreet.getEnd().getY()){
                    TurnDirection firstDirection = newStreet.getStart().turnType(newStreet.getEnd(), existingStreet.getStart(), this.degrees);

                    existingStreet.setEndUTurn(false);
                    newStreet.setEndUTurn(false);
                    switch (firstDirection) {
                        case Left:
                            edges.set(3, new Edge(newStreet, existingStreet, StreetSide.Left, StreetSide.Right));
                            adjacentList.get(existingStreet.getStreetId()).set(4, new Edge(existingStreet, newStreet, StreetSide.Right, StreetSide.Left));
                            break;
                        case Right:
                            edges.set(4, new Edge(newStreet, existingStreet, StreetSide.Left, StreetSide.Right));
                            adjacentList.get(existingStreet.getStreetId()).set(3, new Edge(existingStreet, newStreet, StreetSide.Right, StreetSide.Left));
                            break;
                        case Straight:
                            edges.set(5, new Edge(newStreet, existingStreet, StreetSide.Left, StreetSide.Right));
                            adjacentList.get(existingStreet.getStreetId()).set(5, new Edge(existingStreet, newStreet, StreetSide.Right, StreetSide.Left));
                            break;
                    }
                }
            }
            streets.put(streetId, newStreet);
            adjacentList.put(streetId, edges);
        }
        return true;
    }

    public Point getCenterPoint(Point start, Point end) {
        return new Point((start.getX() + end.getX()) / 2, (start.getY() + end.getY()) / 2);
    }

    public Boolean isSameStreet(Street street1, Street street2) {
        boolean street1SameStart = street1.getStart().getX() == street2.getStart().getX() && street1.getStart().getY() == street2.getStart().getY();
        boolean street1SameEnd = street1.getEnd().getX() == street2.getEnd().getX() && street1.getEnd().getY() == street2.getEnd().getY();

        boolean street2SameStart = street1.getStart().getX() == street2.getEnd().getX() && street1.getStart().getY() == street2.getEnd().getY();
        boolean street2SameEnd = street1.getEnd().getX() == street2.getStart().getX() && street1.getEnd().getY() == street2.getStart().getY();
        return street1SameStart && street1SameEnd && street2SameStart && street2SameEnd;
    }

    /**
     *  Given a depot location, return the street id of the street that is furthest away from the depot by distance,
     *  allowing for left turns to get to the street.
     */
    public String furthestStreet() {
        if(depot == null || streets.isEmpty()) return null;

        Map<String, Double> distances = new HashMap<>();

        for(Map.Entry<String, Street> entry: streets.entrySet()){
            distances.put(entry.getKey(), 100000000.0);
        }

        PriorityQueue<Map.Entry<String, Double>> pq =
                new PriorityQueue<Map.Entry<String, Double>>((x,y) -> (int) (x.getValue() - y.getValue()));

        pq.add(new AbstractMap.SimpleEntry<>(depot.getStreetId(), 0.0));
        distances.put(depot.getStreetId(), 0.0);

        while(!pq.isEmpty()){
            Map.Entry<String, Double> street = pq.peek();
            pq.remove();

             for(Edge edge: adjacentList.get(street.getKey())){
                 if(edge == null) continue;
                 if(edge.getWeight() + street.getValue() < distances.get(edge.getEnd().getStreetId())){
                     distances.put(edge.getEnd().getStreetId(), edge.getWeight() + street.getValue());
                     pq.add(new AbstractMap.SimpleEntry<String, Double>(edge.getEnd().getStreetId(), edge.getWeight() + street.getValue()));
                 }
             }
        }
        double max = 0;
        String furthestStreet = depot.getStreetId();
        for(Map.Entry<String, Double> entry: distances.entrySet()){
            if(entry.getValue() > max){
                furthestStreet = entry.getKey();
                max = entry.getValue();
            }
        }

        return furthestStreet;
    }

    public int getDegrees() {
        return degrees;
    }

    public void setDegrees(int degrees) {
        this.degrees = degrees;
    }

    public Location getDepot() {
        return depot;
    }

    public void setDepot(Location depot) {
        this.depot = depot;
    }

    public Map<String, Street> getStreets() {
        return streets;
    }

    public void setStreets(Map<String, Street> streets) {
        this.streets = streets;
    }

    public Map<String, List<Edge>> getAdjacentList() {
        return adjacentList;
    }

    public void setAdjacentList(Map<String, List<Edge>> adjacentList) {
        this.adjacentList = adjacentList;
    }

    /**
     * Compute a route to the given destination from the depot, given the current map and not allowing
     * the route to make any left turns at intersections.
     * @param destination -- the destination for the route
     * @return -- the route to the destination, or null if no route exists.
     */
    public Route routeNoLeftTurn( Location destination ) {
        Map<String, Double> distances = new HashMap<>();

        Map<String, Leg> previousStreet = new HashMap<>();

        for(Map.Entry<String, Street> entry: streets.entrySet()){
            distances.put(entry.getKey(), 100000000.0);
            previousStreet.put(entry.getKey(), null);
        }

        PriorityQueue<Map.Entry<String, Leg>> pq =
                new PriorityQueue<Map.Entry<String, Leg>>((x,y) -> (int) (x.getValue().getDistance() - y.getValue().getDistance()));

        pq.add(new AbstractMap.SimpleEntry<>(depot.getStreetId(), new Leg(depot.getStreetId(), depot.getStreetSide(), null, 0.0)));
        distances.put(depot.getStreetId(), 0.0);

        while(!pq.isEmpty()){
            Map.Entry<String, Leg> street = pq.peek();
            pq.remove();

            int index = -1;

            Edge edge;

            if(street.getValue().getStreetSide() == StreetSide.Right){

                edge = adjacentList.get(street.getKey()).get(1);

                if(edge != null && edge.getWeight() + street.getValue().getDistance() < distances.get(edge.getEnd().getStreetId())){
                    distances.put(edge.getEnd().getStreetId(), edge.getWeight() + street.getValue().getDistance());
                    previousStreet.put(edge.getEnd().getStreetId(), new Leg(street.getKey(), street.getValue().getStreetSide(), TurnDirection.Right, street.getValue().getDistance()));
                    Leg tempLeg = new Leg(edge.getEnd().getStreetId(), edge.getEndSide(), null, edge.getWeight() + street.getValue().getDistance());
                    pq.add(new AbstractMap.SimpleEntry<String, Leg>(edge.getEnd().getStreetId(), tempLeg));
                }

                edge = adjacentList.get(street.getKey()).get(2);

                if(edge != null && edge.getWeight() + street.getValue().getDistance() < distances.get(edge.getEnd().getStreetId())){
                    distances.put(edge.getEnd().getStreetId(), edge.getWeight() + street.getValue().getDistance());
                    previousStreet.put(edge.getEnd().getStreetId(), new Leg(street.getKey(), street.getValue().getStreetSide(), TurnDirection.Straight, street.getValue().getDistance()));
                    Leg tempLeg = new Leg(edge.getEnd().getStreetId(), edge.getEndSide(), null, edge.getWeight() + street.getValue().getDistance());
                    pq.add(new AbstractMap.SimpleEntry<String, Leg>(edge.getEnd().getStreetId(), tempLeg));
                }
            }
            else {

                edge = adjacentList.get(street.getKey()).get(4);

                if(edge != null && edge.getWeight() + street.getValue().getDistance() < distances.get(edge.getEnd().getStreetId())){
                    distances.put(edge.getEnd().getStreetId(), edge.getWeight() + street.getValue().getDistance());
                    previousStreet.put(edge.getEnd().getStreetId(), new Leg(street.getKey(), street.getValue().getStreetSide(), TurnDirection.Right, street.getValue().getDistance()));
                    Leg tempLeg = new Leg(edge.getEnd().getStreetId(), edge.getEndSide(), null, edge.getWeight() + street.getValue().getDistance());
                    pq.add(new AbstractMap.SimpleEntry<String, Leg>(edge.getEnd().getStreetId(), tempLeg));
                }

                edge = adjacentList.get(street.getKey()).get(5);

                if(edge != null && edge.getWeight() + street.getValue().getDistance() < distances.get(edge.getEnd().getStreetId())){
                    distances.put(edge.getEnd().getStreetId(), edge.getWeight() + street.getValue().getDistance());
                    previousStreet.put(edge.getEnd().getStreetId(), new Leg(street.getKey(), street.getValue().getStreetSide(), TurnDirection.Straight, street.getValue().getDistance()));
                    Leg tempLeg = new Leg(edge.getEnd().getStreetId(), edge.getEndSide(), null, edge.getWeight() + street.getValue().getDistance());
                    pq.add(new AbstractMap.SimpleEntry<String, Leg>(edge.getEnd().getStreetId(), tempLeg));
                }
            }
        }

        if(previousStreet.get(destination.getStreetId()) == null){
            return null;
        }

        Leg temp = previousStreet.get(destination.getStreetId());

        List<Leg> legs = new ArrayList<>();
        legs.add(new Leg(destination.getStreetId(), null, null));

        while(temp != null){
            legs.add(temp);

            temp = previousStreet.get(temp.getStreetId());
        }

        Collections.reverse(legs);

        return new Route(legs, this);
    }
}
