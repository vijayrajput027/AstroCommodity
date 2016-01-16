package com.mohanastrology.commodity.javafiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by niraj  on 11/12/2015.
 */
public class ApplicationStatus {
    Context mContext;
    private static final String SP_START_SERVICE = "start";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_LOGIN = "login";
    private static boolean KEY_IS_LOGIN = false;
    private static String KEY_EMAIL = "";


    public ApplicationStatus(Context ctx) {
        // TODO Auto-generated constructor stub
        this.mContext = ctx;
    }

    public void setEmail(String user) {
        this.KEY_EMAIL = user;
        SharedPreferences sp = mContext.getSharedPreferences(SP_START_SERVICE,
                Context.MODE_PRIVATE);
        Editor edt = sp.edit();
        edt.putString(KEY_USER_EMAIL, KEY_EMAIL);
        edt.commit();
    }

    public String getEmail() {
        SharedPreferences sp2 = mContext.getSharedPreferences(SP_START_SERVICE,
                Context.MODE_PRIVATE);
        return sp2.getString(KEY_USER_EMAIL, KEY_EMAIL);
    }

    public void setLoginStatus(Boolean flag) {
        this.KEY_IS_LOGIN = flag;
        SharedPreferences sp = mContext.getSharedPreferences(SP_START_SERVICE,
                Context.MODE_PRIVATE);
        Editor edt = sp.edit();
        edt.putBoolean(KEY_LOGIN, KEY_IS_LOGIN);
        edt.commit();
    }

    public boolean getLoginStatus() {
        SharedPreferences sp1 = mContext.getSharedPreferences(SP_START_SERVICE,
                Context.MODE_PRIVATE);
        return sp1.getBoolean(KEY_LOGIN, KEY_IS_LOGIN);
    }

}

