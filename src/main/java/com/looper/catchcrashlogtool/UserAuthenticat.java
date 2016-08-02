package com.looper.catchcrashlogtool;

/**
 * Created by looper on 2016/8/2.
 */
public class UserAuthenticat {
    private String useraddr;
    private String key;
    private String host;
    private int port;
    private boolean isSSL;
    private static UserAuthenticat INSTANCE;

    private UserAuthenticat(String username,String key,String host,int port,boolean isSSL ) {
        this.useraddr = username;
        this.key = key;
        this.host = host;
        this.port = port;
        this.isSSL = isSSL;
    }

    public static UserAuthenticat init(String username, String key, String host, int port, boolean isSSL){
        if (INSTANCE == null) {
            INSTANCE = new UserAuthenticat(username,key,host,port,isSSL);
        }
        return  INSTANCE;
    }

    public String getUseraddr() {
        return this.useraddr;
    }

    public String getKey() {
        return this.key;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return port;
    }

    public boolean getIsSSL() {
        return isSSL;
    }


}
