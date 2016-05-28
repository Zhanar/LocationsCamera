package com.zhanar.locationscamera;

import android.hardware.Camera;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Camera mCamera;
    private DisplayView arDisplay;
    List<CustomLocation> capitalCitiesLocs;
    private List<Location> capitalCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout arViewPane = (FrameLayout) findViewById(R.id.ar_view_pane);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        arDisplay = new DisplayView(this, MainActivity.this, mCamera);
        arViewPane.addView(arDisplay);

        capitalCitiesLocs = new ArrayList<>();
        getCapitalCities();
        OverlayView arContent = new OverlayView(getApplicationContext(), mCamera, capitalCitiesLocs);
        arViewPane.addView(arContent);
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public void getCapitalCities() {
//        Location mountWashington = new Location("Washington");
//        mountWashington.setLatitude(44.27179d);
//        mountWashington.setLongitude(-71.3039d);
//        mountWashington.setAltitude(1916.5d);
//        CustomLocation mW = new CustomLocation();
//        mW.location = mountWashington;
//        mW.name = "Mount Washington";
//        capitalCitiesLocs.add(mW);

        Location astanaCity = new Location("Astana");
        astanaCity.setLatitude(51.1811111d);
        astanaCity.setLongitude(71.4277778d);
        astanaCity.setAltitude(1152.795d);
        CustomLocation ac = new CustomLocation();
        ac.location = astanaCity;
        ac.name = "Astana";
        capitalCitiesLocs.add(ac);

//        Location talgar = new Location("Talgar");
//        talgar.setLatitude(43.30358543393359);
//        talgar.setLongitude(77.21592664718628);
//        talgar.setAltitude(3277.378d);
//        CustomLocation t = new CustomLocation();
//        t.location = talgar;
//        t.name = "Talgar";
//        capitalCitiesLocs.add(t);

        Location almaty = new Location("Almaty");
        almaty.setLatitude(43.30358543393359);
        almaty.setLongitude(77.21592664718628);
        almaty.setAltitude(3277.378d);
        CustomLocation a = new CustomLocation();
        a.location = almaty;
        a.name = "Almaty";
        capitalCitiesLocs.add(a);
    }
}
