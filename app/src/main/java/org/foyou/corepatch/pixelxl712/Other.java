package org.foyou.corepatch.pixelxl712;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Other {
    public static void start(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException {
        //11111
        //安装失败 INSTALL_FAILED_TEST_ONLY
        //Failure [INSTALL_FAILED_TEST_ONLY: installPackageLI]
        @SuppressLint("PrivateApi") Class<?> packageParserClass = lpparam.classLoader.loadClass("android.content.pm.PackageParser");
        XposedBridge.hookAllMethods(packageParserClass, "parseBaseApkCommon", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                ApplicationInfo applicationInfo = (ApplicationInfo) XposedHelpers.getObjectField(param.getResult(), "applicationInfo");
                if ((applicationInfo.flags & ApplicationInfo.FLAG_TEST_ONLY) != 0) {
                    applicationInfo.flags = applicationInfo.flags & 0xFFFFFEFF;
                }
                //FLAG_DEBUGGABLE 设置debuggable属性为true
                if ((applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) == 0) {
                    applicationInfo.flags += 2;
                }
            }
        });
    }
}
