package dh.gw.httpserver.server.fw.http;

import dh.gw.httpserver.server.fw.abs.Result;
import fi.iki.elonen.NanoHTTPD;

/**
 * Created by dh on 2019/3/8.
 */

public interface IHttpServerCallback {
    public void onServerStatusChanged(String status);
    public Result onHttpRequest(String str);
}
