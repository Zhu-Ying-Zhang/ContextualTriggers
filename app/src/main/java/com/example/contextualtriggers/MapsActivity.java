package com.example.contextualtriggers;

import static android.view.Gravity.CENTER;

import android.Manifest;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.example.contextualtriggers.context.ContextHolder;
import com.example.contextualtriggers.context.GeofenceContext;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener,GoogleMap.OnCircleClickListener {

    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;


    private GeofencingClient geofencingClient;


    private GeofenceContext geofenceContext;

    private float GEOFENCE_RADIUS = 50;
    String GEOFENCE_ID = "SELECTED_GEOFENCE_ID";

    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;
    private int markerCount = 0;
    private final String[] choices = {"Home", "Work", "Gym", "Supermarket"};

    private ContextHolder contextHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceContext = new GeofenceContext(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng glasgow = new LatLng(55.860553, -4.242481);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(glasgow, 16));

        enableUserLocation();

        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.getMinZoomLevel();
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

    }

    private void enableUserLocation() {
        checkLocationPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationPermission();
            } else {
                //We do not have the permission..
                new AlertDialog.Builder(this,R.style.Theme_ContextualTriggers)
                        .setTitle("Location Denied")
                        .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                ActivityCompat.requestPermissions(MapsActivity.this,
//                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                        FINE_LOCATION_ACCESS_REQUEST_CODE);
                                checkLocationPermission();
                            }

                        })
                        .setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create()
                        .show();

            }
        }

        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                Toast.makeText(this, "You can add geofences...", Toast.LENGTH_SHORT).show();
            } else {
                //We do not have the permission..
                Toast.makeText(this, "Background location access is neccessary for geofences to trigger...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            //Ask for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (Build.VERSION.SDK_INT >= 29) {
            //We need background permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                handleMapLongClick(latLng);
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);

                    checkLocationPermission();
                    //  new AlertDialog.Builder(this,R.style.Theme_ContextualTriggers)
//                            .setTitle("Location Access")
//                            .setMessage("Set Allow all the time  to for background use")
//                            .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    ActivityCompat.requestPermissions(MapsActivity.this,
//                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                            FINE_LOCATION_ACCESS_REQUEST_CODE);
//                                    ActivityCompat.requestPermissions(MapsActivity.this,
//                                            new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
//                                            BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
//
//                                }
//                            })
//                            .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//
//                                }
//                            })
//                            .create()
//                            .show();

                }
            }


        } else {
            handleMapLongClick(latLng);
        }

    }

    private void handleMapLongClick(LatLng latLng) {

        if (markerCount < 10) {
            markerCount = markerCount+ 1;

            addMarker(latLng);
            addCircle(latLng, GEOFENCE_RADIUS);
            addGeofence(latLng, GEOFENCE_RADIUS);

        } else {
            Toast.makeText(getApplicationContext(), "Only 10 Geofence allowed!",
                    Toast.LENGTH_SHORT).show();
            new AlertDialog.Builder(this,R.style.AlertDialog_AppCompat_Dialog)
                    .setTitle("Remove Geofence")
                    .setMessage("Would you like to remove Geofence?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            removeGeofence();
                            mMap.clear();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .create()
                    .show();
        }

    }


    private void addGeofence( LatLng latLng, float radius) {

        Geofence geofence = geofenceContext.getGeofence(GEOFENCE_ID, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
        GeofencingRequest geofencingRequest = geofenceContext.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceContext.getPendingIntent();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Geofence Added...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = geofenceContext.getErrorString(e);
                        Log.d(TAG, "onFailure: " + errorMessage);
                    }
                });
    }


    /**
     * Ref: https://android-er.blogspot.com/2015/10/add-marker-to-google-map-google-maps.html
     **/
    private void addMarker(LatLng latLng) {
        if (mMap != null) {

            //create custom LinearLayout programmatically
            LinearLayout layout = new LinearLayout(MapsActivity.this);
            layout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.setOrientation(LinearLayout.VERTICAL);

            final Spinner spinnerField = new Spinner(MapsActivity.this);


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MapsActivity.this,
                    android.R.layout.simple_spinner_item, choices);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerField.setAdapter(adapter);
            spinnerField.setGravity(CENTER);

            layout.addView(spinnerField);

            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialog_AppCompat_Dialog);
            builder.setTitle("Please select your Geofence name? ");
            builder.setView(layout);
            AlertDialog alertDialog = builder.create();

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    MarkerOptions markerOptions;
                    String strSpinner = spinnerField.getSelectedItem().toString();

                    markerOptions = new MarkerOptions().position(latLng).title(strSpinner);

                    markerOptions.draggable(true);
                    mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        } else {
            Toast.makeText(MapsActivity.this, "Map not ready", Toast.LENGTH_LONG).show();
        }
    }

    private void addCircle(LatLng latLng, float radius) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
        circleOptions.fillColor(Color.argb(64, 255, 0, 0));
        circleOptions.strokeWidth(4);
        mMap.addCircle(circleOptions);
    }
    public  void onCircleClick (Circle circle)
    {

        circle.getZIndex();

        Log.i("kirubel","kirubel"+circle.getZIndex());

    }
    @Override
    public void onMarkerDragStart(Marker marker) {
        marker.setTitle(marker.getTitle());
        marker.showInfoWindow();
        marker.setAlpha(0.5f);

    }

    @Override
    public void onMarkerDrag(Marker marker) {
        marker.setTitle(marker.getTitle());
        marker.showInfoWindow();
        marker.setAlpha(0.5f);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        marker.setTitle(marker.getTitle());
        marker.showInfoWindow();
        marker.setAlpha(1.0f);

        new AlertDialog.Builder(this,R.style.AlertDialog_AppCompat_Dialog)
                .setTitle("Remove Geofances")
                .setMessage("This will remove all geofences")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(Intent.makeMainActivity());
//                        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
//                        for (Geofence geofence : geofenceList) {
//                            Log.d(TAG, "onReceive: " + geofence.getRequestId());
//                        }
                        removeGeofence();
                        mMap.clear();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private void removeGeofence() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
//        if(geofenceRequestIds != null) {
//            geofencingClient.removeGeofences(geofenceRequestIds).addOnSuccessListener(aVoid -> Log.e("TAG", "Geocenfences removed")).addOnFailureListener(e -> {
//                String errorMessage = geofenceContext.getErrorString(e);
//                Log.e("TAG", "onFailure: " + errorMessage);
//                Toast.makeText(getApplicationContext(), "onFailure: " + errorMessage, Toast.LENGTH_SHORT).show();
//            });
//        } else {
            Log.i(TAG,"no list provided, removing ALL geofences");
            geofencingClient.removeGeofences(geofenceContext.getPendingIntent()).addOnSuccessListener(aVoid -> Log.e("TAG", "Geocenfences removed")).addOnFailureListener(e -> {
                String errorMessage = geofenceContext.getErrorString(e);
                Log.e("TAG", "onFailure: " + errorMessage);
                Toast.makeText(getApplicationContext(), "onFailure: " + errorMessage, Toast.LENGTH_SHORT).show();
            });;
        }
//    }
}
