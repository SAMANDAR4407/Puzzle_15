package uz.gita.puzzle_15.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import uz.gita.puzzle_15.R;

public class ResultActivity extends AppCompatActivity {
    private MediaPlayer claps;
    private TextView level2;
    private TextView time2;
    private TextView score2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        claps = MediaPlayer.create(this,R.raw.applause);
        claps.start();

        Bundle extra = getIntent().getExtras();
        String score1 = extra.getString("score");
        String time1 = extra.getString("time");
        String level1 = extra.getString("level");

        level2 = findViewById(R.id.txt_level);
        level2.setText(level1);

        time2 = findViewById(R.id.txt_time);
        time2.setText(time1);

        score2 = findViewById(R.id.txt_score);
        score2.setText(score1);

        findViewById(R.id.btn_main_menu).setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        findViewById(R.id.btn_restart).setOnClickListener(view -> {
            switch (level1){
                case "Easy": startActivity(new Intent(this,EasyActivity.class)); break;
                case "Medium": startActivity(new Intent(this,MediumActivity.class)); break;
                case "Hard": startActivity(new Intent(this,HardActivity.class)); break;
            }
            finish();
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("score",score2.getText().toString());
        outState.putString("time",time2.getText().toString());
        outState.putString("level",level2.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String score1 = savedInstanceState.getString("score");
        String time1 = savedInstanceState.getString("time");
        String level1 = savedInstanceState.getString("level");

        level2.setText(level1);
        time2.setText(time1);
        score2.setText(score1);
    }

}