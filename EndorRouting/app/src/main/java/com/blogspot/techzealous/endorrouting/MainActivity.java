package com.blogspot.techzealous.endorrouting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.blogspot.techzealous.endorrouting.fragments.AddPlanetFragment;
import com.blogspot.techzealous.endorrouting.objects.Planet;
import com.blogspot.techzealous.endorrouting.utils.SharedState;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button mButtonAddPlanet;
    private Button mButtonShowSystem;
    private Button mButtonShowRoute;
    private Button mButtonClear;
    private ListView mListViewPlanets;

    private ListAdapterPlanets mListAdapterPlanets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonAddPlanet = (Button)findViewById(R.id.buttonAddPlanetMain);
        mButtonShowSystem = (Button)findViewById(R.id.buttonShowSystemMain);
        mButtonShowRoute = (Button)findViewById(R.id.buttonShowRouteMain);
        mButtonClear = (Button)findViewById(R.id.buttonClearMain);
        mListViewPlanets = (ListView)findViewById(R.id.listViewPlanetsMain);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {actionBar.hide();}

        mButtonAddPlanet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragmentAddPlanet = new AddPlanetFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.relativeLayoutRootMain, fragmentAddPlanet);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        mButtonShowSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SolarSystemActivity.class);
                startActivity(i);
            }
        });

        mButtonShowRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RouteActivity.class);
                startActivity(i);
            }
        });

        mButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedState.getInstance().getPlanets().clear();
                mListAdapterPlanets.notifyDataSetChanged();
            }
        });

        mListAdapterPlanets = new ListAdapterPlanets(this, SharedState.getInstance().getPlanets());
        mListViewPlanets.setAdapter(mListAdapterPlanets);
        mListViewPlanets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Planet planet = (Planet)mListAdapterPlanets.getItem(position);
                AddPlanetFragment fragmentAddPlanet = new AddPlanetFragment();
                fragmentAddPlanet.setPlanet(planet);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.relativeLayoutRootMain, fragmentAddPlanet);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mListAdapterPlanets.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            mListAdapterPlanets.notifyDataSetChanged();
        }
        super.onBackPressed();
    }
}
