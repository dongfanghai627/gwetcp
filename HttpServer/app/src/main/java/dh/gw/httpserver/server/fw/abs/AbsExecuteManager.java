package dh.gw.httpserver.server.fw.abs;

import dh.gw.httpserver.utils.ILog;

/**
 * Created by dh on 2019/3/8.
 */

public abstract class AbsExecuteManager{

    private IExecutorFactory mFactory;

    private static final String TAG = AbsExecuteManager.class.getSimpleName();

    public abstract IExecutorFactory createFactory();

    public abstract IJsonReader createJsonParser(String json);

    public AbsExecuteManager(){
        mFactory = createFactory();
    }

    public Result execute(String json){
        ILog.d(TAG,"execute");
        Result result = new Result();

         IJsonReader parser = createJsonParser(json);
        if(parser == null){
            ILog.d(TAG,"create json parser fail");
            return result;
        }

        if(mFactory == null){
            ILog.d(TAG,"not find executor factory");
            return result;
        }

        IExecutor executor = mFactory.query(parser.getAction());
        if(executor == null){
            ILog.d(TAG,"not find executor to do");
            return result;
        }

        result = executor.asyncExecute(parser);

        return result;
    }
}
