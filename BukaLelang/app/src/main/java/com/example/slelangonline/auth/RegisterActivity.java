package com.example.slelangonline.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.slelangonline.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    TextView btn_regis, btn_login;
    EditText email_regist, password_regist, name_regist,tanggal_lahir_regist, tempat_lahir_regist,level_regist;
    private FirebaseAuth mAuth;
    private DatabaseReference ref;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        // Get view by id
        btn_regis = (TextView) findViewById(R.id.btnRegister);
        btn_login = (TextView) findViewById(R.id.tvLinkLogin);
        name_regist = (EditText) findViewById(R.id.edtNamaRegis);
        email_regist= (EditText) findViewById(R.id.edtEmailRegis);
        password_regist = (EditText) findViewById(R.id.edtPasswordRegis);
        tanggal_lahir_regist = (EditText) findViewById(R.id.edtTanggal);
        tempat_lahir_regist = (EditText) findViewById(R.id.edtKota) ;
        level_regist = (EditText) findViewById(R.id.edtBio);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(a);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
       btn_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = email_regist.getText().toString(), password = password_regist.getText().toString(), displayName = name_regist.getText().toString();

                if(displayName.isEmpty()){
                    name_regist.setError("Nama tidak boleh kosong");
                    name_regist.requestFocus();
                    return;
                }
                if (email.isEmpty()){
                    email_regist.setError("Email tidak boleh kosong");
                    email_regist.requestFocus();
                    return;
                }
                if (password.isEmpty()){
                    password_regist.setError("Password tidak boleh kosong");
                    password_regist.requestFocus();
                    return;
                }else if(password.length()<6){
                    password_regist.setError("Minimal 6 digit password");
                    password_regist.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // Get reference
                            ref = database.getReference("User");

                            // Get user id
                            String id= mAuth.getCurrentUser().getUid();
                            String tanggal_lahir = tanggal_lahir_regist.getText().toString();
                            String  tempat_lahir = tempat_lahir_regist.getText().toString();
                            String level = level_regist.getText().toString();


                            User user = new User( id,displayName,email,password,null,tanggal_lahir,tempat_lahir,level);

                            // Add data to firebase
                            ref.child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                               }
                            });

                            // Set display name user
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(displayName).build();
                            currentUser.updateProfile(profileUpdates);

                            // Show toast

                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Register gagal", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }
}

