package com.blogspot.techzealous.endorrouting.objects;


public class Planet {

    /** days per orbit */
    private int mSpeed;

    /** degrees at time = 0 */
    private float mStartDegrees;

    /** in km */
    private int mDistanceFromCenter;

    private String mName;
    private int mHoursInAYear;
    private int mMinutesInAYear;
    private float mDegreesAHour;
    private float mDegreesAMinute;
    private double mSpeedKmDay = -1;
    private float mDegrees;

    public Planet() {
        super();
    }

    public Planet(Planet aPlanet) {
        super();
        mName = aPlanet.getName();
        setSpeed(aPlanet.getSpeed());
        setStartDegrees(aPlanet.getStartDegrees());
        mDistanceFromCenter = aPlanet.getDistanceFromCenter();
    }

    /** Days to orbit */
    public int getSpeed() {return mSpeed;}
    /** Days to orbit */
    public void setSpeed(int aSpeed) {
        mSpeed = aSpeed;
        mHoursInAYear = 24 * mSpeed;
        mMinutesInAYear = 60 * mHoursInAYear;
        mDegreesAHour = 360.0f / mHoursInAYear;
        mDegreesAMinute = 360.0f / mMinutesInAYear;
    }

    public double getSpeedKmDay() {
        if(mSpeedKmDay < 0) {
            double orbitCircumference = 2 * Math.PI * mDistanceFromCenter;
            mSpeedKmDay = orbitCircumference / mSpeed;
        }
        return mSpeedKmDay;
    }

    public float getStartDegrees() {return mStartDegrees;}
    public void setStartDegrees(float aStartDegrees) {
        mStartDegrees = aStartDegrees;
        mDegrees = mStartDegrees;
    }

    public int getDistanceFromCenter() {return mDistanceFromCenter;}
    public void setDistanceFromCenter(int aDistanceFromCenter) {mDistanceFromCenter = aDistanceFromCenter;}

    public String getName() {return mName;}
    public void setName(String aName) {mName = aName;}

    /** Current position in degrees */
    public float getDegrees() {return mDegrees;}
    public void setDegrees(float aDegrees) {mDegrees = aDegrees;}

    /** Gets the position of the planet in degrees, according to the hours passed. */
    public float getDegreesWithHours(int aHours) {
        while(aHours > mHoursInAYear) {
            aHours = aHours - mHoursInAYear;
        }
        float degrees = mStartDegrees + (aHours * mDegreesAHour);
        return degrees;
    }

    /** Gets the position of the planet in degrees, according to the minutes passed. */
    public float getDegreesWithMinutes(int aMinutes) {
        while(aMinutes > mMinutesInAYear) {
            aMinutes = aMinutes - mMinutesInAYear;
        }
        float degrees = mStartDegrees + (aMinutes * mDegreesAMinute);
        return degrees;
    }

    /** Gets the position of the planet in degrees, according to the hours passed. */
    public float getPosition(float aHours) {
        while(aHours > mHoursInAYear) {
            aHours = aHours - mHoursInAYear;
        }
        float degrees = mStartDegrees + (aHours * mDegreesAHour);
        return degrees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Planet planet = (Planet) o;

        if (mSpeed != planet.mSpeed) return false;
        if (Float.compare(planet.mStartDegrees, mStartDegrees) != 0) return false;
        if (mDistanceFromCenter != planet.mDistanceFromCenter) return false;
        if (mHoursInAYear != planet.mHoursInAYear) return false;
        if (mMinutesInAYear != planet.mMinutesInAYear) return false;
        if (Float.compare(planet.mDegreesAHour, mDegreesAHour) != 0) return false;
        if (Float.compare(planet.mDegreesAMinute, mDegreesAMinute) != 0) return false;
        if (Double.compare(planet.mSpeedKmDay, mSpeedKmDay) != 0) return false;
        if (Float.compare(planet.mDegrees, mDegrees) != 0) return false;
        return mName != null ? mName.equals(planet.mName) : planet.mName == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = mSpeed;
        result = 31 * result + (mStartDegrees != +0.0f ? Float.floatToIntBits(mStartDegrees) : 0);
        result = 31 * result + mDistanceFromCenter;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        result = 31 * result + mHoursInAYear;
        result = 31 * result + mMinutesInAYear;
        result = 31 * result + (mDegreesAHour != +0.0f ? Float.floatToIntBits(mDegreesAHour) : 0);
        result = 31 * result + (mDegreesAMinute != +0.0f ? Float.floatToIntBits(mDegreesAMinute) : 0);
        temp = Double.doubleToLongBits(mSpeedKmDay);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (mDegrees != +0.0f ? Float.floatToIntBits(mDegrees) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Planet{" +
                "mSpeed=" + mSpeed +
                ", mStartDegrees=" + mStartDegrees +
                ", mDistanceFromCenter=" + mDistanceFromCenter +
                ", mName='" + mName + '\'' +
                ", mDegrees=" + mDegrees +
                '}';
    }
}
