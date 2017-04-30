package com.blogspot.techzealous.endorrouting.utils;

import com.blogspot.techzealous.endorrouting.objects.Planet;
import com.blogspot.techzealous.endorrouting.objects.Ply;
import com.blogspot.techzealous.endorrouting.objects.Route;
import com.blogspot.techzealous.endorrouting.objects.SpaceShip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ConstantsE {

    private static final String TAG = "ConstantsE";

    /**
     * The smallest planet movement that we are going to consider as existing. Everything less than that
     * we consider as not existing.
     * We can adjust this to be as small of a value as we want or need to - meters, ...
     */
    private static final int CLOSE_ENOUGH_KM = 1;

    private ConstantsE() {
        super();
    }

    /**
     * Calculates the distance between two planets taking into account the movement of the planet we are travelling to
     * while we travel to it.
     */
    public static Route getDistance(SpaceShip aShip, Planet aPlanetFrom, Planet aPlanetTo) {
        Route route = new Route();

        //arc length s = (pi/180) * angleDegrees * r
        //arc length s = 2 * p * r * (angle / 360)
        double distanceKm = 0;
        double tripHours = 0;
        float angleRadians = 0;
        float angleDegrees = 0;
        float initialDegrees = aPlanetTo.getDegrees() + 360;
        boolean closeEnough = false;
        boolean intercepted = false;
        double distanceKmShortes = Double.MAX_VALUE;

        distanceKm = ConstantsE.getDistance(aPlanetFrom, aPlanetTo);
        tripHours = distanceKm / aShip.getShipVelocity();
        double distanceTraveledByPlanetKm = aPlanetTo.getSpeedKmH() * tripHours;
        // angle = (s/r) / (pi/180)
        angleRadians = (float)(distanceTraveledByPlanetKm / aPlanetTo.getDistanceFromCenter());
        angleDegrees = (float)(angleRadians * (180 / Math.PI));
        while(!closeEnough) {
            aPlanetTo.setDegrees(aPlanetTo.getDegrees() + angleDegrees);
            if(aPlanetTo.getDegrees() > 360) {
                aPlanetTo.setDegrees(aPlanetTo.getDegrees() - 360);
                initialDegrees = initialDegrees - 360;
            }
            distanceKm = ConstantsE.getDistance(aPlanetFrom, aPlanetTo);
            tripHours = distanceKm / aShip.getShipVelocity();
            if(distanceKm < distanceKmShortes) {distanceKmShortes = distanceKm;}

            distanceTraveledByPlanetKm = aPlanetTo.getSpeedKmH() * tripHours;
            // arc length s = angleDegrees * r
            // angle = (s/r) * (180 / PI)
            angleRadians = (float)(distanceTraveledByPlanetKm / aPlanetTo.getDistanceFromCenter());
            angleDegrees = (float)(angleRadians * (180 / Math.PI));

            if((int)distanceTraveledByPlanetKm < CLOSE_ENOUGH_KM) {
                closeEnough = true;
                intercepted = true;
            }
            if(aPlanetTo.getDegrees() > initialDegrees) {
                closeEnough = true;
            }
        }

        if(intercepted) {
            aPlanetTo.setDegrees(aPlanetTo.getDegrees() + angleDegrees);
            if (aPlanetTo.getDegrees() > 360) {
                aPlanetTo.setDegrees(aPlanetTo.getDegrees() - 360);
            }
            distanceKm = ConstantsE.getDistance(aPlanetFrom, aPlanetTo);
            tripHours = distanceKm / aShip.getShipVelocity();
        } else {
            //The combination of ship speed, distance from sun, speed of planet is such that we cannot
            //catch/intercept the planet by going after/towards it so we just have to use the shortest path
            //and wait until it gets to this point (there are two cases:
            //    1.that it is already past this point we must wait for it to come around
            //    2.we must wait for it to this point
            float degreesA = aPlanetFrom.getDegrees();
            float degreesB = aPlanetTo.getDegrees();
            float travelDegrees;
            double travelDistance;
            double travelTimeHours;
            distanceKm = Math.abs(aPlanetTo.getDistanceFromCenter() - aPlanetFrom.getDistanceFromCenter());
            if(degreesA > degreesB) {
                travelDegrees = degreesA - degreesB;
            } else {
                travelDegrees = degreesA + (360 - degreesB);
            }
            //arc length s = 2 * p * r * (angle / 360)
            travelDistance = 2 * Math.PI * aPlanetTo.getDistanceFromCenter() * (travelDegrees / 360);
            travelTimeHours = travelDistance / aPlanetTo.getSpeedKmH();
            tripHours = travelTimeHours;
        }
        route.setDistance(distanceKm);
        route.setTimeHours(tripHours);
        route.setPlanetFrom(aPlanetFrom);
        route.setPlanetTo(aPlanetTo);

        return route;
    }

    /** Calculates the distance between two planets using Law of Cosines. */
    public static double getDistance(Planet aPlanetFrom, Planet aPlanetTo) {
        double distance = 0;
        int sqrR1 = aPlanetFrom.getDistanceFromCenter() * aPlanetFrom.getDistanceFromCenter();
        int sqrR2 = aPlanetTo.getDistanceFromCenter() * aPlanetTo.getDistanceFromCenter();
        int r1r2 = aPlanetFrom.getDistanceFromCenter() * aPlanetTo.getDistanceFromCenter();
        double cos21 = Math.cos((aPlanetTo.getDegrees() - aPlanetFrom.getDegrees()) * (Math.PI / 180));
        distance = Math.sqrt((sqrR1 + sqrR2) - 2 * r1r2 * cos21);
        //if we are travelling from an outer planet to an inner the distance will come as negative, but we always want it
        //as positive number.
        return Math.abs(distance);
    }

    /**
     * Visits the planets in order starting from the innermost and going outwords.
     * Does not take into account the movement of the planets while we are travelling between planets.
     */
    public static ArrayList<Route> getRouteInOrder(ArrayList<Planet> aArrayPlanets) {
        Collections.sort(aArrayPlanets, new Comparator<Planet>() {
            @Override
            public int compare(Planet planet1, Planet planet2) {
                if(planet1.getDistanceFromCenter() > planet2.getDistanceFromCenter()) {
                    return 1;
                } else if(planet1.getDistanceFromCenter() < planet2.getDistanceFromCenter()) {
                    return -1;
                }
                return 0;
            }
        });

        ArrayList<Route> arrayRoute = new ArrayList<Route>();

        int count = aArrayPlanets.size();
        Planet planetCurrent = null;
        for(int x = 0; x < count; x++) {
            if(planetCurrent == null) {
                planetCurrent = aArrayPlanets.get(x);
                continue;
            }
            Planet planet = aArrayPlanets.get(x);

            Route route = new Route();
            route.setPlanetFrom(planetCurrent);
            route.setPlanetTo(planet);
            route.setDistance(getDistance(planetCurrent, planet));
            arrayRoute.add(route);

            planetCurrent = planet;
        }
        return arrayRoute;
    }

    /**
     * Visits the planets in order starting from the innermost and going outwards,
     * taking into account the movement of the planets while we travel.
     */
    public static ArrayList<Route> getRouteInOrder(ArrayList<Planet> aArrayPlanets, SpaceShip aShip) {
        Collections.sort(aArrayPlanets, new Comparator<Planet>() {
            @Override
            public int compare(Planet planet1, Planet planet2) {
                if(planet1.getDistanceFromCenter() > planet2.getDistanceFromCenter()) {
                    return 1;
                } else if(planet1.getDistanceFromCenter() < planet2.getDistanceFromCenter()) {
                    return -1;
                }
                return 0;
            }
        });

        ArrayList<Route> arrayRoute = new ArrayList<Route>();
        int count = aArrayPlanets.size();
        Planet planetCurrent = null;
        double timeHours = 0;
        for(int x = 0; x < count; x++) {
            if(planetCurrent == null) {
                planetCurrent = aArrayPlanets.get(x);
                continue;
            }
            Planet planet = aArrayPlanets.get(x);
            planet.setDegrees(planet.getPosition((float)timeHours));

            Route route = getDistance(aShip, planetCurrent, planet);
            arrayRoute.add(route);
            timeHours = timeHours + route.getTimeHours();

            planetCurrent = planet;
            planetCurrent.setDegrees(planetCurrent.getPosition((float)timeHours));
        }
        return arrayRoute;
    }

    /**
     * Traverses the planets in the order they are passed in, taking into account the movement of the planets while we travel.
     */
    public static ArrayList<Route> getRoute(ArrayList<Planet> aArrayPlanets, SpaceShip aShip) {
        ArrayList<Route> arrayRoute = new ArrayList<Route>();
        int count = aArrayPlanets.size();
        Planet planetCurrent = null;
        double timeHours = 0;
        for(int x = 0; x < count; x++) {
            if(planetCurrent == null) {
                planetCurrent = aArrayPlanets.get(x);
                continue;
            }
            Planet planet = aArrayPlanets.get(x);
            planet.setDegrees(planet.getPosition((float)timeHours));

            Route route = getDistance(aShip, planetCurrent, planet);
            arrayRoute.add(route);
            timeHours = timeHours + route.getTimeHours();

            planetCurrent = planet;
            planetCurrent.setDegrees(planetCurrent.getPosition((float)timeHours));
        }
        return arrayRoute;
    }

    /** Creates a route with Minimax, starting from the innermost planet. */
    public static ArrayList<Route> getRouteMinimax(ArrayList<Planet> aArrayPlanets, SpaceShip aShip, int aLaunchDay) {
        Collections.sort(aArrayPlanets, new Comparator<Planet>() {
            @Override
            public int compare(Planet planet1, Planet planet2) {
                if(planet1.getDistanceFromCenter() > planet2.getDistanceFromCenter()) {
                    return 1;
                } else if(planet1.getDistanceFromCenter() < planet2.getDistanceFromCenter()) {
                    return -1;
                }
                return 0;
            }
        });

        ArrayList<Route> arrayRoute = new ArrayList<Route>();
        int countPlanets = aArrayPlanets.size();
        if(countPlanets <= 1) {
            return arrayRoute;
        }

        if(aLaunchDay > 0) {
            for (int x = 0; x < countPlanets; x++) {
                Planet planet = aArrayPlanets.get(x);
                planet.setDegrees(planet.getDegreesWithHours(aLaunchDay * 24));
            }
        }

        Ply ply = new Ply();
        ply.setPlanets(SharedState.clonePlanets(aArrayPlanets));
        ply.setPlanet(ply.getPlanets().remove(0));
        ply.setShip(aShip);
        ArrayList<Planet> arrayPlanets = ply.traverse();
        arrayRoute = getRoute(arrayPlanets, aShip);

        return arrayRoute;
    }

}
