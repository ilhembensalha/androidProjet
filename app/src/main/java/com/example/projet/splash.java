package com.example.projet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 new Handler().postDelayed(new Runnable() {
    @Override
    public void run() {
  startActivity(new Intent(splash.this,MainActivity.class));
  finish();
    }
},2000);
     }
}
