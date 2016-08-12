# Parallax 
* 若从外部添加View,是无法直接从onCreate()中getHeight的，因为此时还没有绘制完毕。 
```Java
mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                //在布局填充结束构造完之后，所有的测绘工作都完成之后才会调用
                Log.e("GlobalListener","布局完成");
                listView.setParallaxImage(imageView);
                mHeaderView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
```

```Java
//在这里引用外部的view
  public void setParallaxImage(ImageView imageView) {
        this.imageView = imageView;
        //在这里得到高度
        mOriginalHeight = imageView.getHeight();//设定高度
        mDrawableHeight = imageView.getDrawable().getIntrinsicHeight();//原画高度
    }
```

> 执行顺序为 onCreate-->onResume->(自定义View)onSizeChanged-->onGlobalLayout回调-->测绘完毕  
所以也无法在onSizeChanged之中得到.只能通过回调方法

* 回弹动画,从当前高度到原始高度
```Java
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                final ValueAnimator mValueAnimator = ValueAnimator.ofInt(imageView.getHeight(),mOriginalHeight);//从x->Y，但是并没有指定是哪个View
                mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {//变化过程
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        imageView.getLayoutParams().height = (int) valueAnimator.getAnimatedValue();//得到x->y过程中的某个时刻的数值,修改height
                        imageView.requestLayout();//通知View,Layout参数已经修改
                    }
                });
                mValueAnimator.setInterpolator(new OvershootInterpolator(1f));//会弹效果,值越大弹力越大
                mValueAnimator.setDuration(500);//时长
                mValueAnimator.start();
                break;
        }
        return super.onTouchEvent(ev);
}
```



    
