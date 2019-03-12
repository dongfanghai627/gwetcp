package dh.gw.httpserver.server.fw.abs;

/**
 * Created by dh on 2019/3/8.
 */

public interface IJsonReader {
    public String getAction();
    public String getValue(String key);
    public IJsonReader getSubJson(String key);
    public String toString();
}
