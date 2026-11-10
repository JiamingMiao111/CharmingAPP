package miui.cloud.common;
/** XLogger 兼容层 */
public class XLogger {
    public static final int VERBOSE = 2, DEBUG = 3, INFO = 4, WARN = 5, ERROR = 6;
    private static int level = 2;
    public static void setLogLevel(int l) { level = l; }
    public static int getLogLevel() { return level; }
    public static void v(String tag, String msg) {}
    public static void d(String tag, String msg) {}
    public static void i(String tag, String msg) {}
    public static void w(String tag, String msg) {}
    public static void e(String tag, String msg) {}
    public static void v(String tag, String msg, Throwable t) {}
    public static void d(String tag, String msg, Throwable t) {}
    public static void i(String tag, String msg, Throwable t) {}
    public static void w(String tag, String msg, Throwable t) {}
    public static void e(String tag, String msg, Throwable t) {}
}
