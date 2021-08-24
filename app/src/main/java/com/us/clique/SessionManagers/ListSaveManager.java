package com.us.clique.SessionManagers;

import android.content.Context;
import android.content.SharedPreferences;

import com.us.clique.bottomNavigation.modle.ExperinceModle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ListSaveManager {
    private static final String KEY_ARRAY_LIST = "list";
    SharedPreferences.Editor editor1;
    private SharedPreferences pref;
    private static final String PREFER_NAME = "Clique";
    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    private Context context;
    private int PRIVATE_MODE = 0;
    public ListSaveManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor1 = pref.edit();
    }
    public <T> void setList( ArrayList<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        set(KEY_ARRAY_LIST, json);
    }
    public void set(String key, String value) {
        editor1.putString(key, value);
        editor1.commit();
    }
    public ArrayList<ExperinceModle.Datum> getList(){
        ArrayList<ExperinceModle.Datum> arrayItems = new ArrayList<>();
        String serializedObject = pref.getString(KEY_ARRAY_LIST, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<ExperinceModle.Datum>>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }
    public void Clearlist() {

        // Clearing all user data from Shared Preferences
        editor1.clear();
        editor1.commit();
    }
}
