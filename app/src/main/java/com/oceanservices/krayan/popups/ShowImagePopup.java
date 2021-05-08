package com.oceanservices.krayan.popups;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.oceanservices.krayan.MainActivity;
import com.oceanservices.krayan.R;

public class ShowImagePopup extends DialogFragment {

    View view;
    String link;
    ImageView imageView;
    public ShowImagePopup(String link) {
        this.link = link;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.showimagepopup, container, false);
        imageView = view.findViewById(R.id.image);
        try {
            Glide.with(getContext())
                    .load(link)
                    .centerCrop()
                    .into(imageView);
        }catch (Exception e){
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        return view;
    }
}
