package com.oceanservices.krayan.ui.orders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oceanservices.krayan.R;
import com.oceanservices.krayan.data.OrderData;

import java.util.ArrayList;
import java.util.List;


public class PreviousOrderFragment extends Fragment {

    RecyclerView recyclerView;
    TextView notice;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    List<OrderData> orderDataList;
    OrderAdapter orderAdapter;
    boolean first;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_previous_order, container, false);
        initialize();
        try {
            getOrders();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
    public void initialize(){
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ORDERS");
        orderDataList = new ArrayList<>();
        first = false;
        recyclerView = view.findViewById(R.id.previousrecyclerview);
        notice = view.findViewById(R.id.notice);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    public void getOrders() throws Exception {
        if (firebaseAuth.getUid() == null) {
            Toast.makeText(getContext(), "No internet access", Toast.LENGTH_SHORT).show();
            return;
        }
        databaseReference.orderByChild("user_uid").equalTo(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderDataList.clear();
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    OrderData orderData = post.getValue(OrderData.class);
                    if (orderData.isShopCompleted() && orderData.isUserCompleted()) {
                        orderDataList.add(orderData);
                    }
                    if (first) {
                        orderAdapter.notifyDataSetChanged();
                        recyclerView.scheduleLayoutAnimation();
                    } else {
                        orderAdapter = new OrderAdapter(getContext(),orderDataList,PreviousOrderFragment.this);
                        recyclerView.setAdapter(orderAdapter);
                        recyclerView.scheduleLayoutAnimation();
                        first = true;
                    }
                }
                if (orderDataList.isEmpty()) {
                    notice.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}