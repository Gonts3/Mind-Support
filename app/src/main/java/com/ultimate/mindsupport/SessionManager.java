package com.ultimate.mindsupport;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;
public class SessionManager {
    private static SharedPreferences prefs;

    private static final String PREF_NAME = "secure_prefs";

    // Keys
    private static final String KEY_USER_TYPE = "user_type";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_CLIENT_ID = "client_id";
    private static final String KEY_PROBLEM_ID = "problem_id";
    private static final String KEY_COUNSELLOR_ID = "counsellor_id";
    private static final String KEY_COUNSELLOR_REG = "counsellor_reg";
    private static final String KEY_COUNSELLOR_FNAME = "counsellor_fname";
    private static final String KEY_COUNSELLOR_LNAME = "counsellor_lname";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_HAS_SAVED_CREDENTIALS = "has_saved_credentials";


    public static void init(Context context) throws GeneralSecurityException, IOException {
        if (prefs == null) {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            prefs = EncryptedSharedPreferences.create(
                    context,
                    PREF_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        }
    }

    // Save client session
    public static void saveClientSession(String clientId,String username, String problemId) {
        prefs.edit()
                .putString(KEY_USER_TYPE, "client")
                .putString(KEY_PROBLEM_ID, problemId)
                .putString(KEY_CLIENT_ID, clientId)
                .putString(KEY_USERNAME, username)
                .apply();
    }

    // Save counsellor session
    public static void saveCounsellorSession(String id,String reg_no ,String fname, String lname) {
        prefs.edit()
                .putString(KEY_USER_TYPE, "counsellor")
                .putString(KEY_COUNSELLOR_ID, id)
                .putString(KEY_COUNSELLOR_FNAME, fname)
                .putString(KEY_COUNSELLOR_LNAME, lname)
                .putString(KEY_COUNSELLOR_REG, reg_no)
                .apply();
    }

    // Getters
    public static String getUserType() {
        return prefs.getString(KEY_USER_TYPE, null);
    }

    public static String getClientId() {
        return prefs.getString(KEY_CLIENT_ID, null);
    }

    public static String getCounsellorId() {
        return prefs.getString(KEY_COUNSELLOR_ID, null);
    }

    public static String getCounsellorFname() {
        return prefs.getString(KEY_COUNSELLOR_FNAME, null);
    }

    public static String getCounsellorLname() {
        return prefs.getString(KEY_COUNSELLOR_LNAME, null);
    }

    // Clear session
    public static void clearSession() {
        prefs.edit().clear().apply();
    }
    public static void setLoginState(boolean isLoggedIn) {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply();
    }

    public static boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public static void setSavedCredentials(boolean saved) {
        prefs.edit().putBoolean(KEY_HAS_SAVED_CREDENTIALS, saved).apply();
    }

    public static boolean hasSavedCredentials() {
        return prefs.getBoolean(KEY_HAS_SAVED_CREDENTIALS, false);
    }


}
