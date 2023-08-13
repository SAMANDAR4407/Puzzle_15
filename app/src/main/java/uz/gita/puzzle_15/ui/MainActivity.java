package uz.gita.puzzle_15.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import uz.gita.puzzle_15.R;
import uz.gita.puzzle_15.ui.dialogs.InfoDialog;

public class MainActivity extends AppCompatActivity {
    private InfoDialog infoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoDialog = new InfoDialog(this);

        findViewById(R.id.btn_play).setOnClickListener(v -> {
            startActivity(new Intent(this, DifficultyActivity.class));
        });

        findViewById(R.id.btn_quit).setOnClickListener(v -> {
            finish();
        });

        findViewById(R.id.about).setOnClickListener(v -> {
            infoDialog.show();
            infoDialog.setOnPositiveListener(infoDialog::dismiss);
        });

        findViewById(R.id.btn_records).setOnClickListener(v -> {
            startActivity(new Intent(this, RecordsActivity.class));
        });
    }
}