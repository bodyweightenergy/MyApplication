package com.example.hsalem.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Prefs {
    private static String ACCESS_PREF = "access_code";
    private static String IP_PREF = "ip_address";
    private static String PORT_PREF = "port_number";
    private static String RELAY_PREF_1 = "relay_status_1";
    private static String RELAY_PREF_2 = "relay_status_2";
    private static String RELAY_PREF_3 = "relay_status_3";
    private static String RELAY_PREF_4 = "relay_status_4";

    private static SharedPreferences getPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static int getAccessCode(Context context) {
        return getPrefs(context).getInt(ACCESS_PREF, 0000);
    }

    public static String getIPAddress(Context context) {
        return getPrefs(context).getString(IP_PREF, "10.1.11.157");
    }

    public static int getPortNumber(Context context) {
        return getPrefs(context).getInt(PORT_PREF, 23);
    }

    public static boolean getRelayStatus1(Context context) {
        return getPrefs(context).getBoolean(RELAY_PREF_1, false);
    }
    public static boolean getRelayStatus2(Context context) {
        return getPrefs(context).getBoolean(RELAY_PREF_2, false);
    }
    public static boolean getRelayStatus3(Context context) {
        return getPrefs(context).getBoolean(RELAY_PREF_3, false);
    }
    public static boolean getRelayStatus4(Context context) {
        return getPrefs(context).getBoolean(RELAY_PREF_4, false);
    }

    public static void setAccessCode(Context context, int code) {
        // perform validation etc..
        getPrefs(context).edit().putInt(ACCESS_PREF, code).commit();
    }

    public static void setIPAddress(Context context, String address) {
        // perform validation etc..
        getPrefs(context).edit().putString(IP_PREF, address).commit();
    }

    public static void setPortNumber(Context context, int number) {
        // perform validation etc..
        getPrefs(context).edit().putInt(PORT_PREF, number).commit();
    }

    public static void setRelayStatus1(Context context, boolean status) {
        // perform validation etc..
        getPrefs(context).edit().putBoolean(RELAY_PREF_1, status).commit();
    }
    public static void setRelayStatus2(Context context, boolean status) {
        // perform validation etc..
        getPrefs(context).edit().putBoolean(RELAY_PREF_2, status).commit();
    }
    public static void setRelayStatus3(Context context, boolean status) {
        // perform validation etc..
        getPrefs(context).edit().putBoolean(RELAY_PREF_3, status).commit();
    }
    public static void setRelayStatus4(Context context, boolean status) {
        // perform validation etc..
        getPrefs(context).edit().putBoolean(RELAY_PREF_4, status).commit();
    }
}
