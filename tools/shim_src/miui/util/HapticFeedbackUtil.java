package miui.util;
import android.view.View;
/** MIUI HapticFeedbackUtil 兼容层（用标准 View 震动替代） */
public class HapticFeedbackUtil {
    public static final int EFFECT_CLICK = 0;
    public static final int EFFECT_STRENGTH_LIGHT = 1;
    public static final int EFFECT_STRENGTH_MEDIUM = 2;
    public static final int EFFECT_STRENGTH_STRONG = 3;
    public HapticFeedbackUtil(android.content.Context ctx, int type) {}
    public boolean isSupportLinearMotorVibrate(int effectId) { return false; }
    public boolean performExtHapticFeedback(int effectId) { return false; }
    public void release() {}
    public void stop() {}
}
