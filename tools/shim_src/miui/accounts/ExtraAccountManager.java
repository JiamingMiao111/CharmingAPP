package miui.accounts;
import android.accounts.Account;
import android.content.Context;
/** MIUI ExtraAccountManager 兼容层 - 返回 null 表示未登录 */
public class ExtraAccountManager {
    public static final String XIAOMI_ACCOUNT_TYPE = "com.xiaomi";
    public static final String ACCOUNT_TYPE_XIAOMI = "com.xiaomi";
    public static Account getXiaomiAccount(Context ctx) { return null; }
    public static String getXiaomiAccountName(Context ctx) { return null; }
    public static boolean isXiaomiAccount(Context ctx) { return false; }
    public static Account[] getAccounts(Context ctx) { return new Account[0]; }
}
