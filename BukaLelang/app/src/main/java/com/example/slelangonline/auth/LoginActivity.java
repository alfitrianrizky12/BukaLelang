package com.example.slelangonline.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.slelangonline.Lelang.LelangActivity;
import com.example.slelangonline.Lelang.LelangActivitySpecial;
import com.example.slelangonline.R;
import com.example.slelangonline.Lelang.LelangActivity;
import com.example.slelangonline.Lelang.LelangActivitySpecial;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private TextView mloginBtn, mregisterBtn;
    private EditText mEmailField;
    private EditText mPasswordField;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // Get view by id
        mEmailField = (EditText) findViewById(R.id.edtUsername);
        mPasswordField = (EditText) findViewById(R.id.edtPassword);
        mloginBtn = (TextView) findViewById(R.id.btnLogin);
        mregisterBtn = (TextView) findViewById(R.id.tvLinkRegis);

        // Get instance firebase auth
        mAuth = FirebaseAuth.getInstance();
        //mDatabase = FirebaseDatabase.getInstance().getReference();

        // Event Listener
        mregisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    sesi();
                }
            }
        };
        mloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                startSignIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void startSignIn() {
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        if (TextUtils.isEmpty(email) | TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Fields are Empty", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener() {
                @Override

                public void onComplete(@NonNull Task task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login Problem", Toast.LENGTH_SHORT).show();
                    }else if (task.isSuccessful()){

                    }
                }
            });
        }
    }
    private void sesi(){
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser mUser = mAuth.getCurrentUser();
        String id = mUser.getUid();
        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("User").child(id);

        ValueEventListener valueEventListener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User a = dataSnapshot.getValue(User.class);
                String level = a.getLevel();
                if(level.equals("Admin")){
                    Intent i = new Intent(LoginActivity.this, LelangActivity.class);
                    startActivity(i);
                }else if(level.equals("Petugas")){
                    Intent i = new Intent(LoginActivity.this, LelangActivitySpecial.class);
                    startActivity(i);
                }else {
                    Intent i = new Intent(LoginActivity.this, LelangActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

