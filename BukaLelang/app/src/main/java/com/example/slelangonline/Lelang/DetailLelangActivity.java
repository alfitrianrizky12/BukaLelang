package com.example.slelangonline.Lelang;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.slelangonline.R;
import com.example.slelangonline.auth.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Locale;

public class DetailLelangActivity extends AppCompatActivity {
    public static String noTelp;
    private TextView txtTitle,txtDesc,txtAuthor, txtHargaAwal,txtHargaAkhir,txtPenawar,
            txtPic,txtCreateAt,txtCloseAt,txtPemilik;
    Button btnTawar;
    private String id;
    EditText edtKolom;
    ImageView ivMenuBack, ivDetail;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_detail_lelang);
         Intent intent = getIntent();
         id = intent.getStringExtra(LelangActivitySpecial.LELANG_ID);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("Lelang");

         txtPic= (TextView) findViewById(R.id.tvPicTips);
         txtCreateAt=(TextView)findViewById(R.id.tvCreateAt);
         txtCloseAt=(TextView)findViewById(R.id.tvCloseAt);
         txtPemilik=(TextView)findViewById(R.id.tvPemilik);
         txtTitle = (TextView) findViewById(R.id.tvTitle);
         txtDesc = (TextView) findViewById(R.id.tvContent);
         txtHargaAwal = (TextView) findViewById(R.id.tvHargaAwal);
         txtHargaAkhir= (TextView) findViewById(R.id.tvHargaAkhir);
         txtPenawar= (TextView) findViewById(R.id.tvPenawar);
         ivDetail = (ImageView) findViewById(R.id.ivDetail);
         edtKolom = (EditText) findViewById(R.id.edtKolom);
         btnTawar = (Button) findViewById(R.id.btnTawar);

         ivMenuBack = findViewById(R.id.ivMenuBack);
         ivMenuBack.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent n = new Intent(DetailLelangActivity.this, LelangActivity.class);
                 startActivity(n);

             }
         });
         initComponent();

        btnTawar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateDialog(id);
            }
        });
    }

    private void initComponent() {

        DatabaseReference db = ref.child(id);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Lelang a  = dataSnapshot.getValue(Lelang.class);
                txtPic.setText(a.getPicTips());
                txtTitle.setText(a.getTitleTips());
                txtDesc.setText(a.getContent());
                txtPemilik.setText(a.getPemilik());
                txtCreateAt.setText(a.getCreateAt());
                txtCloseAt.setText(a.getCloseAt());
                txtHargaAwal.setText("Rp."+a.getHargaAwal());
                txtHargaAkhir.setText("Rp."+a.getHargaAkhir());
                txtPenawar.setText(a.getNamePemenang());
                Glide.with(DetailLelangActivity.this).load(a.getPicTips()).into(ivDetail);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        String al= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference a =  firebaseDatabase.getReference("User").child(al);
        a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                noTelp= u.getNoTelp();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void showUpdateDialog(final String id){
        AlertDialog.Builder dialogb = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogv = inflater.inflate(R.layout.dialog_lelang2,null);

        dialogb.setView(dialogv);

        final Button btn_edit = dialogv.findViewById(R.id.Edit);
        final Button btn_delete = dialogv.findViewById(R.id.Delete);
        final AlertDialog dialog = dialogb.create();
        dialog.show();

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                initUpdate(id);
                initUpdate2(id);
                dialog.dismiss();
                edtKolom.setText(null);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(DetailLelangActivity.this, "Berhasil "+noTelp, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initUpdate(final String id){

        int tambahan = Integer.parseInt( edtKolom.getText().toString());
        String angkamentah = txtHargaAkhir.getText().toString();
        String angkalumayan = angkamentah.substring(3);
        int angkajadi= Integer.parseInt(angkalumayan);
        int nambah = tambahan + angkajadi;
        String aa= txtPic.getText().toString();
        String ab= txtTitle.getText().toString();
        String ac= txtDesc.getText().toString();
        String ad= txtPemilik.getText().toString();
        String ae= txtCreateAt.getText().toString();
        String af= txtCloseAt.getText().toString();
        int ag = Integer.parseInt(txtHargaAwal.getText().toString().substring(3));
        int ah = nambah;
        String ai= FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String createdAt = sdf.format(new Date());
        DatabaseReference db = ref.child(id);
        Lelang notea = new Lelang(id,aa,ab,ac,ad,ae,af,ag,ah,ai,"1");
        db.setValue(notea);




    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initUpdate2(final String id){
        int tambahan = Integer.parseInt( edtKolom.getText().toString());
        String angkamentah = txtHargaAkhir.getText().toString();
        String angkalumayan = angkamentah.substring(3);
        int angkajadi= Integer.parseInt(angkalumayan);
        int nambah = tambahan + angkajadi;
        int ah = nambah;
        String ai= FirebaseAuth.getInstance().getCurrentUser().getDisplayName();


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String createdAt = sdf.format(new Date());
        String idrin = System.currentTimeMillis()+"";
        DatabaseReference db1 = ref.child(id).child("RincianLelang");
        RincianPenawaran rin = new RincianPenawaran(idrin,ai,"Rp"+ah,""+noTelp,createdAt);
        db1.child(idrin).setValue(rin);

        Toast.makeText(DetailLelangActivity.this, "Berhasil "+ah, Toast.LENGTH_SHORT).show();

    }

}

