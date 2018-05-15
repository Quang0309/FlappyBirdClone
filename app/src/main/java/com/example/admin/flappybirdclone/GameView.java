package com.example.admin.flappybirdclone;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.View;



public class GameView extends View {
    Handler handler;
    Runnable runnable;
    Display display;
    Point point;
    Rect rect;
    Bitmap background;
    Bitmap birds[];
    int birdFrame = 0;
    public GameView(Context context) {
        super(context);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        background = BitmapFactory.decodeResource(getResources(),R.drawable.background_day);
        display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        rect = new Rect(0,0,point.x,point.y);
        birds = new Bitmap[3];
        birds[0] =  BitmapFactory.decodeResource(getResources(),R.drawable.b1);
        birds[1] =  BitmapFactory.decodeResource(getResources(),R.drawable.b2);
        birds[2] =  BitmapFactory.decodeResource(getResources(),R.drawable.b3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background,null,rect,null);
        if (birdFrame==0)
            birdFrame = 1;
        else if(birdFrame==1)
            birdFrame=2;
        else
            birdFrame=0;
        canvas.drawBitmap(birds[birdFrame],point.x/2-birds[birdFrame].getWidth()/2,point.y/2-birds[birdFrame].getHeight()/2,null);
        handler.postDelayed(runnable,30);
    }
}
