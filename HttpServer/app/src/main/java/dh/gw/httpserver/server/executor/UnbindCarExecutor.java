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
 * Created by dh on 2019/3/11.
 */

public class UnbindCarExecutor implements IExecutor {
    private static final String TAG = UnbindCarExecutor.class.getSimpleName();
    @Override
    public Result asyncExecute(IJsonReader json) {
        String token = json.getSubJson(URLKey.BODY).getValue(URLKey.TOKEN);
        String plateNumber = json.getSubJson(URLKey.BODY).getValue(URLKey.PLATE_NUM);

        ILog.d(TAG, "[checkCarNmu]--" + "[" + token + "," + plateNumber + "," + plateNumber + "]");

        JSONObject jo = new JSONObject();
        try {
            JSONObject joHeader = new JSONObject();
            joHeader.put(URLKey.C, URLKey.Value.CODE_SUCCESS);
            joHeader.put(URLKey.FN, Action.UNBIND_CAR);
            joHeader.put(URLKey.REQ_TS, Utils.getNowTime());
            joHeader.put(URLKey.M, URLKey.Value.M_SUCCESS);
            joHeader.put(URLKey.TS, Utils.getNowTime());

            JSONObject joBody = new JSONObject();
            joBody.put(URLKey.CODE, URLKey.Value.CODE_SUCCESS);
            joBody.put(URLKey.MESSAGE, URLKey.Value.MESSAGE_OK);

            jo.put(URLKey.HEADER, joHeader);
            jo.put(URLKey.BODY, joBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String resultStr = jo.toString();
        ILog.d(TAG, "the result is : " + resultStr);
        Result result = new Result();
        result.setResult(resultStr);
        result.setAction(Action.BIND_CAR);
        result.setParameters(json.getSubJson(URLKey.BODY).toString());
        return result;
    }
}
