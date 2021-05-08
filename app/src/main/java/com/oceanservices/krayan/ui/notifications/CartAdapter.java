package com.oceanservices.krayan.ui.notifications;

import android.app.NotificationManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.oceanservices.krayan.MainActivity;
import com.oceanservices.krayan.data.ProductData;
import com.oceanservices.krayan.databinding.CartCardBinding;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    Context context;
    //public static List<ProductData> list;
    Fragment fragment;
    CartCardBinding binding;
    LottieAnimationView lottieAnimationView;
    TextView amount;
    int TOTAL;
    int MAX;
    public CartAdapter(Context context, List<ProductData> list, Fragment fragment, LottieAnimationView lottieAnimationView, TextView amount) {
        this.context = context;
        //this.list = list;
        this.lottieAnimationView = lottieAnimationView;
        this.fragment = fragment;
        this.amount = amount;
    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = CartCardBinding.inflate(LayoutInflater.from(context),parent,false);

        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {
        final ProductData curr = MainActivity.productDataListl.get(position);
        MAX = 10;
        TOTAL = Integer.parseInt(curr.getPrice()) * curr.getCount();
        holder.setIsRecyclable(false);
        holder.binding.cartname.setText(curr.getName());
        holder.binding.cartamount.setText(curr.getCount()+"");
        holder.binding.cartcost.setText(String.format("\u20B9 %s", TOTAL));
        holder.binding.cartquantity.setText(curr.getQuantity());
        //holder.binding.cartimage.setImageResource(curr.getImage());
        Glide.with(context)
                .load(curr.getImage())
                .centerCrop()
                .into(holder.binding.cartimage);
        holder.binding.cartdecreaseamt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curr.getCount() > 1){
                    MainActivity.productDataListl.get(position).setCount(MainActivity.productDataListl.get(position).getCount() - 1);
                    TOTAL = Integer.parseInt(curr.getPrice()) * curr.getCount();
                    /*NotificationsFragment.total_amount = NotificationsFragment.total_amount - Integer.parseInt(curr.getPrice());
                    String amt = "\u20B9 "+NotificationsFragment.total_amount;
                    amount.setText(amt);*/
                    holder.binding.cartamount.setText(String.valueOf(curr.getCount()));
                    holder.binding.cartcost.setText(String.format("\u20B9 %s.00", TOTAL));
                }
                else if(curr.getCount() == 1){
                    MainActivity.productDataListl.remove(position);
                    /*NotificationsFragment.total_amount = NotificationsFragment.total_amount - Integer.parseInt(curr.getPrice());
                    String amt = "\u20B9 "+NotificationsFragment.total_amount;
                    amount.setText(amt);*/
                    //MainActivity.productDataListl.remove(position);
                    if(MainActivity.productDataListl.isEmpty()){
                        lottieAnimationView.setVisibility(View.VISIBLE);
                    }
                    CartAdapter.this.notifyDataSetChanged();
                    Toast.makeText(context, "The product was removed", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "Cannot order less than that!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.binding.cartaddbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(curr.getCount() != MAX){
                MainActivity.productDataListl.get(position).setCount(curr.getCount() + 1);
                TOTAL = Integer.parseInt(curr.getPrice()) * curr.getCount();
                holder.binding.cartamount.setText(String.valueOf(curr.getCount()));
                holder.binding.cartcost.setText(String.format("\u20B9 %s.00", TOTAL));
                /*NotificationsFragment.total_amount = NotificationsFragment.total_amount + Integer.parseInt(curr.getPrice());
                String amt = "\u20B9 "+NotificationsFragment.total_amount;
                amount.setText(amt);*/
            }
            else{
                Toast.makeText(context, "Cannot order more than that!!", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }
    @Override
    public int getItemCount() {
        return MainActivity.productDataListl.size();
    }
    public static class CartViewHolder extends RecyclerView.ViewHolder{
        CartCardBinding binding;
        public CartViewHolder(@NonNull CartCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }
}
