package org.foyou.corepatch.pixelxl712;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static org.foyou.corepatch.pixelxl712.Utils.log;
import static org.foyou.corepatch.pixelxl712.noSignatures.start_initZygote;

public class HookEntry implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if ("android".equals(lpparam.packageName) && lpparam.processName.equals("android")) {

            try {
                log("Android包加载。。。");
                HookSignatures.start(lpparam);
                compareSignatures.start(lpparam);
                checkDowngrade.start(lpparam);
                Other.start(lpparam);
            } catch (Exception e) {
                log("核心破解失败:" + e);
            }

        }
    }


    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        try {
            start_initZygote();
        } catch (Exception e) {
            log("核心破解失败:" + e);
        }
    }


}

