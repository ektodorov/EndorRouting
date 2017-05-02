package com.blogspot.techzealous.endorrouting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blogspot.techzealous.endorrouting.objects.Planet;
import com.blogspot.techzealous.endorrouting.objects.Route;
import com.blogspot.techzealous.endorrouting.utils.ConstantsE;
import com.blogspot.techzealous.endorrouting.utils.SharedState;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RouteActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private TextView mTextViewTotal;
    private ListView mListView;

    private ArrayList<Route> mArrayRoute;
    private ListAdapterRoute mListAdapter;
    private Handler mHandlerMain;
    private ExecutorService mExecutorService;
    private int mShipVelocity;
    private int mLaunchDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        mProgressBar = (ProgressBar)findViewById(R.id.progressBarRoute);
        mTextViewTotal = (TextView)findViewById(R.id.textViewTotalRoute);
        mListView = (ListView)findViewById(R.id.listViewRoute);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {actionBar.hide();}

        mShipVelocity = getIntent().getIntExtra(ConstantsE.EXTRA_SHIP_VELOCITY, ConstantsE.SHIP_VELOCITY);
        mLaunchDay = getIntent().getIntExtra(ConstantsE.EXTRA_LAUNCH_DAY, ConstantsE.LAUNCH_DAY);

        mHandlerMain = new Handler(Looper.getMainLooper());
        mExecutorService = Executors.newSingleThreadExecutor();
        mArrayRoute = new ArrayList<Route>();

        final WeakReference<RouteActivity> weakActivity = new WeakReference<RouteActivity>(this);
        final ArrayList<Planet> fArrayPlanets = SharedState.clonePlanets(SharedState.getInstance().getPlanets());
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                RouteActivity strongActivity = weakActivity.get();
                if(strongActivity == null) {return;}

//                ArrayList<Route> arrayRoute = ConstantsE.getRouteInOrder(fArrayPlanets);
//                ArrayList<Route> arrayRoute = ConstantsE.getRouteInOrder(fArrayPlanets, ship);
                ArrayList<Route> arrayRoute = ConstantsE.getRouteMinimax(fArrayPlanets,
                        strongActivity.mShipVelocity, strongActivity.mLaunchDay);

                strongActivity.mArrayRoute = arrayRoute;

                mHandlerMain.post(new Runnable() {
                    @Override
                    public void run() {
                        RouteActivity strongActivity = weakActivity.get();
                        if(strongActivity == null) {return;}

                        strongActivity.mListAdapter = new ListAdapterRoute(strongActivity, strongActivity.mArrayRoute);
                        strongActivity.mListView.setAdapter(strongActivity.mListAdapter);
                        strongActivity.mProgressBar.setVisibility(View.GONE);
                        strongActivity.mTextViewTotal.setText("Total Distance:"
                            + String.valueOf(Route.getTotalDistance(strongActivity.mArrayRoute)) + " km\n"
                            + "Total time:" + Route.getTotalTime(strongActivity.mArrayRoute) + " days");
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
