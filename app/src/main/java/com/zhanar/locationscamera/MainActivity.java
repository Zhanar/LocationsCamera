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
        Location mountWashington = new Location("Washington");
        mountWashington.setLatitude(44.27179d);
        mountWashington.setLongitude(-71.3039d);
        mountWashington.setAltitude(1916.5d);
        CustomLocation mW = new CustomLocation();
        mW.location = mountWashington;
        mW.name = "Mount Washington";
        capitalCitiesLocs.add(mW);

        Location astana = new Location("Astana");
        astana.setLatitude(51.1811111d);
        astana.setLongitude(71.4277778d);
        astana.setAltitude(1152.795d);
        CustomLocation customLocationAstana = new CustomLocation();
        customLocationAstana.location = astana;
        customLocationAstana.name = "Astana";
        capitalCitiesLocs.add(customLocationAstana);

        Location almaty = new Location("Almaty");
        almaty.setLatitude(43.30358543393359);
        almaty.setLongitude(77.21592664718628);
        almaty.setAltitude(3277.378d);
        CustomLocation customLocationAlmaty = new CustomLocation();
        customLocationAlmaty.location = almaty;
        customLocationAlmaty.name = "Almaty";
        capitalCitiesLocs.add(customLocationAlmaty);

        Location madrid = new Location("Madrid");
        madrid.setLatitude(40.4165000);
        madrid.setLongitude(-3.7025600);
        madrid.setAltitude(582);
        CustomLocation customLocationMadrid = new CustomLocation();
        customLocationMadrid.location = madrid;
        customLocationMadrid.name = "Madrid";
        capitalCitiesLocs.add(customLocationMadrid);

        Location losAngeles = new Location("Los Angeles");
        losAngeles.setLatitude(34.052235);
        losAngeles.setLongitude(-118.243683);
        losAngeles.setAltitude(115);
        CustomLocation customLocationLosAngeles = new CustomLocation();
        customLocationLosAngeles.location = losAngeles;
        customLocationLosAngeles.name = "Los Angeles";
        capitalCitiesLocs.add(customLocationLosAngeles);

        Location london = new Location("London");
        london.setLatitude(51.500083);
        london.setLongitude(-0.126182);
        london.setAltitude(245);
        CustomLocation customLocationLondon = new CustomLocation();
        customLocationLondon.location = london;
        customLocationLondon.name = "London";
        capitalCitiesLocs.add(customLocationLondon);

        Location dublin = new Location("Dublin");
        dublin.setLatitude(53.350140);
        dublin.setLongitude(-6.266155);
        dublin.setAltitude(85);
        CustomLocation customLocationDublin = new CustomLocation();
        customLocationDublin.location = london;
        customLocationDublin.name = "Dublin";
        capitalCitiesLocs.add(customLocationDublin);
    }
}
