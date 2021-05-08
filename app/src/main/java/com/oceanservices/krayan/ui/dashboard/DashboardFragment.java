package com.oceanservices.krayan.ui.dashboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oceanservices.krayan.MainActivity;
import com.oceanservices.krayan.R;
import com.oceanservices.krayan.data.ProductData;
import com.oceanservices.krayan.data.ShopData;
import com.oceanservices.krayan.databinding.FragmentDashboardBinding;
import com.oceanservices.krayan.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DashboardFragment extends Fragment {

    FragmentDashboardBinding binding;
    ProductSearchAdapter searchAdapter;
    DatabaseReference databaseReference,dref;
    List<String> suggestion;
    ArrayAdapter<String> dataAdapter;
    androidx.appcompat.widget.SearchView.SearchAutoComplete searchAutoComplete;
    boolean pehla;
    boolean first;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater,container,false);
        binding.animationView.setVisibility(View.VISIBLE);
        first = false;
        pehla = false;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ALL_PRODUCTS");
        dref = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPERS");
        suggestion = new ArrayList<>();
        searchAutoComplete = binding.productsearch.findViewById(androidx.appcompat.R.id.search_src_text);
        //binding.productsearch.setIconified(false);
        try {
            searchAutoComplete.setThreshold(1);
        }catch (Exception e){}
        searchAutoComplete.setEnabled(true);
        binding.psrecycle.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.psrecycle.setHasFixedSize(false);
        binding.productsearch.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    getData(newText.toLowerCase());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        onSuggestion();
        listener();
        SearchThread searchThread = new SearchThread();
        new Thread(searchThread).start();
        return binding.getRoot();
    }

    public void getData(String text) throws Exception{
        databaseReference.orderByChild("name").equalTo(text).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MainActivity.shopDataList.clear();
                for(DataSnapshot post: dataSnapshot.getChildren()){
                    //binding.progress.setVisibility(View.VISIBLE);
                    ProductData p = post.getValue(ProductData.class);
                    //Log.i("UID", "onDataChange:"+p.getShop_uid());
                    try {
                        getShops(p.getShop_uid());
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(first){
                        searchAdapter.notifyDataSetChanged();
                    }else{
                        binding.progress.setVisibility(View.INVISIBLE);
                        binding.animationView.setVisibility(View.INVISIBLE);
                        searchAdapter = new ProductSearchAdapter(MainActivity.shopDataList,getContext(),DashboardFragment.this);
                        binding.psrecycle.setAdapter(searchAdapter);
                        first = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                binding.progress.setVisibility(View.INVISIBLE);
            }
        });
    }
    public void getShops(String text) throws Exception{
        dref.child(text).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ShopData shopData1 = dataSnapshot.getValue(ShopData.class);
                shopData1.setUid(dataSnapshot.getKey());
                MainActivity.shopDataList.add(shopData1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                binding.progress.setVisibility(View.INVISIBLE);
            }
        });
    }

    class SearchThread implements Runnable{

        @Override
        public void run() {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   for(DataSnapshot post : dataSnapshot.getChildren()){
                       ProductData productData = post.getValue(ProductData.class);
                       try{
                           if(!suggestion.contains(productData.getName().toLowerCase())){
                               suggestion.add(productData.getName().toLowerCase());
                           }
                       }catch (Exception e){}
                       new Handler(Looper.getMainLooper()).post(new Runnable() {
                           @Override
                           public void run() {
                               if(pehla){
                                   dataAdapter.notifyDataSetChanged();
                               }else{
                                   dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, suggestion);
                                   searchAutoComplete.setAdapter(dataAdapter);
                                   pehla = true;
                               }
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
    public void onSuggestion() {
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = parent.getItemAtPosition(position);
                hideKeyboard(getActivity());
                try {
                    getData(o.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.productsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.productsearch.setIconified(false);
            }
        });
    }
    //to close keyboard
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void listener(){
        binding.querypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(DashboardFragment.this).navigate(R.id.action_navigation_dashboard_to_nav_query);
            }
        });
    }
}