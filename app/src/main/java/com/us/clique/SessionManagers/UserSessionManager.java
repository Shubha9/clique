package com.us.clique.SessionManagers;

import android.content.Context;
import android.content.SharedPreferences;


import java.util.HashMap;

public class UserSessionManager {

    SharedPreferences sharedPreferences;
    // Editor reference for Shared preferences
    SharedPreferences.Editor editor;

    //context
    Context context;

    //shared pref mode
    int PRIVATE_MODE = 0;
    private static final String PREFER_NAME = "Clique";

    public static final String KEY_accessToken = "access_token";
    /*
    public static final String KEY_GUEST = "isguest";*/
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    public static final String Email = "email";
    public static final String Name = "name";
    public static final String UserId = "userId";
    private static final String KEY_IS_DATA_CHANGED = "data_changed";
    private static final String KEY_IS_USER_PROFILE_CLICKED = "user_clicked";
    private static final String KEY_PROFILE_ID = "profile_id";

    public UserSessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }


    public void createUserLoginSession(String accessToken) {
        editor.putString(KEY_accessToken, accessToken);
        editor.putBoolean(IS_USER_LOGIN, true);

        // commit changes
        editor.commit();
    }
    //get session
    public String getEmail() {
        return sharedPreferences.getString(Email, "");
    }
    //set session
    public void createEmail(String email){
        editor.putString(Email,email);
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.commit();
    }

    //set session
    public void createName(String name){
        editor.putString(Name,name);
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.commit();
    }
    //get session
    public String getName() {
        return sharedPreferences.getString(Name, "");
    }

    //set session
    public void createUserId(String userId){
        editor.putString(UserId,userId);
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.commit();
    }
    //get session
    public String getUserId() {
        return sharedPreferences.getString(UserId, "");
    }


    public HashMap<String, String> getUserDetails() {

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        //accessToken
        user.put(KEY_accessToken, sharedPreferences.getString(KEY_accessToken, null));

        return user;

    }

    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(IS_USER_LOGIN, false);
    }

    public void logoutUser() {
        editor.putBoolean(IS_USER_LOGIN, false);
        editor.clear();
        editor.commit();

    }

    public void setisDataChanged(boolean isChanged) {
        editor.putBoolean(KEY_IS_DATA_CHANGED, isChanged);
        editor.commit();
    }

    public boolean getisDataChanged() {
        sharedPreferences.getBoolean(KEY_IS_DATA_CHANGED, false);
        return sharedPreferences.getBoolean(KEY_IS_DATA_CHANGED, false);
    }


    public void setUserProfileCLicked(boolean isChanged) {
        editor.putBoolean(KEY_IS_USER_PROFILE_CLICKED, isChanged);
        editor.commit();
    }

    public boolean getUserProfileCLiked() {
        return sharedPreferences.getBoolean(KEY_IS_USER_PROFILE_CLICKED, false);
    }


    public String  getUserProfile() {
        return sharedPreferences.getString(KEY_PROFILE_ID, "");
    }

    public void setUserProfile(String isChanged) {
        editor.putString(KEY_PROFILE_ID, isChanged);
        editor.commit();
    }

}
