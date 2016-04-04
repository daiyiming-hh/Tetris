package dym.unique.com.tetris.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by daiyiming on 2016/2/23.
 */
public class MaxScore {
    private final static String SP_NAME = "max_socre";
    private final static String SP_KEY_MAX_SCORE = "max_socre";

    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;

    public MaxScore(Context context) {
        sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public int get() {
        return sharedPreferences.getInt(SP_KEY_MAX_SCORE, 0);
    }

    public void save(int score) {
        editor.putInt(SP_KEY_MAX_SCORE, score);
        editor.commit();
    }

}
