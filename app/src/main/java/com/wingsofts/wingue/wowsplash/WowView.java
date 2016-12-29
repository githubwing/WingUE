package com.wingsofts.wingue.wowsplash;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.wingsofts.wingue.R;

/**
 * Created by wing on 12/13/16.
 */

public class WowView extends View {
    private Bitmap mWowSplashBitmap;
    private Bitmap mDstBitmap;
    private float mAlpha;
    private float mScale;
    private PorterDuffXfermode mMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
    private Paint mPaint;
    private long mDuration = 2000;

    public WowView(Context context) {
        this(context, null);
    }

    public WowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        mDstBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wow_splash_shade);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mWowSplashBitmap != null) {
            canvas.drawBitmap(mWowSplashBitmap, 0, 0, null);
            canvas.scale(mScale, mScale, mDstBitmap.getWidth() / 2, mDstBitmap.getHeight() / 2);
            mPaint.setXfermode(mMode);
            canvas.drawBitmap(mDstBitmap, 0, 0, mPaint);
        }
        setAlpha(mAlpha);
    }

    public void setWowSplashBitmap(Bitmap bitmap) {
        mWowSplashBitmap = bitmap;
        invalidate();
    }

    public void startAnimate(Bitmap bitmap) {
        setWowSplashBitmap(bitmap);

        getAlphaValueAnimator().start();
        getScaleValueAnimator().start();
    }

    @NonNull
    private ValueAnimator getScaleValueAnimator() {
        ValueAnimator scaleVa = ValueAnimator.ofFloat(0, 6);
        scaleVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mScale = (float) valueAnimator.getAnimatedValue();
            }
        });
        scaleVa.setDuration(mDuration);
        return scaleVa;
    }

    @NonNull
    private ValueAnimator getAlphaValueAnimator() {
        ValueAnimator alphaVa = ValueAnimator.ofFloat(1, 0f);
        alphaVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAlpha = (float) valueAnimator.getAnimatedValue();
                postInvalidateDelayed(16);
            }
        });

        alphaVa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setVisibility(GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        alphaVa.setDuration(mDuration);
        return alphaVa;
    }
}
