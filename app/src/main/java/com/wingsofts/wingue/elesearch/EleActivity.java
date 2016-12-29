package com.wingsofts.wingue.elesearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wingsofts.wingue.R;

public class EleActivity extends AppCompatActivity {
    private TextView mSearchBGTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ele);

        mSearchBGTxt = (TextView) findViewById(R.id.tv_search_bg);
        mSearchBGTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EleActivity.this,EleSearchActivity.class);
                int location[] = new int[2];
                mSearchBGTxt.getLocationOnScreen(location);
                intent.putExtra("x",location[0]);
                intent.putExtra("y",location[1]);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
    }
}
