package dh.gw.httpserver.server.agreement;

/**
 * Created by dh on 2019/3/8.
 */

public class URLKey {

    public static final String HEADER = "header";
    public static final String BODY = "body";
    public static final String FN = "fn";
    public static final String C = "c";
    public static final String REQ_TS = "reqTs";
    public static final String M = "m";
    public static final String TS = "ts";
    public static final String VIN = "vin";
    public static final String ID = "id";
    public static final String SN = "sn";
    public static final String V = "v";
    public static final String I18N = "i18n";
    public static final String HU_TOKEN = "hoTuken";

    //parameters
    public static final String DATA = "data";
    public static final String TOKEN = "token";
    public static final String CAR_FRAME_NUM = "carFrameNum";
    public static final String PLATE_NUM = "plateNumber";
    public static final String CODE = "code";
    public static final String MESSAGE = "message";
    public static final String APP_ID = "appId";
    public static final String MOBILE_PHONE = "mobilePhone";
    public static final String NUMBER = "number";
    public static final String CAR_LIST = "carList";

    public static class Value{
        public static int CODE_SUCCESS = 0;
        public static int CODE_FAIL = 1;

        public static final String MESSAGE_OK = "ok";
        public static final String MESSAGE_ERROR = "ERROR";

        public static int C_NORMAL = 0;
        public static int C_ERROR = 1;

        public static int M_SUCCESS = 0;
        public static int M_FAIL = 1;
        public static int M_SERVER_EXCEPTION = 3;
        public static int M_TS_OVER_TIME = 4;
    }
}
