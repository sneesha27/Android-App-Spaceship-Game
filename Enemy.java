package sgeorgiev.org.spaceshipgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Random;

public class Enemy implements GameObject {
    //enemy sprite
    private Bitmap bitmap;

    //coordinates
    private int x;
    private int y;

    //enemy speed
    private int speed = 1;

    //random number generator
    private Random generator;

    //hit box
    private Rect hitBox;

    //enemies can shoot as well but more slowly
    //Projectile projectile;
    private ArrayList<Projectile> projectiles;
    private ArrayList<Projectile> removed;
    //constructor
    public Enemy() {
        //load image
        bitmap = BitmapFactory.decodeResource(Constants.CURR_CONTEXT.getResources(), R.drawable.enemy);

        //create a random generator
        generator = new Random();
        speed = generator.nextInt(6) + 10;
        x = Constants.SCREEN_WIDTH;
        y = generator.nextInt(Constants.SCREEN_HEIGHT) - bitmap.getHeight();

        //create hitbox
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());

        //initialise
        projectiles = new ArrayList<>();
        removed = new ArrayList<>();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if(Constants.TEST_MODE) {
            paint.setColor(Color.RED);
            canvas.drawRect(hitBox, paint);
        }
        canvas.drawBitmap(bitmap, x, y, paint);

        for(Projectile p : projectiles)
           p.draw(canvas, paint);
    }

    @Override
    public void update(int playerSpeed) {

        x -= playerSpeed;
        x -= speed;
        //if the enemy reaches the left edge
        if (x < Constants.MIN_X - bitmap.getWidth()) {
            //adding the enemy again to the right edge
            speed = generator.nextInt(10) + 10;
            x = Constants.SCREEN_WIDTH;
            y = generator.nextInt(Constants.SCREEN_HEIGHT) - bitmap.getHeight();
        }

        //update hit box
        hitBox.left = x;
        hitBox.top = y + 10;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight() - 10;

        for(Projectile p : projectiles) {
            p.update("left", playerSpeed);
            if (p.getX() < Constants.MIN_X)
            removed.add(p);
        }
        projectiles.removeAll(removed);

        if(Constants.FRAME_COUNT % (60 * 2) == 0)
            projectiles.add(new Projectile(this.x, this.y + bitmap.getHeight()/2, this.speed*2, "enemy"));
    }

    public Rect getHitBox() {
        return hitBox;
    }

    //setter for x
    public void setX(int value) {
        this.x = value;
    }

    //getters
    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

   public ArrayList<Projectile> getProjectile() {
        return projectiles;
    }

    public void removeProj(Projectile p) {
        removed.add(p);
    }
}
