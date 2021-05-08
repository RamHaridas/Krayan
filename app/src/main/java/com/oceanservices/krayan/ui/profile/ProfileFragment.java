package com.oceanservices.krayan.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oceanservices.krayan.R;
import com.oceanservices.krayan.data.UserData;
import com.oceanservices.krayan.databinding.FragmentProfileBinding;
import com.oceanservices.krayan.popups.LogoutPopup;

public class ProfileFragment extends Fragment {
    View root;
    FragmentProfileBinding binding;
    boolean status;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    public static UserData userData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USERS");
        root = binding.getRoot();
        userData = new UserData();
        status = false;
        binding.pfnameEt.setEnabled(false);
        binding.pemailEt.setEnabled(false);
        binding.plnameEt.setEnabled(false);
        binding.pnumberEt.setEnabled(false);
        binding.paddress.setEnabled(false);
        binding.pedit.setEnabled(false);
        binding.pedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open edit console
                NavHostFragment.findNavController(ProfileFragment.this).navigate(R.id.action_navigation_profile_to_nav_edit);
            }
        });
        binding.pdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //open logout popup
                LogoutPopup logoutPopup = new LogoutPopup();
                logoutPopup.show(getActivity().getSupportFragmentManager(),"logout");
            }
        });
        binding.oceans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open about fragment
            }
        });
        try {
            getProfileData();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        listener();
        return root;
    }

    public void getProfileData() throws Exception{
        if (firebaseAuth.getUid() == null){
            Toast.makeText(getContext(), "No internet access", Toast.LENGTH_SHORT).show();
            return;
        }
        databaseReference.child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userData = dataSnapshot.getValue(UserData.class);
                binding.pfnameEt.setText(userData.getFirst_name());
                binding.plnameEt.setText(userData.getLast_name());
                String concat = userData.getAddress_line1()+"\n"+userData.getAddress_line2();
                binding.paddress.setText(concat);
                binding.pemailEt.setText(userData.getEmail());
                binding.pnumberEt.setText(userData.getMobile());
                binding.pedit.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void listener(){
        binding.needHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ProfileFragment.this).navigate(R.id.action_navigation_profile_to_nav_help);
            }
        });
    }
}