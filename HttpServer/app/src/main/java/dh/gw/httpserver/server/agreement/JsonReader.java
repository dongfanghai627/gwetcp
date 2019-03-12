package dh.gw.httpserver.server.agreement;

import org.json.JSONException;
import org.json.JSONObject;

import dh.gw.httpserver.server.fw.abs.IJsonReader;

/**
 * Created by dh on 2019/3/8.
 */

public class JsonReader implements IJsonReader {
    private static final String TAG = "DH_JsonParser";

    private JSONObject mJson;

    public JsonReader(JSONObject json){
        mJson = json;
    }

    private boolean isJsonValid(){
        return mJson != null;
    }

    @Override
    public String getAction() {
        if(!isJsonValid()){
            return null;
        }


        JSONObject sub = null;
        try {
            sub = mJson.getJSONObject(URLKey.HEADER);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(sub == null){
            return null;
        }

        String fn = null;
        try {
            fn = sub.getString(URLKey.FN);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return fn;
    }

    @Override
    public String getValue(String key) {
        if(!isJsonValid()){
            return null;
        }

        String result = null;
        try {
            result = mJson.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public IJsonReader getSubJson(String key) {
        if(!isJsonValid()){
            return null;
        }

        JSONObject sub = null;
        try {
            sub = mJson.getJSONObject(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(sub == null){
            return null;
        }

        return new JsonReader(sub);
    }

    @Override
    public String toString(){
        return mJson.toString();
    }
}
