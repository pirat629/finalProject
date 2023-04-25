package com.example.forteatchers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CheckHomeWorkActivity extends AppCompatActivity {
    String email;
    ListView checkHomeWork;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> list;
    ArrayList<String> urlUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_home_work);
        email = getIntent().getStringExtra("email");
        checkHomeWork = findViewById(R.id.checkHomework1);
        urlUser = getIntent().getStringArrayListExtra("uri");
        list = getIntent().getStringArrayListExtra("names");
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list);
        checkHomeWork.setAdapter(arrayAdapter);

        checkHomeWork.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(homeworkURI.get(i)));
//                String title = list.get(i);
//                request.setTitle(title);
//                request.setDescription("Downloading file");
//                String cookie = CookieManager.getInstance().getCookie(homeworkURI.get(i));
//                request.addRequestHeader("cookie",cookie);
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title);
//
//                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//                downloadManager.enqueue(request);
//
//                Toast.makeText(HomeworkActivity.this, "Downloading started", Toast.LENGTH_SHORT).show();
            }
        });


    }
}