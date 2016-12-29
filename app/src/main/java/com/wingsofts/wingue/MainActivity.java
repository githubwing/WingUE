package com.wingsofts.wingue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wingsofts.wingue.elesearch.EleActivity;
import com.wingsofts.wingue.wowsplash.WowActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void wowSplash(View view) {
        openActivity(WowActivity.class);
    }

    public void eleSearch(View view) {
        openActivity(EleActivity.class);
    }

    public void openActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);

    }
}
