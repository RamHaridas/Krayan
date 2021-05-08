package com.oceanservices.krayan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.oceanservices.krayan.GPS.GpsUtils;
import com.oceanservices.krayan.databinding.ActivityAddLocationBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    ActivityAddLocationBinding binding;
    Location currLoc;
    private static final int MULTIPLE_PERMISSIONS = 10; // code you want.
    private String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int REQUEST_LOCATION = 101;
    private boolean isGPS = false;
    FusedLocationProviderClient fusedLocationProviderClient;
    GoogleMap googleMap;
    MarkerOptions shopmarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddLocationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(AddLocationActivity.this);
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.onResume();
        binding.mapView.getMapAsync(AddLocationActivity.this);
        checkPermissions();
        binding.search.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                geoLocate(AddLocationActivity.this, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        new GpsUtils(AddLocationActivity.this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
                checkPermissions();
                fetchLastLocation();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        try {
            googleMap.setMyLocationEnabled(true);
        }catch (Exception e){e.printStackTrace();}

        googleMap.setOnMapLongClickListener(this);
        fetchLastLocation();
        if(currLoc != null){
            LatLng latLng = new LatLng(currLoc.getLatitude(),currLoc.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        }else{
            LatLng latLng = new LatLng(21.1458, 79.0882);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        }
    }


    public void geoLocate(Context context, String search){
        if(search == null){
            Toast.makeText(this,"Please Enter Location First",Toast.LENGTH_SHORT).show();
        }
        Log.d("Geo","Geo Loacating");
        String source = search;
        //source = "friends colony, nagpur";
        //String des = dest.getText().toString().trim();
        Geocoder geocoder = new Geocoder(this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(source,1);
        }catch(IOException e){
            Toast.makeText(this,"Error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        if(list.size() > 0){
            Address address = list.get(0);
            double l = address.getLatitude();
            double l2 = address.getLongitude();
            LatLng latLng = new LatLng(l,l2);
            moveCamera(latLng);
            Log.d("Location found",address.toString());
            //Toast.makeText(view.getContext(),"Address:"+address,Toast.LENGTH_SHORT).show();
        }
    }
    private void moveCamera(LatLng latLng) {
        if(latLng != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorId){
        Drawable drawable = ContextCompat.getDrawable(context,vectorId);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void fetchLastLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
            /*Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);*/
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currLoc = location;
                    LatLng latLng = new LatLng(currLoc.getLatitude(),currLoc.getLongitude());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                }
            }
        });
    }
    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
                /*Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);*/
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        googleMap.clear();
        shopmarker = new MarkerOptions().position(latLng).title("Your Shop").icon(bitmapDescriptorFromVector(this,R.drawable.ic_home));
        googleMap.addMarker(shopmarker);
    }

    public void closeMap(View view){
        try{
            if(shopmarker.getPosition().latitude != 0.0 && shopmarker.getPosition().longitude != 0.0){
                Intent intent = new Intent();
                double [] loc = {0.0,0.0};
                loc[0] = shopmarker.getPosition().latitude;
                loc[1] = shopmarker.getPosition().longitude;
                intent.putExtra("location",loc);
                setResult(31,intent);
                finish();
            }else{
                Toast.makeText(this, "Please add marker first", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, "Please add marker first", Toast.LENGTH_SHORT).show();
        }
    }
}