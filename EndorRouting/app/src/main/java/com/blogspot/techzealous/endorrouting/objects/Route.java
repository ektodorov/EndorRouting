package com.blogspot.techzealous.endorrouting.objects;


import java.util.ArrayList;

public class Route {

    private Planet mPlanetFrom;
    private Planet mPlanetTo;
    private double mDistance;
    private double mTimeHours;

    public Planet getPlanetFrom() {return mPlanetFrom;}
    public void setPlanetFrom(Planet aPlanetFrom) {mPlanetFrom = aPlanetFrom;}

    public Planet getPlanetTo() {return mPlanetTo;}
    public void setPlanetTo(Planet aPlanetTo) {mPlanetTo = aPlanetTo;}

    public double getDistance() {return mDistance;}
    public void setDistance(double aDistance) {mDistance = aDistance;}

    public double getTimeHours() {return mTimeHours;}
    public void setTimeHours(double aTimeHours) {mTimeHours = aTimeHours;}

    public static double getTotalDistance(ArrayList<Route> aArrayRoute) {
        double total = 0;
        if(aArrayRoute == null) {
            return total;
        }

        int count = aArrayRoute.size();
        for(int x = 0; x < count; x++) {
            Route route = aArrayRoute.get(x);
            total = total + route.getDistance();
        }

        return total;
    }

    public static double getTotalTime(ArrayList<Route> aArrayRoute) {
        double total = 0;
        if(aArrayRoute == null) {
            return total;
        }

        int count = aArrayRoute.size();
        for(int x = 0; x < count; x++) {
            Route route = aArrayRoute.get(x);
            total = total + route.getTimeHours();
        }

        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (Double.compare(route.mDistance, mDistance) != 0) return false;
        if (Double.compare(route.mTimeHours, mTimeHours) != 0) return false;
        if (mPlanetFrom != null ? !mPlanetFrom.equals(route.mPlanetFrom) : route.mPlanetFrom != null)
            return false;
        return mPlanetTo != null ? mPlanetTo.equals(route.mPlanetTo) : route.mPlanetTo == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = mPlanetFrom != null ? mPlanetFrom.hashCode() : 0;
        result = 31 * result + (mPlanetTo != null ? mPlanetTo.hashCode() : 0);
        temp = Double.doubleToLongBits(mDistance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(mTimeHours);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
