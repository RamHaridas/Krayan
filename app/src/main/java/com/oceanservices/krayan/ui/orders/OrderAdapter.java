package com.oceanservices.krayan.ui.orders;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.oceanservices.krayan.R;
import com.oceanservices.krayan.data.OrderData;

import java.util.List;

public class OrderAdapter  extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{

    public static OrderData static_orderData;
    List<OrderData> list;
    Context context;
    Fragment fragment;
    public OrderAdapter(Context context, List<OrderData> list, Fragment fragment){
        this.context = context;
        this.list = list;
        this.fragment = fragment;
    }
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_card,parent,false);

        return new OrderAdapter.OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder,  int position) {
        final OrderData orderData = list.get(position);
        holder.name.setText(orderData.getShop_name());
        holder.date.setText(orderData.getDate());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                static_orderData = orderData;
                if(fragment instanceof CurrentOrderFragment){
                    NavHostFragment.findNavController(fragment).navigate(R.id.action_nav_current_orders_to_nav_view_order);
                }else if(fragment instanceof PreviousOrderFragment){
                    NavHostFragment.findNavController(fragment).navigate(R.id.action_nav_previous_to_nav_view_order);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder{
        TextView name, date;
        Button button;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tnd);
            date = itemView.findViewById(R.id.ordername);
            button = itemView.findViewById(R.id.viewbt);
        }
    }
}
