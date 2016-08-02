package com.looper.catchcrashlogtool;

import android.text.TextUtils;
import android.util.Log;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;

import java.io.File;

/**
 * Created by looper on 2016/7/28.
 */
public class SendEmail {

    private static SendEmail INSTANCE;
    private String sendto = "517709409@qq.com";
    private UserAuthenticat userAuthenticat;
    private SendEmail() {}

    public static SendEmail getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SendEmail();
        }
        return INSTANCE;
    }

    public SendEmail setSendto(String addr) {
        if (!TextUtils.isEmpty(addr)) {
            this.sendto = addr;
        }
        return this;
    }

    public SendEmail setAuthenticat(UserAuthenticat userAuthenticat) {
        this.userAuthenticat = userAuthenticat;
        return  this;
    }

    public  void send() {
        try {
            EmailAttachment attachment = new EmailAttachment();
            attachment.setPath(Cclt.Path);
            attachment.setDisposition(EmailAttachment.ATTACHMENT);
            attachment.setDescription("CrashInfo");
            attachment.setName(Cclt.fileName);

            MultiPartEmail email = new MultiPartEmail();
            email.setHostName(userAuthenticat.getHost());
            email.setSmtpPort(userAuthenticat.getPort());
            email.setAuthenticator(new DefaultAuthenticator(userAuthenticat.getUseraddr(), userAuthenticat.getKey()));
            email.setSSLOnConnect(userAuthenticat.getIsSSH());
            email.setFrom(userAuthenticat.getUseraddr());
            email.setSubject("Crash警报");
            email.setMsg("App 刚刚发生了一次Crash，可以下载附件查看崩溃详情");
            email.addTo(sendto);
            email.attach(attachment);
            String id = email.send();
            if (!TextUtils.isEmpty(id)) {
               Log.i("SendEmail",id);
                File f = new File(Cclt.Path);
                if (f.exists()) {
                    f.delete();
                }
            }
        }catch (Exception e) {
            Log.i("sendEmail","sendEmail error !!!");
        }

    }

}
