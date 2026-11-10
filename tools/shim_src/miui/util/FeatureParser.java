package miui.util;
/** MIUI FeatureParser 兼容层 - 完整版 */
public final class FeatureParser {
    public static boolean getBoolean(String key, boolean def) { return def; }
    public static boolean getBoolean(String key) { return false; }
    public static String getString(String key, String def) { return def; }
    public static String getString(String key) { return ""; }
    public static int getInteger(String key, int def) { return def; }
    public static int getInteger(String key) { return 0; }
    public static long getLong(String key, long def) { return def; }
    public static long getLong(String key) { return 0L; }
    public static float getFloat(String key, float def) { return def; }
    public static float getFloat(String key) { return 0f; }
    public static int[] getIntArray(String key) { return null; }
    public static int[] getIntArray(String key, int[] def) { return def; }
    public static String[] getStringArray(String key) { return null; }
    public static boolean hasFeature(String feature) { return false; }
    public static boolean hasFeature(String feature, int version) { return false; }
}
