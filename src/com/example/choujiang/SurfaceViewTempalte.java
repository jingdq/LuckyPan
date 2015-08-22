package com.example.choujiang;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by jingdongqi on 8/22/15.
 */
public class SurfaceViewTempalte extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mHolder;

    private Canvas mCanvas;


    private Thread t;

    private boolean isRunning;




    public SurfaceViewTempalte(Context context) {
        super(context, null);
    }

    public SurfaceViewTempalte(Context context, AttributeSet attrs) {
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
    public void surfaceCreated(SurfaceHolder holder) {

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


            draw();

        }


    }

    private void draw() {

        try {
            mCanvas = mHolder.lockCanvas();

            if (mCanvas != null) {






            }
        } catch (Exception e) {


        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }


    }
}
