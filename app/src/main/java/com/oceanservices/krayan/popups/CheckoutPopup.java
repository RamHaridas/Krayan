package com.oceanservices.krayan.popups;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.oceanservices.krayan.MainActivity;
import com.oceanservices.krayan.R;
import com.oceanservices.krayan.ui.notifications.OrderFragment;

public class CheckoutPopup extends DialogFragment {
    View view;
    Fragment fragment;
    public CheckoutPopup(Fragment fragment){
        this.fragment = fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.cart_checkout_popup, container, false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NavHostFragment.findNavController(fragment).navigate(R.id.action_orderFragment_to_navigation_home);
                getDialog().dismiss();
            }
        },2500);
        return view;
    }
}
