package com.wingsofts.wowsplash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
  private WowSplashView mWowSplashView;
  private WowView mWowView;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mWowSplashView = (WowSplashView) findViewById(R.id.wowSplash);
    mWowView = (WowView) findViewById(R.id.wowView);

    mWowSplashView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        mWowSplashView.startAnimate();
      }
    });

    mWowSplashView.startAnimate();


    mWowSplashView.setOnEndListener(new WowSplashView.OnEndListener() {
      @Override public void onEnd(WowSplashView wowSplashView) {
        mWowSplashView.setVisibility(View.GONE);
        mWowView.setVisibility(View.VISIBLE);
        mWowView.startAnimate(wowSplashView.getDrawingCache());

      }
    });
  }




}
