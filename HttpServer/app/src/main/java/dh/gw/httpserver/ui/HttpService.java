package dh.gw.httpserver.ui;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import dh.gw.httpserver.server.ServerManager;
import dh.gw.httpserver.server.fw.http.IHttpServerCallback;
import dh.gw.httpserver.utils.Utils;

public class HttpService extends Service {

    private static final String TAG = "DH_HttpService";

    private IServiceCallback mCallback = null;



    public class HttpServerBinder extends Binder{

        public void start(int port){
            HttpService.this.startHttpServer(port);
        }

        public void stop(){
            HttpService.this.stopHttpServer();
        }

        public void registerCallback(IServiceCallback callback){
            HttpService.this.registerCallback(callback);
        }

        public void unregisterCallback(IServiceCallback callback){
            HttpService.this.unregisterCallback(callback);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new HttpServerBinder();
    }

    @Override
    public void onCreate(){
        Log.d(TAG,"onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent,int flag,int startId){
        Log.d(TAG,"onStartCommand");

        String command = intent.getStringExtra(HttpStatus.COMMAND);
        if(!Utils.isStrEmpty(command) && command.equals(HttpStatus.Value.SERVICE_STOP)){
            onDestroy();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        Log.d(TAG,"onDestroy");
        ServerManager.getInstance().stopServer();
        if(mCallback != null) {
            mCallback.onHttpStatusChanged(HttpStatus.Value.SERVICE_STOP);
        }
        super.onDestroy();
    }

    public void startHttpServer(int port)  {
        Log.d(TAG,"startHttpServer");
        ServerManager.getInstance().init(this,port);
        ServerManager.getInstance().registerListener(new ServerManager.ServerStatusListener() {
            @Override
            public void onServerStatusChanged(String status) {
                if(mCallback != null) {
                    mCallback.onServiceStatusChanged(status);
                }
            }
        });
        ServerManager.getInstance().startServer();
    }

    public void stopHttpServer()  {
        Log.d(TAG,"stopHttpServer");
        ServerManager mg = ServerManager.getInstance();
        mg.stopServer();
        mg.unregisterListener(null);
    }

    public void registerCallback(IServiceCallback callback){
        mCallback = callback;
        startHttpServer(8080);
    }

    public void unregisterCallback(IServiceCallback callback){
        mCallback = null;
    }
}
