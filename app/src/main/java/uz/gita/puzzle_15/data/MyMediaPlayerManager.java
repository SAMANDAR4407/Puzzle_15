package uz.gita.puzzle_15.data;

import android.content.Context;
import android.media.MediaPlayer;

import uz.gita.puzzle_15.R;

public class MyMediaPlayerManager {
    private static MyMediaPlayerManager instance;

    // MediaPlayers
    private MediaPlayer playerClick; // null
//    private MediaPlayer playerMusic; // null
    private boolean isPlayingMusic;
    private static Context savedContext;

    private MyMediaPlayerManager(Context context) {
        playerClick = MediaPlayer.create(context, R.raw.click);
//        playerMusic = MediaPlayer.create(context, R.raw.background);
//
//        playerMusic.setLooping(true);
    }

    public static void init(Context context) {
        if(instance == null) {
            savedContext = context;
            instance = new MyMediaPlayerManager(context);
        }
    }

    public synchronized static MyMediaPlayerManager getInstance() throws NullPointerException {
        if(instance == null) {
            throw new NullPointerException("MyMediaPlayerManage class instance is null");
        }
        return instance;
    }

//    public void startMusic() {
////        if(!isPlayingMusic) {
////            playerMusic = MediaPlayer.create(savedContext, R.raw.background);
////        }
//        isPlayingMusic = true;
//        playerMusic.start();
//    }

    public void startClickSound() {
        playerClick.start();
    }

//    public void pauseMusic() {
//        playerMusic.pause();
//        isPlayingMusic = false;
//    }

    public void pauseClickSound() {
        playerClick.pause();
    }

//    public void stopMusic() {
//        playerMusic.stop();
//        isPlayingMusic = false;
//    }

    public void stopClickSound() {
        playerClick.stop();
    }

//    public void releaseMusic() {
//        playerMusic.release();
//        isPlayingMusic = false;
//    }

    public void releaseClickSound() {
        playerClick.release();
    }

    public boolean isMusicPlaying() {
        return isPlayingMusic;
    }
}
