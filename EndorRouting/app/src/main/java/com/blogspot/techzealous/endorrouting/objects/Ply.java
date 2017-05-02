package com.blogspot.techzealous.endorrouting.objects;


import com.blogspot.techzealous.endorrouting.utils.ConstantsE;
import com.blogspot.techzealous.endorrouting.utils.SharedState;

import java.util.ArrayList;

public class Ply {

    private int mShipVelocity;
    private Ply mParentPly;
    private Ply mChildPly;
    /** The current planet we are on at this ply. */
    private Planet mPlanet;
    /** Planets that should be visited by this ply. */
    private ArrayList<Planet> mArrayPlanets;

    public Ply() {
        mArrayPlanets = new ArrayList<Planet>();
    }

    /** Score is the distance traveled total. Lowest score wins. */
    private double mScore = Double.MAX_VALUE;
    private double mDistance;

    public int getShipVelocity() {return mShipVelocity;}
    public void setShipVelocity(int aShipVelocity) {mShipVelocity = aShipVelocity;}

    public Ply getParent() {return mParentPly;}
    public void setParent(Ply aParent) {mParentPly = aParent;}

    public Planet getPlanet() {return mPlanet;}
    public void setPlanet(Planet aPlanet) {mPlanet = aPlanet;}

    public ArrayList<Planet> getPlanets() {return mArrayPlanets;}
    public void setPlanets(ArrayList<Planet> aArrayPlanets) {mArrayPlanets = aArrayPlanets;}

    public double getScore() {return mScore;}
    public void setScore(double aScore) {mScore = aScore;}

    public double getDistance() {return mDistance;}
    public void setDistance(double aDistance) {mDistance = aDistance;}

    public ArrayList<Planet> traverse() {
        ArrayList<Planet> arrayPlanets = new ArrayList<>();

        if(mParentPly != null) {
            Route route = ConstantsE.getDistance(mShipVelocity, mParentPly.getPlanet(), mPlanet);
            mDistance = route.getDistance() + mParentPly.getDistance();
        }

        int count = mArrayPlanets.size();
        if(count == 0) {
            mScore = mDistance;
            return arrayPlanets;
        }
        ArrayList<Planet> arrayChildPlanets = new ArrayList<>();
        for(int x = 0; x < count; x++) {
            Ply ply = new Ply();
            ply.setParent(this);
            ply.setPlanets(SharedState.clonePlanets(mArrayPlanets));
            ply.setPlanet(ply.getPlanets().remove(x));
            ply.setShipVelocity(mShipVelocity);
            ArrayList<Planet> planets = ply.traverse();
            if(ply.getScore() < mScore) {
                mScore = ply.getScore();
                mChildPly = ply;
                arrayChildPlanets = planets;
            }
        }

        arrayPlanets.add(mPlanet);
        if(arrayChildPlanets.size() == 0) {
            arrayPlanets.add(mChildPly.getPlanet());
        } else {
            arrayPlanets.addAll(arrayChildPlanets);
        }

        return arrayPlanets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ply ply = (Ply) o;

        if (mShipVelocity != ply.mShipVelocity) return false;
        if (Double.compare(ply.mScore, mScore) != 0) return false;
        if (Double.compare(ply.mDistance, mDistance) != 0) return false;
        if (mParentPly != null ? !mParentPly.equals(ply.mParentPly) : ply.mParentPly != null)
            return false;
        if (mChildPly != null ? !mChildPly.equals(ply.mChildPly) : ply.mChildPly != null)
            return false;
        if (mPlanet != null ? !mPlanet.equals(ply.mPlanet) : ply.mPlanet != null) return false;
        return mArrayPlanets != null ? mArrayPlanets.equals(ply.mArrayPlanets) : ply.mArrayPlanets == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = mShipVelocity;
        result = 31 * result + (mParentPly != null ? mParentPly.hashCode() : 0);
        result = 31 * result + (mChildPly != null ? mChildPly.hashCode() : 0);
        result = 31 * result + (mPlanet != null ? mPlanet.hashCode() : 0);
        result = 31 * result + (mArrayPlanets != null ? mArrayPlanets.hashCode() : 0);
        temp = Double.doubleToLongBits(mScore);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(mDistance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
