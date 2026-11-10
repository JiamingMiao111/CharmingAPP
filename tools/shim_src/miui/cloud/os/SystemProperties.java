package miui.cloud.os;
import java.lang.reflect.Method;
/** miui.cloud.os.SystemProperties 兼容层（反射调用 android.os.SystemProperties） */
public class SystemProperties {
    private static Method mGetS, mGetSI, mGetI, mGetL, mGetB;
    static {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            mGetS = c.getMethod("get", String.class);
            mGetSI = c.getMethod("get", String.class, String.class);
            mGetI = c.getMethod("getInt", String.class, int.class);
            mGetL = c.getMethod("getLong", String.class, long.class);
            mGetB = c.getMethod("getBoolean", String.class, boolean.class);
        } catch (Throwable t) {}
    }
    public static String get(String key) {
        try { if (mGetS != null) return (String) mGetS.invoke(null, key); } catch (Throwable t) {}
        return "";
    }
    public static String get(String key, String def) {
        try { if (mGetSI != null) return (String) mGetSI.invoke(null, key, def); } catch (Throwable t) {}
        return def;
    }
    public static int getInt(String key, int def) {
        try { if (mGetI != null) return (Integer) mGetI.invoke(null, key, def); } catch (Throwable t) {}
        return def;
    }
    public static long getLong(String key, long def) {
        try { if (mGetL != null) return (Long) mGetL.invoke(null, key, def); } catch (Throwable t) {}
        return def;
    }
    public static boolean getBoolean(String key, boolean def) {
        try { if (mGetB != null) return (Boolean) mGetB.invoke(null, key, def); } catch (Throwable t) {}
        return def;
    }
    public static void set(String key, String val) {}
}
