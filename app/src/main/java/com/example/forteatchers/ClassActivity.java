package com.example.forteatchers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClassActivity extends AppCompatActivity {
    ProgressBar progressBar;
    ListView lvClass;
    Button btnAddClass, btnAddClass2,buttonBack;
    EditText etClassName;
    CardView cardView;
    ArrayList<String> className;
    boolean isTeacher;
    ArrayList<String> id;

    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        init();
        Intent intent = getIntent();
        isTeacher = intent.getBooleanExtra("isTeacher",false);

        setTitle("classes");

        if(!isTeacher) btnAddClass.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.GONE);
        btnAddClass.setVisibility(View.GONE);
        lvClass.setVisibility(View.GONE);
        getDataFromDB();

        btnAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView.setVisibility(View.VISIBLE);
                btnAddClass.setVisibility(View.GONE);
                lvClass.setVisibility(View.GONE);
            }
        });

        btnAddClass2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String className = etClassName.getText().toString();
                String id = String.valueOf(UUID.randomUUID());
                FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("classes").child(id).setValue(new Class(className,id));
                FirebaseDatabase.getInstance().getReference("classes").child(id).push().setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                getDataFromDB();
                cardView.setVisibility(View.GONE);
                btnAddClass.setVisibility(View.VISIBLE);
                lvClass.setVisibility(View.VISIBLE);
                etClassName.setText("");
            }
        });
        lvClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ClassActivity.this,ClassListActivity.class).putExtra("id",id.get(i)).putExtra("className",list.get(i)).putExtra("isTeacher",isTeacher);
                startActivity(intent);
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView.setVisibility(View.GONE);
                if(isTeacher)btnAddClass.setVisibility(View.VISIBLE);
                lvClass.setVisibility(View.VISIBLE);
                etClassName.setText("");
            }
        });

    }

    void init(){
        buttonBack = findViewById(R.id.buttonBack);
        progressBar = findViewById(R.id.progressBar);
        lvClass = findViewById(R.id.lvClass);
        btnAddClass = findViewById(R.id.btnAddClass);
        btnAddClass2 = findViewById(R.id.btnAddClass2);
        etClassName = findViewById(R.id.etClassName);
        cardView = findViewById(R.id.CardView);
        className = new ArrayList<>();
        id = new ArrayList<>();
        list = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list);
        lvClass.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_item_profile){
            startActivity(new Intent(ClassActivity.this,ProfileActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    void getDataFromDB(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(list.size() > 0)list.clear();
                if(id.size()>0)id.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    Class clas = ds.getValue(Class.class);
                    assert clas != null;
                    list.add(clas.name);
                    id.add(clas.id);
                }
                arrayAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                if(isTeacher) btnAddClass.setVisibility(View.VISIBLE);
                lvClass.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/classes").addValueEventListener(valueEventListener);
    }


}