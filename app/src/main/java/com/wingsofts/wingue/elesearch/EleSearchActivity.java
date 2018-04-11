package com.wingsofts.wingue.elesearch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wingsofts.wingue.R;

public class EleSearchActivity extends AppCompatActivity {
  private TextView mSearchBGTxt;
  private TextView mHintTxt;
  private TextView mSearchTxt;
  private FrameLayout mContentFrame;
  private ImageView mArrowImg;
  private boolean finishing;
  private float originX;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ele_search);
    initView();
    execute();
  }

  private void initView() {
    mSearchBGTxt = (TextView) findViewById(R.id.tv_search_bg);
    mHintTxt = (TextView) findViewById(R.id.tv_hint);
    mContentFrame = (FrameLayout) findViewById(R.id.frame_content_bg);
    mSearchTxt = (TextView) findViewById(R.id.tv_search);
    mArrowImg = (ImageView) findViewById(R.id.iv_arrow);
  }

  private void execute() {
    mSearchBGTxt.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override public void onGlobalLayout() {
            mSearchBGTxt.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            performEnterAnimation();
          }
        });
  }

  private void performEnterAnimation() {
    initLocation();
    final float top = getResources().getDisplayMetrics().density * 20;
    final ValueAnimator translateVa = translateVa(mSearchBGTxt.getY(), top);
    final ValueAnimator scaleVa = scaleVa(1, 0.8f);
    final ValueAnimator alphaVa = alphaVa(0, 1f);
    originX = mHintTxt.getX();
    final float leftSpace = mArrowImg.getRight() * 2;
    final ValueAnimator translateVaX = translateVax(originX, leftSpace);

    setDuration(translateVa, scaleVa, translateVaX, alphaVa);
    star(translateVa, scaleVa, translateVaX, alphaVa);
  }

  private void initLocation() {
    final float translateY = getTranslateY();
    //放到前一个页面的位置
    mSearchBGTxt.setY(mSearchBGTxt.getY() + translateY);
    mHintTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mHintTxt.getHeight()) / 2);
    mSearchTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mSearchTxt.getHeight()) / 2);
  }

  private float getTranslateY() {
    float originY = getIntent().getIntExtra("y", 0);
    int[] location = new int[2];
    mSearchBGTxt.getLocationOnScreen(location);
    return originY - (float) location[1];
  }

  @Override public void onBackPressed() {
    if (!finishing) {
      finishing = true;
      performExitAnimation();
    }
  }

  private void performExitAnimation() {
    final float translateY = getTranslateY();
    final ValueAnimator translateVa = translateVa(mSearchBGTxt.getY(), mSearchBGTxt.getY() + translateY);
    final ValueAnimator scaleVa = scaleVa(0.8f, 1f);
    final ValueAnimator alphaVa = alphaVa(1f, 0f);
    exitListener(translateVa);
    final float currentX = mHintTxt.getX();
    ValueAnimator translateVaX = translateVax(currentX, originX);

    setDuration(translateVa, scaleVa, translateVaX, alphaVa);
    star(translateVa, scaleVa, translateVaX, alphaVa);
  }

  @NonNull private ValueAnimator translateVax(float from, float to) {
    ValueAnimator translateVaX = ValueAnimator.ofFloat(from, to);
    translateVaX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        Float value = (Float) animation.getAnimatedValue();
        mHintTxt.setX(value);
      }
    });
    return translateVaX;
  }

  @NonNull private ValueAnimator translateVa(float from, float to) {
    ValueAnimator translateVa = ValueAnimator.ofFloat(from, to);
    translateVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        mSearchBGTxt.setY((Float) valueAnimator.getAnimatedValue());
        mArrowImg.setY(
            mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mArrowImg.getHeight()) / 2);
        mHintTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mHintTxt.getHeight()) / 2);
        mSearchTxt.setY(
            mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mSearchTxt.getHeight()) / 2);
      }
    });
    return translateVa;
  }

  @NonNull private ValueAnimator scaleVa(float from, float to) {
    ValueAnimator scaleVa = ValueAnimator.ofFloat(from, to);
    scaleVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        mSearchBGTxt.setScaleX((Float) valueAnimator.getAnimatedValue());
      }
    });
    return scaleVa;
  }

  @NonNull private ValueAnimator alphaVa(float from, float to) {
    ValueAnimator alphaVa = ValueAnimator.ofFloat(from, to);
    alphaVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        mContentFrame.setAlpha((Float) valueAnimator.getAnimatedValue());
        mSearchTxt.setAlpha((Float) valueAnimator.getAnimatedValue());
        mArrowImg.setAlpha((Float) valueAnimator.getAnimatedValue());
      }
    });
    return alphaVa;
  }

  private void exitListener(ValueAnimator translateVa) {
    translateVa.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        finish();
        overridePendingTransition(0, 0);
      }
    });
  }

  private void setDuration(ValueAnimator translateVa, ValueAnimator scaleVa,
      ValueAnimator translateVaX, ValueAnimator alphaVa) {
    alphaVa.setDuration(350);
    translateVa.setDuration(350);
    scaleVa.setDuration(350);
    translateVaX.setDuration(350);
  }

  private void star(ValueAnimator translateVa, ValueAnimator scaleVa, ValueAnimator translateVaX,
      ValueAnimator alphaVa) {
    alphaVa.start();
    translateVa.start();
    scaleVa.start();
    translateVaX.start();
  }
}
