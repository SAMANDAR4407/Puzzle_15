package uz.gita.puzzle_15.app;

import android.app.Application;

import uz.gita.puzzle_15.data.MemorySharedPref;
import uz.gita.puzzle_15.data.MyMediaPlayerManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MyMediaPlayerManager.init(this);
        MemorySharedPref.init(this);
    }
}
