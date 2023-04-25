package com.example.forteatchers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class HomeworkActivity extends AppCompatActivity {
    Button btnAddHomework,btnYes,btnNo;
    EditText fileName;
    ListView homeworkList;
    CardView cardViewHomework;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> list;
    Uri file;
    String id;
    ArrayList<String> homeworkURI;
    boolean isTeacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        init();
        setTitle("Homework");
        cardViewHomework.setVisibility(View.GONE);
        btnAddHomework.setVisibility(View.VISIBLE);
        homeworkList.setVisibility(View.VISIBLE);
        id = getIntent().getStringExtra("id");
        isTeacher = getIntent().getBooleanExtra("isTeacher",false);
        getHomeWork();
        if(isTeacher) {
            btnAddHomework.setText("add homework");
        } else {
            btnAddHomework.setText("sent homework");
        }
        btnAddHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 1);
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileName.setText("");
                cardViewHomework.setVisibility(View.GONE);
                btnAddHomework.setVisibility(View.VISIBLE);
                homeworkList.setVisibility(View.VISIBLE);
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
                cardViewHomework.setVisibility(View.GONE);
                btnAddHomework.setVisibility(View.VISIBLE);
                homeworkList.setVisibility(View.VISIBLE);
            }
        });

        homeworkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(homeworkURI.get(i)));
                String title = list.get(i);
                request.setTitle(title);
                request.setDescription("Downloading file");
                String cookie = CookieManager.getInstance().getCookie(homeworkURI.get(i));
                request.addRequestHeader("cookie",cookie);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title);

                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                downloadManager.enqueue(request);

                Toast.makeText(HomeworkActivity.this, "Downloading started", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            if(data!=null){
                file = data.getData();
                cardViewHomework.setVisibility(View.VISIBLE);
                btnAddHomework.setVisibility(View.GONE);
                homeworkList.setVisibility(View.GONE);
            }
        }
    }

    void init(){
        homeworkList = findViewById(R.id.homeworkList);
        list = new ArrayList<>();
        homeworkURI = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list);
        homeworkList.setAdapter(arrayAdapter);
        btnAddHomework  = findViewById(R.id.btnAddHomework);
        btnNo = findViewById(R.id.btnNo);
        btnYes = findViewById(R.id.btnYes);
        cardViewHomework = findViewById(R.id.cardViewHomework);
        fileName = findViewById(R.id.etFileName);
    }

    void uploadFile(){
        ProgressDialog progressDialog = new ProgressDialog(HomeworkActivity.this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        FirebaseStorage.getInstance().getReference("files/" + UUID.randomUUID()).putFile(file).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(isTeacher)
                                FirebaseDatabase.getInstance().getReference("homework").child(id).push().setValue(new Homework(task.getResult().toString(),fileName.getText().toString()));
                            else
                                FirebaseDatabase.getInstance().getReference("homeworkInClass").child(id).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(new HomeworkInClass(task.getResult().toString(),fileName.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid()));
                            fileName.setText("");
                        }
                    });
                    Toast.makeText(HomeworkActivity.this, "file uploaded", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(HomeworkActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = 100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded " + (int)progress + "%");
            }
        });
    }


    void getHomeWork(){
        FirebaseDatabase.getInstance().getReference("homework/" + id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(list.size()>0)list.clear();
                if(homeworkURI.size() > 0)homeworkURI.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    Homework homework = ds.getValue(Homework.class);
                    homeworkURI.add(homework.url);
                    list.add(homework.name);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}