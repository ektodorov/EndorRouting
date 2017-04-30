package com.blogspot.techzealous.endorrouting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blogspot.techzealous.endorrouting.objects.Route;

import java.util.ArrayList;


public class ListAdapterRoute extends BaseAdapter {

    private ArrayList<Route> mArrayData;
    private LayoutInflater mLayoutInflater;

    public ListAdapterRoute(Context aContext, ArrayList<Route> aArrayData) {
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if(view == null) {
            view = mLayoutInflater.inflate(R.layout.listitem_planet, viewGroup, false);
        }

        TextView textView = (TextView)view.findViewById(R.id.textViewLabelListItemPlanet);
        Route route = mArrayData.get(position);
        textView.setText("From:" + route.getPlanetFrom().getName() + "\n" +
            "To:" + route.getPlanetTo().getName() + "\n" +
            "Distance:" + route.getDistance());
        return view;
    }
}
