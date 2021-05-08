package com.oceanservices.krayan.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.oceanservices.krayan.MainActivity;
import com.oceanservices.krayan.R;
import com.oceanservices.krayan.data.ProductData;
import com.oceanservices.krayan.databinding.FragmentProductDetailsBinding;
import com.oceanservices.krayan.popups.ShowImagePopup;

import static android.content.ContentValues.TAG;

public class ProductDetailsFragment extends Fragment {
    FragmentProductDetailsBinding binding;
    NavController navcont;
    int MAX;
    int count;
    ProductData productData;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        navcont = NavHostFragment.findNavController(this);
        binding = FragmentProductDetailsBinding.inflate(inflater,container,false);
        count = 0;
        MAX = 10;
        productData = ProductAdapter.productData;
        if(check(productData)){
            binding.addbt.setVisibility(View.GONE);
            binding.ad.setVisibility(View.VISIBLE);
        }
        if(productData.isStock()){
            binding.addbt.setEnabled(true);
            binding.stock.setAnimation(R.raw.krayan_in_stock);
            binding.txt.setVisibility(View.VISIBLE);
        }else{
            binding.txt.setText("OUT OF STOCK");
            binding.txt.setVisibility(View.VISIBLE);
            binding.addbt.setEnabled(false);
            binding.stock.setAnimation(R.raw.krayan_out_of_stock);
        }
        binding.addbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.productDataListl.isEmpty()){
                    binding.stock.setVisibility(View.INVISIBLE);
                    binding.addbt.setVisibility(View.GONE);
                    binding.ad.setVisibility(View.VISIBLE);
                    productData.setCount(1);
                    MainActivity.productDataListl.add(productData);
                }else{
                    if(productData.getShop_uid().equals(MainActivity.productDataListl.get(0).getShop_uid())){
                        binding.stock.setVisibility(View.INVISIBLE);
                        binding.addbt.setVisibility(View.GONE);
                        binding.ad.setVisibility(View.VISIBLE);
                        productData.setCount(1);
                        MainActivity.productDataListl.add(productData);
                    }else{
                        Toast.makeText(getContext(), "You cannot add products of different shops in one cart", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        try {
            binding.pname.setText(productData.getName());
            binding.pcost.setText(productData.getPrice());
            binding.pquantity.setText(productData.getQuantity());
            Glide.with(getContext())
                    .load(productData.getImage())
                    .centerCrop()
                    .into(binding.pimage);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
       /* binding.decreaseamt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count != 0){
                    count = count - 1;
                    binding.amount.setText(String.valueOf(count));
                }
                else{
                    Toast.makeText(getContext(), "Cannot order less than that!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.increaseamt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count != MAX){
                    count = count + 1;
                    binding.amount.setText(String.valueOf(count));
                }
                else{
                    Toast.makeText(getContext(), "Cannot order more than that!!", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
       binding.pimage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(productData.getImage() == null){
                   Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                   return;
               }
               ShowImagePopup sp = new ShowImagePopup(productData.getImage());
               sp.show(getActivity().getSupportFragmentManager(),"image");
           }
       });
        return binding.getRoot();
    }

    public boolean check(ProductData productData){
        for(ProductData p: MainActivity.productDataListl){
            if(p.getImage().equals(productData.getImage())){
                return true;
            }
        }
        return false;
    }
}