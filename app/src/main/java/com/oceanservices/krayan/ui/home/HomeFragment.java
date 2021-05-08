package com.oceanservices.krayan.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oceanservices.krayan.R;
import com.oceanservices.krayan.data.ProductData;
import com.oceanservices.krayan.data.ShopData;
import com.oceanservices.krayan.databinding.FragmentHomeBinding;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    List<ShopData> homeList;
    NavController navcont;
    FirebaseAuth firebaseAuth;
    HomeAdapter adapter;
    DatabaseReference databaseReference;
    boolean first;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        firebaseAuth = FirebaseAuth.getInstance();
        first = false;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPERS");
        navcont = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        homeList = new ArrayList<>();
        binding.progressAnim.setVisibility(View.VISIBLE);
        binding.homesearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        binding.homerecycle.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.homerecycle.setHasFixedSize(true);
        MyShopThread thread = new MyShopThread(getContext());
        new Thread(thread).start();
        return binding.getRoot();
    }

    class MyShopThread implements Runnable{
        Context context;
        public MyShopThread(Context context){
            this.context = context;
        }
        @Override
        public void run() {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot post : dataSnapshot.getChildren()){
                        ShopData shopData = post.getValue(ShopData.class);
                        try {
                            shopData.setUid(post.getKey());
                        }catch (Exception r){}
                        homeList.add(shopData);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if(first){
                                    adapter.notifyDataSetChanged();
                                    binding.homerecycle.scheduleLayoutAnimation();
                                }else{
                                    binding.progressAnim.setVisibility(View.INVISIBLE);
                                    adapter = new HomeAdapter(homeList,getContext(),navcont);
                                    binding.homerecycle.setAdapter(adapter);
                                    binding.homerecycle.scheduleLayoutAnimation();
                                    first = true;
                                }
                            }
                        });
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if(homeList.isEmpty()){
                                binding.notice.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }
        });
    }
    private void filter(String text){
        ArrayList<ShopData> arr = new ArrayList<ShopData>();
        for(ShopData s : homeList){
            if(s.getName().toLowerCase().contains(text.toLowerCase())){
                arr.add(s);
            }
        }
        try {
            adapter.filterList(arr);
        }catch (Exception e){}
    }
}