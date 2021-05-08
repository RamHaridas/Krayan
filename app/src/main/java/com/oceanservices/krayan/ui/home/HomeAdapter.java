package com.oceanservices.krayan.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.oceanservices.krayan.R;
import com.oceanservices.krayan.data.ProductData;
import com.oceanservices.krayan.data.ShopData;
import com.oceanservices.krayan.databinding.HomecardBinding;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    List<ShopData> mList;
    Context mContext;
    HomecardBinding binding;
    NavController mnavcont;
    public static  ShopData shopData;
    public HomeAdapter(List<ShopData> mList, Context mContext,NavController mnavcont) {
        this.mList = mList;
        this.mContext = mContext;
        this.mnavcont=mnavcont;
        shopData = new ShopData();
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = HomecardBinding.inflate(LayoutInflater.from(mContext),parent,false);
        return new HomeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        final ShopData currItem = mList.get(position);
        //holder.binding.cardiv.setImageResource(currItem.getImage());
        try {
            Glide.with(mContext)
                    .load(currItem.getImage())
                    .centerCrop()
                    .into(holder.binding.cardiv);
            holder.binding.shopnametv.setText(currItem.getName());
            holder.binding.address.setText(currItem.getAddress());
        }catch (Exception e){
            e.printStackTrace();
        }
        holder.binding.cardrv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick:"+currItem.getUid());
                shopData = currItem;
                Bundle args = new Bundle();
                try {
                    args.putString("uid", currItem.getUid());
                    mnavcont.navigate(R.id.action_navigation_home_to_shopDetailsFragment,args);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class HomeViewHolder extends RecyclerView.ViewHolder{
        HomecardBinding binding;
        public HomeViewHolder(@NonNull HomecardBinding b) {
            super(b.getRoot());
            binding=b;
        }
    }
    public void filterList(ArrayList<ShopData> arr){
        mList = arr;
        notifyDataSetChanged();
    }
}
