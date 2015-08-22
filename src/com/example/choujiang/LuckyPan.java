package com.example.choujiang;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by jingdongqi on 8/22/15.
 */
public class LuckyPan extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mHolder;

    private Canvas mCanvas;


    private Thread t;

    private boolean isRunning;

    private String[] mStrs = new String[]{"单反相机", "IPAD", "恭喜发财", "IPHONE", "服装一套", "恭喜发财"};

    private int[] mImgs = new int[]{R.drawable.danfan, R.drawable.ipad, R.drawable.f040, R.drawable.iphone, R.drawable.meizi, R.drawable.f040};

    private Bitmap[] mImagesBitmap;

    private int[] mColor = new int[]{0xFFFFc300, 0xFFF17E01, 0xFFFFc300, 0xFFF17E01, 0xFFFFc300, 0xFFF17E01};

    private int mItemCount = 6;


    private RectF mRang = new RectF();

    private int mRadius;

    private Paint mArcPaint;

    private Paint mTextPaint;


    private double mSpeed = 5;

    private volatile float mStartAngle = 0;

    private boolean isShouldEnd;

    private int mCenter;

    private int mPadding;

    private Bitmap mBgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);

    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());


    public LuckyPan(Context context) {
        super(context, null);
    }

    public LuckyPan(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHolder = getHolder();

        mHolder.addCallback(this);


        //可获得焦点

        setFocusable(true);
        setFocusableInTouchMode(true);

        //设置常量

        setKeepScreenOn(true);


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mPadding = getPaddingLeft();

        mRadius = width - mPadding * 2;

        mCenter = width / 2;

        setMeasuredDimension(width, width);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setTextSize(mTextSize);

        mRang = new RectF(mPadding, mPadding, mPadding + mRadius, mPadding + mRadius);

        mImagesBitmap = new Bitmap[mItemCount];

        for (int i = 0; i < mItemCount; i++) {
            mImagesBitmap[i] = BitmapFactory.decodeResource(getResources(), mImgs[i]);
        }


        isRunning = true;

        t = new Thread(this);
        t.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;

    }

    @Override
    public void run() {


        while (isRunning) {

            long start = System.currentTimeMillis();

            draw();

            long end = System.currentTimeMillis();

            if (end - start < 50) {

                try {
                    Thread.sleep(50 - (end - start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }


    }

    private void draw() {

        try {
            mCanvas = mHolder.lockCanvas();

            if (mCanvas != null) {

                drawBg();

                float tmpAngle = mStartAngle;

                float sweepAngle = 360 / mItemCount;

                for (int i = 0;i<mItemCount;i++){

                    mArcPaint.setColor(mColor[i]);

                    mCanvas.drawArc(mRang, tmpAngle, sweepAngle, true, mArcPaint);

                    drawText(tmpAngle, sweepAngle, mStrs[i]);

                    drawIcon(tmpAngle,mImagesBitmap[i]);

                    tmpAngle += sweepAngle;



                }

                mStartAngle += mSpeed;

                if (isShouldEnd){

                    mSpeed -=1;
                }

                if (mSpeed<=0){

                    mSpeed = 0;

                    isShouldEnd = false;

                }



            }
        } catch (Exception e) {


        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }


    }

    private void drawIcon(float tmpAngle, Bitmap bitmap) {

        int imgWidth = mRadius /8;

        float angle = (float)((tmpAngle+360/mItemCount/2)*Math.PI/180);

        int x = (int)(mCenter + mRadius/4*Math.cos(angle));
        int y = (int)(mCenter + mRadius/4*Math.sin(angle));


        Rect rect = new Rect(x-imgWidth/2,y-imgWidth/2,x+imgWidth/2,y+imgWidth/2);

        mCanvas.drawBitmap(bitmap,null,rect,null);



    }

    private void drawText(float tmpAngle, float sweepAngle, String mStr) {

        Path p = new Path();
        p.addArc(mRang,tmpAngle,sweepAngle);

        float textWidth = mTextPaint.measureText(mStr);

        int hOffset = (int)(mRadius*Math.PI/mItemCount/2 - textWidth/2);

        int vOffset = mRadius /2 /6;

        mCanvas.drawTextOnPath(mStr,p,hOffset,vOffset,mTextPaint);




    }

    private void drawBg() {

        mCanvas.drawColor(0xffffffff);

        mCanvas.drawBitmap(mBgBitmap,null,new Rect((int)(mPadding/2),(int)mPadding/2,(int)(getMeasuredWidth() - mPadding/2) ,(int)(getMeasuredHeight()-mPadding/2)),null);


    }



    public void luckyStart(int index){


        float angle = 360 / mItemCount;

        float from = 270 -(index+1)*angle;
        float end = from + angle;


        //设置停下来需要旋转的距离

        float targetFrom = 4*360 + from;
        float targetEnd = 4*360 + end;


        float v1 =  (float)((-1+Math.sqrt(1+8*targetFrom))/2);
        float v2 =  (float)((-1+Math.sqrt(1+8*targetEnd))/2);


        mSpeed = v1+Math.random()*(v2-v1);



        mSpeed = 10;
        isShouldEnd = false;

    }

    public void luckyEnd(){

        isShouldEnd = true;

        mStartAngle = 0;

    }


    public boolean isStart(){

        return mSpeed!=0;



    }

    public boolean isShouldEnd(){

        return isShouldEnd;
    }



}
