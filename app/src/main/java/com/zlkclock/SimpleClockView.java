package com.zlkclock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zhenglk on 2020-07-05.
 */
public class SimpleClockView extends View {
    private Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint timePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint datePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint footerPaint = new Paint();
    private int centerX;
    private int centerY;
    private int radius;
    private int textSizeTime;
    private int textSizeDate;
    private RectF mRectF = new RectF();
    String textTime = "00:00";
    String textDateYl = "00-00";
    String textDateNl = "00-00";
    String textDateWeek = "00";
    @SuppressLint("SimpleDateFormat")
    private final static SimpleDateFormat FMT_TIME = new SimpleDateFormat("HH:mm");
    @SuppressLint("SimpleDateFormat")
    private final static SimpleDateFormat FMT_DATA = new SimpleDateFormat("MM月dd日");
    final static String[] WEEK = {"日", "一", "二", "三", "四", "五", "六"};

    Rect rect = new Rect();

    private int batteryLevel = 30;
    private int batteryWidth;
    private int batteryHeight;

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
        textTime = FMT_TIME.format(new Date());

        footerPaint.setColor(0xFFCACACA);
        footerPaint.setAntiAlias(true);
        footerPaint.setTextAlign(Paint.Align.LEFT);

        Paint.FontMetrics metrics = footerPaint.getFontMetrics();
        batteryHeight = (int) (metrics.descent - metrics.ascent);
        batteryWidth = (int) (batteryHeight * 5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        centerX = width / 2;
        centerY = height / 2;
        radius = Math.max(centerX, centerY) - (int) dpToPixel(5);
        textSizeTime = (int) (width / 2.6);
        textSizeDate = width / 12;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        //灰
        canvas.save();
        circlePaint.setColor(Color.GRAY);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(dpToPixel(5));
        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        //时间
        timePaint.setColor(Color.GRAY);
        timePaint.setStyle(Paint.Style.FILL);
        timePaint.setTextSize(textSizeTime);
        canvas.drawText(textTime, centerX - timePaint.measureText(textTime) / 2,
                centerY - (timePaint.ascent() + timePaint.descent()) / 3, timePaint);

        //日期
        datePaint.setColor(Color.GRAY);
        datePaint.setStyle(Paint.Style.FILL);
        datePaint.setTextSize(textSizeDate);
        if (centerX > centerY) {
            String textDate = textDateYl + "," + textDateNl + "," + textDateWeek;
            canvas.drawText(textDate,
                    centerX - datePaint.measureText(textDate) / 2,
                    centerY - (datePaint.ascent() + datePaint.descent()) * 3,
                    datePaint);
        } else {
            canvas.drawText(textDateYl,
                    centerX - datePaint.measureText(textDateYl) / 2,
                    centerY - (datePaint.ascent() + datePaint.descent()) * 4,
                    datePaint);
            canvas.drawText(textDateNl,
                    centerX - datePaint.measureText(textDateNl) / 2,
                    centerY - (datePaint.ascent() + datePaint.descent()) * 6,
                    datePaint);
            String txtWeek = "星期" + textDateWeek;
            canvas.drawText(txtWeek,
                    centerX - datePaint.measureText(txtWeek) / 2,
                    centerY - (datePaint.ascent() + datePaint.descent()) * 8,
                    datePaint);
        }

        footerPaint.setColor(Color.GRAY);
        footerPaint.setAntiAlias(true);
        footerPaint.setTextAlign(Paint.Align.RIGHT);

        int batterLeft = centerX * 5 / 3;
        int batterTop = (int) (centerY + (timePaint.ascent() + timePaint.descent()) * 4 / 5);
        int batteryBorder = 2;
        int batterHeaderSize = 5;

        //电量框
        rect.left = batterLeft;
        rect.top = batterTop;
        rect.right = rect.left + batteryWidth;
        rect.bottom = rect.top + batteryHeight;

        footerPaint.setStyle(Paint.Style.STROKE);

        footerPaint.setStrokeWidth(batteryBorder);
        canvas.drawRect(rect, footerPaint);

        //电量框头部
        rect.left = batterLeft + batteryWidth;
        rect.top = (int) (batterTop + batteryHeight * 1f / 4);

        rect.right = rect.left + batterHeaderSize;
        rect.bottom = (int) (batterTop + batteryHeight * 3f / 4);
        footerPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, footerPaint);

        //电量
        float battery = (batteryWidth - batteryBorder - 1) * batteryLevel / 100f;
        rect.left = (int) (batterLeft + batteryBorder / 2f + 1);
        rect.top = (int) (batterTop + batteryBorder / 2f + 1);
        rect.right = (int) (batterLeft + battery);
        rect.bottom = (int) (batterTop + batteryHeight - batteryBorder / 2f - 1);
        canvas.drawRect(rect, footerPaint);
        canvas.restore();
    }

    public void showNewTime() {
        Date date = new Date();
        textTime = FMT_TIME.format(date);

        textDateYl = FMT_DATA.format(date);
        textDateNl = new Lunar(Calendar.getInstance()).toString();
        int weekDay;
        if ((weekDay = date.getDay()) <= 6) {
            textDateWeek = WEEK[weekDay];
        }

        postInvalidate();
    }

    public void showNewBatteryLevel(int level) {
        batteryLevel = level;
        postInvalidate();
    }
}
