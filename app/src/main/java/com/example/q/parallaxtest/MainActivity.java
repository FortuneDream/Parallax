package com.example.q.parallaxtest;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        final View mHeaderView=View.inflate(this,R.layout.view_header,null);
        listView = (MyListView) findViewById(R.id.list_view);
        final ImageView imageView= (ImageView) mHeaderView.findViewById(R.id.iv_header);
        listView.addHeaderView(mHeaderView);
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                //在布局填充结束构造完之后，此方法会被调用
                listView.setParallaxImage(imageView);//此时调用此方法便可以获得imageView的高度
                mHeaderView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        //填充数据
        listView.setAdapter(new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,Cheeses.names));
    }
}
