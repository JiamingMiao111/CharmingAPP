package miui.cloud.sync;
import android.content.Context;
import java.util.Map;
/** MiCloudStatusInfo 兼容层 - 全部返回"未授权/零配额" */
public class MiCloudStatusInfo {
    private static final MiCloudStatusInfo EMPTY = new MiCloudStatusInfo();

    public static MiCloudStatusInfo fromUserData(Context ctx) { return EMPTY; }
    public static MiCloudStatusInfo parseMap(Map<?, ?> map) { return EMPTY; }
    public static MiCloudStatusInfo parseQuotaString(String s) { return EMPTY; }

    public QuotaInfo getQuotaInfo() { return new QuotaInfo(); }
    public ItemInfo getItemInfo(String key) { return new ItemInfo(); }
    public Map<String, ItemInfo> getItemInfoMap() { return new java.util.HashMap<String, ItemInfo>(); }
    public boolean isUnauthorized() { return true; }
    public String getUpdateTime() { return ""; }
    public String getStatus() { return "off"; }

    public static class QuotaInfo {
        public long getTotalSize() { return 0L; }
        public long getUsedSize() { return 0L; }
        public long getFreeSize() { return 0L; }
        public String getTotalSizeStr() { return "0"; }
        public String getUsedSizeStr() { return "0"; }
    }

    public static class ItemInfo {
        public String getOperateSizeStr() { return ""; }
        public String getStatus() { return "off"; }
        public String getPerm() { return ""; }
    }

    public static class ItemInfos { class EnumOfKey {} }
}
