package com.oceanservices.krayan.ui.orders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oceanservices.krayan.R;
import com.oceanservices.krayan.data.OrderData;
import com.oceanservices.krayan.data.ProductData;
import com.oceanservices.krayan.data.ShopData;
import com.oceanservices.krayan.popups.OrderCompletedPopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewOrderFragment extends Fragment {

    ProductAdapter productAdapter;
    List<ProductData> productDataList;
    Double myLatitude = 0.0;
    Double myLongitude = 0.0;
    String labelLocation = "address";
    TextView username,datetime,mobile,address,cost;
    Button nav,completed;
    View view;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_ordder, container, false);
        initialize();
        try {
            setData();
            getShopData(OrderAdapter.static_orderData.getShop_uid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + myLatitude  + ">,<" + myLongitude + ">?q=<" + myLatitude  + ">,<" + myLongitude + ">(" + labelLocation + ")"));
                startActivity(intent);
            }
        });
        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderCompletedPopup ord = new OrderCompletedPopup(OrderAdapter.static_orderData.getUnique_id(),ViewOrderFragment.this);
                ord.show(getActivity().getSupportFragmentManager(),"completed");
            }
        });
        return view;
    }
    public void initialize(){
        productDataList = new ArrayList<>();
        nav = view.findViewById(R.id.nav);
        address = view.findViewById(R.id.address);
        username = view.findViewById(R.id.uname);
        datetime = view.findViewById(R.id.timeanddate);
        mobile = view.findViewById(R.id.contact);
        cost = view.findViewById(R.id.amount);
        recyclerView = view.findViewById(R.id.viewOrdersRecycle);
        completed = view.findViewById(R.id.completeys);
        completed.setEnabled(false);
    }
    public void setData() throws Exception{
        int total = 0;
        if(OrderAdapter.static_orderData == null){
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            return;
        }
        username.setText(OrderAdapter.static_orderData.getShop_name());
        datetime.setText(OrderAdapter.static_orderData.getDate()+" "+OrderAdapter.static_orderData.getTime());
        myLatitude = OrderAdapter.static_orderData.getShopLat();
        myLongitude = OrderAdapter.static_orderData.getShopLong();
        if(OrderAdapter.static_orderData.getUnique_id() != null && !OrderAdapter.static_orderData.isUserCompleted()){
            completed.setEnabled(true);
        }
        if(OrderAdapter.static_orderData.isUserCompleted() && OrderAdapter.static_orderData.isShopCompleted()){
            completed.setEnabled(false);
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ProductAdapter(OrderAdapter.static_orderData.getProductDataList(),getContext()));
        for(ProductData p : OrderAdapter.static_orderData.getProductDataList()){
            total = total + p.getCount() * Integer.parseInt(p.getPrice());
        }
        int t = Integer.parseInt(OrderAdapter.static_orderData.getDelivery_charge()) + total;
        cost.setText("\u20B9 "+t);
    }

    public void getShopData(String uid) throws Exception{
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPERS");
        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ShopData shopData = dataSnapshot.getValue(ShopData.class);
                address.setText(shopData.getAddress());
                mobile.setText(shopData.getNumber());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}