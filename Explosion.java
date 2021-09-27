package sgeorgiev.org.spaceshipgame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Explosion implements GameObject {
    //bitmap object
    private Bitmap bitmap;

    //coordinates
    private int x;
    private int y;

    //constructor
    public Explosion(int x, int y) {
        //getting boom image from drawable resource
        bitmap = BitmapFactory.decodeResource
                (Constants.CURR_CONTEXT.getResources(), R.drawable.boom);

        //set coordinates
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bitmap, x, y, paint);
    }

    @Override
    public void update(int speed) {

    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
