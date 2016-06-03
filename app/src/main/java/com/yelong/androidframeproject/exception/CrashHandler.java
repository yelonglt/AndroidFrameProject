package com.yelong.androidframeproject.exception;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import com.yelong.androidframeproject.BuildConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * App的异常处理
 * Created by eyetech on 16/5/30.
 * mail:354734713@qq.com
 */
public class CrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";

    private static final String PATH = Environment
            .getExternalStorageDirectory() + "/Crash/log/";
    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".trace";
    private static final String ABSOLUTE_PATH = PATH + FILE_NAME + FILE_NAME_SUFFIX;

    private static CrashHandler sInstance;
    private UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if (sInstance == null) {
            synchronized (CrashHandler.class) {
                sInstance = new CrashHandler();
            }
        }
        return sInstance;
    }

    public void init(Context context) {
        // 获取系统默认的UncaughtException处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }

    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用#uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息。
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            // 导出异常信息到SD卡中
            dumpExceptionToSDCard(ex);

        } catch (IOException e) {
            e.printStackTrace();
        }

        ex.printStackTrace();

        // 如果系统提供了默认的异常处理器，则交给系统去结束我们的程序，否则就由我们自己结束自己
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            Process.killProcess(Process.myPid());
        }

    }

    /**
     * 把异常信息写入SD卡
     *
     * @param ex
     * @return
     * @throws IOException
     */
    private File dumpExceptionToSDCard(Throwable ex) throws IOException {
        // 如果SD卡不存在或无法使用，则无法把异常信息写入SD卡
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "sdcard unmounted,skip dump exception");
                return null;
            }
        }

        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date(current));

        File file = new File(ABSOLUTE_PATH);

        if (!file.exists()) {
            file.createNewFile();
        } else {
            try {
                // 追加内容
                PrintWriter pw = new PrintWriter(new BufferedWriter(
                        new FileWriter(file, true)));
                pw.println(time);
                dumpPhoneInfo(pw);
                pw.println();
                ex.printStackTrace(pw);
                pw.println("--------------------------分割线---------------------------");
                pw.println();
                pw.close();
            } catch (Exception e) {
                Log.e(TAG, "dump crash info failed");
            }

        }

        return file;
    }

    /**
     * 收集收集信息
     *
     * @param pw
     * @throws PackageManager.NameNotFoundException
     */
    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),
                PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);

        // android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);

        // 手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);

        // 手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);

        // cpu架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);

    }

    /**
     * 提供方法上传异常信息到服务器
     *
     * @param log
     */
    private void uploadExceptionToServer(File log) {

    }


}
