package sgeorgiev.org.spaceshipgame;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class Star implements GameObject{
    private int x;
    private int y;
    private int speed;

    Random generator;

    public Star()
    {
        //create a random generator
        generator = new Random();
        speed = generator.nextInt(10);

        //generate random coordinates within the screen
        //move it to the right
        x = generator.nextInt(Constants.SCREEN_WIDTH);
        y = generator.nextInt(Constants.SCREEN_HEIGHT);
    }

    @Override
    public void draw(Canvas canvas, Paint paint)
    {
            paint.setStrokeWidth(this.getStarWidth());
            canvas.drawPoint(x, y, paint);
    }

    @Override
    public void update(int playerSpeed)
    {
        //move stars left using player speed
        x -= playerSpeed;
        x -= speed;

        //if it reaches the left side of the screen
        if (x < 0)
        {
            x = Constants.SCREEN_WIDTH;
            y = generator.nextInt(Constants.SCREEN_HEIGHT);
            speed = generator.nextInt(15);
        }
    }

    //generate random star width
    public float getStarWidth()
    {
        float minX = 1.0f;
        float maxX = 4.0f;
        Random rand = new Random();
        float finalX = rand.nextFloat() * (maxX - minX) + minX;
        return finalX;
    }

    //getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
