package com.example.jolp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
//import com.google.firebase.database.core.view.View;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import java.util.HashMap;


public class UploadActivity extends AppCompatActivity {


    private static final int PICK_FILE = 1 ;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        databaseReference = FirebaseDatabase.getInstance("https://jolp-a5446-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("UserData");

    }

    public void FileUpload(View view) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent,PICK_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_FILE){

            if(resultCode == RESULT_OK){

                Uri FileUri = data.getData();
                StorageReference Folder = FirebaseStorage.getInstance("gs://jolp-a5446.appspot.com").getReference().child("Files");
                final StorageReference file_name = Folder.child("file"+FileUri.getLastPathSegment());
                file_name.putFile(FileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        file_name.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("filelink", String.valueOf(uri));


                                databaseReference.setValue(hashMap);
                                Toast.makeText(UploadActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
    }
}

/*
이전의 upload_activity.xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".UploadActivity">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_marginTop="111dp"
        android:orientation="vertical">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_margin="9dp"
            android:orientation="horizontal">

            <TextView
                android:id="@+id/textView"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:gravity="center"
                android:text="Not Selected"
                android:textColor="@android:color/background_dark"
                android:textSize="18dp" />

            <Button
                android:id="@+id/btnSelect"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="강의 계획서 선택" />
        </LinearLayout>

        <Button
            android:id="@+id/btnUpload"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_margin="9dp"
            android:text="강의 계획서 업로드" />
    </LinearLayout>

</RelativeLayout>
 */