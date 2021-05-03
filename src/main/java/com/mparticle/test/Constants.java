package com.mparticle.test;

public class Constants {
    public static int ONE_SECOND = 1000;
    public static int DEFAULT_SOCKET_TIMEOUT = 5_000; // 5s
    public static int DEFAULT_CONNECTION_TIMEOUT = 5_000; // 5s

    public static String AUTH_KEY_HEADER = "X-Api-Key";
    public static String DEFAULT_AUTH_KEY = "RIqhxTAKNGaSw2waOY2CW3LhLny2EpI27i56VA6N";

    public static String DEFAULT_HTTP_METHOD = "get";
    public static String DEFAULT_TARGET_RPS = "100";

    public static String DEFAULT_POST_PAYLOAD = "{\"name\":\"YOUR_NAME\",\"date\":\"2021-05-03T06:17:34.436Z\",\"requests_sent\":1}";

    public static int STATUS_SUCCESSFUL_INDEX = 0;
    public static int STATUS_FAIL_INDEX = 1;
}
