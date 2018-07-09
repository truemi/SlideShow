package com.truemi.slideshow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.BaseInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.truemi.slideshow.adapter.SlideAdapter;
import com.truemi.slideshow.utils.Uiutils;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by YJ on 2017-10-13.
 */

public class SlideShowView extends RelativeLayout implements ViewPager.OnPageChangeListener, View.OnTouchListener {

    private Context   context;
    private Paint     paint;
    private ViewPager viewPager;
    private int       RADUIS;//小圆点半径
    private int MARGING = 50;
    private int height;
    private int dotNormalColor = 0xFFFFFF;
    private int dotSelectColor = 0x000000;
    private PageChangeListener listener;
    private DotView            dot;
    private boolean            isTouch;//是否手势触摸
    private int                count;//轮播图数量
    private long               touchDownTime;
    private int                scaledEdgeSlop;
    private int                mPosX;
    private int                mPosY;
    private int                mCurPosX;
    private int                mCurPosY;
    private int                dotLocation;//小圆点的位置(-1,0,1),分别表示左边,居中,右边
    private float   bottomTextSize    = 12;//标题文字大小,sp
    private int     mTextBgColor = 0x44000000;//标题背景颜色
    private int     bottomTextColor   = 0x00e9e9e9;//标题文字颜色
    private int     mDuration         = 500;//滚动时间
    private int     mAutoStandTime    = 5000;//自动滚动停留时间
    private boolean haveBottomText    = true;//是否显示标题栏,默认显示
    private boolean mDotNavigation    = true;//是否显示小圆点,默认显示
    private TextView textView;//标题
    private int bottomTextHeight = 20;//dp,标题栏高度
    private String[]           titleText;//标题String数组
    private FixedSpeedScroller mScroller;

    public SlideShowView(Context context) {
        this(context, null);
    }

    public SlideShowView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SlideShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
        scaledEdgeSlop = ViewConfiguration.get(context).getScaledEdgeSlop();
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SlideShowView);
            RADUIS = (int) typedArray.getDimension(R.styleable.SlideShowView_mDotRaduis, 0);
            MARGING = 4 * RADUIS;
            dotNormalColor = typedArray.getColor(R.styleable.SlideShowView_mDotNormalColor, 0);
            dotSelectColor = typedArray.getColor(R.styleable.SlideShowView_mDotSelectColor, 0);
            dotLocation = typedArray.getInt(R.styleable.SlideShowView_mDotlocation, 1);
            mDuration = typedArray.getInt(R.styleable.SlideShowView_mDuration, 500);
            mAutoStandTime = typedArray.getInt(R.styleable.SlideShowView_mAutoStandTime, 5000);
            bottomTextHeight = typedArray.getDimensionPixelOffset(R.styleable.SlideShowView_mBottomTextViewHeight, 20);
            mDotNavigation = typedArray.getBoolean(R.styleable.SlideShowView_mDotNavigation, true);
            haveBottomText = typedArray.getBoolean(R.styleable.SlideShowView_mDuration, true);
            bottomTextSize = typedArray.getDimension(R.styleable.SlideShowView_mTextSize, 12);//sp
            bottomTextColor = typedArray.getColor(R.styleable.SlideShowView_mTextColor, 0x00e9e9e9);
            mTextBgColor = typedArray.getColor(R.styleable.SlideShowView_mTextBgColor, 0x1d000000);
            typedArray.recycle();

        }

        paint = new Paint();
        paint.setColor(dotNormalColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        viewPager = new ViewPager(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        viewPager.setLayoutParams(layoutParams);
        viewPager.setOnPageChangeListener(this);
        viewPager.setOnTouchListener(this);
        addView(viewPager);

        if (mDotNavigation) {
            drawCricle(0);
        }
        initBottomTextView();
        setDuration(mDuration,new DecelerateInterpolator());
    }

    /**
     * 初始化底部标题栏
     */
    private void initBottomTextView() {
        if (dotLocation == -1 || dotLocation == 1 && haveBottomText) {
            textView = new TextView(context);
            textView.setBackgroundColor(mTextBgColor);
            textView.setTextColor(bottomTextColor);
            textView.setTextSize(Uiutils.px2sp(context,bottomTextSize));
            if (dotLocation == -1) {
                textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                textView.setPadding(0, 0, 20, 0);
            } else {
                textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                textView.setPadding(20, 0, 0, 0);
            }
            Log.e("height","--------"+bottomTextHeight);
            LayoutParams layoutParamsText = new LayoutParams(LayoutParams.MATCH_PARENT, bottomTextHeight);
            layoutParamsText.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            addView(textView, layoutParamsText);
        }
    }

    public void setAdapter(SlideAdapter adapter) {
//        ArrayList<String> urlList = adapter.getpagerItemUrl();
//        int[] imgList = adapter.getpagerItemImg();
//        int[] imgs = new int[imgList.length+2];
//        if (urlList!=null&&urlList.size()>0){
//            String s = urlList.get(urlList.size() - 1);
//            urlList.add(0,s);
//            urlList.add(urlList.get(1));
//            adapter.setUrlList(urlList);
//        }
//        if (imgList!=null&&imgList.length>0){
//            int length = imgList.length;
//            LogUtil.e(length+" 长度----------------------");
//            imgs[0] =imgList[length-1];
//            for (int i = 0; i < imgList.length; i++) {
//                imgs[i+1] =imgList[i];
//            }
//            imgs[imgs.length-1] = imgList[0];
//            adapter.setImgsList(imgs);
//        }
        count = adapter.getImgCount();
        titleText = adapter.getTitleText();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2);
        mHandler.sendEmptyMessageDelayed(0, mAutoStandTime);
    }


    public void setBottomTextBg(int color) {
        if (textView == null) return;
        this.textView.setBackgroundColor(color);
    }

    public void setBottomTextColor(int color) {
        if (textView == null) return;
        this.textView.setTextColor(color);
    }

    public void setBottomTextSize(int size) {
        if (textView == null) return;
        this.textView.setTextSize(Uiutils.dp2px(context, size));
    }

    /**
     * 设置动画时间
     * @param mDuration
     */
    public void  setDuration(int mDuration, Interpolator interpolator){
        this.mDuration =mDuration;
        try {
            // 通过class文件获取mScroller属性
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(viewPager.getContext(), interpolator);
            mField.set(viewPager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置transformer
     *
     * @param b
     * @param transformer
     */
    public void setPageTransformer(boolean b, ViewPager.PageTransformer transformer) {
        if (viewPager != null) {
            viewPager.setPageTransformer(b, transformer);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownTime = System.currentTimeMillis();
                mPosX = (int) motionEvent.getX();
                mPosY = (int) motionEvent.getY();
            case MotionEvent.ACTION_MOVE:
                isTouch = true;
                mCurPosX = (int) motionEvent.getX();
                mCurPosY = (int) motionEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                long touchUpTime = System.currentTimeMillis();
                long l = touchUpTime - touchDownTime;
                //触摸移动距离小于滑动识别距离,触摸时间小于200ms确认为点击事件
                if (scaledEdgeSlop > (mCurPosY - mPosY) && scaledEdgeSlop > (mCurPosX - mPosX) && l < 200) {
                    if (clickListener != null)
                        clickListener.onViewPagerItemClick(viewPager.getCurrentItem()%count);
                }
                isTouch = false;
                //延时1秒,如果没有触摸就自动开始滚动
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (!isTouch) {
                            mHandler.sendEmptyMessageDelayed(0, mAutoStandTime - 1000);
                        }
                    }
                }, 1000);

                break;
        }

        return false;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
    }


    private int     itemPosition = 0;
    private Handler mHandler     = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            mHandler.removeMessages(0);
            if (!isTouch) {
//                if (itemPosition == count-1) {
//                    itemPosition = 1;
//                    viewPager.setCurrentItem(itemPosition, false);
//                    mHandler.sendEmptyMessageDelayed(0, 0);
//                }else if (itemPosition==0){
//                    itemPosition = count-1;
//                    viewPager.setCurrentItem(itemPosition, false);
//                    mHandler.sendEmptyMessageDelayed(0, 0);
//                }else {
                itemPosition++;
                viewPager.setCurrentItem(itemPosition, true);
                mHandler.sendEmptyMessageDelayed(0, mAutoStandTime);
//                }

            }
            return false;
        }
    });


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (listener != null) {
            listener.onPageScrolled(position% count, positionOffset, positionOffsetPixels);
        }

//        if (position == 0 && positionOffset == 0) viewPager .setCurrentItem(count - 1, false);
//        else if (position == count - 1 && positionOffset == 0) viewPager .setCurrentItem(1, false);
    }

    @Override
    public void onPageSelected(int position) {
        if (listener != null)
            listener.onPageSelected(position% count);
        itemPosition = position;
        viewPager.setCurrentItem(position);
        mScroller.setmDuration(mDuration);//设置页面切换时间
        if (textView != null && titleText != null && titleText.length > 0) {
            textView.setText(titleText[position % count]);
        }
        drawCricle(position % count);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (listener != null)
            listener.onPageScrollStateChanged(state);
    }

    /**
     * 绘制小圆点
     *
     * @param position
     */
    private void drawCricle(int position) {
        if (dot != null)
            removeView(dot);
        dot = new DotView(context);

        dot.setDotCount(count)
                .setdotNormalColor(dotNormalColor)
                .setdotSelectColor(dotSelectColor)
                .setMaring(MARGING).setRaduis(RADUIS);
        LayoutParams layoutParamsDot2 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RADUIS * 2);

        layoutParamsDot2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        if (dotLocation == 0) {
            layoutParamsDot2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            layoutParamsDot2.setMargins(0, 0, 0, bottomTextHeight / 3);
        } else if (dotLocation == -1) {
            layoutParamsDot2.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            layoutParamsDot2.setMargins(30, 0, 0, bottomTextHeight / 3);
        } else if (dotLocation == 1) {
            layoutParamsDot2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            layoutParamsDot2.setMargins(0, 0, 30, bottomTextHeight / 3);
        }
        dot.changePager(position);
        addView(dot, layoutParamsDot2);

    }


    /**
     * 页面切换监听
     */
    public interface PageChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }

    public void setOnPageChangeListener(PageChangeListener listener) {
        this.listener = listener;
    }


    /**
     * 页面点击监听
     */
    public interface OnViewPagerItemClickListener {
        void onViewPagerItemClick(int position);
    }

    private OnViewPagerItemClickListener clickListener;

    public void setOnItemClickListener(OnViewPagerItemClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
