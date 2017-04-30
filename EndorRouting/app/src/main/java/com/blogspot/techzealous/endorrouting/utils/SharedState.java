package com.blogspot.techzealous.endorrouting.utils;


import com.blogspot.techzealous.endorrouting.objects.Planet;

import java.util.ArrayList;

public class SharedState {

    private static final SharedState sInstance = new SharedState();
    private static ArrayList<Planet> sArrayPlanets;

    private SharedState() {
        super();
        sArrayPlanets = new ArrayList<>();
    }

    public static SharedState getInstance() {
        return sInstance;
    }

    public ArrayList<Planet> getPlanets() {
        return sArrayPlanets;
    }

    public static ArrayList<Planet> clonePlanets(ArrayList<Planet> aArrayPlanets) {
        ArrayList<Planet> arrayPlanets = new ArrayList<>();
        int count = aArrayPlanets.size();
        for(int x = 0; x < count; x++) {
            Planet planet = new Planet(aArrayPlanets.get(x));
            arrayPlanets.add(planet);
        }
        return arrayPlanets;
    }
}
