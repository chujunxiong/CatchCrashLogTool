package com.looper.catchcrashlogtool;

import android.app.ApplicationErrorReport;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Calendar;


/**
 * Created by looper on 2016/7/27.
 */
public class IActivityManagerHandler implements InvocationHandler {

    private static  final String TAG = "IActivityManagerHandler";
    Object iam;
    Context ctx;
    private ApplicationErrorReport.CrashInfo crashInfo = null;
    private Method newMethod = null;
    private String time;
    private PhoneInfo phoneInfo;

    public IActivityManagerHandler(Object iam, Context context) {
        Log.i(TAG,"construct!!!!!!");
        this.iam = iam;
        this.ctx = context;
        phoneInfo = PhoneInfo.getInstance(ctx);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Log.i(TAG,"AMS 已经被 Hook 啦");
        if (method.getName().equals("handleApplicationCrash")) {
            time = getDate();
            newMethod = method;
            crashInfo = (ApplicationErrorReport.CrashInfo) objects[1];
            if (crashInfo != null) {
                String className = crashInfo.exceptionClassName;
                String msg = crashInfo.exceptionMessage;
                String stack = crashInfo.stackTrace;
                String packageName = crashInfo.throwClassName;
                String[] infos = {packageName, className, msg, stack};
                StringBuffer sb = CreateSB(infos);
                if (!TextUtils.isEmpty(Cclt.crashinfo)) {
                    String filePath = FileTool.CreateNewFile(Cclt.crashinfo, "Crash详情" + "_" + time);
                    if (!TextUtils.isEmpty(filePath)) {
                        FileTool.Data2File(sb.toString());
                    }
                }
                Log.i(TAG, "CrashInfo:  " + className + " " + msg);
            }
            if (newMethod != null) {
                return newMethod.invoke(iam, objects);
            }
        }
        return method.invoke(iam,objects);
    }


    private StringBuffer CreateSB(String[] info) {
        if(info != null) {
            StringBuffer sb = new StringBuffer();
            sb.append(phoneInfo.getInfo());
            sb.append("\n");
            sb.append("Crash信息输出------> ");
            sb.append("\n");
            sb.append("Crash发生时间: ");
            sb.append(time);
            sb.append("\n");
            sb.append("Crash所在类: ");
            sb.append(info[0]);
            sb.append("\n");
            sb.append("Crash类型: ");
            sb.append(info[1]);
            sb.append("\n");
            sb.append("Crash具体信息: ");
            sb.append(info[2]);
            sb.append("\n");
            sb.append("Crash堆栈: ");
            sb.append(info[3]);
            return sb;
        }
        return null;
    }


    public  static  String getDate(){
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH);
        int day = ca.get(Calendar.DATE);
        int minute = ca.get(Calendar.MINUTE);
        int hour = ca.get(Calendar.HOUR_OF_DAY);
        int second = ca.get(Calendar.SECOND);
        String date =  year+"年" + (month + 1 )+"月"+ day+"日" + hour +"时"+ minute+"分" + second+"秒";
        return date;
    }

}
