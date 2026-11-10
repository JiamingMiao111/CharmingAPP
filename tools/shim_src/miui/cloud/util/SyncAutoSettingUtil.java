package miui.cloud.util;
import android.content.Context;
/** 同步自动设置 兼容层 - 全部关闭 */
public class SyncAutoSettingUtil {
    public static boolean getAutoSync(Context ctx) { return false; }
    public static boolean getAutoSync(String key, Context ctx) { return false; }
    public static void setAutoSync(String key, Context ctx, boolean on) {}
    public static boolean isWifiOnly(Context ctx) { return true; }
    public static void setWifiOnly(Context ctx, boolean on) {}
}
