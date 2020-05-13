
package com.example.myplaces;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MyPlacesMapsActivity extends AppCompatActivity {
    MapView map = null;
    static int NEW_PLACE = 1;
    IMapController mapController = null;
    static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    MyLocationNewOverlay myLocationOverlay;


    public static final int SHOW_MAP = 0;
    public static final int CENTER_PLACE_ON_MAP=1;
    public static final int SELECT_COORDINATES = 2;


    public static final int EDIT_PLACE = 2;

    private ItemizedIconOverlay<OverlayItem> myPlacesOverlay;


    private int state = 0;
    private boolean selCoorsEnabled = false;
    private GeoPoint placeLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            Intent mapIntent = getIntent();
            Bundle mapBundle = mapIntent.getExtras();
            if(mapBundle != null)
            {
                state = mapBundle.getInt("state");
                if(state==CENTER_PLACE_ON_MAP)
                {
                    String placeLat = mapBundle.getString("lat");
                    String placeLon = mapBundle.getString("lon");
                    placeLoc = new GeoPoint(Double.parseDouble(placeLat), Double.parseDouble(placeLon));

                }
            }
        }catch (Exception e)
        {
            Log.d("Error", "Error reading state");
        }




        setContentView(R.layout.activity_my_places_maps);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        if(state != SELECT_COORDINATES) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MyPlacesMapsActivity.this, EditMyPlaceActivity.class);
                    startActivityForResult(i, NEW_PLACE);
                }
            });
        }else{
            ViewGroup layout = (ViewGroup) fab.getParent();
            if(null != layout)
            {
                layout.removeView(fab);
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map = (MapView)findViewById(R.id.map);

        map.setMultiTouchControls(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_FINE_LOCATION);

        }
        else{
            setupMap();
            //setOnMapClickOverlay();
        }



//        mapController = map.getController();
//        if(mapController!=null){
//            mapController.setZoom(15.0);
//            GeoPoint startPoint = new GeoPoint(43.3209, 21.8958);
//            mapController.setCenter(startPoint);
//        }

    }


    private void setMyLocationOverlay()
    {
        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        myLocationOverlay.enableMyLocation();
        map.getOverlays().add(this.myLocationOverlay);
        mapController = map.getController();
        if(mapController!=null)
        {
            mapController.setZoom(15.0);
            myLocationOverlay.enableFollowLocation();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult( int requestCode, String permissions[], int[] grantResults )
    {
        switch (requestCode)
        {
            case PERMISSION_ACCESS_FINE_LOCATION: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    setupMap();
                }
                return;
            }

        }
    }

//    private HashMap<Marker, Integer> markerPlaceIdMap;
//    private void addMyPlaceMarkers()
//    {
//        ArrayList<MyPlaces> places = MyPlacesData.getInstance().getMyPlaces();
//        markerPlaceIdMap = new HashMap<Marker, Integer>((int) ((double) places.size()*1.2));
//        for(int i = 0; i<places.size(); i++)
//        {
//            MyPlaces place = places.get(i);
//            String lat = place.getLatitude();
//            String lon = place.getLongitude();
//            GeoPoint loc = new GeoPoint(Double.parseDouble(lat), Double.parseDouble(lon));
//            Marker markerOptions = new Marker();
//
//        }
//    }




    @Override
    public void onResume(){
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        map.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(state == SELECT_COORDINATES && !selCoorsEnabled){
            menu.add(0,1,1,"Select Coordinates");
            menu.add(0,2,2,"Cancel");
            return super.onCreateOptionsMenu(menu);
        }
        else {
            getMenuInflater().inflate(R.menu.menu_my_places_maps, menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if(state == SELECT_COORDINATES && !selCoorsEnabled)
        {
            if(id == 1){
                selCoorsEnabled = true;
                Toast.makeText(this, "Select coordinates", Toast.LENGTH_SHORT).show();
            }else if (id == 2)
            {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        }else {


            if (id == R.id.new_place_item) {
                Intent i = new Intent(this, EditMyPlaceActivity.class);
                startActivityForResult(i, 1);
            } else if (id == R.id.about_item) {
                Intent i = new Intent(this, About.class);
                startActivity(i);
            } else if (id == android.R.id.home) {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }





    private void setOnMapClickOverlay()
    {
        MapEventsReceiver mReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                if(state == SELECT_COORDINATES && selCoorsEnabled) {
                    //p = new GeoPoint(43.3209,21.8958);
                    String lon = Double.toString(p.getLongitude());
                    String lat = Double.toString(p.getLatitude());

                    Intent locationIntent = new Intent();
                    locationIntent.putExtra("lon", lon);
                    locationIntent.putExtra("lat", lat);
                    setResult(Activity.RESULT_OK, locationIntent);



                    finish();
                }
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };

        MapEventsOverlay overlayEvents = new MapEventsOverlay(mReceiver);
        map.getOverlays().add(overlayEvents);


    }



    private void setCenterPlaceOnMap()
    {
        mapController = map.getController();
        if(mapController!=null)
        {
            mapController.setZoom(15.0);
            mapController.setCenter(placeLoc);
        }
    }

    private void setupMap()
    {
        switch (state){
            case SHOW_MAP:
                setMyLocationOverlay();
                break;
            case SELECT_COORDINATES:
                mapController = map.getController();
                if(mapController!=null){
                    mapController.setZoom(15.0);
                    mapController.setCenter(new GeoPoint(43.3209,21.8958));
                }
                setOnMapClickOverlay();
                break;

            case CENTER_PLACE_ON_MAP:
            default:
                setCenterPlaceOnMap();
                break;
        }
        showMyPlaces();
    }


    private void showMyPlaces(){
        if(myPlacesOverlay != null)
            this.map.getOverlays().remove(myPlacesOverlay);
        final ArrayList<OverlayItem> items = new ArrayList<>();
        for (int i=0; i < MyPlacesData.getInstance().getMyPlaces().size(); i++)
        {
            MyPlaces myPlace = MyPlacesData.getInstance().getMyPlaces().get(i);
            OverlayItem item = new OverlayItem(myPlace.getName(), myPlace.getDesc(),new GeoPoint(Double.parseDouble(myPlace.getLatitude()), Double.parseDouble(myPlace.getLongitude())));
            item.setMarker(this.getResources().getDrawable(R.drawable.marker_default));
            items.add(item);
        }
        ItemizedIconOverlay<OverlayItem> myPlacesOverlay = new ItemizedIconOverlay<>(items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                Intent intent = new Intent(MyPlacesMapsActivity.this, ViewMyPlaceActivity.class);
                intent.putExtra("position", index);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                Intent intent = new Intent(MyPlacesMapsActivity.this, EditMyPlaceActivity.class);
                intent.putExtra("position", index);
                startActivityForResult(intent, EDIT_PLACE);
                return false;
            }
        }, getApplicationContext());
        this.map.getOverlays().add(myPlacesOverlay);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
        {
           showMyPlaces();
        }
    }

}
