package dh.gw.httpserver.server.executor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dh.gw.httpserver.server.agreement.Action;
import dh.gw.httpserver.server.agreement.URLKey;
import dh.gw.httpserver.server.fw.abs.IExecutor;
import dh.gw.httpserver.server.fw.abs.IJsonReader;
import dh.gw.httpserver.server.fw.abs.Result;
import dh.gw.httpserver.utils.ILog;
import dh.gw.httpserver.utils.Utils;

/**
 * Created by dh on 2019/3/11.
 */

public class CarNumQueryExecutor implements IExecutor{

    private static final String TAG = CarNumQueryExecutor.class.getSimpleName();
    @Override
    public Result asyncExecute(IJsonReader json) {
        String token = json.getSubJson(URLKey.BODY).getValue(URLKey.TOKEN);
        ILog.d(TAG,"[CarNumQueryExecutor]--"+"["+token+"]");

        JSONObject jo = new JSONObject();
        try {
            JSONObject joHeader = new JSONObject();
            joHeader.put(URLKey.C, URLKey.Value.CODE_SUCCESS);
            joHeader.put(URLKey.FN, Action.CAR_NUM_QUERY);
            joHeader.put(URLKey.REQ_TS, Utils.getNowTime());
            joHeader.put(URLKey.M, URLKey.Value.M_SUCCESS);
            joHeader.put(URLKey.TS, Utils.getNowTime());

            JSONObject joBody = new JSONObject();
            joBody.put(URLKey.CODE,URLKey.Value.CODE_SUCCESS);
            joBody.put(URLKey.MESSAGE,URLKey.Value.MESSAGE_OK);

            JSONObject joData = new JSONObject();
            joData.put(URLKey.NUMBER,3);
            JSONArray list = new JSONArray();
            list.put("晋 BMZ105");
            list.put("云 C12345");
            joData.put(URLKey.CAR_LIST,list);
            joBody.put(URLKey.DATA,joData);

            jo.put(URLKey.HEADER,joHeader);
            jo.put(URLKey.BODY,joBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String resultStr = jo.toString();
        ILog.d(TAG,"the result is : "+resultStr);
        Result result = new Result();
        result.setResult(resultStr);
        result.setAction(Action.CAR_NUM_QUERY);
        result.setParameters(json.getSubJson(URLKey.BODY).toString());
        return result;
    }
}
