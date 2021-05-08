package com.oceanservices.krayan.ui.profile;

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

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.oceanservices.krayan.AddLocationActivity;
import com.oceanservices.krayan.GPS.GpsUtils;
import com.oceanservices.krayan.R;
import com.oceanservices.krayan.data.UserData;
import com.oceanservices.krayan.databinding.ActivityAddLocationBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LocationFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener{
    public interface SendData{
        void setLocation(double lat, double longt);
    }
    SendData sendData;
    View view;
    Button confirm;
    androidx.appcompat.widget.SearchView searchView;
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
    MapView mapView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_location, container, false);
        searchView = view.findViewById(R.id.search);
        confirm = view.findViewById(R.id.confirm_location);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mapView = view.findViewById(R.id.mapView);
        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(LocationFragment.this);
        checkPermissions();
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                geoLocate(getContext(), query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        new GpsUtils(getContext()).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
                checkPermissions();
                fetchLastLocation();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    closeMap();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        googleMap.clear();
        shopmarker = new MarkerOptions().position(latLng).title("Your Home").icon(bitmapDescriptorFromVector(getContext(),R.drawable.ic_home));
        googleMap.addMarker(shopmarker);
        setMarkerBounce(shopmarker);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        try {
            //googleMap.setMyLocationEnabled(true);
        }catch (Exception e){e.printStackTrace();}

        googleMap.setOnMapLongClickListener(this);
        fetchLastLocation();
        googleMap.clear();
        UserData userData = ProfileFragment.userData;
        if(userData != null) {
            LatLng latLng = new LatLng(userData.getLatitude(), userData.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            shopmarker = new MarkerOptions().position(latLng).title("Your Home").icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_home));
            googleMap.addMarker(shopmarker);
        }else{
            LatLng latLng = new LatLng(21.1458, 79.0882);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        }
    }
    public void geoLocate(Context context, String search){
        if(search == null){
            Toast.makeText(getContext(),"Please Enter Location First",Toast.LENGTH_SHORT).show();
        }
        Log.d("Geo","Geo Loacating");
        String source = search;
        //source = "friends colony, nagpur";
        //String des = dest.getText().toString().trim();
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(source,1);
        }catch(IOException e){
            Toast.makeText(getContext(),"Error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
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
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
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

                }
            }
        });
    }
    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(getContext(),p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
                /*Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);*/
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }
    private void setMarkerBounce(final MarkerOptions marker) {
        final Handler handler = new Handler();
        final long startTime = SystemClock.uptimeMillis();
        final long duration = 2000;
        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - startTime;
                float t = Math.max(1 - interpolator.getInterpolation((float) elapsed/duration), 0);
                marker.anchor(0.5f, 1.0f +  t);

                if (t > 0.0) {
                    handler.postDelayed(this, 16);
                } else {
                    setMarkerBounce(marker);
                }
            }
        });
    }

    public void closeMap() throws Exception{
        if(shopmarker == null){
            Toast.makeText(getContext(), "Please add your home location", Toast.LENGTH_SHORT).show();
            return;
        }else if(shopmarker.getPosition().longitude == 0.0 || shopmarker.getPosition().latitude == 0.0){
            Toast.makeText(getContext(), "Please add your home location", Toast.LENGTH_SHORT).show();
            return;
        }
        Location location = new Location("");
        location.setLatitude(shopmarker.getPosition().latitude);
        location.setLongitude(shopmarker.getPosition().longitude);
        Log.i("lat",shopmarker.getPosition().latitude+","+shopmarker.getPosition().longitude);
        //sendData.setLocation(shopmarker.getPosition().latitude,shopmarker.getPosition().longitude);
        EditFragment.userData.setLatitude(shopmarker.getPosition().latitude);
        EditFragment.userData.setLongitude(shopmarker.getPosition().longitude);
        NavHostFragment.findNavController(LocationFragment.this).navigate(R.id.action_nav_loc_to_nav_edit);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            sendData = (SendData)getContext();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}