package com.example.q.parallaxtest;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private MyListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //加Header
        final View mHeaderView = View.inflate(this, R.layout.view_header, null);
        listView = (MyListView) findViewById(R.id.list_view);
        final ImageView imageView = (ImageView) mHeaderView.findViewById(R.id.iv_header);
        listView.addHeaderView(mHeaderView);
        Log.e("onCreate", "还在onCreate中");
        //onResume之后才可以得到
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                //在布局填充结束构造完之后，所有的测绘工作都完成之后才会调用
                Log.e("GlobalListener", "布局完成");
                listView.setParallaxImage(imageView);//此时调用此方法便可以获得imageView的高度
                mHeaderView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        //填充数据
        listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, Cheeses.names));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TAG", "onResume");
    }
}
