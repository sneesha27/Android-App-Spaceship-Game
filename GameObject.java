package sgeorgiev.org.spaceshipgame;

import android.graphics.Canvas;
import android.graphics.Paint;

public interface GameObject {
    public void update(int speed);
    public void draw(Canvas canvas, Paint paint);
}
