package miui.os;
/** MIUI Build 兼容层 - 全字段覆盖 */
public final class Build {
    public static final String DEVICE = android.os.Build.DEVICE;
    public static final String MODEL = android.os.Build.MODEL;
    public static final String BRAND = android.os.Build.BRAND;
    public static final String MANUFACTURER = android.os.Build.MANUFACTURER;
    public static final String PRODUCT = android.os.Build.PRODUCT;
    public static final String HARDWARE = android.os.Build.HARDWARE;
    public static final String ID = android.os.Build.ID;
    public static final String TYPE = "user";
    public static final String TAGS = android.os.Build.TAGS;
    public static final String VERSION_INCREMENTAL = "V15.0.0.0";
    public static final String VERSION_CODE = "15";
    public static final boolean IS_ALPHA_BUILD = false;
    public static final boolean IS_DEVELOPMENT_VERSION = false;
    public static final boolean IS_INTERNATIONAL_BUILD = false;
    public static final boolean IS_GLOBAL_BUILD = false;
    public static final boolean IS_PRIVATE_BUILD = false;
    public static final boolean IS_STABLE_VERSION = true;
    public static final boolean IS_TABLET = false;
    public static final boolean IS_MIPAD = false;
    public static final boolean IS_FOLDABLE = false;
    public static final boolean HAS_NOTCH = false;
    public static final int DEVICE_LEVEL = 1;

    public static final boolean IS_DEBUGGABLE = false;
    public static final boolean IS_CTS_BUILD = false;
    public static final boolean IS_ENG_BUILD = false;
    public static final boolean IS_USER_BUILD = true;
    public static final boolean IS_USERDEBUG_BUILD = false;
    public static final boolean IS_OFFICIAL_BUILD = true;
    public static final boolean IS_MTK_BUILD = true;
    public static final boolean IS_QCOM_BUILD = false;
    public static final boolean IS_MIUI_BUILD = true;
    public static final boolean IS_AOSP_BUILD = false;
    public static final String VERSION = "15";
    public static final String INCREMENTAL = "V15.0.0.0";
    public static final String REGION = "CN";
    public static final String MARKET_NAME = "CN";
    public static String getRegion() { return "CN"; }
    public static String checkRegion(String r) { return r; }
    public static int getUserMode() { return 0; }
    public static boolean isBetaVersion() { return false; }
    public static boolean isDevelopmentVersion() { return false; }
}
