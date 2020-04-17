package com.example.slelangonline.Lelang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.slelangonline.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailLelangActivitySpecial extends AppCompatActivity {

    private TextView txtTitle,txtDesc,txtAuthor, txtHargaAwal,txtHargaAkhir,txtPenawar;
    private String id;

    ImageView ivMenuBack, ivDetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_detail_lelang_special);
         Intent intent = getIntent();
         id = intent.getStringExtra(LelangActivitySpecial.LELANG_ID);
         ivMenuBack = findViewById(R.id.ivMenuBack);
         ivMenuBack.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent n = new Intent(DetailLelangActivitySpecial.this, LelangActivitySpecial.class);
                 startActivity(n);

             }
         });
         initComponent();
    }

    private void initComponent() {
        txtTitle = (TextView) findViewById(R.id.tvTitle);
        txtDesc = (TextView) findViewById(R.id.tvContent);
        txtHargaAwal = (TextView) findViewById(R.id.tvHargaAwal);
        txtHargaAkhir= (TextView) findViewById(R.id.tvHargaAkhir);
        txtPenawar= (TextView) findViewById(R.id.tvPenawar);
        ivDetail = (ImageView) findViewById(R.id.ivDetail);


        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Lelang").child(id);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Lelang a  = dataSnapshot.getValue(Lelang.class);
                txtTitle.setText(a.getTitleTips());
                txtDesc.setText(a.getContent());
                txtHargaAwal.setText("Rp."+a.getHargaAwal());
                txtHargaAkhir.setText("Rp."+a.getHargaAkhir());
                txtPenawar.setText(a.getNamePemenang());
                Glide.with(DetailLelangActivitySpecial.this).load(a.getPicTips()).into(ivDetail);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
