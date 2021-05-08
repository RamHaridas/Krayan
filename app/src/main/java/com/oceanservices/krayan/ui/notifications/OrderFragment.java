package com.oceanservices.krayan.ui.notifications;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
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
import com.oceanservices.krayan.MainActivity;
import com.oceanservices.krayan.R;
import com.oceanservices.krayan.data.OrderData;
import com.oceanservices.krayan.data.ProductData;
import com.oceanservices.krayan.data.UserData;
import com.oceanservices.krayan.databinding.FragmentOrderBinding;
import com.oceanservices.krayan.popups.CheckoutPopup;
import com.oceanservices.krayan.ui.profile.ProfileFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderFragment extends Fragment {
    FragmentOrderBinding binding;
    List<ProductData> list;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    UserData userData;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderBinding.inflate(inflater,container,false);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ORDERS");
        //userData = new UserData();
        try {
            getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        binding.note.setVisibility(View.INVISIBLE);
        binding.finish.setEnabled(false);
        binding.finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userData == null){
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    return;
                }else if(MainActivity.productDataListl.isEmpty()){
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    placeOrder();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        list = MainActivity.productDataListl;
        if(getWeight() > 15000){
            binding.note.setVisibility(View.VISIBLE);
        }
        return binding.getRoot();
    }

    public void placeOrder() throws Exception{
        if(userData == null){
            Toast.makeText(getContext(), "Something went wrong, try again", Toast.LENGTH_SHORT).show();
            return;
        }
        OrderData orderData = new OrderData();
        orderData.setProductDataList(MainActivity.productDataListl);
        orderData.setDate(getDate());
        orderData.setTime(getTime());
        orderData.setDriver(false);
        orderData.setShop_name(NotificationsFragment.shopData.getName());
        orderData.setShop_uid(NotificationsFragment.shopData.getUid());
        orderData.setShopCompleted(false);
        orderData.setShopLat(NotificationsFragment.shopData.getLatitude());
        orderData.setShopLong(NotificationsFragment.shopData.getLongitude());
        orderData.setUserCompleted(false);
        orderData.setUser_name(userData.getFirst_name()+" "+userData.getLast_name());
        orderData.setUser_address1(userData.getAddress_line1());
        orderData.setUser_address2(userData.getAddress_line2());
        orderData.setUser_mobile(userData.getMobile());
        orderData.setUser_uid(firebaseAuth.getUid());
        orderData.setUserLat(userData.getLatitude());
        orderData.setUserLong(userData.getLongitude());
        orderData.setDelivery_charge(String.valueOf(deliveryCharges(userData)));
        String name = firebaseAuth.getUid()+System.currentTimeMillis();
        orderData.setUnique_id(name);
        binding.finish.setEnabled(false);
        databaseReference.child(name).setValue(orderData);
        MainActivity.productDataListl.clear();
        CheckoutPopup ck = new CheckoutPopup(OrderFragment.this);
        ck.show(getActivity().getSupportFragmentManager(), "cart");
    }
    public void getUserData() throws Exception{
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("USERS");
        dbref.child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData1 = dataSnapshot.getValue(UserData.class);
                setUserData(userData1);
                try {
                    setInitialData(userData1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                binding.yourname.setText(userData1.getFirst_name()+" "+userData1.getLast_name());
                binding.youraddress.setText(userData1.getAddress_line1()+", "+userData1.getAddress_line2());
                binding.yourcontact.setText(userData1.getMobile());
                binding.finish.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }


    public void setInitialData(UserData userData) throws Exception{
        int max = 0;
        int total = 0;
        for(ProductData p : MainActivity.productDataListl){
            max = max + p.getCount();
            int cost = p.getCount() * Integer.parseInt(p.getPrice());
            total = total + cost;
        }
        binding.noofitems.setText(String.valueOf(max));
        binding.ordercost.setText("₹ "+total);
        binding.deliverycost.setText("₹ "+deliveryCharges(userData));
        binding.totalcost.setText("₹ "+(deliveryCharges(userData)+total));
    }
    public int deliveryCharges(UserData userData){
        int amount = 0;
        Location user = new Location("");
        user.setLatitude(userData.getLatitude());
        user.setLongitude(userData.getLongitude());
        Location shop = new Location("");
        shop.setLatitude(NotificationsFragment.shopData.getLatitude());
        shop.setLongitude(NotificationsFragment.shopData.getLongitude());
        double distance = user.distanceTo(shop);

        if(distance <= 1500){
            amount = 15;
        }else if(distance > 1500 && distance < 6500){
            amount = 25;
        }else if(distance > 7500 && distance < 8500){
            amount = 45;
        }else if(distance > 9500 && distance < 10500){
            amount = 100;
        }else if (distance > 10500){
           amount = 100;
        }else{
            amount = 15;
        }

        return amount;
    }
    public String getDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return formatter.format(date);
    }
    public String getTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }
    public int getWeight(){
        int total = 0;
        for(ProductData p:MainActivity.productDataListl){
            total += p.getCount() * Integer.parseInt(p.getQuantity());
        }
        return total;
    }
}