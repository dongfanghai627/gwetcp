package dh.gw.httpserver.server.executor;

import dh.gw.httpserver.server.agreement.Action;
import dh.gw.httpserver.server.fw.abs.IExecutor;
import dh.gw.httpserver.server.fw.abs.IExecutorFactory;
import dh.gw.httpserver.utils.Utils;

/**
 * Created by dh on 2019/3/8.
 */

public class ExecutorFactory implements IExecutorFactory {

    @Override
    public IExecutor query(String fn){
        if(Utils.isStrEmpty(fn)){
            return null;
        }
        switch (fn){
            case Action.CAR_AUTH:
                return new CarAuthExecutor();
            case Action.USER_SIGNIN:
                return new UserSigninExecutor();
            case Action.BIND_CAR:
                return new BindCarExecutor();
            case Action.UNBIND_CAR:
                return new UnbindCarExecutor();
            case Action.CAR_NUM_QUERY:
                return new CarNumQueryExecutor();
            default:
        }
        return null;
    }
}
