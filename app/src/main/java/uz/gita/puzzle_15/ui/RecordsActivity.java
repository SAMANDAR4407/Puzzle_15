package uz.gita.puzzle_15.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import uz.gita.puzzle_15.R;
import uz.gita.puzzle_15.data.MemorySharedPref;

public class RecordsActivity extends AppCompatActivity {

    TextView easy_time, easy_score;
    TextView medium_time, medium_score;
    TextView hard_time, hard_score;
    private MemorySharedPref memorySharedPref;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        memorySharedPref = MemorySharedPref.getInstance();

        loadView();
    }

    private void loadView(){
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        easy_time = findViewById(R.id.easy_time_record);
        easy_score = findViewById(R.id.easy_score_record);

        medium_time = findViewById(R.id.medium_time_record);
        medium_score = findViewById(R.id.medium_score_record);

        hard_time = findViewById(R.id.hard_time_record);
        hard_score = findViewById(R.id.hard_score_record);

        memorySharedPref = MemorySharedPref.getInstance();

        easy_time.setText(memorySharedPref.getEasyTime());
        easy_score.setText(memorySharedPref.getEasyScore());

        medium_time.setText(memorySharedPref.getMediumTime());
        medium_score.setText(memorySharedPref.getMediumScore());

        hard_time.setText(memorySharedPref.getHardTime());
        hard_score.setText(memorySharedPref.getHardScore());
    }
}