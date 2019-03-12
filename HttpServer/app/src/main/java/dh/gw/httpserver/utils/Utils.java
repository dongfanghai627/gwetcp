package dh.gw.httpserver.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by dh on 2019/3/6.
 */

public class Utils {
    public static boolean isStrEmpty(String str){
         return (str == null || str.isEmpty() || str.length() == 0);
    }

    public static String getLocalIpStr(Context context) {
        WifiManager wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return intToIpAddr(wifiInfo.getIpAddress());
    }

    private static String intToIpAddr(int ip) {
        return (ip & 0xff) + "." + ((ip>>8)&0xff) + "." + ((ip>>16)&0xff) + "." + ((ip>>24)&0xff);
    }

    public static String getNowTime(){
        return "20190311110737896";
    }
}
