package com.looper.catchcrashlogtool;


import android.content.Context;
import android.os.Build;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by looper on 2016/7/29.
 */
public class PhoneInfo {

    private static PhoneInfo INSTANCE;
    private static Context context;
    private WindowManager wm;

    private PhoneInfo(Context context) {
        this.context = context;
        wm = (WindowManager)this.context.getSystemService(Context.WINDOW_SERVICE);
    }

    public static PhoneInfo getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new PhoneInfo(context);
        }
        return INSTANCE;
    }


    private int[] DisPlayInfo() {
        android.view.Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        int height = dm.heightPixels;
        int width  = dm.widthPixels;
        int[] pixels = {height,width};
        return pixels;
    }


    private String[] CpuInfo() {
            String str1 = "/proc/cpuinfo";
            String str2 = "";
            String[] cpuInfo = {"", ""};  //1cpu型号  2cpu频率
            String[] arrayOfString;
            try {
                FileReader fr = new FileReader(str1);
                BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
                str2 = localBufferedReader.readLine();
                arrayOfString = str2.split("\\s+");
                for (int i = 2; i < arrayOfString.length; i++) {
                    cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
                }
                str2 = localBufferedReader.readLine();
                arrayOfString = str2.split("\\s+");
                cpuInfo[1] += arrayOfString[2];
                localBufferedReader.close();
            } catch (IOException e) {
            }

            return cpuInfo;
    }


    private String getTotalMemory() {
        String str1 = "/proc/meminfo";  // 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();  // 读取系统总内存大小

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024; // 获得系统总内存
            localBufferedReader.close();

        } catch (IOException e) {
        }
        return Formatter.formatFileSize(context, initial_memory);  //转换
    }

    public  String getInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("手机品牌: ");
        sb.append(Build.BRAND);
        sb.append("\n");
        sb.append("手机型号: ");
        sb.append(Build.MODEL);
        sb.append("\n");
        sb.append("OS 版本: ");
        sb.append(Build.VERSION.RELEASE);
        sb.append("\n");
        sb.append("ROM版本号: ");
        sb.append(Build.DISPLAY);
        sb.append("\n");
        sb.append("屏幕分辨率: ");
        sb.append(String.valueOf(DisPlayInfo()[0]+"*"+DisPlayInfo()[1]));
        sb.append("\n");
        sb.append("CPU 型号: ");
        sb.append(CpuInfo()[0]);
        sb.append("\n");
//        sb.append("内存大小: ");
//        sb.append(getTotalMemory());
//        sb.append("\n");
        sb.append("---------------------------------------------------------------------");
        sb.append("\n");
        return sb.toString();
    }

}
