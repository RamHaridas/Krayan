package com.oceanservices.krayan.ui.home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oceanservices.krayan.R;
import com.oceanservices.krayan.data.ProductData;
import com.oceanservices.krayan.data.ShopData;
import com.oceanservices.krayan.databinding.FragmentShopDetailsBinding;

import java.util.ArrayList;
import java.util.List;

public class ShopDetailsFragment extends Fragment {
    FragmentShopDetailsBinding binding;
    List<ProductData> productlist;
    NavController navcont;
    ProductAdapter productAdapter;
    ShopData shopData;
    DatabaseReference databaseReference,dref;
    boolean first;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShopDetailsBinding.inflate(inflater,container,false);
        navcont = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        productlist = new ArrayList<>();
        //shopData = HomeAdapter.shopData;
        Bundle args = getArguments();
        String uid = args.getString("uid","");
        Log.i("UID786", uid);
        first = false;
        binding.progressAnim.setVisibility(View.VISIBLE);
        binding.notice.setVisibility(View.INVISIBLE);
        try {
            log(1);
            dref = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPERS");
            databaseReference = FirebaseDatabase.getInstance().getReference().child("SHOP_DATA").child(uid);
            getShops(uid);
            //binding.sdshopname.setText(shopData.getName());
            //binding.sdaddress.setText(shopData.getAddress());
        }catch (Exception e){
            log(2);
            e.printStackTrace();
            Toast.makeText(getContext(),"Something went wrong", Toast.LENGTH_SHORT).show();
        }
        binding.sdsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        binding.sdrecycle.setHasFixedSize(true);
        binding.sdrecycle.setLayoutManager(new GridLayoutManager(getContext(),2));

        return binding.getRoot();
    }

    class ProductThread implements Runnable{
        String uid;
        Context context;
        public ProductThread(Context context , String uid){
            this.context = context;
            this.uid = uid;
        }
        @Override
        public void run() {
            if(uid == null || uid.isEmpty()){
                log(6);
                return;
            }
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    productlist.clear();
                    log(7);
                    for(DataSnapshot post : dataSnapshot.getChildren()){
                        ProductData productData = post.getValue(ProductData.class);
                        productlist.add(productData);
                        log(8);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if(first){
                                    log(9);
                                    productAdapter.notifyDataSetChanged();
                                    //binding.sdrecycle.scheduleLayoutAnimation();
                                }else{
                                    log(10);
                                    binding.progressAnim.setVisibility(View.INVISIBLE);
                                    productAdapter = new ProductAdapter(productlist,getContext(),navcont);
                                    binding.sdrecycle.setAdapter(productAdapter);
                                    //binding.sdrecycle.scheduleLayoutAnimation();
                                }
                            }
                        });
                    }
                    if(productlist.isEmpty()){
                        log(11);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                binding.notice.setVisibility(View.VISIBLE);
                                binding.progressAnim.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    private void filter(String text){
        ArrayList<ProductData> arr = new ArrayList<ProductData>();
        for(ProductData s : productlist){
            if(s.getName().toLowerCase().contains(text.toLowerCase())){
                arr.add(s);
            }
        }
        productAdapter.filterList(arr);
    }
    public void getShops(String text) throws Exception{
        log(3);
        dref.child(text).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ShopData shopData1 = dataSnapshot.getValue(ShopData.class);
                log(4);
                binding.sdshopname.setText(shopData1.getName());
                Log.i("NAME", shopData1.getName());
                binding.sdaddress.setText(shopData1.getAddress());
                log(5);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ProductThread productThread = new ProductThread(getContext(),text);
        new Thread(productThread).start();
    }
    public void log(int i){
        Log.i("Reached here", String.valueOf(i));
    }
}