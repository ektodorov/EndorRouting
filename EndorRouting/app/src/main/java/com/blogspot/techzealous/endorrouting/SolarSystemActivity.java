package com.blogspot.techzealous.endorrouting;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.techzealous.endorrouting.objects.Planet;
import com.blogspot.techzealous.endorrouting.utils.SharedState;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SolarSystemActivity extends AppCompatActivity {

    private static final String TAG = "SolarSystemActivity";
    private TextView mTextView;
    private ImageView mImageView;

    private ArrayList<Planet> mArrayPlanets;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    private ScheduledExecutorService mScheduledExecutorService;
    private Handler mHandlerMain;
    private Runnable mRunnableSystemUpdate;
    private int mTimeQuarterDay;
    private int mSystemWidth;
    private int mSystemOrigin;
    private int mColorBackground;
    private int mDistanceBiggest;
    /** Scale the solar system planet distance from center according the screen width. */
    private float mStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solar_system);

        mTextView = (TextView)findViewById(R.id.textViewSolarSystem);
        mImageView = (ImageView)findViewById(R.id.imageViewSolarSystem);

        mArrayPlanets = SharedState.getInstance().getPlanets();
        int count = mArrayPlanets.size();
        for(int x = 0; x < count; x++) {
            Planet planet = mArrayPlanets.get(x);
            if(planet.getDistanceFromCenter() > mDistanceBiggest) {
                mDistanceBiggest = planet.getDistanceFromCenter();
            }
        }

        mColorBackground = getResources().getColor(R.color.gray);
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                mSystemWidth = mImageView.getWidth();
                mBitmap = Bitmap.createBitmap(mSystemWidth, mSystemWidth, Bitmap.Config.ARGB_8888);
                mCanvas = new Canvas(mBitmap);
                ViewGroup.LayoutParams params = mImageView.getLayoutParams();
                params.height = mSystemWidth;
                mSystemOrigin = mSystemWidth / 2;
                mImageView.setLayoutParams(params);
                mImageView.requestLayout();

                mStep = (mSystemOrigin - 20) / mDistanceBiggest;
            }
        });

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setFilterBitmap(true);

        mHandlerMain = new Handler(Looper.getMainLooper());
        mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        final WeakReference<SolarSystemActivity> weakThis = new WeakReference<SolarSystemActivity>(this);
        mRunnableSystemUpdate = new Runnable() {
            @Override
            public void run() {
                SolarSystemActivity strongThis = weakThis.get();
                if(strongThis == null) {return;}

                strongThis.mCanvas.drawColor(mColorBackground);
                strongThis.mPaint.setColor(Color.YELLOW);
                strongThis.mCanvas.drawCircle(strongThis.mSystemOrigin, strongThis.mSystemOrigin, 3, strongThis.mPaint);

                strongThis.mPaint.setColor(Color.GREEN);
                int count = strongThis.mArrayPlanets.size();
                for(int z = 0; z < count; z++) {
                    Planet planet = strongThis.mArrayPlanets.get(z);
                    float position = planet.getPosition(strongThis.mTimeQuarterDay * 6);
                    planet.setDegrees(position);
                    int radius = planet.getDistanceFromCenter();
                    radius = (int)(strongThis.mStep * radius);
                    float degrees = planet.getDegrees();

                    int posX = (int)(strongThis.mSystemOrigin + radius * Math.cos(Math.toRadians(degrees)));
                    int posY = (int)(strongThis.mSystemOrigin + radius * Math.sin(Math.toRadians(degrees)));
                    strongThis.mCanvas.drawCircle(posX, posY, 3, strongThis.mPaint);
                }

                mHandlerMain.post(new Runnable() {
                    @Override
                    public void run() {
                        SolarSystemActivity strongThis = weakThis.get();
                        if(strongThis == null) {return;}

                        strongThis.mTimeQuarterDay++;
                        strongThis.mImageView.setImageBitmap(strongThis.mBitmap);
                        strongThis.mTextView.setText(String.valueOf(mTimeQuarterDay / 4));
                    }
                });
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        mArrayPlanets = SharedState.getInstance().getPlanets();
        mScheduledExecutorService.scheduleAtFixedRate(mRunnableSystemUpdate, 250, 250, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScheduledExecutorService.shutdownNow();
    }
}
