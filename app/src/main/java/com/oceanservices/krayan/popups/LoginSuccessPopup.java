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

import com.oceanservices.krayan.LoginActivity;
import com.oceanservices.krayan.MainActivity;
import com.oceanservices.krayan.R;

public class LoginSuccessPopup extends DialogFragment {

    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.login_success, container, false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        },2500);
        return view;
    }
}
