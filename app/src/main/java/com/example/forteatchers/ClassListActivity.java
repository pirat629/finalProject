package com.example.forteatchers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClassListActivity extends AppCompatActivity {
    Button btnAddUsers,btnAddUser,btnBack,btnSetHomework;
    CardView cardView;
    EditText etUserNameForAdd;
    ListView listView;
    ProgressBar progressBar;
    String id;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> listId;
    ArrayList<String> idUsers;
    ArrayList<String> ids;
    ArrayList<String> uriUser;
    ArrayList<String> name;
    String className;
    boolean isTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        init();
        id = getIntent().getStringExtra("id");
        className = getIntent().getStringExtra("className");
        setTitle(className);
        isTeacher = getIntent().getBooleanExtra("isTeacher",false);
        getClasses();
        progressBar.setVisibility(View.VISIBLE);
        btnSetHomework.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        btnAddUsers.setVisibility(View.GONE);
        cardView.setVisibility(View.GONE);
        
        
        btnAddUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSetHomework.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                btnAddUsers.setVisibility(View.GONE);
                cardView.setVisibility(View.VISIBLE);
                
            }
        });
        
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etUserNameForAdd.getText().toString();
                if(ids.contains(email)){
                    getIdUser();
                } else {
                    Toast.makeText(ClassListActivity.this, "user does not exist", Toast.LENGTH_SHORT).show();
                }
                
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etUserNameForAdd.setText("");
                listView.setVisibility(View.VISIBLE);
                btnSetHomework.setVisibility(View.VISIBLE);
                if(isTeacher){
                    btnAddUsers.setVisibility(View.VISIBLE);
                }
                cardView.setVisibility(View.GONE);
            }
        });

        btnSetHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ClassListActivity.this,HomeworkActivity.class).putExtra("id",id).putExtra("isTeacher",isTeacher));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(isTeacher){
                    getUriUser(idUsers.get(i),i);

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_item_profile){
            startActivity(new Intent(ClassListActivity.this,ProfileActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    void init(){
        btnSetHomework = findViewById(R.id.btnSetHomework);
        btnBack = findViewById(R.id.btnBack);
        etUserNameForAdd = findViewById(R.id.etUserNameForAdd);
        btnAddUser = findViewById(R.id.btnAddUser);
        cardView = findViewById(R.id.cardViewUser);
        btnAddUsers = findViewById(R.id.btnAddUsers);
        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar3);
        listId = new ArrayList<>();
        ids = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listId);
        idUsers = new ArrayList<>();
        listView.setAdapter(arrayAdapter);
        uriUser = new ArrayList<>();
        name = new ArrayList<>();
    }

    void getClasses(){
        FirebaseDatabase.getInstance().getReference("classes/" + id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(idUsers.size()>0)idUsers.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    idUsers.add(dataSnapshot.getValue(String.class));
                }
                getUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    void getUsers(){
        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listId.size()>0)listId.clear();
                if(ids.size()>0)ids.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    ids.add(user.email);
                    String id = user.id;
                    if(idUsers.contains(id)){
                        listId.add(user.name + " " + user.surname);
                    }
                }
                arrayAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                btnSetHomework.setVisibility(View.VISIBLE);
                if(isTeacher){
                    btnAddUsers.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void getIdUser(){
        FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if(user.email.equals(etUserNameForAdd.getText().toString())){
                        FirebaseDatabase.getInstance().getReference("users").child(user.id).child("classes").child(id).setValue(new Class(className,id));
                        FirebaseDatabase.getInstance().getReference("classes").child(id).push().setValue(user.id);
                        cardView.setVisibility(View.GONE);
                        etUserNameForAdd.setText("");
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void getUriUser(String idUser,int i){
        FirebaseDatabase.getInstance().getReference("homeworkInClass").child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    HomeworkInClass users = ds.getValue(HomeworkInClass.class);
                    uriUser.add(users.url);
                    name.add(users.name);
                }
                startActivity(new Intent(ClassListActivity.this,CheckHomeWorkActivity.class).putExtra("email",ids.get(i)).putExtra("uri",uriUser).putExtra("names",name));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}