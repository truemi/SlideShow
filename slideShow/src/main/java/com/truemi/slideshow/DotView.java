package com.truemi.slideshow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by YJ on 2017-10-13.
 */

public class DotView extends View {
    private int RADUIS;
    private int MARGING;
    private int dotCount;
    private Paint paint;
    private int dotNormalColor;
    private int dotSelectColor;
    private int selectedPager;
    Context context;
    private int viewWidth;

    public DotView(Context context) {
        this(context, null);
    }

    public DotView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        paint = new Paint();
        paint.setColor(dotNormalColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCricle(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < dotCount; i++) {
            viewWidth = i * MARGING + RADUIS;
        }
       int viewHeight = RADUIS*2;
        setMeasuredDimension(viewWidth+RADUIS, viewHeight);
    }

    private void drawCricle(Canvas canvas) {

        for (int i = 0; i < dotCount; i++) {
            if (selectedPager == i) {
                paint.setColor(dotSelectColor);
            } else {
                paint.setColor(dotNormalColor);
            }
            canvas.drawCircle((i) * MARGING + RADUIS, RADUIS, RADUIS, paint);
        }
    }

    public DotView setRaduis(int raduis) {
        RADUIS = raduis;
        return this;
    }

    public DotView setMaring(int maring) {
        MARGING = maring;
        return this;
    }


    public DotView setDotCount(int count) {
        dotCount = count;
        return this;
    }

    public DotView setdotNormalColor(int color) {
        this.dotNormalColor = color;
        return this;
    }

    public DotView setdotSelectColor(int color) {
        this.dotSelectColor = color;
        paint.setColor(dotSelectColor);
        return this;
    }

    public DotView changePager(int pager) {
        this.selectedPager = pager;
        postInvalidate();
        return this;
    }
}
