package com.example.crocheptc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    public void abrir_tela_sobre(View v){
        Intent intent = new Intent(this, tela_sobre.class);
        startActivity(intent);



    }

    public void abrir_login(View v){
        Intent intent1 = new Intent(this, login_activity.class);
        startActivity(intent1);

    }


}
