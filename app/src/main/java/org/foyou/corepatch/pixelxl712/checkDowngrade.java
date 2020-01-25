package org.foyou.corepatch.pixelxl712;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static org.foyou.corepatch.pixelxl712.Utils.log;


public class checkDowngrade {
    public static void start(XC_LoadPackage.LoadPackageParam lpparam) {
        final Class<?> cls = XposedHelpers.findClass("com.android.server.pm.PackageManagerService", lpparam.classLoader);
        //降版本安装唯一关键点
        //PackageParser类的内部类Package
        XposedBridge.hookAllMethods(cls, "checkDowngrade", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                super.beforeHookedMethod(methodHookParam);
                Object packageInfoLite = methodHookParam.args[0];
                log("降版本安装检查:");
                log("包名：" + XposedHelpers.getObjectField(packageInfoLite, "packageName"));
                log("当前版本号:" + XposedHelpers.getIntField(packageInfoLite, "mVersionCode"));
                log("预安装版本号:" + XposedHelpers.getIntField(methodHookParam.args[1], "versionCode"));
                XposedHelpers.setObjectField(packageInfoLite, "mVersionCode", -1);
                log("hook成功~");
            }
        });
    }
}
