package dym.unique.com.tetris.utils;

import android.content.Context;

/**
 * Created by daiyiming on 2016/2/23.
 */
public class MaxScoreTool extends SaveDataTool {

    private final static String SP_KEY_MAX_SCORE = "max_socre";

    public MaxScoreTool(Context context) {
        super(context);
    }

    public int get() {
        return sharedPreferences.getInt(SP_KEY_MAX_SCORE, 0);
    }

    public void save(int score) {
        editor.putInt(SP_KEY_MAX_SCORE, score);
        editor.commit();
    }

}
