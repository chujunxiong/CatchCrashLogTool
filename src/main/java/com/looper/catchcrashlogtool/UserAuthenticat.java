package com.looper.catchcrashlogtool;

/**
 * Created by looper on 2016/8/2.
 */
public class UserAuthenticat {
    private String useraddr;
    private String key;
    private String host;
    private int port;
    private boolean isSSH;
    private static UserAuthenticat INSTANCE;

    private UserAuthenticat(String username,String key,String host,int port,boolean isSSH ) {
        this.useraddr = username;
        this.key = key;
        this.host = host;
        this.port = port;
        this.isSSH = isSSH;
    }

    public static UserAuthenticat init(String username, String key, String host, int port, boolean isSSH){
        if (INSTANCE == null) {
            INSTANCE = new UserAuthenticat(username,key,host,port,isSSH);
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

    public boolean getIsSSH() {
        return isSSH;
    }


}
