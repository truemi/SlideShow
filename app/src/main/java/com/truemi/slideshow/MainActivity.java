package com.truemi.slideshow;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.truemi.slideshow.adapter.SlideAdapter;

import java.util.ArrayList;

import javax.xml.datatype.Duration;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> urlLists = new ArrayList<>();
        urlLists.add("https://img03.sogoucdn.com/app/a/100520024/c25c07885f822d67c91256b3033749e7");
        urlLists.add("https://img04.sogoucdn.com/app/a/100520024/ee6b8a48e6322e18a85a62ddcb01f432");
        urlLists.add("https://img01.sogoucdn.com/app/a/100520024/ebb532d5da0e26e285ac2dc025bc99ec");
        urlLists.add("https://img01.sogoucdn.com/app/a/100520024/83922cd9e4aaf9b4c012f08629a5e160");
        String titles[] ={" 足球 ","设计 时尚"," 风华绝代 一代巨星张国荣","发现时光的痕迹"};
        final SlideShowView slideShow = findViewById(R.id.slide_show);
        slideShow.setAdapter(new SlideAdapter(this,urlLists,titles));
        slideShow.setOnItemClickListener(new SlideShowView.OnViewPagerItemClickListener() {
            @Override
            public void onViewPagerItemClick(int position) {
                Toast.makeText(MainActivity.this,"点击了第"+position+"张图片", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
