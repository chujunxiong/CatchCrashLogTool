# CatchCrashLogTool
CatchCrashLogTool(CCTL) 是一个Android平台的Crash捕获和Log日志记录工具，帮你更好发现并解决APP中的问题

## 特点
| 功能          | 说明          | 
| ------------- |:-------------:|
| Crash捕获并存储|通过Hook 系统核心服务(AMS)中的关键方法，在Crash发生第一时间捕获并存储在本地|
| 附带常用信息|在存储Crash信息时附带手机和系统的常用信息，还原用户硬件环境，辅助你对Crash问题的定位|
| 自动邮件通知|Crash发生后，根据设置自动发送邮件通知你相关的Crash详情| 
| 自定义上传方式|邮件上传和HTTP上传任你选，如有需要还可以随时替换默认的HTTP实现(暂时支持邮件上传，完整功能敬请期待)|
| 自定义上传时机|可以选择是Wifi上传，还是移动网络上传(暂时支持有网络上传，无网络不上传，完整功能敬请期待)|
| Log日志记录|全程记录Log日志，帮你再现用户Crash操作前的路径(敬请期待)|
| 自定义路径|根据需要自定义存储的路径|
| 自定义缓存大小|默认大小10MB，超出后自动删除，节省空间|


## 如何使用
**可以先下载代码，在Android studio中以Android library的形式添加到项目中，修改相关的gradle文件然后编译即可**

**接下来就很简单了，在你项目中的LAUNCHER Activity中的attachBaseContext方法中添加一个链式调用即可**
**就像下面这样:**
```java
 protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        Cclt.init(getApplicationContext())
                .setUserAuthentication(UserAuthenticat.init("你的要发送邮件的账户地址","账户密码","发送邮件的服务器主机名","端口(int)","是否使用SSL(boolean)"))
                .setEmailAddr("接收邮件的Email地址").Ok();
    }
```
**当然，为了你的账户安全，你可以把账户名和密码先加密存储在本地，然后再需要时读取解密**

**最后，在你的项目中千万别忘了像下面这样申请权限:**
```java
 <uses-permission android:name="android.permission.INTERNET" />
 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
 <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
 <uses-permission android:name="android.permission.READ_PHONE_STATE" />
```
