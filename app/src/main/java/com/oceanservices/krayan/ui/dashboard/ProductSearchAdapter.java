package com.oceanservices.krayan.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.oceanservices.krayan.R;
import com.oceanservices.krayan.data.ShopData;
import com.oceanservices.krayan.databinding.SearchProductCardBinding;

import java.util.List;

public class ProductSearchAdapter extends RecyclerView.Adapter<ProductSearchAdapter.ProductSearchViewHolder> {
    SearchProductCardBinding binding;

    List<ShopData> mList;
    Context mContext;
    Fragment fragment;

    public ProductSearchAdapter(List<ShopData> mList, Context mContext, Fragment fragment) {
        this.mList = mList;
        this.mContext = mContext;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ProductSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = SearchProductCardBinding.inflate(LayoutInflater.from(mContext),parent,false);

        return new ProductSearchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSearchViewHolder holder, int position) {
        final ShopData curr = mList.get(position);
        try {
            Glide.with(mContext)
                    .load(curr.getImage())
                    .centerCrop()
                    .into(holder.binding.sproductiv);
            binding.sproductshop.setText(curr.getName());
            binding.sproductstock.setText(curr.getAddress());
        }catch (Exception e){
            e.printStackTrace();
        }
        holder.binding.sproductrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("uid",curr.getUid());
                NavHostFragment.findNavController(fragment).navigate(R.id.action_navigation_dashboard_to_shopDetailsFragment,args);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ProductSearchViewHolder extends RecyclerView.ViewHolder{
        SearchProductCardBinding binding;
        public ProductSearchViewHolder(@NonNull SearchProductCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
