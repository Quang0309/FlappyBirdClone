package com.example.admin.flappybirdclone;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class GameView extends View {
    Handler handler;
    Runnable runnable;
    Runnable loadBitmap;
    Runnable drawBitmap;

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
    int score = 0;
    int degree = 0;
    int i = -1;
    Paint paint;
    Matrix matrix;
    public GameView(Context context) {
        super(context);
        handler = new Handler();
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(100);
        topPipe = new Bitmap[2];
        botPipe = new Bitmap[2];
        display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        rect = new Rect(0,0,point.x,point.y);
        birds = new Bitmap[1];
        ran = new Random();
        pipeX = new int[2];
        pipeY = new int[2];
        matrix = new Matrix();

        loadBitmap = new Runnable() {
            @Override
            public void run() {
                background = BitmapFactory.decodeResource(getResources(),R.drawable.background_day);
                over = BitmapFactory.decodeResource(getResources(),R.drawable.gameover);
                topPipe[0] = BitmapFactory.decodeResource(getResources(),R.drawable.pipe_green);
                botPipe[0] = BitmapFactory.decodeResource(getResources(),R.drawable.pipe_green2);
                base = BitmapFactory.decodeResource(getResources(),R.drawable.base);
                birds[0] =  BitmapFactory.decodeResource(getResources(),R.drawable.b1);
                birdX = point.x/2-birds[birdFrame].getWidth()/2;
                birdY = point.y/3;
                topPipe[1] = topPipe[0];
                base = Bitmap.createScaledBitmap(base,point.x,base.getHeight(),false);
                botPipe[1] = botPipe[0];
                bitmap1 = new Rect(birdX-birds[birdFrame].getWidth()/2,birdY-birds[birdFrame].getHeight()/2,birdX+birds[birdFrame].getWidth()/2,birdY+birds[birdFrame].getHeight()/2);
                bitmap2 = new Rect(pipeX[index2%2]-topPipe[index2%2].getWidth()/2+birds[birdFrame].getWidth()/2,0,pipeX[index2%2]+topPipe[index2%2].getWidth()/2+birds[birdFrame].getWidth()/2,pipeY[index2%2]-birds[birdFrame].getWidth()/3);
                bitmap3 = new Rect(pipeX[index2%2]-topPipe[index2%2].getWidth()/2+birds[birdFrame].getWidth()/2,pipeY[index2%2]+gap-birds[birdFrame].getHeight()/2,pipeX[index2%2]+topPipe[index2%2].getWidth()/2+birds[birdFrame].getWidth()/2,point.y);
                gap = birds[0].getHeight()*4;
                pipeY[0] = ran.nextInt(topPipe[0].getHeight()/2-over.getHeight()*3)+over.getHeight()*4;
                pipeY[1] = ran.nextInt(topPipe[0].getHeight()/2-over.getHeight()*3)+over.getHeight()*4;
                pipeX[0] = point.x;
                pipeX[1] = point.x;
                rect.bottom = point.y-over.getHeight();
                runnable =new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                };

            }
        };

        handler.post(loadBitmap);



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(background,null,rect,null);
        handler.post(drawBitmap);
        if(birdY< point.y-base.getHeight() - birds[birdFrame].getHeight()&&gameOver==false) {
            if(degree<90)
                degree+=gravity;
            velocity += gravity;
            birdY += velocity;
            pipeX[index%2]-=pipeVelocity;
            matrix.setRotate(degree,birds[birdFrame].getWidth()/2,birds[birdFrame].getHeight()/2);
            matrix.postTranslate(birdX,birdY);
            canvas.drawBitmap(birds[birdFrame],/*birdX,birdY*/matrix,null);
        }
        else
            gameOver = true;
        /*matrix.setRotate(degree,birds[birdFrame].getWidth()/2,birds[birdFrame].getHeight()/2);
        matrix.postTranslate(birdX,birdY);
        canvas.drawBitmap(birds[birdFrame],*//*birdX,birdY*//*matrix,null);*/

        canvas.drawBitmap(topPipe[index%2],pipeX[index%2],pipeY[index%2] - topPipe[0].getHeight(),null);
        canvas.drawBitmap(botPipe[index%2],pipeX[index%2],pipeY[index%2]+gap,null);

        if (pipeX[index%2]<birdX-birds[birdFrame].getWidth()/2) {
            if (birdY < point.y -base.getHeight()- birds[birdFrame].getHeight() &&gameOver==false ) {
                pipeX[(index + 1) % 2] -= pipeVelocity;
                if(birdX-birds[0].getWidth()/2>pipeX[(i+1)%2]+topPipe[0].getWidth()/2)
                {
                    i++;
                    score++;
                }
            }
            else
                gameOver = true;
            canvas.drawBitmap(topPipe[(index + 1) % 2], pipeX[(index + 1) % 2], pipeY[(index + 1) % 2] - topPipe[0].getHeight(), null);
            canvas.drawBitmap(botPipe[(index + 1) % 2], pipeX[(index + 1) % 2], pipeY[(index + 1) % 2] + gap, null);
            }
        if(birdY<birds[birdFrame].getHeight())
        {
            gameOver = true;
        }

        if(pipeX[index%2]<0-topPipe[0].getWidth())
        {
            pipeX[index%2] = point.x;
            pipeY[index%2] = ran.nextInt(topPipe[0].getHeight()/2-over.getHeight()*3)+over.getHeight()*4;
            index++;
        }

        if(birdX-birds[birdFrame].getWidth()/2>pipeX[index%2]+topPipe[0].getWidth()/2) {
            //isCheck = false;

            index2++;
        }
        bitmap1.top = birdY-birds[birdFrame].getHeight()/2;
        bitmap1.bottom = birdY+birds[birdFrame].getHeight()/2;
        bitmap2.bottom = pipeY[index2%2]-birds[birdFrame].getHeight()/2;
        bitmap2.left = pipeX[index2%2]-topPipe[index2%2].getWidth()/2+birds[birdFrame].getWidth()/3;
        bitmap2.right = pipeX[index2%2]+topPipe[index2%2].getWidth()/2+birds[birdFrame].getWidth()/3;
        bitmap3.top = pipeY[index2%2]+gap-birds[birdFrame].getHeight()/2;
        bitmap3.left =  pipeX[index2%2]-topPipe[index2%2].getWidth()/2+birds[birdFrame].getWidth()/3;
        bitmap3.right = pipeX[index2%2]+topPipe[index2%2].getWidth()/2+birds[birdFrame].getWidth()/3;
        if(Rect.intersects(bitmap1,bitmap2)) {
            gameOver = true;

        }
        if(Rect.intersects(bitmap1,bitmap3)) {
            gameOver = true;

        }
        canvas.drawBitmap(base,0,point.y-base.getHeight(),null);
        if(gameOver ==true)
        {

            if(birdY < point.y -base.getHeight()- birds[birdFrame].getHeight()) {
                if(degree<90)
                    degree+=gravity;
                velocity += gravity;
                birdY += velocity;
            }
            matrix.setRotate(degree,birds[birdFrame].getWidth()/2,birds[birdFrame].getHeight()/2);
            matrix.postTranslate(birdX,birdY);
            canvas.drawBitmap(birds[birdFrame],matrix,null);
            canvas.drawBitmap(over,point.x/2-over.getWidth()/2,point.y/2-over.getHeight(),null);

        }
        canvas.drawText(Integer.toString(score),point.x/2,point.y/8, paint);

        handler.postDelayed(runnable,15);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN)
            if(gameOver==true)
            {
                if (event.getX()>point.x/2-over.getWidth()/2&&event.getX()<point.x/2+over.getWidth()/2
                        &&event.getY()<point.y/2+over.getHeight()/2&&event.getY()>point.y/2-over.getHeight()) {
                    birdX = point.x / 2 - birds[birdFrame].getWidth() / 2;
                    birdY = point.y / 3 /*- birds[birdFrame].getHeight() / 2*/;
                    pipeY[0] = ran.nextInt(topPipe[0].getHeight()/2-over.getHeight()*3)+over.getHeight()*4;
                    pipeY[1] = ran.nextInt(topPipe[0].getHeight()/2-over.getHeight()*3)+over.getHeight()*4;

                    pipeX[0] = point.x;
                    pipeX[1] = point.x;
                    birdFrame = 0;
                    velocity = 0;
                    gravity = 1.5f;
                    index = 0;
                    index2 = 0;
                    score = 0;
                    i = -1;
                    gameOver = false;
                    degree = 0;
                    invalidate();
                }
            }
            else {
                velocity = -22;
                degree-=40;
            }
        return true;
    }
}
