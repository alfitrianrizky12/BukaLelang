package com.example.slelangonline.Barang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.slelangonline.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;

public class ManageBarangActivity extends AppCompatActivity {

    //Firebase Database
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;

    //Firebase Storage
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageTask uploadTask;

    private EditText txtJudul, txtDesc;
    private TextView btnSimpan;
    private ImageView ivChange;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_barang);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("Barang");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        initComponent();
        initEvent();
    }

    private void initComponent() {
        txtJudul = (EditText) findViewById(R.id.txt_title);
        txtDesc = (EditText) findViewById(R.id.txt_desc);
        btnSimpan = (TextView) findViewById(R.id.btn_simpan);
        ivChange = (ImageView) findViewById(R.id.iv_change);
    }

    private void initEvent() {
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        ivChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void Insert(String url){
        // Calendar
        Calendar now = Calendar.getInstance();
        int years = now.get(Calendar.YEAR);
        int months = now.get(Calendar.MONTH);
        int days = now.get(Calendar.DAY_OF_MONTH);

        // Get value to string
        String title = txtJudul.getText().toString();
        String desc = txtDesc.getText().toString();

        if(!TextUtils.isEmpty(title)){
            // get key id
            String id = ref.push().getKey();


            Barang barangData = new Barang(id,url,title,desc);
            barangData.setIdBarang(id);
            barangData.setPicTips(url);
            barangData.setNamaBarang(title);
            barangData.setContent(desc);
            // Add data to firebase
            ref.child(id).setValue(barangData);

            Toast.makeText(this,"Post berhasil di masukan",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Post gagal di masukan",Toast.LENGTH_LONG).show();
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivChange.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Sedang Upload...");
            progressDialog.show();

            final StorageReference storageRef = storageReference.child("Lelang").child(System.currentTimeMillis()+"");
            uploadTask = storageRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Insert(uri.toString());
                                }
                            });
                            progressDialog.dismiss();
                            Toast.makeText(ManageBarangActivity.this, "Data Berhasil Terupload", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ManageBarangActivity.this, "Upload gagal "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Sedang Upload "+(int)progress+"%");
                        }
                    });
        }
    }
}
