package sgeorgiev.org.spaceshipgame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

public class Asteroid implements GameObject{
    //sprite
    private Bitmap bitmap;
    //coordinates
    private int x;
    private int y;
    //speed
    int speed = 1;

    private final int max = Constants.SCREEN_WIDTH + 60;
    private final int min = Constants.SCREEN_WIDTH;

    //random generator
    Random generator;
    //hit box
    private Rect hitBox;

    //constructor
    public Asteroid() {
        //load image
        bitmap = BitmapFactory.decodeResource(Constants.CURR_CONTEXT.getResources(), R.drawable.asteroid);
        //initialise random generator
        generator = new Random();
        //set coordinates
        x = generator.nextInt(max - min + 1) + min;
        y = generator.nextInt(Constants.SCREEN_HEIGHT) - bitmap.getHeight();

        //calculate hit box
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    @Override
    public void update(int playerSpeed) {
        //move the asteroid with the screen
        x -= playerSpeed;
        x -= this.speed;
        //when it reaches x = 0, move to the right again
        if(x < Constants.MIN_X) {
            x = generator.nextInt(max - min + 1) + min;
            y = generator.nextInt(Constants.SCREEN_HEIGHT) - bitmap.getHeight();
        }

        //update hit box every frame
        //had to reduce the hitbox as it wasn't very accurate hence the substractions
        hitBox.left = x + 35;
        hitBox.top = y + 38;
        hitBox.right = x + bitmap.getWidth() - 35;
        hitBox.bottom = y + bitmap.getHeight() - 35;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bitmap, x, y, paint);
    }

    //getter for the hitbox
    public Rect getHitBox() {
        return hitBox;
    }

}
