package com.example.slelangonline.Lelang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.slelangonline.Barang.Barang;
import com.example.slelangonline.R;
import com.example.slelangonline.Barang.Barang;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ManageLelangActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {
    Spinner spin;
    private Query refCare;
    private FirebaseDatabase database;
    //Firebase Database
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref,mDatabase;

    //Firebase Storage
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageTask uploadTask;

    private EditText txtJudul, txtDesc,txtBatasWaktu,txtHargaAwal;
    private TextView btnSimpan;
    private ImageView ivChange;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_lelang);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("Lelang");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        initComponent();
        initEvent();
        initSpin();
        spin.setOnItemSelectedListener(this);
    }

    private void initComponent() {
        txtJudul = (EditText) findViewById(R.id.txt_title);
        txtDesc = (EditText) findViewById(R.id.txt_desc);
        txtHargaAwal = (EditText)findViewById(R.id.txt_hargaAwal);
        txtBatasWaktu= (EditText)findViewById(R.id.txt_batasWaktu);
        btnSimpan = (TextView) findViewById(R.id.btn_simpan);
        ivChange = (ImageView) findViewById(R.id.iv_change);
        spin =(Spinner) findViewById(R.id.spin);
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String createdAt = sdf.format(new Date());
        String author = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String value = txtHargaAwal.getText().toString();
        int harga = Integer.parseInt(value);
        String batasWaktu = txtBatasWaktu.getText().toString();
        if(!TextUtils.isEmpty(title)){
            // get key id
            String id = ref.push().getKey();

            Lelang lelangData = new Lelang(id,url,title,desc,author,createdAt,batasWaktu,harga,harga,"none","1");

            // Add data to firebase
            ref.child(id).setValue(lelangData);

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
                            Toast.makeText(ManageLelangActivity.this, "Data Berhasil Terupload", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ManageLelangActivity.this, "Upload gagal "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void initSpin(){
        mDatabase = FirebaseDatabase.getInstance().getReference("Barang");
        spin = (Spinner)findViewById(R.id.spin);

        Query query = mDatabase.orderByChild("namaBarang");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<Barang> titleList = new ArrayList<Barang>();

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    String id = dataSnapshot1.child("idBarang").getValue(String.class);
                    String nama = dataSnapshot1.child("namaBarang").getValue(String.class);
                    String desk = dataSnapshot1.child("content").getValue(String.class);
                    String picTips= dataSnapshot.child("picTips").getValue(String.class);

                    Barang barang = new Barang(id,picTips,nama,desk);
                    titleList.add(barang);
                }
                ArrayAdapter<Barang> arrayAdapter = new ArrayAdapter<Barang>(ManageLelangActivity.this,
                        android.R.layout.simple_spinner_item, titleList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ManageLelangActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Barang barang = (Barang)parent.getSelectedItem();
        txtJudul.setText(barang.getNamaBarang());
        txtDesc.setText(barang.getContent());


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

