package sgeorgiev.org.spaceshipgame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Shield implements GameObject {
    private boolean active;
    private int x, y;
    private Bitmap bitmap;
    private Rect hitBox;

    public Shield(int x, int y) {
        this.x = x;
        this.y = y - 65;
        bitmap = BitmapFactory.decodeResource(Constants.CURR_CONTEXT.getResources(), R.drawable.shield);
        active = false;
        hitBox = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    public void update(int x, int y) {
        if(active) {
            this.x = x;
            this.y = y - 65;
            hitBox.left = x;
            hitBox.top = y - 60;
            hitBox.right = x + bitmap.getWidth();
            hitBox.bottom = y + bitmap.getHeight() - 80;
        }
    }

    @Override
    public void update(int speed) {
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if(active) {
            if(Constants.TEST_MODE)
                canvas.drawRect(hitBox, paint);

            canvas.drawBitmap(bitmap, x, y, paint);
        }
    }

    public void setActive(boolean value) {
        active = value;
    }

    public boolean isActive() {
        return active;
    }

    public Rect getHitBox() {
        return hitBox;
    }
}
