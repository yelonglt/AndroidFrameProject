package com.yelong.androidframeproject.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.yelong.androidframeproject.R;


/**
 * 带阴影的圆形图片控件
 * Created by yelong on 16/9/13.
 * mail:354734713@qq.com
 */
public class ShadowCircleImageView extends ImageView {

    private int mBorderWidth = 0;
    private float mShadowRadius = 0.0f;
    private int mShadowColor = Color.BLACK;
    private float mShadowDx = 0.0f;
    private float mShadowDy = 0.0f;

    private int mWidth;
    private int mHeight;
    private Bitmap mBitmap;
    private Paint paint;
    private Paint paintBorder;

    public ShadowCircleImageView(Context context) {
        this(context, null);
    }

    public ShadowCircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowCircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShadowCircleImageView, defStyle, 0);

        mBorderWidth = a.getDimensionPixelSize(R.styleable.ShadowCircleImageView_sivBorderWidth, 0);
        mShadowRadius = a.getDimension(R.styleable.ShadowCircleImageView_sivShadowRadius, 0.0f);
        mShadowColor = a.getColor(R.styleable.ShadowCircleImageView_sivShadowColor, Color.BLACK);
        mShadowDx = a.getDimension(R.styleable.ShadowCircleImageView_sivShadowDx, 0.0f);
        mShadowDy = a.getDimension(R.styleable.ShadowCircleImageView_sivShadowDy, 0.0f);

        a.recycle();

        setup();
    }

    private void setup() {
        // init paint
        paint = new Paint();
        paint.setAntiAlias(true);

        paintBorder = new Paint();
        int borderColor = Color.WHITE;
        paintBorder.setColor(borderColor);
        paintBorder.setAntiAlias(true);
        this.setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);
        paintBorder.setShadowLayer(mShadowRadius, mShadowDx, mShadowDy, mShadowColor);
    }

    public void setBorderWidth(int borderWidth) {
        this.mBorderWidth = borderWidth;
        this.invalidate();
    }

    private void loadBitmap() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getDrawable();
        if (bitmapDrawable != null)
            mBitmap = bitmapDrawable.getBitmap();
    }

    @Override
    public void onDraw(Canvas canvas) {
        // load the bitmap
        loadBitmap();

        // init shader
        if (mBitmap != null) {
            BitmapShader shader = new BitmapShader(Bitmap.createScaledBitmap(mBitmap, canvas.getWidth(), canvas.getHeight(), false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            int circleCenter = mWidth / 2;

            canvas.drawCircle(circleCenter + mBorderWidth, circleCenter + mBorderWidth, circleCenter + mBorderWidth - 4.0f, paintBorder);
            canvas.drawCircle(circleCenter + mBorderWidth, circleCenter + mBorderWidth, circleCenter - 4.0f, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        mWidth = width - (mBorderWidth * 2);
        mHeight = height - (mBorderWidth * 2);

        setMeasuredDimension(width, height);
    }

    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        result = specMode == MeasureSpec.EXACTLY ? specSize : mWidth;

        return result;
    }

    private int measureHeight(int measureSpecHeight) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpecHeight);
        int specSize = MeasureSpec.getSize(measureSpecHeight);

        result = specMode == MeasureSpec.EXACTLY ? specSize : mHeight;

        return (result + 2);
    }
}