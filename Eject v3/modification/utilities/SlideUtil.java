package modification.utilities;

import net.minecraft.util.MathHelper;

public final class SlideUtil {
    public final float slide(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, boolean paramBoolean) {
        return MathHelper.clamp_float(paramFloat1 > paramFloat2 ? paramFloat1 - (paramFloat1 - paramFloat2) * paramFloat4 : paramBoolean ? paramFloat1 : paramFloat1 < paramFloat3 ? paramFloat1 + (paramFloat3 - paramFloat1) * paramFloat4 : paramFloat1, paramFloat2, paramFloat3);
    }

    public final float slideOther(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {
        return MathHelper.clamp_float(paramFloat1 > paramFloat2 ? paramFloat1 - (paramFloat1 - paramFloat2) * paramFloat5 : paramFloat1 < paramFloat2 ? paramFloat1 + (paramFloat2 - paramFloat1) * paramFloat5 : paramFloat1, paramFloat3, paramFloat4);
    }

    public final float slideNormal(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, boolean paramBoolean) {
        return MathHelper.clamp_float(paramFloat1 > paramFloat2 ? paramFloat1 - paramFloat4 : paramBoolean ? paramFloat1 : paramFloat1 < paramFloat3 ? paramFloat1 + paramFloat4 : paramFloat1, paramFloat2, paramFloat3);
    }
}




