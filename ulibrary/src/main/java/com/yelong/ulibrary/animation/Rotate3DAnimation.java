package com.yelong.ulibrary.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by yelong on 2018/3/8.
 * mail:354734713@qq.com
 */
public class Rotate3DAnimation extends Animation {

    private float startDegree;
    private float endDegree;
    private float centerX;
    private float centerY;
    private float deepZ;
    private DIRECTION direction;
    private boolean reverse;

    //辅助实现3D效果
    private Camera mCamera;

    public enum DIRECTION {
        X, Y
    }

    /**
     * 创建3D动画
     *
     * @param startDegree 开始角度
     * @param endDegree   结束角度
     * @param centerX     中心点X坐标
     * @param centerY     中心点Y坐标
     * @param deepZ       控制镜头景深，不需要给0就好
     * @param reverse     旋转方向，true为反方向，false为正向
     */
    public Rotate3DAnimation(float startDegree, float endDegree, float centerX, float centerY,
                             float deepZ, DIRECTION direction, boolean reverse) {
        this.startDegree = startDegree;
        this.endDegree = endDegree;
        this.centerX = centerX;
        this.centerY = centerY;
        this.deepZ = deepZ;
        this.direction = direction;
        this.reverse = reverse;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float fromDegree = startDegree;
        float degree = fromDegree + (endDegree - startDegree) * interpolatedTime;

        final Matrix matrix = t.getMatrix();

        mCamera.save();
        if (reverse) {
            mCamera.translate(0, 0, deepZ * interpolatedTime);
        } else {
            mCamera.translate(0, 0, deepZ * (1 - interpolatedTime));
        }
        if (direction == DIRECTION.Y) {
            mCamera.rotateY(degree);
        } else {
            mCamera.rotateX(degree);
        }
        mCamera.getMatrix(matrix);
        mCamera.restore();

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}
