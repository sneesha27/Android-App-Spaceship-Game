package sgeorgiev.org.spaceshipgame;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HighScore extends AppCompatActivity {

    TextView textView1, textView2, textView3;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        textView1 = (TextView) findViewById(R.id.tv1);
        textView2 = (TextView) findViewById(R.id.tv2);
        textView3 = (TextView) findViewById(R.id.tv3);

        prefs = getSharedPreferences("HIGH_SCORE", Context.MODE_PRIVATE);
        textView1.setText("1. " + prefs.getInt("score1", 0));
        textView2.setText("2. " + prefs.getInt("score2", 0));
        textView3.setText("3. " + prefs.getInt("score3", 0));
    }
}
