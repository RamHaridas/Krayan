package com.oceanservices.krayan.ui.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Registry;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oceanservices.krayan.R;
import com.oceanservices.krayan.data.QueryData;
import com.oceanservices.krayan.popups.QuerySuccessPopup;

import java.util.ArrayList;


public class QueryFragment extends Fragment {

    View view;
    TextInputEditText name,desc;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Button send;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_query, container, false);
        name = view.findViewById(R.id.pname_et);
        desc = view.findViewById(R.id.pdesc_et);
        send = view.findViewById(R.id.send);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("REQUIRED");
        firebaseAuth = FirebaseAuth.getInstance();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    sendQuery();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public void sendQuery() throws Exception{
        if(firebaseAuth.getUid() == null){
            Toast.makeText(getContext(), "Internet not available", Toast.LENGTH_SHORT).show();
            return;
        }
        String pname = name.getText().toString().trim();
        String pdesc = desc.getText().toString().trim();

        if(pname.isEmpty()){
            name.setError("Cannot be empty");
            return;
        }else if(pdesc.isEmpty()){
            pdesc = "";
        }
        QueryData queryData = new QueryData();
        queryData.setName(pname);
        queryData.setDescription(pdesc);
        queryData.setShopkeepers(new ArrayList<String>());
        String qname = firebaseAuth.getUid()+System.currentTimeMillis();
        queryData.setUid(qname);
        databaseReference.child(qname).setValue(queryData);
        QuerySuccessPopup qp = new QuerySuccessPopup();
        qp.show(getActivity().getSupportFragmentManager(),"pop");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NavHostFragment.findNavController(QueryFragment.this).popBackStack();
            }
        },2500);
    }
}