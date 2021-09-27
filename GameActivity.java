package sgeorgiev.org.spaceshipgame;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity {

    // create a GameView object
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialise the GameView object
        gameView = new GameView(this);
        //and add it to the content view
        setContentView(gameView);
    }


    //override onPause so we can pause the gameView
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    // override onResume as well
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}
