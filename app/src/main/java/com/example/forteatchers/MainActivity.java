package com.example.forteatchers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    EditText etName, etLName, etPass, etPhone, etLogin;
    Button btnEnter, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String name = etName.getText().toString();
//                String lName = etLName.getText().toString();
//                String phone = etPhone.getText().toString();
//                String login = etLogin.getText().toString();
//                String pass = etPass.getText().toString();
//                User user = new User(name,lName,phone,login,pass);
//                mDatabase.child("teachers").child(login).setValue(user);
                
            }
        });

    }
    void init(){
//        etName = findViewById(R.id.etName);
//        etLName = findViewById(R.id.etLName);
//        etLogin = findViewById(R.id.etLogin);
//        etPass = findViewById(R.id.etPass);
//        etPhone = findViewById(R.id.etPhone);
//        btnRegister = findViewById(R.id.btnRegister);
//        btnEnter = findViewById(R.id.btnEnter);
//        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
}