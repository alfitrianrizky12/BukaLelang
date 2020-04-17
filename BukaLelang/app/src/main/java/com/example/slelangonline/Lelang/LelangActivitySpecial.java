package com.example.slelangonline.Lelang;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slelangonline.Barang.BarangActivity;
import com.example.slelangonline.R;
import com.example.slelangonline.auth.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.widget.Toast.LENGTH_LONG;

public class LelangActivitySpecial extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    public static final String LELANG_ID = "LELANG_ID";
    public static String aa,ab,ac,ad,ae,af,ai;
    public static int ag,ah;
    private ArrayList<Lelang> dataList;
    private FloatingActionButton btnAdd;
    private RecyclerView recyclerViewBarang;
    private LelangAdapter adapterCare;

    private Query refCare;
    private FirebaseDatabase database;

    private ImageView ivMenuBars;
    private NavigationView navigationSidebar;
    LelangActivitySpecial context;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lelang_special);
        context=this;
        // Get view by id
        recyclerViewBarang = (RecyclerView) findViewById(R.id.rvCare);
        btnAdd = (FloatingActionButton) findViewById(R.id.fab_add);

        dataList = new ArrayList<>();

        ivMenuBars = (ImageView) findViewById(R.id.ivMenuBars);

        navigationSidebar = (NavigationView)findViewById(R.id.navigationBar);
        navigationSidebar.setNavigationItemSelectedListener(this);


        final DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);

        ivMenuBars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });

        eventListener();

        // Get a reference to our posts
        database = FirebaseDatabase.getInstance();
        refCare = database.getReference("Lelang");
//filter
//        refCare = database.getReference("Lelang").orderByChild("content").equalTo("Ada");
    }

    @Override
    protected void onStart() {
        super.onStart();
        refCare.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataList.clear();
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    Lelang lelang = value.getValue(Lelang.class);
                    dataList.add(lelang);
                }
                adapterCare = new LelangAdapter(LelangActivitySpecial.this, dataList);
                recyclerViewBarang.setAdapter(adapterCare);
                recyclerViewBarang.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                adapterCare.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void eventListener() {
        recyclerViewBarang.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Lelang lelang = dataList.get(position);
                String id= lelang.getIdLelang();
                aa = lelang.getPicTips();
                ab = lelang.getTitleTips();
                ac = lelang.getContent();
                ad = lelang.getPemilik();
                ae = lelang.getCreateAt();
                ag = lelang.getHargaAwal();
                ah = lelang.getHargaAkhir();
                ai= lelang.getNamePemenang();


                showUpdateDialog(lelang.getIdLelang());
            }
        }));
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LelangActivitySpecial.this, ManageLelangActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showUpdateDialog(final String id){
        AlertDialog.Builder dialogb = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogv = inflater.inflate(R.layout.dialog_lelang,null);

        dialogb.setView(dialogv);



        final Button btn_edit = dialogv.findViewById(R.id.Edit);
        final Button btn_delete = dialogv.findViewById(R.id.Delete);
        final Button btn_lihat = dialogv.findViewById(R.id.Lihat);
        final AlertDialog dialog = dialogb.create();
        dialog.show();
        btn_lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LelangActivitySpecial.this, DetailLelangActivitySpecial.class);
                intent.putExtra(LELANG_ID, id);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                getValue(id);
                dialog.dismiss();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete(id);
                dialog.dismiss();
            }
        });

    }
    private void Delete(String id){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Lelang").child(id);
        db.removeValue();
        Toast.makeText(this,"Data berhasil dihapus", LENGTH_LONG).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.menu_barang) {
            startActivity(new Intent(context, BarangActivity.class));
        } else if (id == R.id.menu_lelang) {
            startActivity(new Intent(context, LelangActivitySpecial.class));
        } else if (id == R.id.menu_hasil_lelang) {
            startActivity(new Intent(context, LoginActivity.class));
        } else if (id == R.id.menu_Logout) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            finish();
            startActivity(new Intent(LelangActivitySpecial.this, LoginActivity.class));


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getValue(final String id){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Lelang").child(id);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String tanggal = sdf.format(new Date());
        Lelang notea = new Lelang(id,aa,ab,ac,ad,ae,af,ag,ah,ai,"0");
        db.setValue(notea);
        Toast.makeText(LelangActivitySpecial.this, "Berhasil ", Toast.LENGTH_SHORT).show();
    }
}
