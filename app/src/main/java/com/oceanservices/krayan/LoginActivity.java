package com.oceanservices.krayan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oceanservices.krayan.data.ShopData;
import com.oceanservices.krayan.databinding.ActivityLoginBinding;
import com.oceanservices.krayan.popups.LoginSuccessPopup;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        isUser = false;
        sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        firebaseAuth = FirebaseAuth.getInstance();
        binding.loginbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(LoginActivity.this,MainActivity.class));
                try {
                    loginUser();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        setContentView(binding.getRoot());
        textListener();
        if(firebaseAuth.getUid() == null){
            Toast.makeText(this, "No internet access", Toast.LENGTH_SHORT).show();
        }
    }

    public void loginUser() throws Exception{

        final String email = binding.emailloginEt.getText().toString().trim();
        String password = binding.passloginEt.getText().toString().trim();

        if(email.isEmpty()){
            binding.emailloginEt.setError("Cannot be empty");
            return;
        }else if(password.isEmpty()){
            binding.passloginEt.setError("Cannot be empty");
            return;
        }else if(!isUser()){
            binding.emailloginEt.setError("Invalid User");
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    editor.putBoolean("login",true);
                    editor.apply();
                    editor.putBoolean("first",false);
                    editor.apply();
                    LoginSuccessPopup lg = new LoginSuccessPopup();
                    lg.show(getSupportFragmentManager(),"login");
                }else{
                    Toast.makeText(LoginActivity.this, "Invalid User", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public void checkUser(String email){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USERS");
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ShopData shopData = dataSnapshot.getValue(ShopData.class);
                if(shopData == null){
                    setUser(false);
                }else{
                    setUser(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Log.i("USER", String.valueOf(isUser));
    }

    public void textListener(){
        binding.emailloginEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkUser(s.toString());
            }
        });
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    public boolean isUser() {
        return isUser;
    }
}