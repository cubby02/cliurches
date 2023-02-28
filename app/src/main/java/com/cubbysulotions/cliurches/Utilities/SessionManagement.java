package com.cubbysulotions.cliurches.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_id";
    String SESSION_KEY_2 = "session_username";

    String SESSION_KEY_3 = "acc_type";
    String SESSION_KEY_4 = "admin_name";


    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(UserSessionManagement users){
        //save session of user whenever user is logged in
        int id = users.getId();
        String username = users.getUsername();
        String acc_type = users.getAcc_type();
        String admin = users.getAdmin_name();

        editor.putInt(SESSION_KEY, id)
                .putString(SESSION_KEY_2, username)
                .putString(SESSION_KEY_3, acc_type)
                .putString(SESSION_KEY_4, admin)
                .commit();
    }

    public int getSession(){
        //return user id whose session is saved
        return sharedPreferences.getInt(SESSION_KEY, -1); //-1 is the default value of the shared preferences
    }

    public String getSession2(){
        return sharedPreferences.getString(SESSION_KEY_2, "");
    }

    public String getAccountType(){
        return sharedPreferences.getString(SESSION_KEY_3, "");
    }

    public String getAdminName(){
        return sharedPreferences.getString(SESSION_KEY_4, "");
    }


    public void removeSession(){
        //this will remove the current session_id and it will return -1 as the default value
        editor.putInt(SESSION_KEY, -1).commit();
    }
}
