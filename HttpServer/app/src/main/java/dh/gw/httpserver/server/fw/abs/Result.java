package dh.gw.httpserver.server.fw.abs;

/**
 * Created by dh on 2019/3/8.
 */

public class Result {
    private String mResult;
    private String mAction;
    private String mParameters;

    public void setResult(String result){
        mResult = result;
    }
    public String getResult(){
        return mResult;
    }
    public void setAction(String str){mAction = str;}
    public void setParameters(String str){mParameters = str;};
    public String getAction(){return mAction;};
    public String getParameters(){return mParameters;}
}
