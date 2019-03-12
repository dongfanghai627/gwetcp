package dh.gw.httpserver.ui;

/**
 * Created by dh on 2019/3/6.
 */

public class HttpStatus {
    public static String COMMAND = "COMMAND";
    public static String SERVICE_STATUS = "service";
    public static String HTTP_SERVER = "server";
    public static String HTTP_CLIENT = "client";
    public static String URL = "url";
    public static String ADRESS = "ADRESS";
    public static String PORT = "PORT";

    public static class Value {
        public static String DEFAULT = "default";
        public static String SERVICE_START = "service start...";
        public static String SERVICE_STOP = "service stop...";
        public static String SERVICE_BIND = "service bind...";
        public static String SERVICE_UNBIND = "service unbind...";
        public static String HTTP_SERVER_START = "http server start...";
        public static String HTTP_SERVER_STOP = "http server stop...";
    }
}
