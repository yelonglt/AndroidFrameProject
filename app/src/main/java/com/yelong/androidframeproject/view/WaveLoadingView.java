package com.yelong.androidframeproject.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.yelong.androidframeproject.R;

/**
 * 自定义水波加载进度视图
 * Created by yelong on 16/8/11.
 * mail:354734713@qq.com
 */
public class WaveLoadingView extends View {
    //背景画笔
    private final Paint mSRCPaint;
    //水波纹画笔
    private Paint mPaint;
    //文字画笔
    private Paint mTextPaint;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private int y;
    private int x;

    private PorterDuffXfermode mMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    private Path mPath;
    private boolean isLeft;

    private int mWidth;
    private int mHeight;
    private int mPercent;

    public WaveLoadingView(Context context) {
        this(context, null);
    }

    public WaveLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取属性值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveLoadingView);
        float waveWidth = typedArray.getDimension(R.styleable.WaveLoadingView_wlvWaveWidth, 1000);
        float waveHeight = typedArray.getDimension(R.styleable.WaveLoadingView_wlvWaveHeight, 1000);
        int waveColor = typedArray.getColor(R.styleable.WaveLoadingView_wlvWaveColor, Color.parseColor("#8800ff66"));
        int waveBgColor = typedArray.getColor(R.styleable.WaveLoadingView_wlvWaveBgColor, Color.parseColor("#88dddddd"));
        typedArray.recycle();

        //初始化背景画笔
        mSRCPaint = new Paint();
        mSRCPaint.setAntiAlias(true);
        mSRCPaint.setColor(waveBgColor);

        //初始化水波纹画笔
        mPaint = new Paint();
        mPaint.setStrokeWidth(10);
        mPaint.setAntiAlias(true);
        mPaint.setColor(waveColor);

        //初始化文本画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(80);

        mPath = new Path();
        mBitmap = Bitmap.createBitmap((int) waveWidth, (int) waveHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        }

        y = mHeight;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (x > 50) {
            isLeft = true;
        } else if (x < 0) {
            isLeft = false;
        }

        if (isLeft) {
            x = x - 1;
        } else {
            x = x + 1;
        }
        mPath.reset();
        y = (int) ((1 - mPercent / 100f) * mHeight);
        mPath.moveTo(0, y);
        //绘制二阶贝塞尔曲线
        //mPath.quadTo(100 + x * 2, 50 + y,mWidth, y);
        //绘制三阶贝塞尔曲线
        mPath.cubicTo(100 + x * 2, 50 + y, 100 + x * 2, y - 50, mWidth, y);
        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.close();


        //清除掉图像 不然path会重叠
        mBitmap.eraseColor(Color.parseColor("#00000000"));
        mCanvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2, mSRCPaint);

        mPaint.setXfermode(mMode);
        //src
        mCanvas.drawPath(mPath, mPaint);
        mPaint.setXfermode(null);

        canvas.drawBitmap(mBitmap, 0, 0, null);

        String str = mPercent + "";
        float txtLength = mTextPaint.measureText(str);
        canvas.drawText(str, mWidth / 2 - txtLength / 2, mHeight / 2 + 15, mTextPaint);
        canvas.drawText("%", mWidth / 2 + 60, mHeight / 2 + 15, mTextPaint);

        postInvalidateDelayed(10);
    }

    public void setPercent(int percent) {
        mPercent = percent;
    }

}
