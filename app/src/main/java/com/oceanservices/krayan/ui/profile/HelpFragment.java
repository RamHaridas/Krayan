package com.oceanservices.krayan.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.oceanservices.krayan.R;


public class HelpFragment extends Fragment {

    View view;
    ImageView imageView;
    TextView apeksha,makrand,nupur,varun,lokesh,help,contact;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_help, container, false);
        initialize();
        callListeners();
        return view;
    }
    public void initialize(){
        imageView = view.findViewById(R.id.ocean_logo);
        apeksha = view.findViewById(R.id.apeksha);
        varun = view.findViewById(R.id.varun);
        lokesh = view.findViewById(R.id.lokesh);
        makrand = view.findViewById(R.id.makrand);
        nupur = view.findViewById(R.id.nupur);
        help = view.findViewById(R.id.help_email);
        contact = view.findViewById(R.id.contact_mail);
        try {
            Glide.with(getContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/grocery-a3260.appspot.com/o/KRAYN%2Foceans_logo.png?alt=media&token=547476cb-97c5-4df7-b152-9d13c807f0b1")
                    .centerCrop()
                    .into(imageView);
        }catch (Exception e){

        }
    }

    public void callListeners(){
        apeksha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+apeksha.getText()));
                startActivity(intent);
            }
        });
        varun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+varun.getText()));
                startActivity(intent);
            }
        });
        lokesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+lokesh.getText()));
                startActivity(intent);
            }
        });
        makrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+makrand.getText()));
                startActivity(intent);
            }
        });
        nupur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+nupur.getText()));
                startActivity(intent);
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"help@krayan.in"});
                i.putExtra(Intent.EXTRA_SUBJECT, "I need help");
                i.putExtra(Intent.EXTRA_TEXT   , "");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact@krayan.in"});
                i.putExtra(Intent.EXTRA_SUBJECT, "I need help");
                i.putExtra(Intent.EXTRA_TEXT   , "");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}