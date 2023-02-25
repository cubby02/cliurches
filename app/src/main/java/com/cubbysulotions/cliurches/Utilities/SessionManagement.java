package com.cubbysulotions.cliurches.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_id";
    String SESSION_KEY_2 = "session_username";


    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(UserSessionManagement users){
        //save session of user whenever user is logged in
        int id = users.getId();
        String username = users.getUsername();

        editor.putInt(SESSION_KEY, id)
                .commit();
    }

    public int getSession(){
        //return user id whose session is saved
        return sharedPreferences.getInt(SESSION_KEY, -1); //-1 is the default value of the shared preferences
    }


    public void removeSession(){
        //this will remove the current session_id and it will return -1 as the default value
        editor.putInt(SESSION_KEY, -1).commit();
    }
}
