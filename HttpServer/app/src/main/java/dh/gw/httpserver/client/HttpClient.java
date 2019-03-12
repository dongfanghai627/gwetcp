package dh.gw.httpserver.client;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import dh.gw.httpserver.utils.Utils;

/**
 * Created by dh on 2019/3/7.
 */

public class HttpClient {

    private static final String TAG = "DH_HttpClient";

    private IHttpClientCallback mCallback;

    private String mUrl;

    //private static final String URL_TEST    = "http://localhost:8080/time.txt";

    //private static final String URL_TEST    = "http://localhost:8080/sourceAccess?p={\"header\":{\"fn\":\"checkCarNum\"},\"body\":{\"token\":\"2019\",\"carFrameNum\":\"00001\",\"plateNumber\":\"00001\"}}";

    //private static final String URL_TEST  = "http://localhost:8080/sourceAccess?p={\"header\":{\"fv\":\"0203\",\"rs\":\"3\",\"v\":\"1.0\",\"sign\":\"\",\"fn\":\"textApi_park_1.0_userSignin\",\"vin\":\"HARMAN00000000010\",\"id\":\"12345hm0010000000001\",\"sn\":\"QBCWH BALYV1812050010\",\"i18n\":\"zh-cn\",\"ts\":\"20181221222101762\",\"huToken\":\"122\"},\"body\":{\" appId\":\"112\",\"mobilePhone\":\"18812344321\"}}";

    private static final String URL_TEST = "http://localhost:8080/sourceAccess?p={\"header\":{\"fv\":\"0203\",\"rs\":\"3\",\"v\":\"1.0\",\"sign\":\"\",\"fn\":\"textApi_park_1.0_carNum\",\"vin\":\"HARMAN00000000010\",\"id\":\"12345hm0010000000001\",\"sn\":\"QBCWHBALYV1812050010\",\"i18n\":\"zh-cn\",\"ts\":\"20181221222101762\",\"huToken\":\"122\"},\"body\":{\"token\":\"20181221222101762\"}}";

    //private static final String URL_TEST = "http://localhost:8080/sourceAccess?p={\"header\":{\"fv\":\"0203\",\"rs\":\"3\",\"v\":\"1.0\",\"sign\":\"\",\"fn\":\"textApi_park_1.0_bindCar\",\"vin\":\"HARMAN00000000010\",\"id\":\"12345hm0010000000001\",\"sn\":\"QBCWHBALYV1812050010\",\"i18n\":\"zh-cn\",\"ts\":\"20181221222101762\",\"huToken\":\"122\"},\"body\":{\"token\":\"20181221222101762\",\"plateNumber\":\"京 00001\"}}";

    public void syncRequestHttp(String url,IHttpClientCallback callback) {
        mCallback = callback;
        if(Utils.isStrEmpty(url)){
            mUrl = URL_TEST;
        }else {
            mUrl = url;
        }

        new ClientThread().start();
    }

    private void requestHttp(){
        Log.d(TAG,"requestHttp");
        HttpURLConnection httpURLConnection = null;
        if(Utils.isStrEmpty(mUrl)){
            mUrl = URL_TEST;
        }
        try {
            //根据URL地址实例化一个URL对象，用于创建HttpURLConnection对象。
            Log.d(TAG,"url: "+mUrl);
            URL url = new URL(mUrl);
             if (url != null) {
                 //openConnection获得当前URL的连接
                 httpURLConnection = (HttpURLConnection) url.openConnection();
                 //设置3秒的响应超时
                 httpURLConnection.setConnectTimeout(3000);
                 //设置允许输入
                 httpURLConnection.setDoInput(true);
                 //设置为GET方式请求数据
                 httpURLConnection.setRequestMethod("POST");
                 //获取连接响应码，200为成功，如果为其他，均表示有问题

                 httpURLConnection.connect();
                 int responseCode=httpURLConnection.getResponseCode();
                 Log.d(TAG,"requestHttp respond code: "+responseCode);
                 if(responseCode==200)
                 {
                     InputStream in = httpURLConnection.getInputStream();
                     //TODO chinese not display well
                     BufferedReader br = new BufferedReader(new InputStreamReader(in,"ISO-8859-1"));
                     StringBuffer sb = new StringBuffer();
                     String str = "";
                     while ((str = br.readLine()) != null){
                         sb.append(str);
                     }
                     Log.d(TAG,"response: "+sb.toString());
                     //getInputStream获取服务端返回的数据流。

                     if(mCallback != null){
                         mCallback.onResult(sb.toString());
                         mCallback.onUrlError(mUrl);
                     }
                 }
             }
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,"http connect error!");
        }
    }

    private class ClientThread extends Thread{
        @Override
        public void run(){
                requestHttp();
            Log.d(TAG,"client thread quit!");
        }
    }
}
