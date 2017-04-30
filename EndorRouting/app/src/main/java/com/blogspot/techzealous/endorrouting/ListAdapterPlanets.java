package com.blogspot.techzealous.endorrouting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blogspot.techzealous.endorrouting.objects.Planet;

import java.util.ArrayList;


public class ListAdapterPlanets extends BaseAdapter {

    private ArrayList<Planet> mArrayData;
    private LayoutInflater mLayoutInflater;

    public ListAdapterPlanets(Context aContext, ArrayList<Planet> aArrayData) {
        mArrayData = aArrayData;
        mLayoutInflater = (LayoutInflater)aContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mArrayData.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mArrayData.hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if(view == null) {
            view = mLayoutInflater.inflate(R.layout.listitem_planet, viewGroup, false);
        }

        Planet planet = mArrayData.get(position);

        TextView textViewLabel = (TextView)view.findViewById(R.id.textViewLabelListItemPlanet);
        textViewLabel.setText(planet.getName() + "\n"
                + "distanceFromCenter=" + planet.getDistanceFromCenter() + "\n"
                + "startDegrees=" + planet.getStartDegrees() + "\n"
                + "speed=" + planet.getSpeed());

        return view;
    }
}
