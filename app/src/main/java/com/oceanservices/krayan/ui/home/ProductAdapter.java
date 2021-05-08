package com.oceanservices.krayan.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.oceanservices.krayan.R;
import com.oceanservices.krayan.data.ProductData;
import com.oceanservices.krayan.databinding.ProductcardBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    List<ProductData> mList;
    Context mContext;
    ProductcardBinding binding;
    NavController mnavcont;
    public static ProductData productData;
    public ProductAdapter(List<ProductData> mList, Context mContext, NavController mnavcont) {
        this.mList = mList;
        this.mContext = mContext;
        this.mnavcont = mnavcont;
        productData = new ProductData();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=ProductcardBinding.inflate(LayoutInflater.from(mContext),parent,false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        final ProductData currItem=mList.get(position);
        //holder.binding.productiv.setImageResource(currItem.getImage());
        try {
            Glide.with(mContext)
                    .load(currItem.getImage())
                    .centerCrop()
                    .into(holder.binding.productiv);
            holder.binding.productname.setText(currItem.getName().toUpperCase());
            holder.binding.productcost.setText("\u20B9"+currItem.getPrice());
            holder.binding.productstock.setText(currItem.isStock() ? "In Stock" : "Out of stock");
        }catch (Exception e){}
        holder.binding.productrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productData = currItem;
                mnavcont.navigate(R.id.action_shopDetailsFragment_to_productDetailsFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder{
        ProductcardBinding binding;
        public ProductViewHolder(@NonNull ProductcardBinding b) {
            super(b.getRoot());
            binding=b;
        }
    }
    public void filterList(ArrayList<ProductData> arr){
        mList = arr;
        notifyDataSetChanged();
    }
}
