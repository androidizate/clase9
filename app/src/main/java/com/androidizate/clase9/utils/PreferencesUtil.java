package com.androidizate.clase9.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

/**
 * @author Andres Oller
 */
public class PreferencesUtil {

    SharedPreferences preferences;

    public PreferencesUtil(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getStringPreference(String key) {
        String value = null;

        if (preferences != null) {
            value = preferences.getString(key, null);
        }
        return value;
    }

    public boolean setStringPreference(String key, String value) {
        if (preferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            return editor.commit();
        }
        return false;
    }

    public float getFloatPreference(String key, float defaultValue) {
        float value = defaultValue;
        if (preferences != null) {
            value = preferences.getFloat(key, defaultValue);
        }
        return value;
    }

    public boolean setFloatPreference(String key, float value) {
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putFloat(key, value);
            return editor.commit();
        }
        return false;
    }

    public long getLongPreference(String key, long defaultValue) {
        long value = defaultValue;
        if (preferences != null) {
            value = preferences.getLong(key, defaultValue);
        }
        return value;
    }

    public boolean setLongPreference(String key, long value) {
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(key, value);
            return editor.commit();
        }
        return false;
    }

    public int getIntegerPreference(String key, int defaultValue) {
        int value = defaultValue;
        if (preferences != null) {
            value = preferences.getInt(key, defaultValue);
        }
        return value;
    }

    public boolean setIntegerPreference(String key, int value) {
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(key, value);
            return editor.commit();
        }
        return false;
    }

    public boolean getBooleanPreference(String key, boolean defaultValue) {
        boolean value = defaultValue;
        if (preferences != null) {
            value = preferences.getBoolean(key, defaultValue);
        }
        return value;
    }

    public boolean setBooleanPreference(String key, boolean value) {
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(key, value);
            return editor.commit();
        }
        return false;
    }
}
