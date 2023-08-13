package uz.gita.puzzle_15.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import uz.gita.puzzle_15.R;
import uz.gita.puzzle_15.data.MemorySharedPref;

public class DifficultyActivity extends AppCompatActivity {
    private MemorySharedPref memorySharedPref;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        memorySharedPref = MemorySharedPref.getInstance();

        findViewById(R.id.btn_easy).setOnClickListener(v -> {
            Intent intent = new Intent(this, EasyActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.btn_medium).setOnClickListener(v -> {
            Intent intent = new Intent(this, MediumActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.btn_hard).setOnClickListener(v -> {
            Intent intent = new Intent(this, HardActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.btn_main_menu).setOnClickListener(view -> {
            finish();
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d("TAG", "onResume: called");
        setResumeVisible();
        super.onResume();
    }

    private void setResumeVisible(){
        if (memorySharedPref.getLastLevel() != null){
            findViewById(R.id.btn_resume).setEnabled(true);
            Intent intent;
            switch (memorySharedPref.getLastLevel()){
                case MemorySharedPref.EASY:{
                    intent = new Intent(this, EasyActivity.class);
                    intent.putExtra(MemorySharedPref.LAST_LEVEL, MemorySharedPref.EASY);
                    break;
                }
                case MemorySharedPref.MEDIUM:{
                    intent = new Intent(this, MediumActivity.class);
                    intent.putExtra(MemorySharedPref.LAST_LEVEL, MemorySharedPref.MEDIUM);
                    break;
                }
                case MemorySharedPref.HARD:{
                    intent = new Intent(this, HardActivity.class);
                    intent.putExtra(MemorySharedPref.LAST_LEVEL, MemorySharedPref.HARD);
                    break;
                }
                default: intent = new Intent();
            }
            findViewById(R.id.btn_resume).setOnClickListener(v -> startActivity(intent));
        } else {
            findViewById(R.id.btn_resume).setEnabled(false);
        }
    }
}