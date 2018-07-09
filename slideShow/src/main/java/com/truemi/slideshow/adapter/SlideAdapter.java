package com.truemi.slideshow.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * 新手引导界面GuidAdapter
 * Created by yuanjian on 2017/3/1.
 */

public class SlideAdapter extends PagerAdapter {

    public Activity          activity;
    public int[]             imgId;
    public String[]             texts;
    public ArrayList<String> urlList;

    public SlideAdapter(Activity activity, int[] imgId) {
        this.activity = activity;
        this.imgId = imgId;
    }
    public SlideAdapter(Activity activity) {
        this.activity = activity;

    }
    public SlideAdapter(Activity activity, ArrayList<String> urlList) {
        this.activity = activity;
        this.urlList = urlList;
    }
    public SlideAdapter(Activity activity, ArrayList<String> urlList, String[] texts) {
        this.activity = activity;
        this.urlList = urlList;
        this.texts = texts;
    }

    public void setUrlList(ArrayList<String> urlList) {
        this.urlList = urlList;
        notifyDataSetChanged();
    }
    public void setImgsList(int[] imgs) {
        this.imgId = imgs;
        notifyDataSetChanged();
    }

    public ArrayList<String> getpagerItemUrl() {
        return urlList;
    }
    public int[] getpagerItemImg() {
        return imgId;
    }
    public String[] getTitleText() {
        return this.texts;
    }
    public int getImgCount(){
        if (imgId != null && imgId.length > 0) {
            return imgId.length;
        } else if (urlList != null && urlList.size() > 0) {
            return urlList.size();
        }
        return 0;
    }

    @Override
    public int getCount() {

        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(activity);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (imgId != null && imgId.length > 0) {
            Glide.with(activity).load(imgId[position%imgId.length]).into(imageView);
        } else if (urlList != null && urlList.size() > 0) {
            Glide.with(activity).load(urlList.get(position%urlList.size())).into(imageView);
        }
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
