package miui.cloud;
import android.content.Context;
/** MiCloud 兼容层 - 全部返回"未启用" */
public class MiCloudCompat {
    public static boolean isMiCloudEnabled(Context ctx) { return false; }
    public static boolean isCloudAvailable(Context ctx) { return false; }
    public static void setCloudEnabled(Context ctx, boolean on) {}
    public static String getMiCloudVersion(Context ctx) { return ""; }
}
