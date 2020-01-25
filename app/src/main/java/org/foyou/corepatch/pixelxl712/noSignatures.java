package org.foyou.corepatch.pixelxl712;

import android.annotation.SuppressLint;

import java.io.File;
import java.lang.reflect.Method;
import java.security.cert.X509Certificate;
import java.util.jar.JarFile;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static org.foyou.corepatch.pixelxl712.Utils.log;

public class noSignatures {

    public static void start_LoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Exception {
        @SuppressLint("PrivateApi") final Class<?> packageParserClass = lpparam.classLoader.loadClass("android.content.pm.PackageParser");
        @SuppressLint("PrivateApi") final Class<?> packageParser_PackageClass = lpparam.classLoader.loadClass("android.content.pm.PackageParser$Package");
        @SuppressLint("PrivateApi") final Method collectCertificatesMethod = packageParserClass.getDeclaredMethod("collectCertificates", packageParser_PackageClass, File.class, int.class);
        XposedBridge.hookMethod(collectCertificatesMethod, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                try {
                    XposedBridge.invokeOriginalMethod(collectCertificatesMethod, param.thisObject, param.args);
                } catch (Exception e) {
                    //说明没有签名
                    log("有异常说明没有签名");
                }
                return null;
            }
        });
    }

    static X509Certificate[][] x509;

    public static void start_initZygote() {
        //无签名安装
        //2019年12月7日17时19分41秒
        //无签名安装唯一关键hook点
        //有副作用，暂时弃用
        XposedHelpers.findAndHookMethod(XposedHelpers.findClass("android.util.apk.ApkSignatureSchemeV2Verifier", null), "verify", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String arg = (String) param.args[0];
                log(arg);
                if (param.getThrowable() != null) {
                    log("出现异常：无签名");
                    if (arg.startsWith("/system") || arg.startsWith("/vendor")) {
                        log("无签名Hook：跳过系统或厂商驱动应用。");
                        return;
                    }
                    if (arg.startsWith("/data/app/vmd")) {
                        log("无V2签名:" + arg);
                        //判断是否有签名
                        if (!hasSignatures(arg)) {
                            log("无签名：Hook" + arg);
                            param.setResult(x509);
                        }
                    }
                } else {
                    log("未出现异常！");
                    Object result = param.getResult();
                    if (result != null) {
                        x509 = (X509Certificate[][]) result;
                        log("X509Certificate[][] 不为空！");
                    }
                }
            }
        });
    }

    private static boolean hasSignatures(String apk) throws Exception {
        //TODO
        if ((new JarFile(apk).getEntry("META-INF/MANIFEST.MF")) == null) {
            return false;
        } else {
            return true;
        }
    }

}
