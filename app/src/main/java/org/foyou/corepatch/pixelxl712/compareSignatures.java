package org.foyou.corepatch.pixelxl712;

import android.content.pm.PackageManager;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class compareSignatures {
    public static void start(XC_LoadPackage.LoadPackageParam lpparam) {
        //Hook签名验证的唯一关键点
        XposedBridge.hookAllMethods(XposedHelpers.findClass("com.android.server.pm.PackageManagerService", lpparam.classLoader), "compareSignatures", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult(PackageManager.SIGNATURE_MATCH);
            }
        });
    }
}
