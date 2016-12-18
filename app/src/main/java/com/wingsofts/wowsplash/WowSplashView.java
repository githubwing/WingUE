package com.wingsofts.wowsplash;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import com.wingsofts.wowsplash.support.SvgPathParser;

/**
 * Created by wing on 12/12/16.
 */

public class WowSplashView extends View {
  private float mLength;
  private PathMeasure mTowerPathMeasure;
  private Path mTowerPath;
  private float mAnimatorValue;
  private Path mTowerDst;
  private Paint mPaint;
  private boolean isAnimateEnd;
  private String mTitle = "AndroidWing";

  public static final float SCALE = 2f;
  public static float translateX;
  public static float translateY;

  // from the svg file
  private int mTowerHeight = 600;
  private int mTowerWidth = 440;

  private Path[] mCouldPaths;
  private int couldCount = 4;

  private float mCouldX[] = { 0f, 100f, 350f, 400f };
  private float mCouldY[] = { -5000f, -5000f, -5000f, -5000f };
  private float mCouldFinalY[] = { 100f, 80f, 200f, 300f };

  private long mDuration = 3000;
  private int mWidth;
  private float mTitleY;
  private float mFinalTitleY = mTowerHeight + 100;

  private OnEndListener mListener;
  private int mAlpha;

  public WowSplashView(Context context) {
    this(context, null);
  }

  public WowSplashView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public WowSplashView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
    setDrawingCacheEnabled(true);

    if (Build.VERSION.SDK_INT < 21) {
      setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }
    mPaint = new Paint();

    //init TowerPath

    mTowerPath = new SvgPathParser().parsePath(context.getResources().getString(R.string.path_00));

    mTowerPathMeasure = new PathMeasure(mTowerPath, true);
    mLength = mTowerPathMeasure.getLength();
    mTowerDst = new Path();

    mCouldPaths = new Path[couldCount];

    for (int i = 0; i < mCouldPaths.length; i++) {
      mCouldPaths[i] = new Path();
    }
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mWidth = w;
  }

  private void setupCouldPath(Path path, int pos) {
    path.reset();
    path.moveTo(mCouldX[pos], mCouldY[pos]);
    path.lineTo(mCouldX[pos] + 30, mCouldY[pos]);
    path.quadTo(mCouldX[pos] + 30 + 30, mCouldY[pos] - 50, mCouldX[pos] + 30 + 60, mCouldY[pos]);
    path.lineTo(mCouldX[pos] + 30 + 60 + 30, mCouldY[pos]);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    mPaint.setStyle(Paint.Style.STROKE);
    //这里SVG过小  就临时这样适配一下。。
    canvas.scale(SCALE, SCALE);
    translateX = (mWidth - mTowerWidth * SCALE) / 2 - 80;
    translateY = 100;
    canvas.translate(translateX, translateY);

    mTowerDst.reset();
    mPaint.setAntiAlias(true);
    mPaint.setStrokeWidth(2);
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setColor(Color.WHITE);
    //canvas.drawPath(mTowerPath, paint);

    float stop = mLength * mAnimatorValue;
    mTowerPathMeasure.getSegment(0, stop, mTowerDst, true);

    drawTower(canvas);
    mPaint.setAlpha(255);

    drawCould(canvas);

    drawTitle(canvas);
  }

  private void drawTitle(Canvas canvas) {
    mPaint.setStrokeWidth(3);
    mPaint.setStyle(Paint.Style.FILL);
    mPaint.setTextSize(80);
    int length = (int) mPaint.measureText(mTitle);

    int x = (mTowerWidth - length) / 2;

    canvas.drawText(mTitle, x, mTitleY, mPaint);
  }

  private void drawCould(Canvas canvas) {
    for (int i = 0; i < mCouldPaths.length; i++) {
      setupCouldPath(mCouldPaths[i], i);
      canvas.drawPath(mCouldPaths[i], mPaint);
    }
  }

  private void drawTower(Canvas canvas) {
    canvas.drawPath(mTowerDst, mPaint);

    if (isAnimateEnd) {
      mPaint.setAlpha(mAlpha);
      canvas.drawPath(mTowerPath, mPaint);
    }
  }

  public void startAnimate() {

    restore();
    //start tower animate
    getTowerValueAnimator().start();

    //start could animate
    for (int i = 0; i < mCouldPaths.length; i++) {
      final ValueAnimator couldAnimator = getCouldValueAnimator(i);
      postDelayed(new Runnable() {
        @Override public void run() {
          couldAnimator.start();
        }
      }, mDuration / 2);
    }

    getTitleAnimate().start();
  }

  private void restore() {
    for (int i = 0; i < couldCount; i++) {
      mCouldY[i] = 0;
    }
  }

  private ValueAnimator getTitleAnimate() {

    ValueAnimator va = ValueAnimator.ofFloat(0, mFinalTitleY);
    va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        mTitleY = (float) valueAnimator.getAnimatedValue();
        invalidate();
      }
    });
    va.setInterpolator(new DecelerateInterpolator());
    va.setDuration(mDuration / 3);
    return va;
  }

  private ValueAnimator getCouldValueAnimator(final int pos) {
    ValueAnimator animator = ValueAnimator.ofFloat(getHeight(), mCouldFinalY[pos]);
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        mCouldY[pos] = (float) valueAnimator.getAnimatedValue();
        postInvalidateDelayed(10);
      }
    });
    animator.setDuration(1500);
    animator.setInterpolator(new DecelerateInterpolator());
    return animator;
  }

  @NonNull private ValueAnimator getTowerValueAnimator() {
    final ValueAnimator towerAnimator = ValueAnimator.ofFloat(0, 1);
    towerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        mAnimatorValue = (float) valueAnimator.getAnimatedValue();
        postInvalidateDelayed(10);
      }
    });
    towerAnimator.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animator) {
        isAnimateEnd = false;
      }

      @Override public void onAnimationEnd(Animator animator) {

        isAnimateEnd = true;
        invalidate();

        getAlphaAnimator().start();

        towerAnimator.removeAllUpdateListeners();
      }

      @Override public void onAnimationCancel(Animator animator) {

      }

      @Override public void onAnimationRepeat(Animator animator) {

      }
    });

    towerAnimator.setInterpolator(new DecelerateInterpolator());
    towerAnimator.setDuration(mDuration);
    return towerAnimator;
  }

  private ValueAnimator getAlphaAnimator() {
    final ValueAnimator va = ValueAnimator.ofInt(0, 255);
    va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        mAlpha = (int) animation.getAnimatedValue();
        invalidate();
      }
    });
    va.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animation) {

      }

      @Override public void onAnimationEnd(Animator animation) {
        va.removeAllUpdateListeners();
        if (mListener != null) {
          mListener.onEnd(WowSplashView.this);
        }
      }

      @Override public void onAnimationCancel(Animator animation) {

      }

      @Override public void onAnimationRepeat(Animator animation) {

      }
    });
    va.setDuration(500);
    return va;
  }

  public interface OnEndListener {
    void onEnd(WowSplashView wowSplashView);
  }

  public void setOnEndListener(OnEndListener listener) {
    mListener = listener;
  }
}
