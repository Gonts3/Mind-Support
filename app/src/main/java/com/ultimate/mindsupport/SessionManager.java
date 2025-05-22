package com.ultimate.mindsupport;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.ultimate.mindsupport.client.Client;

import java.io.IOException;
import java.security.GeneralSecurityException;
public class SessionManager {
    private static SharedPreferences prefs;

    private static final String PREF_NAME = "secure_prefs";

    // Keys
    private static final String KEY_USER_TYPE = "user_type";
    private static final String KEY_CLIENT_ID = "client_id";
    private static final String KEY_COUNSELLOR_ID = "counsellor_id";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";



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
    public static void setLoggedIn(boolean value) {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply();
    }



    // Save client session
    public static void saveClientSession(String clientId) {
        prefs.edit()
                .putString(KEY_USER_TYPE, "client")
                .putString(KEY_CLIENT_ID, clientId)
                .apply();
    }
    public static void loadClientSession(){
        String clientId = prefs.getString(KEY_CLIENT_ID, null);
        GetUser.getClient(clientId, new GetUser.GetUserCallback() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onFailure(String error) {

            }
        });
    }
    public static void loadCounsellorSession(){
        String counsellorId = prefs.getString(KEY_COUNSELLOR_ID, null);
        GetUser.GetCounsellor(counsellorId, new GetUser.GetUserCallback() {


                    @Override
                    public void onSuccess(String message) {

                    }

                    @Override
                    public void onFailure(String error) {

                    }
                });
    }
    // Save counsellor session
    public static void saveCounsellorSession(String id) {
        prefs.edit()
                .putString(KEY_USER_TYPE, "counsellor")
                .putString(KEY_COUNSELLOR_ID, id)
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



    // Clear session

    public static void clearSession() {
        prefs.edit().clear().apply();
    }
    public static void setLogin(boolean isLoggedIn) {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply();
    }

    public static boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }




}
