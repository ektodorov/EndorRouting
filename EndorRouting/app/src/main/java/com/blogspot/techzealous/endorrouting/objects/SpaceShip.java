package com.blogspot.techzealous.endorrouting.objects;


public class SpaceShip {

    /** km per day */
    private int mShipVelocity;
    private int mShipVelocityPerHour;

    public SpaceShip() {
        super();
    }

    /** km/day */
    public int getShipVelocity() {return mShipVelocity;}
    /** km/day */
    public void setShipVelocity(int aShipVelocity) {
        mShipVelocity = aShipVelocity;
        mShipVelocityPerHour = mShipVelocity / 24;
    }

    /** km/h */
    public int getShipVelocityPerHour() {return mShipVelocityPerHour;}
}
