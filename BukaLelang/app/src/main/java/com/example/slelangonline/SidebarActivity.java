package com.example.slelangonline;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class SidebarActivity extends AppCompatActivity {
    ImageView ivBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidebar);

        ivBack = (ImageView) findViewById(R.id.ivMenuBack);

        eventListener();
    }

    private void eventListener() {
        ivBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
                System.out.println("finish");
            }
        });
    }
}
