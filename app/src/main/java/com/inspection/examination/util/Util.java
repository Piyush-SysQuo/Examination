package com.inspection.examination.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

public class Util {

    private static Util utilInstance = null;
    private SharedPreferences fSharedPreferences;
    private Context fContext;

    private Util(@NotNull Context ctxt) {
        fContext = ctxt;
        fSharedPreferences = ctxt.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Get singleton instance of this class
     *
     * @param ctxt
     * @return
     */
    public static Util getInstance(Context ctxt) {
        if (null == utilInstance) {

            utilInstance = new Util(ctxt);
        }
        return utilInstance;
    }

    /**
     * Serialize the given object and stores in shared preference
     *
     * @return
     */
    public void putGsonObject(String key, Object obj, TypeToken token) {
        setString(key, new GsonBuilder().create().toJson(obj, token.getType()));
    }

    /**
     * Deserialize the response for the given key stored in shared preference
     *
     * @return
     */
    public Object pickGsonObject(String key, TypeToken token) {
        return new GsonBuilder().create().fromJson(getString(key), token.getType());
    }

    /**
     * Set string values in preference
     *
     * @param key
     * @param value
     */
    public void setString(String key, String value) {
        SharedPreferences.Editor editor = fSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Get string value from preference
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        return fSharedPreferences.getString(key, null);
    }

    /**
     * Removes the string value from preference
     *
     * @param key
     */
    public void removePrefs(String key) {
        SharedPreferences.Editor editor = fSharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * Deletes the shared preference data and closes the app
     *
     * @param context
     * @return
     */
    public static boolean logout(Context context) {
        Util.getInstance(context).removePrefs(Constants.PREFS_LOGIN_RESPONSE);
        return true;
    }


}
