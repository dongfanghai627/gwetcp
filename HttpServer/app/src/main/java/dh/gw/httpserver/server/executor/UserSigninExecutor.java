package dh.gw.httpserver.server.executor;

import android.util.Log;

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
 * Created by dh on 2019/3/9.
 */

public class UserSigninExecutor implements IExecutor{

    private static final String TAG = UserSigninExecutor.class.getSimpleName();
    @Override
    public Result asyncExecute(IJsonReader json) {
        String appId = json.getSubJson(URLKey.BODY).getValue(URLKey.APP_ID);
        String mobilePhone = json.getSubJson(URLKey.BODY).getValue(URLKey.MOBILE_PHONE);

        ILog.d(TAG,"[checkCarNmu]--"+"["+appId+ ","+mobilePhone+","+"]");

        JSONObject jo = new JSONObject();
        try {
            JSONObject joHeader = new JSONObject();
            joHeader.put(URLKey.C, URLKey.Value.CODE_SUCCESS);
            joHeader.put(URLKey.FN, Action.CAR_AUTH);
            joHeader.put(URLKey.REQ_TS, Utils.getNowTime());
            joHeader.put(URLKey.M, URLKey.Value.M_SUCCESS);
            joHeader.put(URLKey.TS, Utils.getNowTime());

            JSONObject joData = new JSONObject();
            joData.put(URLKey.TOKEN,makeToken());

            JSONObject joBody = new JSONObject();
            joBody.put(URLKey.CODE,URLKey.Value.CODE_SUCCESS);
            joBody.put(URLKey.DATA,joData);
            joBody.put(URLKey.MESSAGE,URLKey.Value.MESSAGE_OK);

            jo.put(URLKey.HEADER,joHeader);
            jo.put(URLKey.BODY,joBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String resultStr = jo.toString();
        Result result = new Result();
        result.setResult(resultStr);
        result.setAction(Action.USER_SIGNIN);
        result.setParameters(json.getSubJson(URLKey.BODY).toString());
        return result;
    }

    private String makeToken(){
        return "c539e7ad-f160-43a6-9c31-f1b99d97e571";
    }
}
