package uz.gita.puzzle_15.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class MediumActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_medium);
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

        // Music button listener
//        findViewById(R.id.btn_musicOn).setOnClickListener(v -> {
//            if (myPlayerManager.isMusicPlaying()) {
//                myPlayerManager.pauseMusic();
//                musicButton.setBackgroundResource(R.drawable.ic_volume_off);
//            } else {
//                myPlayerManager.startMusic();
//                musicButton.setBackgroundResource(R.drawable.ic_volume_up_white);
//            }
//        });
    }

    private void loadView() {
        score = findViewById(R.id.text_score);
        time = findViewById(R.id.time);

        final ViewGroup relativeLayout = findViewById(R.id.container);
        final int childCount = relativeLayout.getChildCount();

        items = new Button[4][4];
        for (int i = 0; i < childCount; i++) {
            final View view = relativeLayout.getChildAt(i);
            final Button button = (Button) view;

            final int y = i / 4;
            final int x = i % 4;

            button.setOnClickListener(v -> onItemClick(button, x, y));

            items[y][x] = button;
        }

    }

    private void loadData() {
        if(memorySharedPref.getLastLevel() != null && Objects.equals(memorySharedPref.getLastLevel(), MemorySharedPref.MEDIUM)) {
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
                if (data[(i * 4) + j].equals(" ")){
                    emptySpace = new Coordinate(j, i);
                } else {
                    numbers.add(Integer.valueOf(data[(i * 4) + j]));
                }
                items[i][j].setText(data[(i * 4) + j]);
            }
        }
    }

    private void createFreshData() {
        emptySpace = new Coordinate(3, 3);
        numbers = new ArrayList<>();
        for (int i = 1; i < 16; i++) {
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
        for (int i = 0; i < 15; i++)
            for (int j = i + 1; j < 15; j++)
                if (arr[i] > 0 && arr[j] > 0 && arr[i] > arr[j])
                    inv_count++;
        return inv_count;
    }

    boolean isSolvable(List<Integer> numbers){
        int[][] puzzle = toMatrix(numbers);
        int[] linearPuzzle;
        linearPuzzle = new int[15];
        int k = 0;

        for(int i=0; i<4; i++)
            for(int j=0; j<4; j++){
                if (i!=3 || j!=3){
                    linearPuzzle[k++] = puzzle[i][j];
                }
            }
        int invCount = getInvCount(linearPuzzle);

        return (invCount % 2 == 0);
    }

    private int[][] toMatrix(List<Integer> numbers) {
        int[][] res = new int[4][4];
        for (int i = 0; i < 15; i++) {
            int x = i / 4;
            int y = i % 4;
            res[x][y] = numbers.get(i);
        }
        return res;
    }

    private void dataToView() {
        stepsCounter = 0;
        score.setText("0");
        time.setText("00:00");
        emptySpace.setX(3);
        emptySpace.setY(3);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int index = 4 * i + j;
                if (index < 15) {
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
//        time.start();
        createFreshData();
//        loadData();
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
        if (emptySpace.getX() == 3 || emptySpace.getY() == 3){
            for (int i = 0; i < 15; i++) {
                int y = i / 4;
                int x = i % 4;
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
            if (Integer.parseInt(memorySharedPref.getMediumScore()) == 0 ||
                    Integer.parseInt(memorySharedPref.getMediumScore()) > Integer.parseInt(score.getText().toString())) {
                memorySharedPref.setMediumScore(score.getText().toString());
                memorySharedPref.setMediumTime(time.getText().toString());
            }

            Intent intent = new Intent(this,ResultActivity.class);
            intent.putExtra("score", score.getText());
            intent.putExtra("time",time.getText());
            intent.putExtra("level","Medium");
            memorySharedPref.setLastLevel(null);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!hasWon) {
            String[] arr = new String[16];
            for (int i = 0; i < items.length; i++) {
                for (int j = 0; j < items[i].length; j++) {
                    arr[i * 4 + j] = items[i][j].getText().toString();
                }
            }
            Log.d("TTT", "onDestroy: list "+ Arrays.toString(arr));
            memorySharedPref.setLastLevel(MemorySharedPref.MEDIUM);
            memorySharedPref.setLastScore(Integer.parseInt(score.getText().toString()));
            memorySharedPref.setLastTime((SystemClock.elapsedRealtime() - time.getBase()));
            memorySharedPref.saveListData(arr);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        if(savedInstanceState.getBoolean("MUSIC_STATUS")) {
//            myPlayerManager.startMusic();
//            musicButton.setBackgroundResource(R.drawable.ic_volume_up_white);
//        }
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
//        myPlayerManager.pauseMusic();
//        if (!hasWon) {
//            String[] arr = new String[9];
//            for (int i = 0; i < items.length; i++) {
//                for (int j = 0; j < items[i].length; j++) {
//                    arr[i * 3 + j] = items[i][j].getText().toString();
//                }
//            }
//            Log.d("TTT", "onPause: list "+ Arrays.toString(arr));
//            memorySharedPref.setLastLevel(MemorySharedPref.EASY);
//            memorySharedPref.setLastScore(Integer.parseInt(score.getText().toString()));
//            memorySharedPref.setLastTime((int) (SystemClock.elapsedRealtime() - time.getBase()));
//            memorySharedPref.saveListData(arr);
//        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        stopGameDialog.show();
        isDialogActive = true;
        stopGameDialog.setOnPositiveListener(() -> {
            time.stop();
            if (!hasWon) {
                String[] arr = new String[16];
                for (int i = 0; i < items.length; i++) {
                    for (int j = 0; j < items[i].length; j++) {
                        arr[i * 4 + j] = items[i][j].getText().toString();
                    }
                }
                Log.d("TTT", "onBackPressed: list "+ Arrays.toString(arr));
                memorySharedPref.setLastLevel(MemorySharedPref.MEDIUM);
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
//        musicButton.setBackgroundResource( (myPlayerManager.isMusicPlaying()) ? R.drawable.ic_volume_up_white: R.drawable.ic_volume_off );
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
