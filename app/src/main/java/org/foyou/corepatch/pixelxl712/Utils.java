package org.foyou.corepatch.pixelxl712;

import de.robv.android.xposed.XposedBridge;

public class Utils {
    private static final String TAG = "HookEntry:";

    public static void log(Object msg) {
        XposedBridge.log(TAG + msg);
    }
}
