package sgeorgiev.org.spaceshipgame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Projectile implements GameObject {
    //sprite
    private Bitmap bitmap;
    //speed
    private int speed;
    //coordinates
    private int x;
    private int y;

    //hitbox to detect collisions
    private Rect hitBox;

    //constructor, set x, y, speed
    public Projectile(int x, int y, int speed, String type) {
        if(type.equals("player")) {
            bitmap = BitmapFactory.decodeResource(Constants.CURR_CONTEXT.getResources(), R.drawable.projectile);
        } else if (type.equals("enemy"))
            bitmap = BitmapFactory.decodeResource(Constants.CURR_CONTEXT.getResources(), R.drawable.enemyproj);

        this.x = x;
        this.y = y;
        this.speed = speed;
        hitBox = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    //override the draw method
    @Override
    public void draw(Canvas canvas, Paint paint) {
        if(Constants.TEST_MODE) {
            paint.setColor(Color.WHITE);
            canvas.drawRect(hitBox, paint);
        }
        canvas.drawBitmap(bitmap, x, y, paint);
    }

    public void update() {
        x += speed;
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
    }

    public void update(String direction, int playerSpeed) {
        x -= speed;
        x-=playerSpeed;
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
    }

    public float getSpeed() {
        return speed;
    }

    @Override
    public void update(int speed) {}

    public boolean checkState() {
        if (this.x > Constants.SCREEN_WIDTH)
                return true;
        else return false;
    }

    public Rect getHitBox() {
        return hitBox;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
