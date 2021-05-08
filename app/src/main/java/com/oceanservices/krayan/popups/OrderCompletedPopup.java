package com.oceanservices.krayan.popups;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oceanservices.krayan.LoginActivity;
import com.oceanservices.krayan.R;
import com.oceanservices.krayan.ui.orders.OrderAdapter;

import java.util.HashMap;
import java.util.Map;

public class OrderCompletedPopup extends DialogFragment {
    Button button;
    TextView textView;
    View view;
    String uid;
    Fragment fragment;
    public OrderCompletedPopup(String uid, Fragment fragment){
        this.uid = uid;
        this.fragment = fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ordercompletepopup, container, false);


        textView = view.findViewById(R.id.question);
        button = view.findViewById(R.id.delete);
        Dialog dialog = getDialog();
        //Bundle args = getArguments();
        //String text = args.getString("token","");
        //textView.setText(text);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completedOrder();
                NavHostFragment.findNavController(fragment).popBackStack();
                getDialog().dismiss();
            }
        });
        return view;
    }
    public void completedOrder(){
        if (uid == null || uid.isEmpty()){
            return;
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ORDERS")
                .child(uid);
        Map<String,Object> map = new HashMap<>();
        map.put("userCompleted",true);
        databaseReference.updateChildren(map);
    }
}
