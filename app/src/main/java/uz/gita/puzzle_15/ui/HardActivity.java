package uz.gita.puzzle_15.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import uz.gita.puzzle_15.R;
import uz.gita.puzzle_15.data.MemorySharedPref;
import uz.gita.puzzle_15.data.MyMediaPlayerManager;
import uz.gita.puzzle_15.model.Coordinate;
import uz.gita.puzzle_15.ui.dialogs.StopGameDialog;

public class HardActivity extends AppCompatActivity {

    private TextView score;
    private int stepsCounter;
    private Chronometer time;

    private Button[][] items;
    private List<Integer> numbers;
    private Coordinate emptySpace;

    private MyMediaPlayerManager myPlayerManager; //null
    private MemorySharedPref memorySharedPref;
    private StopGameDialog stopGameDialog;

    private boolean hasWon; // false
    private boolean isDialogActive;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard);
        memorySharedPref = MemorySharedPref.getInstance();
        myPlayerManager = MyMediaPlayerManager.getInstance();
        loadView();
        setListeners();
        loadData(); // loads data whether from sharedPref or new one
        stopGameDialog = new StopGameDialog(this);
    }

    private void setListeners() {
        // Back button listener
        findViewById(R.id.btn_back).setOnClickListener(v -> {
            onBackPressed();
        });

        // restart button listener
        findViewById(R.id.btn_restart).setOnClickListener(v -> restart());

        // finish button listener
        findViewById(R.id.btn_finish).setOnClickListener(v -> {
            onBackPressed();
        });

    }

    private void loadView() {
        score = findViewById(R.id.text_score);
        time = findViewById(R.id.time);

        final ViewGroup relativeLayout = findViewById(R.id.container);
        final int childCount = relativeLayout.getChildCount();

        items = new Button[5][5];
        for (int i = 0; i < childCount; i++) {
            final View view = relativeLayout.getChildAt(i);
            final Button button = (Button) view;

            final int y = i / 5;
            final int x = i % 5;

            button.setOnClickListener(v -> onItemClick(button, x, y));

            items[y][x] = button;
        }

    }

    private void loadData() {
        if(memorySharedPref.getLastLevel() != null && Objects.equals(memorySharedPref.getLastLevel(), MemorySharedPref.HARD)) {
            getDataFromSharedPref();
        } else {
            createFreshData();
        }

    }

    private void getDataFromSharedPref() {
        stepsCounter = memorySharedPref.getLastScore();
        score.setText(String.valueOf(stepsCounter));

        long lastTime = memorySharedPref.getLastTime();
        time.setBase(SystemClock.elapsedRealtime() - lastTime);
        time.start();

        numbers = new ArrayList<>();
        String[] data = memorySharedPref.getSavedList();
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[i].length; j++) {
                if (data[(i * 5) + j].equals(" ")){
                    emptySpace = new Coordinate(j, i);
                } else {
                    numbers.add(Integer.valueOf(data[(i * 5) + j]));
                }
                items[i][j].setText(data[(i * 5) + j]);
            }
        }
    }

    private void createFreshData() {
        emptySpace = new Coordinate(4, 4);
        numbers = new ArrayList<>();
        for (int i = 1; i < 25; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);
        while (!isSolvable(numbers)){
            Collections.shuffle(numbers);
        }
        numbers.add(0);
        dataToView();
    }
    int getInvCount(int[] arr){
        int inv_count = 0;
        for (int i = 0; i < 24; i++)
            for (int j = i + 1; j < 24; j++)
                if (arr[i] > 0 && arr[j] > 0 && arr[i] > arr[j])
                    inv_count++;
        return inv_count;
    }

    boolean isSolvable(List<Integer> numbers){
        int[][] puzzle = toMatrix(numbers);
        int[] linearPuzzle;
        linearPuzzle = new int[24];
        int k = 0;

        for(int i=0; i<5; i++)
            for(int j=0; j<5; j++){
                if (i!=4 || j!=4){
                    linearPuzzle[k++] = puzzle[i][j];
                }
            }
        int invCount = getInvCount(linearPuzzle);

        return (invCount % 2 == 0);
    }

    private int[][] toMatrix(List<Integer> numbers) {
        int[][] res = new int[5][5];
        for (int i = 0; i < 24; i++) {
            int x = i / 5;
            int y = i % 5;
            res[x][y] = numbers.get(i);
        }
        return res;
    }

    private void dataToView() {
        stepsCounter = 0;
        score.setText("0");
        time.setText("00:00");
        emptySpace.setX(4);
        emptySpace.setY(4);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int index = 5 * i + j;
                if (index < 24) {
                    int number = numbers.get(index);
                    items[i][j].setText(String.valueOf(number));
                } else {
                    items[i][j].setText(" ");
                }
            }
        }
    }

    private void restart() {
        memorySharedPref.clearDataList();
        time.setBase(SystemClock.elapsedRealtime());
        createFreshData();
    }

    private void onItemClick(Button button, int x, int y) {
        myPlayerManager.startClickSound();
        final int dx = Math.abs(emptySpace.getX() - x);
        final int dy = Math.abs(emptySpace.getY() - y);
        if (dx + dy == 1) {

            score.setText(String.valueOf(++stepsCounter));

            final String text = button.getText().toString();
            button.setText(" ");
            button.setBackgroundResource(R.drawable.bg_item_views);

            final Button temp = items[emptySpace.getY()][emptySpace.getX()];
            temp.setText(text);
            temp.setBackgroundResource(R.drawable.bg_item_views);

            emptySpace.setX(x);
            emptySpace.setY(y);

            checkIfWon();
        }
    }

    private void checkIfWon() {
        if (emptySpace.getX() == 4 || emptySpace.getY() == 4){
            for (int i = 0; i < 24; i++) {
                int y = i / 5;
                int x = i % 5;
                if (items[y][x].getText().toString().equals(String.valueOf(i + 1))) {
                    hasWon = true;
                } else {
                    hasWon = false;
                    break;
                }
            }
        }
        if (hasWon){
            time.stop();
            if (Integer.parseInt(memorySharedPref.getHardScore()) == 0 ||
                    Integer.parseInt(memorySharedPref.getHardScore()) > Integer.parseInt(score.getText().toString())) {
                memorySharedPref.setHardScore(score.getText().toString());
                memorySharedPref.setHardTime(time.getText().toString());
            }

            Intent intent = new Intent(this,ResultActivity.class);
            intent.putExtra("score", score.getText());
            intent.putExtra("time",time.getText());
            intent.putExtra("level","Hard");
            memorySharedPref.setLastLevel(null);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!hasWon) {
            String[] arr = new String[25];
            for (int i = 0; i < items.length; i++) {
                for (int j = 0; j < items[i].length; j++) {
                    arr[i * 5 + j] = items[i][j].getText().toString();
                }
            }
            Log.d("TTT", "onDestroy: list "+ Arrays.toString(arr));
            memorySharedPref.setLastLevel(MemorySharedPref.HARD);
            memorySharedPref.setLastScore(Integer.parseInt(score.getText().toString()));
            memorySharedPref.setLastTime((SystemClock.elapsedRealtime() - time.getBase()));
            memorySharedPref.saveListData(arr);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TTT", "onResume: called");
        time.start(); // o'zgaruvchi isCounting;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TTT", "onPause: called");
        time.stop();

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        stopGameDialog.show();
        isDialogActive = true;
        stopGameDialog.setOnPositiveListener(() -> {
            time.stop();
            if (!hasWon) {
                String[] arr = new String[25];
                for (int i = 0; i < items.length; i++) {
                    for (int j = 0; j < items[i].length; j++) {
                        arr[i * 5 + j] = items[i][j].getText().toString();
                    }
                }
                Log.d("TTT", "onBackPressed: list "+ Arrays.toString(arr));
                memorySharedPref.setLastLevel(MemorySharedPref.HARD);
                memorySharedPref.setLastScore(Integer.parseInt(score.getText().toString()));
                memorySharedPref.setLastTime((SystemClock.elapsedRealtime() - time.getBase()));
                memorySharedPref.saveListData(arr);
            }
            finish();
            stopGameDialog.dismiss();
            isDialogActive = false;
        });
        stopGameDialog.setOnNegativeListener(() -> {
            memorySharedPref.setLastLevel(null);
            memorySharedPref.clearDataList();
            time.stop();
            finish();
            stopGameDialog.dismiss();
            isDialogActive = false;
        });
        stopGameDialog.setOnNeutralListener(() -> {
            stopGameDialog.dismiss();
            isDialogActive = false;
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d("TTT", "onStop: called");
        super.onStop();
        time.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TTT", "onDestroy: called");
    }
}
