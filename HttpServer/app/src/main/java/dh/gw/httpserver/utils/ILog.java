package dh.gw.httpserver.utils;

import android.util.Log;

import dh.gw.httpserver.server.executor.BindCarExecutor;

/**
 * Created by dh on 2019/3/11.
 */

public class ILog {
    private static final String TAG = "DH_";
    public static void d(String tag,String value){
        Log.d(TAG+tag,value);
    }
}
