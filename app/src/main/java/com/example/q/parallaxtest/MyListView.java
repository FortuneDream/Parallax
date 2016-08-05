package com.example.q.parallaxtest;

import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by YQ on 2016/8/3.
 */

/**
 * 视觉特效
 * overScrollBy
 */
public class MyListView extends ListView {
    private ImageView imageView;
    private int mOriginalHeight;
    private int mDrawableHeight;
    private IntEvaluator intEvaluator=new IntEvaluator();

    public MyListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyListView(Context context) {
        this(context, null);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //超出滚动
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        //deltaY:垂直方向的瞬时变化量,顶部到头再拉为-，底部到头再拉为+
        //scrollY:竖直方向的偏移量
        //scrollRange：竖直方向最大范围
        //maxOverScrollY:竖直方向偏移最大量
        //isTouchEvent:是否是手指触摸滑动(true为手指，false为惯性)
        //手指拉动并且是最顶部下拉
        if (isTouchEvent && deltaY < 0) {
            //把拉动的瞬时变化量交给Header，就可以只放大效果

            if (imageView.getHeight()<= mDrawableHeight) {
                imageView.getLayoutParams().height +=Math.abs(deltaY);
                imageView.requestLayout();
            }
        }
        // Log.e("TAG", "overScrollBy: deltaY:" + deltaY + " scrollY:" + scrollY + " scrollRangeY:" + scrollRangeY + " maxOverScrollY" + maxOverScrollY + " isTouchEvent:" + isTouchEvent);
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    //设置图片，拿到引用
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setParallaxImage(ImageView imageView) {
        this.imageView = imageView;
        //必须经过ListView测量才可以得到个高度,所以最好在onSizeChange拿到高度,这里的height和measuredHeight都等于0
        mOriginalHeight=imageView.getHeight();
        mDrawableHeight =  imageView.getDrawable().getIntrinsicHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
                //执行回弹动画
                //从当前高度到原始高度
                final ValueAnimator mValueAnimator=ValueAnimator.ofInt(1);
                mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float fragction=mValueAnimator.getAnimatedFraction();
                        Log.e("TAG","fragction:"+fragction);//从0-1；
                        int newHeight=intEvaluator.evaluate(fragction,imageView.getHeight(),mOriginalHeight);
                        imageView.getLayoutParams().height=newHeight;
                        imageView.requestLayout();
                    }
                });
                mValueAnimator.setInterpolator(new OvershootInterpolator());
                mValueAnimator.setDuration(500);
                mValueAnimator.start();
                break;
        }
        return super.onTouchEvent(ev);
    }
}
