package com.example.admin.flappybirdclone;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Random;


public class GameView extends View {
    Handler handler;
    Runnable runnable;
    Display display;
    Point point;
    Rect rect;
    Bitmap background;
    Bitmap birds[];
    Bitmap base;
    int birdFrame = 0, birdX, birdY;
    float velocity = 0, gravity = 1.5f;
    int pipeX[], pipeY[];
    int gap;
    Bitmap topPipe[], botPipe[];
    Random ran;
    int index = 0;
    int index2 = 0;
    int pipeVelocity=5;
    boolean gameOver = false;
    Bitmap over;
    Rect bitmap1;
    Rect bitmap2;
    Rect bitmap3;
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
        topPipe = new Bitmap[2];
        botPipe = new Bitmap[2];
        topPipe[0] = BitmapFactory.decodeResource(getResources(),R.drawable.pipe_green);
        botPipe[0] = BitmapFactory.decodeResource(getResources(),R.drawable.pipe_green2);
        base = BitmapFactory.decodeResource(getResources(),R.drawable.base);
        over = BitmapFactory.decodeResource(getResources(),R.drawable.gameover);
        display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        rect = new Rect(0,0,point.x,point.y);


        birds = new Bitmap[3];
        birds[0] =  BitmapFactory.decodeResource(getResources(),R.drawable.b1);
        birds[1] =  BitmapFactory.decodeResource(getResources(),R.drawable.b2);
        birds[2] =  BitmapFactory.decodeResource(getResources(),R.drawable.b3);
        birdX = point.x/2-birds[birdFrame].getWidth()/2;
        birdY = point.y/2-birds[birdFrame].getHeight()/2;
        ran = new Random();
        pipeX = new int[2];
        pipeY = new int[2];
        pipeY[0] = ran.nextInt(ran.nextInt(point.y/2-300))+300;
        pipeY[1] = ran.nextInt(ran.nextInt(point.y/2-300))+300;
        gap = birds[0].getHeight()*4/*+birds[0].getHeight()/2*/;
        pipeX[0] = point.x;
        pipeX[1] = point.x;
        topPipe[0] = Bitmap.createScaledBitmap(topPipe[0],topPipe[0].getWidth(),1500,false);
        topPipe[1] = topPipe[0];
        botPipe[0] = Bitmap.createScaledBitmap(botPipe[0],topPipe[0].getWidth(),1500,false);
        botPipe[1] = botPipe[0];
        base = Bitmap.createScaledBitmap(base,point.x,base.getHeight(),false);
        bitmap1 = new Rect(birdX-birds[birdFrame].getWidth()/2,birdY-birds[birdFrame].getHeight()/2,birdX+birds[birdFrame].getWidth()/2,birdY+birds[birdFrame].getHeight()/2);
        bitmap2 = new Rect(pipeX[index2%2]-topPipe[index2%2].getWidth()/2+birds[birdFrame].getWidth()/2,0,pipeX[index2%2]+topPipe[index2%2].getWidth()/2+birds[birdFrame].getWidth()/2,pipeY[index2%2]-birds[birdFrame].getWidth()/3);
        bitmap3 = new Rect(pipeX[index2%2]-topPipe[index2%2].getWidth()/2+birds[birdFrame].getWidth()/2,pipeY[index2%2]+gap-birds[birdFrame].getHeight()/2,pipeX[index2%2]+topPipe[index2%2].getWidth()/2+birds[birdFrame].getWidth()/2,point.y);
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
        if(birdY< point.y-base.getHeight() - birds[birdFrame].getHeight()&&gameOver==false) {

            velocity += gravity;
            birdY += velocity;
            pipeX[index%2]-=pipeVelocity;
        }
        else
            gameOver = true;
        canvas.drawBitmap(birds[birdFrame],birdX,birdY,null);
        canvas.drawBitmap(topPipe[index%2],pipeX[index%2],pipeY[index%2] - 1500,null);
        canvas.drawBitmap(botPipe[index%2],pipeX[index%2],pipeY[index%2]+gap,null);

        if (pipeX[index%2]<birdX) {
            if (birdY < point.y -base.getHeight()- birds[birdFrame].getHeight() &&gameOver==false) {
                pipeX[(index + 1) % 2] -= pipeVelocity;
            }
            else
                gameOver = true;
                canvas.drawBitmap(topPipe[(index + 1) % 2], pipeX[(index + 1) % 2], pipeY[(index + 1) % 2] - 1500, null);
                canvas.drawBitmap(botPipe[(index + 1) % 2], pipeX[(index + 1) % 2], pipeY[(index + 1) % 2] + gap, null);
            }



        if(pipeX[index%2]<0-topPipe[0].getWidth())
        {
            pipeX[index%2] = point.x;
            pipeY[index%2] = ran.nextInt(ran.nextInt(point.y/2-300))+300;
            index++;
        }
        /*if(birdX+birds[birdFrame].getWidth()/2>pipeX[0]-topPipe[0].getWidth()/2&&birdX+birds[birdFrame].getWidth()/2<pipeX[0]+topPipe[0].getWidth()/2)
            if(birdY+birds[birdFrame].getHeight()/2>pipeY[0]+gap ||birdY-birds[birdFrame].getHeight()/2<pipeY[0] - 1500)
            gameOver = true;*/
        if(birdX-birds[birdFrame].getWidth()/2>pipeX[index%2])
            index2++;

        bitmap1.top = birdY-birds[birdFrame].getHeight()/2;
        bitmap1.bottom = birdY+birds[birdFrame].getHeight()/2;
        bitmap2.bottom = pipeY[index2%2]-birds[birdFrame].getWidth()/3;
        bitmap2.left = pipeX[index2%2]-topPipe[index2%2].getWidth()/2+birds[birdFrame].getWidth()/2;
        bitmap2.right = pipeX[index2%2]+topPipe[index2%2].getWidth()/2+birds[birdFrame].getWidth()/2;
        bitmap3.top = pipeY[index2%2]+gap-birds[birdFrame].getHeight()/2;
        bitmap3.left =  pipeX[index2%2]-topPipe[index2%2].getWidth()/2+birds[birdFrame].getWidth()/2;
        bitmap3.right = pipeX[index2%2]+topPipe[index2%2].getWidth()/2+birds[birdFrame].getWidth()/2;
        if(Rect.intersects(bitmap1,bitmap2))
            gameOver = true;
        if(Rect.intersects(bitmap1,bitmap3))
            gameOver = true;
        canvas.drawBitmap(base,0,point.y-base.getHeight(),null);
        if(gameOver ==true)
        {

            if(birdY < point.y -base.getHeight()- birds[birdFrame].getHeight()) {
                velocity += gravity;
                birdY += velocity;
            }
            canvas.drawBitmap(birds[birdFrame],birdX,birdY,null);
            canvas.drawBitmap(over,point.x/2-over.getWidth()/2,point.y/2-over.getHeight(),null);
        }

        handler.postDelayed(runnable,15);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN)

            velocity=-22;
        return true;
    }
}
