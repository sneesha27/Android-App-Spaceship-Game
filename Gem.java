package sgeorgiev.org.spaceshipgame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

public class Gem implements GameObject {
    private int x, y, speed, points;
    private Bitmap bitmap;
    private Rect hitBox;
    private Random generator;
    private int type;
    private boolean shield = false;

    public Gem(int x, int y) {
        this.x = x;
        this.y = y;
        speed = 5;
        generator = new Random();

        type = generator.nextInt(6);

        if(type == 0 || type == 1) {
            bitmap = BitmapFactory.decodeResource(Constants.CURR_CONTEXT.getResources(), R.drawable.pu1);
            points = 20;
        } else if (type == 2 || type == 3) {
            bitmap = BitmapFactory.decodeResource(Constants.CURR_CONTEXT.getResources(), R.drawable.pu3);
            points = 30;
        } else if(type == 4){
            bitmap = BitmapFactory.decodeResource(Constants.CURR_CONTEXT.getResources(), R.drawable.pu2);
            points = 50;
        } else if (type == 5){
            bitmap = BitmapFactory.decodeResource(Constants.CURR_CONTEXT.getResources(), R.drawable.shieldicon);
            shield = true;
            points = 50;
        }

        hitBox = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if(Constants.TEST_MODE)
            canvas.drawRect(hitBox, paint);
        canvas.drawBitmap(bitmap, x, y, paint);
    }

    @Override
    public void update(int playerSpeed) {
        x -= this.speed;
        x -= playerSpeed;

        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();

    }

    public int getType() {
        return type;
    }

    public int getPoints() {
        return points;
    }

    public Rect getHitBox() {
        return hitBox;
    }

    public boolean isShield() {
        return shield;
    }
}
