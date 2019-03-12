package dh.gw.httpserver.ui;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import dh.gw.httpserver.R;
import dh.gw.httpserver.client.HttpClient;
import dh.gw.httpserver.client.IHttpClientCallback;
import dh.gw.httpserver.utils.Utils;

public class HttpServerActivity extends AppCompatActivity {

    private static final String TAG = "DH_HttpServerActivity";

    private TextView mHttpStats;

    private EditText mHttpText;

    private HttpService.HttpServerBinder mBinder;

    private HttpHandler mHandler;

    private HttpCallback mCallback;

    private StatusFIFO mFIFO;


    public static void lunchActivity(Context context){
        Intent intent = new Intent(context,HttpServerActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_server);

        mHttpText = findViewById(R.id.httpRequest);
        mHttpStats = findViewById(R.id.serverStatus);
        mHandler = new HttpHandler(Looper.getMainLooper());
        mCallback = new HttpCallback();
        mFIFO = new StatusFIFO(6);

        Button b1 = findViewById(R.id.sendCommand);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent intent = new Intent(HttpServerActivity.this,HttpService.class);
                  intent.putExtra(HttpStatus.COMMAND, HttpStatus.Value.SERVICE_START);
                  startService(intent);
            }
        });

        Button b2 = findViewById(R.id.stopServer);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HttpServerActivity.this,HttpService.class);
                intent.putExtra(HttpStatus.COMMAND, HttpStatus.Value.SERVICE_STOP);
                startService(intent);
                mBinder = null;
            }
        });

        Button b3 = findViewById(R.id.bindServer);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HttpServerActivity.this,HttpService.class);
                //Log.d(TAG,"bindService");
                bindService(intent,mConn, Service.BIND_AUTO_CREATE);
            }
        });

        Button b4 = findViewById(R.id.unbindServer);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBinder != null){
                    mBinder.unregisterCallback(mCallback);
                    unbindService(mConn);
                    mBinder = null;
                }
            }
        });

        /*
        Button b5 = findViewById(R.id.start);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBinder != null){
                    mBinder.start(8080);
                }
            }
        });

        Button b6 = findViewById(R.id.stop);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBinder != null){
                    mBinder.stop();
                }
            }
        });

*/
        Button b7 = findViewById(R.id.sendHttpRequest);
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   String url = mHttpText.getText().toString();
                   new HttpClient().syncRequestHttp(url, new IHttpClientCallback() {
                       @Override
                       public void onResult(String str) {
                           mFIFO.push("["+HttpStatus.HTTP_CLIENT+"]: "+str+"\n");
                           mHandler.sendHttpMessage(HttpHandler.COMMAND1,mFIFO.convertToStr());
                       }

                       @Override
                       public void onUrlError(String str) {
                           mHandler.sendHttpMessage(HttpHandler.COMMAND2,str);
                       }
                   });
            }
        });
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG,"onServiceConnected");
            mBinder = (HttpService.HttpServerBinder)service;
            mFIFO.push("["+HttpStatus.SERVICE_STATUS+"]: "+HttpStatus.Value.SERVICE_BIND+"\n");
            mHandler.sendHttpMessage(HttpHandler.COMMAND1,mFIFO.convertToStr());
            mBinder.registerCallback(mCallback);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private class HttpCallback implements IServiceCallback {

        @Override
        public void onHttpStatusChanged(String status) {
            mFIFO.push("["+HttpStatus.HTTP_SERVER +"]: "+status+"\n");
            mHandler.sendHttpMessage(HttpHandler.COMMAND1,mFIFO.convertToStr());
        }

        @Override
        public void onServiceStatusChanged(String status) {
            mFIFO.push("["+HttpStatus.SERVICE_STATUS+"]: "+status+"\n");
            mHandler.sendHttpMessage(HttpHandler.COMMAND1,mFIFO.convertToStr());
        }

        @Override
        public void onHttpRequest(String str) {
            mFIFO.push("["+HttpStatus.HTTP_SERVER+"]: "+str+"\n");
            mHandler.sendHttpMessage(HttpHandler.COMMAND1,mFIFO.convertToStr());
        }
    }

    private class HttpHandler extends Handler{
        private static final String KEY = "key";

        public static final int COMMAND1 = 1;

        public static final int COMMAND2 = 2;

        HttpHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            int command = msg.arg1;
            switch (command){
                case COMMAND1:
                    mHttpStats.setText(msg.getData().getString(KEY));
                    break;
                case COMMAND2:
                    mHttpText.setText(msg.getData().getString(KEY));
                    break;
                default:
            }

        }

        public void sendHttpMessage(int comand,String str){
            if(Utils.isStrEmpty(str)){
                return;
            }

            Message msg = obtainMessage();
            msg.arg1 = comand;
            Bundle bundle = new Bundle();
            bundle.putString(KEY,str);
            msg.setData(bundle);
            sendMessage(msg);
        }
    }

    private static class StatusFIFO{

        private int mSize = 0;

        private Node mTop;

        private Node mEnd;

        public StatusFIFO(int size){
            init(size);
        }

        public void init(int size){
            mSize = size;
            if(size == 0){
                return;
            }

            mTop = new Node();
            mEnd = mTop;
            for(int i=1;i<size;i++){
                Node node = new Node();
                mEnd.next = node;
                mEnd.value = null;
                mEnd = mEnd.next;
            }
            mEnd.next = mTop;
        }

        public void push(String status){
            Log.d(TAG,"fifo put: "+ status);
            if(mSize <= 0){
                return;
            }
            if(mEnd.next.equals(mTop) && !Utils.isStrEmpty(mEnd.value)){
                mTop = mTop.next;
            }
            mEnd = mEnd.next;
            mEnd.value = status;
        }

        public String pop(){
            if(mSize <= 0){
                return null;
            }
            String str = mTop.value;
            mTop = null;
            mTop = mTop.next;
            return str;
        }

        public String convertToStr(){
            if(Utils.isStrEmpty(mTop.value)){
                Log.d(TAG,"str is null");
                return null;
            }
            StringBuilder sb= new StringBuilder();
            Node cur = mTop;
            while(!cur.equals(mEnd)  && !Utils.isStrEmpty(cur.value)){
                sb.append(cur.value);
                cur = cur.next;
            }
            sb.append(mEnd.value);
            return sb.toString();
        }

        private class Node{
            Node next;
            String value;
        }
    }
}
