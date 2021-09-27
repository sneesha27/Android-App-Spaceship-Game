package sgeorgiev.org.spaceshipgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;
import java.util.concurrent.CompletionService;

public class GameView extends SurfaceView implements Runnable {
    // track if the game is running
    volatile boolean playing = true;

    //game thread
    private Thread gameThread = null;

    //player
    private Player player;

    //objects needed for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    //stars
    private StarManager starManager;

    //enemies
    private EnemyManager enemyManager;

    //create asteroids
    private AsteroidManager asteroidManager;

    //random generator
    Random generator;

    //highscore
    int highScore[];

    //shared prefs for the highscore
    SharedPreferences sharedPreferences;

    //need to track time as difficulty should increase with time
    private long initTime, deltaTime, startTime;
    private int elapsedTime;

    private Sound sound;

    //constructor
    public GameView(Context context) {
        super(context);

        Constants.GAME_OVER = false;

        //initialise random gen
        generator = new Random();

        //store the context in Constants
        Constants.CURR_CONTEXT = context;

        //initialise player
        player = new Player(context);

        //and initialise drawing objects
        surfaceHolder = getHolder();

        //initialise paint
        paint = new Paint();

        //initialise star manager
        starManager = new StarManager();

        //initialise enemy manager
        enemyManager = new EnemyManager(1);

        //initialise asteroids
        asteroidManager = new AsteroidManager(1);

        //set the frame count to 0 in the beginning
        Constants.FRAME_COUNT = 0;

        //set score to 0 when the game starts
        Constants.SCORE = 0;
        highScore = new int[3];

        //init shared prefs as well
        sharedPreferences = context.getSharedPreferences("HIGH_SCORE", Context.MODE_PRIVATE);
        highScore[0] = sharedPreferences.getInt("score1", 0);
        highScore[1] = sharedPreferences.getInt("score2", 0);
        highScore[2] = sharedPreferences.getInt("score3", 0);

        //set init time
        startTime = initTime = System.currentTimeMillis();

        sound = new Sound();
        sound.playBg();
    }

    @Override
    public void run() {
        // while playing the game
        while (playing) {
            //update the frame
            update();
            //increment every frame
            Constants.FRAME_COUNT++;
            //draw objects on the frame
            draw();
            //control
            control();
        }
    }

    private void update() {
        //if game is nt over
        if(!Constants.GAME_OVER) {
            deltaTime = (int) (System.currentTimeMillis() - startTime);
            startTime = System.currentTimeMillis();
            elapsedTime = (int) (System.currentTimeMillis() - initTime)/1000;
            Constants.ELAPSED_TIME = elapsedTime;
            //Log.d("FRAMES ", ""+ Constants.FRAME_COUNT);
            //call update on every object
            player.update();
            enemyManager.update(player);
            starManager.update(player.getSpeed());
            asteroidManager.update(player);
        } else {
            playing = false;
            sound.stopBg();
            for(int i = 0; i < highScore.length; i++) {
                if (Constants.SCORE > highScore[i]) {
                    final int endI = i;
                    highScore[i] = Constants.SCORE;
                   // Log.d("SCORE ", "" + highScore[i]);
                    break;
                }
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            for(int i = 0; i < highScore.length; i++) {
                int index = i + 1;
                editor.putInt("score" + index, highScore[i]);
            }
            editor.apply();
        }
    }

    private void draw() {
        //if surface is valid
        if (surfaceHolder.getSurface().isValid()) {
            //lock the canvas
            canvas = surfaceHolder.lockCanvas();
            //set background colour
            canvas.drawColor(Color.BLACK);
            //draw stars
            paint.setColor(Color.WHITE);
            starManager.draw(this.canvas, this.paint);
            //draw player
            player.draw(this.canvas, this.paint);
            //draw enemies
            enemyManager.draw(this.canvas, this.paint);
            //draw asteroids
            asteroidManager.draw(this.canvas, this.paint);

            //draw the score as well
            paint.setTextSize(100);
            paint.setColor(Color.WHITE);
            canvas.drawText("" + Constants.SCORE, 50, paint.descent() - paint.ascent(), paint);

            //if game is over
            if (Constants.GAME_OVER) {
                paint.setTextSize(100);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setColor(Color.WHITE);

                //tell the player
                int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Game over", canvas.getWidth() / 2, yPos, paint);

                paint.setTextSize(50);
                yPos = (int) ((canvas.getHeight() - 2 * paint.descent()) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Tap anywhere to return to main menu...", canvas.getWidth() / 2, yPos, paint);
            }
            //unlock canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    //this should result in the game having around 60 FPS
    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) { }
    }

    //pause
    public void pause() {
        sound.stopBg();
        //set paying to false
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {}
    }

    //resume
    public void resume() {
        sound.playBg();
        // playing is true now
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    //override onTouchEvent to implement movement of the player
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction() & event.ACTION_MASK) {
            //when released
            case MotionEvent.ACTION_UP:
                //stop boosting
                player.setBoosting(false);
                break;
            //when pressed
            case MotionEvent.ACTION_DOWN:
                //boost
                if(Constants.GAME_OVER && System.currentTimeMillis() - Constants.GAMEOVER_TIME >= 1000)
                    Constants.CURR_CONTEXT.startActivity(new Intent(Constants.CURR_CONTEXT, MainActivity.class));
                player.setBoosting(true);
                break;
        }
        return true;
    }
}
