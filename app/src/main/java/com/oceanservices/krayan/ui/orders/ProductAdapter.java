package com.oceanservices.krayan.ui.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.circularreveal.CircularRevealWidget;
import com.oceanservices.krayan.R;
import com.oceanservices.krayan.data.OrderData;
import com.oceanservices.krayan.data.ProductData;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.OrderViewHolder> {
    List<ProductData> list;
    Context context;

    public ProductAdapter(List<ProductData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_detail_card,parent,false);

        return new ProductAdapter.OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        ProductData productData = list.get(position);
        holder.name.setText(productData.getName());
        holder.count.setText(String.valueOf(productData.getCount()));
        holder.cost.setText(productData.getPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder{
        TextView name,count,cost;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.orderpname);
            count = itemView.findViewById(R.id.pqt);
            cost = itemView.findViewById(R.id.quantity);
        }
    }
}
