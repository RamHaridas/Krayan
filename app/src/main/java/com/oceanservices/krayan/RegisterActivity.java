package com.oceanservices.krayan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oceanservices.krayan.GPS.GpsUtils;
import com.oceanservices.krayan.data.UserData;
import com.oceanservices.krayan.databinding.ActivityRegisterBinding;
import com.oceanservices.krayan.popups.RegisterSucessPopup;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    private static final int MULTIPLE_PERMISSIONS = 10; // code you want.
    private String[] permissions= new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    double[] loc = {0.0,0.0};
    boolean gps = false;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String first_name,last_name,address_line1,address_line2,email,password,mobile,macAddress,uid,code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkPermissions();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USERS");
        binding.reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                try {
                    register();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        listener();
    }

    public void register() throws Exception{

        first_name = binding.fnameEt.getText().toString().trim();
        last_name = binding.lnameEt.getText().toString().trim();
        address_line1 = binding.address.getText().toString().trim();
        address_line2 = binding.address2.getText().toString().trim();
        email = binding.emailEt.getText().toString().trim();
        mobile = binding.numberEt.getText().toString().trim();
        password = binding.passEt.getText().toString().trim();
        code = binding.codeEt.getText().toString().trim();
        String confirm = binding.cpassEt.getText().toString().trim();
        macAddress = getWifiMacAddress();

        if(first_name.isEmpty()){
            binding.fnameEt.setError("Cannot be empty");
            return;
        }else if(last_name.isEmpty()){
            binding.lnameEt.setError("Cannot be empty");
            return;
        }else if(address_line1.isEmpty()){
            binding.address.setError("Cannot be empty");
            return;
        }else if(address_line2.isEmpty()){
            binding.address2.setError("Cannot be empty");
            return;
        }else if(!isEmailValid(email)){
            binding.emailEt.setError("Invalid Format");
            return;
        }else if(mobile.isEmpty()){
            binding.numberEt.setError("Cannot be empty");
            return;
        }else if(password.isEmpty()){
            binding.passEt.setError("Cannot be empty");
            return;
        }else if(!confirm.equals(password)){
            binding.cpassEt.setError("Does not match!");
            return;
        }else if(loc[0] == 0.0 || loc[1] == 0.0){
            Toast.makeText(this, "Please add your location first!", Toast.LENGTH_SHORT).show();
            return;
        }else if(password.length() < 6){
            Toast.makeText(this, "Length of password should be greater than 6", Toast.LENGTH_SHORT).show();
            return;
        }else if(code == null || code.isEmpty()){
            code = "0000";
        }else if(!binding.terms.isChecked()){
            Toast.makeText(this, "Please accept terms and conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                UserData userData = new UserData();
                userData.setFirst_name(first_name);
                userData.setLast_name(last_name);
                userData.setAddress_line1(address_line1);
                userData.setAddress_line2(address_line2);
                userData.setLatitude(loc[0]);
                userData.setLongitude(loc[1]);
                userData.setEmail(email);
                userData.setMacAddress(macAddress);
                userData.setMobile(mobile);
                userData.setPassword(password);
                userData.setCode(code);
                userData.setUid(firebaseAuth.getUid());
                try {
                    databaseReference.child(firebaseAuth.getUid()).setValue(userData);
                    RegisterSucessPopup rg = new RegisterSucessPopup();
                    rg.show(getSupportFragmentManager(),"success");
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void openMap(View v){
        startActivityForResult(new Intent(RegisterActivity.this,AddLocationActivity.class),31);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 31 && resultCode == 31){
            loc = data.getDoubleArrayExtra("location");
            String text = loc[0]+","+loc[1];
            binding.location.setText(text);
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
                /*Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);*/
                if(!gps){
                    new GpsUtils(RegisterActivity.this).turnGPSOn(new GpsUtils.onGpsListener() {
                        @Override
                        public void gpsStatus(boolean isGPSEnable) {
                            gps = isGPSEnable;
                        }
                    });
                }
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    public static String getWifiMacAddress() {
        try {
            String interfaceName = "wlan0";
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (!intf.getName().equalsIgnoreCase(interfaceName)){
                    continue;
                }

                byte[] mac = intf.getHardwareAddress();
                if (mac==null){
                    return "";
                }

                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) {
                    buf.append(String.format("%02X:", aMac));
                }
                if (buf.length()>0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                return buf.toString();
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

    public void listener(){
        binding.readTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("https://www.krayan.in/krayan_terms&conditions/"); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}