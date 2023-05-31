public class Main {

    public static void main(String[] args){

        MapPlanner mapPlanner = new MapPlanner(5);

        // City map creation
        mapPlanner.addStreet("A", new Point(0, 0), new Point(10, 0));
        mapPlanner.addStreet("B", new Point(0, 5), new Point(10, 5));
        mapPlanner.addStreet("C", new Point(0, 0), new Point(0, 3));
        mapPlanner.addStreet("D", new Point(10, 0), new Point(10, 5));
        mapPlanner.addStreet("E", new Point(10, 5), new Point(10, 10));
        mapPlanner.addStreet("F", new Point(10, 10), new Point(30, 10));

        // Selecting depot location
        mapPlanner.depotLocation(new Location("D", StreetSide.Left));
        Route route = new Route(mapPlanner);

        // Find the furthest street from depot
        System.out.println("Furthest Street======> " + mapPlanner.furthestStreet());

        Route noLeftTurnRoute = mapPlanner.routeNoLeftTurn(new Location("F", StreetSide.Left));

        System.out.println("Shortest distance with no left turn rule");
        for(Leg leg: noLeftTurnRoute.getRouteLocation()) {
            System.out.print(leg.getStreetId() + "====>");
        }

    }
}
