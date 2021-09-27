package sgeorgiev.org.spaceshipgame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

public class Planet implements GameObject {

    private Random generator;
    private Bitmap planet;
    private int x;
    private int y;
    private int speed;

    private final int max = Constants.SCREEN_WIDTH + 60;
    private final int min = Constants.SCREEN_WIDTH;

    public Planet() {
        generator = new Random();
        int bmp = generator.nextInt(2);
        if(bmp == 0)
            planet = BitmapFactory.decodeResource(Constants.CURR_CONTEXT.getResources(), R.drawable.bg1);
        else if (bmp == 2)
            planet = BitmapFactory.decodeResource(Constants.CURR_CONTEXT.getResources(), R.drawable.bg2);
        else
            planet = BitmapFactory.decodeResource(Constants.CURR_CONTEXT.getResources(), R.drawable.bg3);

        Log.d("BMP", "" + bmp);

        x = generator.nextInt(max - min + 1) + min;
        y = generator.nextInt(Constants.SCREEN_HEIGHT) - planet.getHeight();
        speed = generator.nextInt(10);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(planet, x, y, paint);
    }

    @Override
    public void update(int playerSpeed) {
        x -= playerSpeed;
        x -= speed;
    }

    public boolean getState() {
        if(x < Constants.MIN_X - planet.getWidth())
            return true;

        return false;
    }
}
