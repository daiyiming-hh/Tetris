package dym.unique.com.tetris.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by daiyiming on 2016/5/28.
 */
public abstract class SaveDataTool {
    private final static String SP_NAME = "save_data";

    protected SharedPreferences sharedPreferences = null;
    protected SharedPreferences.Editor editor = null;

    public SaveDataTool(Context context) {
        sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
}
