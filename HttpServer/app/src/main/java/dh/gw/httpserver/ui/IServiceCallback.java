package dh.gw.httpserver.ui;

public interface IServiceCallback {
    public void onHttpStatusChanged(String status);
    public void onServiceStatusChanged(String status);
    public void onHttpRequest(String str);
}
