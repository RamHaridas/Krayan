package com.oceanservices.krayan.ui.profile;

import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oceanservices.krayan.R;
import com.oceanservices.krayan.data.UserData;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class EditFragment extends Fragment implements LocationFragment.SendData{

    TextInputEditText fname,lname,address1,address2,mobile;
    Button done,edit;
    View view;
    public static UserData userData;
    Location curr;
    Map<String,Object> map;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_edit, container, false);
        initialize();
        map = new HashMap<String, Object>();
        userData = ProfileFragment.userData;
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USERS");
        try {
            setData(userData);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        clickListeners();
        return view;
    }

    private void setData(UserData userData) throws Exception{
        if(userData == null){
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            return;
        }
        fname.setText(userData.getFirst_name());
        lname.setText(userData.getLast_name());
        address1.setText(userData.getAddress_line1());
        address2.setText(userData.getAddress_line2());
        mobile.setText(userData.getMobile());

    }
    private void initialize(){
        fname = view.findViewById(R.id.pfname_et);
        lname = view.findViewById(R.id.plname_et);
        address1 = view.findViewById(R.id.paddress);
        address2 = view.findViewById(R.id.paddress2);
        mobile = view.findViewById(R.id.pnumber_et);
        done = view.findViewById(R.id.save);
        edit = view.findViewById(R.id.loc);
    }

    public void clickListeners(){
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_nav_edit_to_nav_loc);
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    updateData();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void setLocation(double lat, double longt) {
        //userData.setLatitude(lat);
        //userData.setLatitude(longt);
    }

    public void setCurr(Location curr) {
        this.curr = curr;
        userData.setLatitude(curr.getLatitude());
        userData.setLongitude(curr.getLongitude());
    }

    public void updateData() throws Exception{
        String first = fname.getText().toString().trim();
        String last = lname.getText().toString().trim();
        String addr1 = address1.getText().toString().trim();
        String addr2 = address2.getText().toString().trim();
        String mob = mobile.getText().toString().trim();
        if(first.isEmpty()){
            fname.setError("Cannot be empty");
            return;
        }else if(last.isEmpty()){
            lname.setError("Cannot be empty");
            return;
        }else if(addr1.isEmpty()){
            address1.setError("Cannot be empty");
            return;
        }else if(addr2.isEmpty()){
            address2.setError("Cannot be empty");
            return;
        }else if (mob.isEmpty() || mob.length() < 10){
            mobile.setError("INVALID FORMAT");
            return;
        }else if (userData.getLatitude() == 0.0 || userData.getLongitude() == 0.0){
            Toast.makeText(getContext(), "Please set your location", Toast.LENGTH_SHORT).show();
        }
        map.put("first_name",first);
        map.put("last_name",last);
        map.put("address_line1",addr1);
        map.put("address_line2",addr2);
        map.put("mobile",mob);
        map.put("latitude",userData.getLatitude());
        map.put("longitude",userData.getLongitude());
        databaseReference.child(firebaseAuth.getUid()).updateChildren(map);
        NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_nav_edit_to_navigation_profile);
    }
}