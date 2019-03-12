package dh.gw.httpserver.server.fw.abs;

/**
 * Created by dh on 2019/3/8.
 */

public interface IExecutor {
    // async method is no well, but have to design like this.
    public Result asyncExecute(IJsonReader json);
}
