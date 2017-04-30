package com.blogspot.techzealous.endorrouting.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.blogspot.techzealous.endorrouting.R;
import com.blogspot.techzealous.endorrouting.objects.Planet;
import com.blogspot.techzealous.endorrouting.utils.SharedState;


public class AddPlanetFragment extends Fragment {

    private EditText mEditTextName;
    private EditText mEditTextSpeed;
    private EditText mEditTextStartDegrees;
    private EditText mEditTextDistanceFromCenter;
    private Button mButtonAddPlanet;
    private Button mButtonDeletePlanet;

    private Planet mPlanet;

    public AddPlanetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_planet, container, false);

        mEditTextName = (EditText)view.findViewById(R.id.editTextNameAddPlanet);
        mEditTextSpeed = (EditText)view.findViewById(R.id.editTextSpeedAddPlanet);
        mEditTextStartDegrees = (EditText)view.findViewById(R.id.editTextStartDegreesAddPlanet);
        mEditTextDistanceFromCenter = (EditText)view.findViewById(R.id.editTextDistanceFromCenterAddPlanet);
        mButtonAddPlanet = (Button)view.findViewById(R.id.buttonAddPlanetAddPlanet);
        mButtonDeletePlanet = (Button)view.findViewById(R.id.buttonDeletePlanetAddPlanet);

        mButtonAddPlanet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mEditTextName.getText().toString();
                String strSpeed = mEditTextSpeed.getText().toString();
                int speed = Integer.parseInt(strSpeed);
                String strStartDegrees = mEditTextStartDegrees.getText().toString();
                int startDegrees = Integer.parseInt(strStartDegrees);
                String strDistanceFromCenter = mEditTextDistanceFromCenter.getText().toString();
                int distanceFromCenter = Integer.parseInt(strDistanceFromCenter);

                Planet planet = mPlanet;
                if(planet == null) {
                    planet = new Planet();
                }
                planet.setName(name);
                planet.setSpeed(speed);
                planet.setStartDegrees(startDegrees);
                planet.setDistanceFromCenter(distanceFromCenter);
                if(mPlanet == null) {
                    SharedState.getInstance().getPlanets().add(planet);
                }
                getActivity().onBackPressed();
            }
        });

        mButtonDeletePlanet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlanet != null) {
                    SharedState.getInstance().getPlanets().remove(mPlanet);
                }
            }
        });

        if(mPlanet != null) {
            mButtonAddPlanet.setText(getString(R.string.update_planet));
            mEditTextName.setText(mPlanet.getName());
            mEditTextSpeed.setText(String.valueOf(mPlanet.getSpeed()));
            mEditTextStartDegrees.setText(String.valueOf((int)mPlanet.getStartDegrees()));
            mEditTextDistanceFromCenter.setText(String.valueOf(mPlanet.getDistanceFromCenter()));
        } else {
            mButtonAddPlanet.setText(getString(R.string.add_planet));
        }

        return view;
    }

    public Planet getPlanet() {return mPlanet;}
    public void setPlanet(Planet aPlanet) {mPlanet = aPlanet;}

}
