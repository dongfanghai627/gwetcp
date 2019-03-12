package dh.gw.httpserver.server.fw.http;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.Map;
import java.util.Set;

import dh.gw.httpserver.server.fw.abs.AbsExecuteManager;
import dh.gw.httpserver.server.fw.abs.Result;
import dh.gw.httpserver.utils.Utils;
import fi.iki.elonen.NanoHTTPD;

/**
 * Created by dh on 2019/3/7.
 */

public class HttpServer extends NanoHTTPD {

    private static final String TAG = "DH_HttpServer";

    private IHttpServerCallback mCallback;

    private Context mContext;

    public void registerHttpServerCallback(IHttpServerCallback callback){
        mCallback = callback;
    }

    public void unregisterHttpServerCallback(IHttpServerCallback callback){
        mCallback = null;
    }

    public HttpServer(Context context,int port) {
        super(port);
        mContext = context;
    }

    @Override
    public Response serve(IHTTPSession session) {
        Log.d(TAG,"httpServer serve");
        String uri = session.getUri();
        Method method = session.getMethod();
        Map<String,String> parameters = session.getParms();
        String query = session.getQueryParameterString();
        //Log.d(TAG,"query: "+query);
        String json = parameters.get("p");
        //Log.d(TAG, "json: "+json);
        //String test = "{\"header\":{\"fn\":\"checkCarNum\"},\"body\":{\"token\":\"2019\",\"carFrameNum\":\"00001\",\"plateNumber\":\"00001\"}}";
        Log.d(TAG, "json: "+json);
        //Log.d(TAG, "test: "+test);
        json.replace("\"","\\\"");



        Result result = null;
        if(mCallback  != null){
            result = mCallback.onHttpRequest(json);
            String status = "error!";
            if(result != null) {
                StringBuffer sb = new StringBuffer();
                sb.append(method).append("#");
                sb.append(result.getAction()).append("#");
                sb.append(result.getParameters());
                status = sb.toString();
            }
            mCallback.onServerStatusChanged(status);
        }

        Response response = new Response(null);
        response.setStatus(Response.Status.OK);
        InputStream in = new StringBufferInputStream(result.getResult());
        response.setData(in);
        //return super.serve(session);
        return response;
    }


    private void disMap(Map<String,String> parameters){
        Set<String> p = parameters.keySet();
        Log.d(TAG,p.toString());
        for(String key: p){
            Log.d(TAG,"[key]: "+key+" [vlaue]: "+parameters.get(key));
        }
    }

    @Override
    public void start(){
        if(isAlive()){
            Log.d(TAG,"http server has been started!");
            return;
        }
        Log.d(TAG,"server start");
        try {
            super.start();
        } catch (IOException e) {
            Log.d(TAG,"http server start error!");
        }
        if(mCallback != null) {
            mCallback.onServerStatusChanged("server start...");
            String ip = Utils.getLocalIpStr(mContext);
            mCallback.onServerStatusChanged(ip + ":8080");
        }
    }

    @Override
    public void stop() {
        if (!isAlive()) {
            Log.d(TAG, "http server has been stoped!");
            return;
        }
        Log.d(TAG,"server stop");
        super.stop();
        if(mCallback != null) {
            mCallback.onServerStatusChanged("server stop...");
        }
    }
}
