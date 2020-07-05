package com.zlkclock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhenglk on 2020-07-05.
 */
public class SimpleClockView extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint timePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint datePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int centerX;
    private int centerY;
    private int radius;
    private int textSizeTime;
    private int textSizeDate;
    private RectF mRectF = new RectF();
    public static final float START_ANGLE = -90;
    String textTime = "00:00";
    String textDate = "0000-00-00";
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat fmt_time = new SimpleDateFormat("HH:mm");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat fmt_data = new SimpleDateFormat("yyyy-MM-dd");
    final static String[] WEEK = {"日", "一", "二", "三", "四", "五", "六"};


    public SimpleClockView(Context context) {
        super(context);
        init();

    }

    public SimpleClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimpleClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public static float dpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * metrics.density;
    }

    void init() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        textTime = sdf.format(new Date());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        centerX = width / 2;
        centerY = height / 2;
        radius = Math.max(centerX, centerY) - (int) dpToPixel(10);
        textSizeTime = (int) (width / 2.7);
        textSizeDate = width / 10;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        //灰
        canvas.save();
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dpToPixel(5));
        canvas.drawCircle(centerX, centerY, radius, mPaint);
        canvas.restore();

        //时间
        canvas.save();
        timePaint.setColor(Color.GRAY);
        timePaint.setStyle(Paint.Style.FILL);
        timePaint.setTextSize(textSizeTime);

        canvas.drawText(textTime, centerX - timePaint.measureText(textTime) / 2,
                centerY - (timePaint.ascent() + timePaint.descent()) / 3, timePaint);
        canvas.restore();

        //日期
        canvas.save();
        datePaint.setColor(Color.GRAY);
        datePaint.setStyle(Paint.Style.FILL);
        datePaint.setTextSize(textSizeDate);

        canvas.drawText(textDate,
                centerX - datePaint.measureText(textDate) / 2,
                (float) (centerY - (datePaint.ascent() + datePaint.descent()) * 3),
                datePaint);
        canvas.restore();
    }

    public void showNewTime() {
        Date date = new Date();
        textTime = fmt_time.format(date);
        int weekDay = -1;
        if ((weekDay = date.getDay()) <= 6) {
            textDate = fmt_data.format(date) + " " + WEEK[weekDay];
        } else {
            textDate = fmt_data.format(date);
        }
        postInvalidate();
    }

}
