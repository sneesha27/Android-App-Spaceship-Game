package sgeorgiev.org.spaceshipgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

public class Player {
    //player image
    private Bitmap bitmap;

    //coordinates
    private int x;
    private int y;

    //speed of the player
    private int speed = 0;

    //going up?
    private boolean boosting;

    //weapon
    ArrayList<Projectile> projectiles;
    ArrayList<Projectile> removedProjectiles;

    //Controlling Y coordinate so that ship won't go outside the screen
    private int maxY;
    private int minY;

    //rectangle to use for collision detection
    private Rect hitBox;
    private Rect hitBoxWings;

    Sound sound;

    private Shield shield;

    //constructor
    public Player(Context context) {
        //set initial location and speed
        x = 75;
        y = 50;
        speed = 1;

        //load asset
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player1);

        //set to false initially
        boosting = false;

        //min and max y
        minY = 0;
        maxY = Constants.SCREEN_HEIGHT - bitmap.getHeight();

        //create the hitbox
        hitBox = new Rect(x , y, bitmap.getWidth(), bitmap.getHeight());
        hitBoxWings = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());

        //projectiles
        projectiles = new ArrayList<>();
        removedProjectiles = new ArrayList<>();

        //initialise sound
        sound = new Sound();

        //initialise shield
        shield = new Shield(this.x, this.y);
    }

    //setter for boosting
    public void setBoosting(boolean value) {
        this.boosting = value;
    }

    //update player
    public void update() {
       //if we're boosting
       if(boosting)
           //increase speed
          speed += 2;
       else
           //if not, slow down
           speed -= 5;

        //limit min and max speed
        if(speed > Constants.MAX_SPEED)
            speed = Constants.MAX_SPEED;

        if(speed < Constants.MIN_SPEED)
            speed = Constants.MIN_SPEED;

        //move the ship down
        y -= speed + Constants.GRAVITY;

        //make sure it doesn't go out of the screen
        if (y < minY) {
            y = minY;
        }
        if (y > maxY) {
           Constants.GAME_OVER = true;
        }

        //need to make sure the hitbox is updated every frame
        //had to reduce the hitbox as it wasn't very accurate hence the substractions
        hitBox.left = x + 10;
        hitBox.top = y + 35;
        hitBox.right = x + bitmap.getWidth() - 10;
        hitBox.bottom = y + bitmap.getHeight() - 35;

        //update the other hitbox as well
        hitBoxWings.left = x + 45;
        hitBoxWings.top = y;
        hitBoxWings.right = x + bitmap.getWidth() - 115;
        hitBoxWings.bottom = y + bitmap.getHeight();

        for(Projectile p : projectiles) {
            if(p.checkState()) {
                //store projectiles which reach the end of the screen
                removedProjectiles.add(p);
            }
            //update position
            p.update();
        }
        //remove them after the loop
        projectiles.removeAll(removedProjectiles);

        //every 15 frames
        if(Constants.FRAME_COUNT % 30 == 0) {
            //shoot
            projectiles.add(new Projectile(x + bitmap.getWidth(),
                   y + bitmap.getHeight()/2, 20, "player"));
            //sound.playShoot();
        }

        shield.update(this.x, this.y);
    }

    public void draw(Canvas canvas, Paint paint) {
        if(Constants.TEST_MODE) {
            paint.setColor(Color.RED);
            canvas.drawRect(hitBox, paint);
            canvas.drawRect(hitBoxWings, paint);
        }
        canvas.drawBitmap(this.bitmap, this.x, this.y, paint);

        for(Projectile p : projectiles)
            p.draw(canvas, paint);

        shield.draw(canvas, paint);
    }

    public void destroyProjectile(Projectile proj) {
        removedProjectiles.add(proj);
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

    public Rect getHitBox() {
        return hitBox;
    }

    public Rect getHitBoxWings() {return hitBoxWings;}

    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }

    public boolean gemCollision(Rect gemHitBox) {
        if(Rect.intersects(hitBox, gemHitBox) || Rect.intersects(hitBoxWings, gemHitBox))
            return true;

        return false;
    }

    public void setShield(boolean value) {
        shield.setActive(value);
    }

    public boolean isShieldActive() {
        return shield.isActive();
    }

    public Shield getShield() {
        return shield;
    }
}
