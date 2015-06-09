package com.example.pcuser.myweather.ss.pku.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

/**
 * Created by pcuser on 15/3/23.
 */
public class NetUtil {
    public static final int NET_CONNECTION_NONE = 0;
    public static final int NET_CONNECTION_WIFI = 1;
    public static final int NET_CONNECTION_MOBILE = 2;

    public static int getNetConnectionState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //wifi
        State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NET_CONNECTION_WIFI;
        }

        //mobile
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NET_CONNECTION_MOBILE;
        }

        return NET_CONNECTION_NONE;
    }

}
