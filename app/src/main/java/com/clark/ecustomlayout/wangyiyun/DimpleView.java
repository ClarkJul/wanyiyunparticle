package com.clark.ecustomlayout.wangyiyun;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DimpleView extends View {

    private float[] pos;
    private int pathRadius;

    public DimpleView(Context context) {
        this(context, null);
    }

    public DimpleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DimpleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private List<Particle> particlesContainer;//存放粒子的容器
    private Paint mPaint;


    private float mCenterX;//中心点的x坐标
    private float mCenterY;//中心点的y坐标

    private float pRadius;
    private int pNumber = 1500;//粒子的数量
    private int maxOffset = 160;//粒子移动的最大距离

    private ValueAnimator animator;

    Random random = new Random();

    private Path mPath;//散发粒子的圆的路径
    private PathMeasure mPathMeasure;

    private void init() {
        particlesContainer = new ArrayList<>();
        mPaint = new Paint();
        mPath = new Path();
        pRadius = dip2px(getContext(), 1f);
        pathRadius = dip2px(getContext(), 80);
        mPathMeasure = new PathMeasure();
        pos = new float[2];
    }

    private void initAnimator() {
        animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(4000);
        animator.setRepeatCount(-1);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();
                updateParticlesStatus(v);
                invalidate();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
        mPath.addCircle(mCenterX, mCenterY, pathRadius, Path.Direction.CCW);
        mPathMeasure.setPath(mPath, false);
        for (int i = 0; i < pNumber; i++) {
            //获取圆形路径上的点的坐标，存放在数组pos中，第一个参数表示路径的距离
            mPathMeasure.getPosTan(i * 1f / pNumber * mPathMeasure.getLength(), pos, null);
            //根据反余弦函数拿到坐标点到圆心连线与x轴的夹角
            Particle particle = new Particle(pos[0], pos[1], pRadius, getRandomSpeed(), 255, (float) Math.acos((pos[0] - mCenterX) / pathRadius), 0);
            particlesContainer.add(particle);
        }
        initAnimator();
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawParticles(canvas);
    }

    /**
     * 绘制粒子
     *
     * @param canvas
     */
    private void drawParticles(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        if (!particlesContainer.isEmpty()) {
            for (int i = 0; i < particlesContainer.size(); i++) {
                Particle particle = particlesContainer.get(i);
                mPaint.setAlpha(particle.alpha);
                canvas.drawCircle(particle.x, particle.y, particle.radius, mPaint);
                mPaint.setAlpha(255);
            }
        }
    }

    /**
     * 更新粒子的状态
     *
     * @param frag
     */
    private void updateParticlesStatus(float frag) {
        for (int i = 0; i < particlesContainer.size(); i++) {
            Particle particle = particlesContainer.get(i);
            if (particle.offset > maxOffset) {
                //当粒子移动超过最大距离时，重置粒子状态
                particle.offset = 0;
                particle.speed=getRandomSpeed();
                float[] floats = new float[2];
                mPathMeasure.getPosTan(random.nextInt(pNumber) *1f/ pNumber * mPathMeasure.getLength(), floats, null);
                particle.x=floats[0];
                particle.y=floats[1];
            }
            //粒子移动时更新其x轴的坐标
            particle.x = (float) (mCenterX + Math.cos(particle.angle) * (pathRadius + particle.offset));

            //粒子移动时更新其y轴的坐标
            if (particle.y > mCenterY) {
                particle.y = (float) (Math.sin(particle.angle) * (pathRadius + particle.offset) + mCenterY);
            } else {
                particle.y = (float) (mCenterY - Math.sin(particle.angle) * (pathRadius + particle.offset));
            }
            //粒子的透明度随着移动距离不断减小，直至消失
            particlesContainer.get(i).alpha = 255 - (int) (255 * particle.offset / maxOffset);
            //更新粒子的移动距离
            particle.offset += particle.speed / 10;
        }
    }

    private int getRandomSpeed() {
        return random.nextInt(30) + 10;
    }

    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
