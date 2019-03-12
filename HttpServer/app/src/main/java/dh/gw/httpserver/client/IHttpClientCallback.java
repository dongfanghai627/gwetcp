package dh.gw.httpserver.client;

/**
 * Created by dh on 2019/3/8.
 */

public interface IHttpClientCallback {
    public void onResult(String str);
    public void onUrlError(String str);
}
