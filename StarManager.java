package sgeorgiev.org.spaceshipgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Random;

public class StarManager {
    //store the stars in an array list
    private ArrayList<Star> stars;
    private ArrayList<Planet> planets;
    Random generator;

    public StarManager() {
        stars = new ArrayList<>();
        planets = new ArrayList<>();
        populateStars(100);
    }

    public void populateStars(int numOfStars) {
        for (int i = 0; i < numOfStars; i++) {
            Star s  = new Star();
            stars.add(s);
        }
    }

    public void update(int playerSpeed) {
        if(Constants.FRAME_COUNT % (60 * 6) == 0)
            planets.add(new Planet());

        for(Star s : stars)
            s.update(playerSpeed);

        boolean flag = false;
        for(Planet p : planets) {
            p.update(playerSpeed);

            if (p.getState())
                flag = true;
        }

        if(flag)
            planets.clear();
    }

    public void draw(Canvas canvas, Paint paint) {
        for (Planet p : planets)
            p.draw(canvas, paint);

        for (Star s : stars)
            s.draw(canvas, paint);
    }
}
