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
            //把拉动的瞬时变化量交给Header中的imageView，实现拉伸效果
            if (imageView.getHeight() <= mDrawableHeight) {
                imageView.getLayoutParams().height += Math.abs(deltaY);
                imageView.requestLayout();//通知View，Layout已经修改
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    //设置图片，拿到引用
    public void setParallaxImage(ImageView imageView) {
        this.imageView = imageView;
        mOriginalHeight = imageView.getHeight();
        mDrawableHeight = imageView.getDrawable().getIntrinsicHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                //执行回弹动画
                //从当前高度到原始高度
                final ValueAnimator mValueAnimator = ValueAnimator.ofInt(imageView.getHeight(),mOriginalHeight);//从x->Y，但是并没有指定是哪个View
                mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {//变化过程
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        imageView.getLayoutParams().height = (int) valueAnimator.getAnimatedValue();//得到x->y过程中的某个时刻的数值。然后自行修改height
                        imageView.requestLayout();
                    }
                });
                mValueAnimator.setInterpolator(new OvershootInterpolator());//会弹效果,值越大弹力越大
                mValueAnimator.setDuration(500);
                mValueAnimator.start();
                break;
        }
        return super.onTouchEvent(ev);
}


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
