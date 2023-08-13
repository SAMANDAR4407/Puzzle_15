package uz.gita.puzzle_15.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class MemorySharedPref {

    public static final String SHARED_PREF = "sharedPref";
    public static final String EASY_TIME = "easy_time";
    public static final String MEDIUM_TIME = "medium_time";
    public static final String HARD_TIME = "hard_time";
    public static final String EASY_SCORE = "easy_score";
    public static final String MEDIUM_SCORE = "medium_score";
    public static final String HARD_SCORE = "hard_score";

    public static final String LAST_TIME = "lastTime";
    public static final String LAST_LEVEL = "lastLevel";
    public static final String LAST_SCORE = "lastScore";
    public static final String SAVED_LIST_DATA = "lastPosition";

    public static final String EASY = "easy";
    public static final String MEDIUM = "medium";
    public static final String HARD = "hard";

    public static final String RESUME_LAST_TIME = "resume_last_time";
    public static final String RESUME_LAST_SCORE = "resume_last_score";
    public static final String RESUME_CLICK_STATE = "resume_click_state";
    public static final String RESUME_LAST_POSITION = "resume_last_position";

    static MemorySharedPref instance;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private MemorySharedPref(Context context) {
        pref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static void init(Context context){
        if (instance == null){
            instance = new MemorySharedPref(context);
        }
    }

    public static MemorySharedPref getInstance(){
        return instance;
    }

    public void setEasyTime(String time) {
        editor.putString(EASY_TIME, time).commit();
    }
    public String getEasyTime() {
        return pref.getString(EASY_TIME, "00:00");
    }
    public void setEasyScore(String score) {
        editor.putString(EASY_SCORE, score).commit();
    }
    public String getEasyScore() {
        return pref.getString(EASY_SCORE, "0");
    }

    public void setMediumTime(String time) {
        editor.putString(MEDIUM_TIME, time).commit();
    }
    public String getMediumTime() {
        return pref.getString(MEDIUM_TIME, "00:00");
    }
    public void setMediumScore(String score) {
        editor.putString(MEDIUM_SCORE, score).commit();
    }
    public String getMediumScore() {
        return pref.getString(MEDIUM_SCORE, "0");
    }

    public void setHardTime(String time) {
        editor.putString(HARD_TIME, time).commit();
    }
    public String getHardTime() {
        return pref.getString(HARD_TIME, "00:00");
    }
    public void setHardScore(String score) {
        editor.putString(HARD_SCORE, score).commit();
    }
    public String getHardScore() {
        return pref.getString(HARD_SCORE, "0");
    }


    public void setLastLevel(String lastLevel){
        editor.putString(LAST_LEVEL, lastLevel).commit();
    }
    public String getLastLevel(){
        return pref.getString(LAST_LEVEL,null);
    }
    public void setLastTime(long time) {
        editor.putLong(LAST_TIME, time).commit();
    }
    public long getLastTime() {
        return pref.getLong(LAST_TIME,0);
    }
    public void setLastScore(int score) {
        editor.putInt(LAST_SCORE, score).commit();
    }
    public int getLastScore() {
        return pref.getInt(LAST_SCORE, 0);
    }

    public void saveListData(String[] arr){
        if (arr != null) {
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < arr.length; i++) {
                if (i == 0) str.append(arr[i]);
                else str.append("##").append(arr[i]);
            }
            editor.putString(SAVED_LIST_DATA, str.toString()).commit();
        } else {
            editor.putString(MemorySharedPref.SAVED_LIST_DATA, null).commit();
        }
    }
    public String[] getSavedList(){
        return pref.getString(SAVED_LIST_DATA,"").split("##");
    }

    public void clearDataList() {
        editor.remove(SAVED_LIST_DATA).apply();
    }

}
