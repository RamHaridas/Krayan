package com.oceanservices.krayan.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oceanservices.krayan.MainActivity;
import com.oceanservices.krayan.R;
import com.oceanservices.krayan.data.ProductData;
import com.oceanservices.krayan.data.ShopData;
import com.oceanservices.krayan.databinding.FragmentNotificationsBinding;
import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {
    FragmentNotificationsBinding binding;
    //public static List<ProductData> mList;
    CartAdapter cartAdapter;
    DatabaseReference databaseReference;
    public static int total_amount = 0;
    public static ShopData shopData;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentNotificationsBinding.inflate(inflater,container,false);
        //mList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPERS");
        //mList = MainActivity.productDataListl;
        binding.animationView.setVisibility(View.INVISIBLE);
        if (MainActivity.productDataListl.isEmpty()){
            binding.animationView.setVisibility(View.VISIBLE);
        }
        cartAdapter = new CartAdapter(getContext(),MainActivity.productDataListl,this,binding.animationView,binding.amount);
        binding.cartrecycle.setHasFixedSize(false);
        binding.cartrecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.cartrecycle.setAdapter(cartAdapter);
        binding.ordernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.productDataListl.isEmpty()){
                    Toast.makeText(getContext(), "Cart is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                NavHostFragment.findNavController(NotificationsFragment.this).navigate(R.id.action_navigation_notifications_to_orderFragment);
            }
        });
        try{
            getData(MainActivity.productDataListl.get(0).getShop_uid());
        }catch (Exception e){

        }
        return binding.getRoot();
    }
    public void getData(String name) throws Exception{
        databaseReference.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shopData = dataSnapshot.getValue(ShopData.class);
                shopData.setUid(dataSnapshot.getKey());
                binding.cartshopname.setText(shopData.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}