package dh.gw.httpserver.server.fw.abs;

/**
 * Created by dh on 2019/3/8.
 */

public interface IExecutorFactory {
    public IExecutor query(String fn);
}
