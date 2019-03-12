package dh.gw.httpserver.server;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dh.gw.httpserver.server.executor.ExecutorFactory;
import dh.gw.httpserver.server.fw.abs.AbsExecuteManager;
import dh.gw.httpserver.server.fw.abs.IExecutorFactory;
import dh.gw.httpserver.server.fw.abs.IJsonReader;
import dh.gw.httpserver.server.fw.abs.Result;
import dh.gw.httpserver.server.fw.http.HttpServer;
import dh.gw.httpserver.server.fw.http.IHttpServerCallback;
import dh.gw.httpserver.server.agreement.JsonReader;

/**
 * Created by dh on 2019/3/8.
 */

public class ServerManager {

    private static ServerManager mServerManager = null;

    private HttpServer mServer;

    private ExecuteManger mManager;

    private HttpListener mListener;

    private ArrayList<ServerStatusListener> mLists;

    public interface ServerStatusListener{
        public void onServerStatusChanged(String status);
    }

    private ServerManager(){
        mLists = new ArrayList<>();
        mManager = new ExecuteManger();
        mListener = new HttpListener();
    }

    public static ServerManager getInstance(){
        if(mServerManager == null){
            synchronized (ServerManager.class) {
                if(mServerManager == null) {
                    mServerManager = new ServerManager();
                }
            }
        }
        return mServerManager;
    }

    public void registerListener(ServerStatusListener listener){
        if(!mLists.contains(listener)){
            mLists.add(listener);
        }
    }

    public void unregisterListener(ServerStatusListener listener){
         mLists.remove(listener);
    }

    public void init(Context context,int port){
        if(mServer == null){
            mServer = new HttpServer(context,port);
        }
    }

    public void startServer(){
        if(mServer != null){
            mServer.registerHttpServerCallback(mListener);
            mServer.start();
        }
    }

    public void stopServer(){
        if(mServer != null){
            mServer.stop();
            mServer.unregisterHttpServerCallback(mListener);
        }
    }

    private void notifyStatus(String status){
        for(ServerStatusListener listener:mLists){
            listener.onServerStatusChanged(status);
        }
    }

    private class ExecuteManger extends AbsExecuteManager{
        @Override
        public IExecutorFactory createFactory() {
            return new ExecutorFactory();
        }

        @Override
        public IJsonReader createJsonParser(String json) {
            IJsonReader parse = null;
            try {
                JSONObject object = new JSONObject(json);
                parse = new JsonReader(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return parse;
        }
    }

    private class HttpListener implements IHttpServerCallback{
        @Override
        public void onServerStatusChanged(String status) {
            notifyStatus(status);
        }

        @Override
        public Result onHttpRequest(String str) {
            if(mManager != null){
                 return mManager.execute(str);
            }
            return null;
        }
    }
}
