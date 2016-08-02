package com.looper.catchcrashlogtool;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * Created by looper on 2016/8/1.
 */
public class Cclt {

    private static final String TAG = "Cclt";
    private static Cclt INSTANCE;
    private final Context ctx;
    private String dirPath = "";
    private static String addr = "";
    private double size = 10.00;
    private static final String PATH = "/CrashInfoLog";
    public static String path;
    public static String crashinfo;
    public static String loginfo;
    public boolean isSend = false;
    public static final String PREFS_NAME ="com.looper.catchcrashlogtool";

    private static final LooperEmailRunnable erunnable =  new LooperEmailRunnable();
    private static final Thread t = new Thread(erunnable);
    public static String Path;
    public static String fileName;
    public static boolean sendOk = false;
    private static UserAuthenticat user;

    public static class LooperEmailRunnable implements  Runnable{

        @Override
        public void run() {
            SendEmail.getInstance().setAuthenticat(user).setSendto(Cclt.addr).send();
        }
    }

    private Cclt(Context ctx){
        this.ctx = ctx;
    }

    public static Cclt init(Context ctx)  {
        if (INSTANCE == null) {
            INSTANCE = new Cclt(ctx);
        }
        HookAMS.getInstance(ctx).hookHandler();
        return INSTANCE;
    }


    public Cclt setDirPath(String dirPath) {
        if (!TextUtils.isEmpty(dirPath)) {
            this.dirPath = dirPath;
        }
        return this;
    }


    public Cclt setEmailAddr(String addr) {
        if (!TextUtils.isEmpty(addr)) {
            this.addr = addr;
        }
        return this;
    }

    public Cclt setUserAuthentication(UserAuthenticat userAuthentication) {
        if (userAuthentication == null) {
            sendOk = false;
        }else {
            if (checkUserInfo(userAuthentication)) {
                sendOk = true;
                this.user = userAuthentication;
            }
        }
        return this;
    }

    private boolean checkUserInfo(UserAuthenticat userAuthenticat) {
        if (TextUtils.isEmpty(userAuthenticat.getUseraddr())) {
            return false;
        }
        if (TextUtils.isEmpty(userAuthenticat.getKey())) {
            return false;
        }
        if (TextUtils.isEmpty(userAuthenticat.getHost())) {
            return false;
        }
        if (TextUtils.isEmpty(String.valueOf(userAuthenticat.getPort()))) {
            return false;
        }
        return true;
    }

    public Cclt setDirSize(double size) {
        if (size >0.00) {
            this.size = size;
        }
        return this;
    }

    public void Ok(){
        if (checkDirPath()) {
            NewFileDir();
        }else {
            return;
        }
        if (!TextUtils.isEmpty(addr)) {
            isSend = true;
        }
        saveEmailAddr();
        if (checkNetWork()) {
            if (checkfile()) {
                FileTool.ZipFile();
                Path = FileTool.ZipPath;
                File f  = new File(Path);
                if (f.exists()) {
                    fileName = f.getName();
                }
                getAddr();
            }else {
                File f = new File(Cclt.crashinfo);
                if (f.exists()) {
                    if (f.isDirectory()) {
                        for (File e:f.listFiles()) {
                            Path = e.getAbsolutePath();
                            fileName = e.getName();
                        }
                    }
                }
                getAddr();
            }
        }else {
            // 没有网络,do nothing 什么也不做
        }
    }

    private boolean checkDirPath() {
        if (TextUtils.isEmpty(dirPath)) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                if (!TextUtils.isEmpty(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                    dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+PATH;
                    return true;
                }
                return false;
            }
            return false;
        }
        return true;
    }

    private void  NewFileDir() {
        try {
            path = CreateDir(dirPath).getAbsolutePath();
            crashinfo = CreateDir(path+"/crashinfo").getAbsolutePath();
            loginfo = CreateDir(path+"/loginfo").getAbsolutePath();
        }catch (Exception e) {
            Log.i(TAG,"设置Crash信息或log日志目录路径错误！");
        }
    }

    private File CreateDir(String pathDir) {
        File file = new File(pathDir);
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    private void saveEmailAddr() {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        if (isSend) {
            editor.putString("addr", addr);
        }else {
            editor.putString("addr","");
        }
        editor.commit();
    }

    private void getAddr() {
        SharedPreferences settings = ctx.getSharedPreferences(Cclt.PREFS_NAME, 0);
        addr = settings.getString("addr", "");
        if (!TextUtils.isEmpty(addr)&&sendOk) {
            t.start();
        }
    }

    private boolean checkNetWork() {
        boolean flag = false;
        try {
            ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager.getActiveNetworkInfo() != null) {
                flag = manager.getActiveNetworkInfo().isAvailable();
            }
        }catch (Exception e) {
            Log.i(TAG,"checkNetWork错误！");
        }
        return flag;
    }

    private boolean checkfile() {
        File file = new File(Cclt.crashinfo);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if(files.length > 1 ) {
                    return true;
                }
            }
        }
        return false;
    }
}
