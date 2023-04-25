package com.example.forteatchers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    EditText etName,etPasswordR,etConformPassword,etEmail,etSurname;
    Button btnRegister;
    RadioButton rbTeacher, rbStudent;
    TextView tvAHA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        setTitle("Register");

        tvAHA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etName.getText().toString().isEmpty() && !etEmail.getText().toString().isEmpty()
                        && !etConformPassword.getText().toString().isEmpty() && !etPasswordR.getText().toString().isEmpty() &&
                !etSurname.getText().toString().isEmpty()){
                    if(etPasswordR.getText().toString().equals(etConformPassword.getText().toString())){
                        handleSignUp();
                    } else
                        Toast.makeText(RegisterActivity.this, "Password mismatch", Toast.LENGTH_SHORT).show();
                    
                } else
                    Toast.makeText(RegisterActivity.this, "field is empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void init(){
        etConformPassword = findViewById(R.id.etConformPassword);
        etEmail = findViewById(R.id.etEmail);
        etPasswordR = findViewById(R.id.etPasswordR);
        etName = findViewById(R.id.etName);
        btnRegister = findViewById(R.id.btnRegister);
        tvAHA = findViewById(R.id.tvAHA);
        etSurname = findViewById(R.id.etSurname);
        rbStudent = findViewById(R.id.rbStudent);
        rbTeacher = findViewById(R.id.rbTeacher);
    }

    void handleSignUp(){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(etEmail.getText().toString(),etPasswordR.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(rbStudent.isChecked()){
                        FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new User(etName.getText().toString(),etSurname.getText().toString(),etEmail.getText().toString(),"",false,
                                FirebaseAuth.getInstance().getCurrentUser().getUid()));
                    } else {
                        FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new User(etName.getText().toString(),etSurname.getText().toString(),etEmail.getText().toString(),"",true,
                                FirebaseAuth.getInstance().getCurrentUser().getUid()));
                    }
                    Toast.makeText(RegisterActivity.this, "Sign Up successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}