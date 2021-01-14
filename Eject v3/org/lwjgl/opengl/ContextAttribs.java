package org.lwjgl.opengl;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLUtil;

import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public final class ContextAttribs {
    public static final int CONTEXT_MAJOR_VERSION_ARB = 8337;
    public static final int CONTEXT_MINOR_VERSION_ARB = 8338;
    public static final int CONTEXT_PROFILE_MASK_ARB = 37158;
    public static final int CONTEXT_CORE_PROFILE_BIT_ARB = 1;
    public static final int CONTEXT_COMPATIBILITY_PROFILE_BIT_ARB = 2;
    public static final int CONTEXT_ES2_PROFILE_BIT_EXT = 4;
    public static final int CONTEXT_FLAGS_ARB = 8340;
    public static final int CONTEXT_DEBUG_BIT_ARB = 1;
    public static final int CONTEXT_FORWARD_COMPATIBLE_BIT_ARB = 2;
    public static final int CONTEXT_ROBUST_ACCESS_BIT_ARB = 4;
    public static final int CONTEXT_RESET_ISOLATION_BIT_ARB = 8;
    public static final int CONTEXT_RESET_NOTIFICATION_STRATEGY_ARB = 33366;
    public static final int NO_RESET_NOTIFICATION_ARB = 33377;
    public static final int LOSE_CONTEXT_ON_RESET_ARB = 33362;
    public static final int CONTEXT_RELEASE_BEHABIOR_ARB = 8343;
    public static final int CONTEXT_RELEASE_BEHAVIOR_NONE_ARB = 0;
    public static final int CONTEXT_RELEASE_BEHAVIOR_FLUSH_ARB = 8344;
    public static final int CONTEXT_LAYER_PLANE_ARB = 8339;
    private int majorVersion;
    private int minorVersion;
    private int profileMask;
    private int contextFlags;
    private int contextResetNotificationStrategy = 33377;
    private int contextReleaseBehavior = 8344;
    private int layerPlane;

    public ContextAttribs() {
        this(1, 0);
    }

    public ContextAttribs(int paramInt1, int paramInt2) {
        this(paramInt1, paramInt2, 0, 0);
    }

    public ContextAttribs(int paramInt1, int paramInt2, int paramInt3) {
        this(paramInt1, paramInt2, 0, paramInt3);
    }

    public ContextAttribs(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        if ((paramInt1 < 0) || (4 < paramInt1) || (paramInt2 < 0) || ((paramInt1 == 4) && (5 < paramInt2)) || ((paramInt1 == 3) && (3 < paramInt2)) || ((paramInt1 == 2) && (1 < paramInt2)) || ((paramInt1 == 1) && (5 < paramInt2))) {
            throw new IllegalArgumentException("Invalid OpenGL version specified: " + paramInt1 + '.' + paramInt2);
        }
        if (LWJGLUtil.CHECKS) {
            if ((1 < Integer.bitCount(paramInt3)) || (4 < paramInt3)) {
                throw new IllegalArgumentException("Invalid profile mask specified: " + Integer.toBinaryString(paramInt3));
            }
            if (15 < paramInt4) {
                throw new IllegalArgumentException("Invalid context flags specified: " + Integer.toBinaryString(paramInt3));
            }
        }
        this.majorVersion = paramInt1;
        this.minorVersion = paramInt2;
        this.profileMask = paramInt3;
        this.contextFlags = paramInt4;
    }

    private ContextAttribs(ContextAttribs paramContextAttribs) {
        this.majorVersion = paramContextAttribs.majorVersion;
        this.minorVersion = paramContextAttribs.minorVersion;
        this.profileMask = paramContextAttribs.profileMask;
        this.contextFlags = paramContextAttribs.contextFlags;
        this.contextResetNotificationStrategy = paramContextAttribs.contextResetNotificationStrategy;
        this.contextReleaseBehavior = paramContextAttribs.contextReleaseBehavior;
        this.layerPlane = paramContextAttribs.layerPlane;
    }

    public int getMajorVersion() {
        return this.majorVersion;
    }

    public int getMinorVersion() {
        return this.minorVersion;
    }

    public int getProfileMask() {
        return this.profileMask;
    }

    private boolean hasMask(int paramInt) {
        return this.profileMask == paramInt;
    }

    public boolean isProfileCore() {
        return hasMask(1);
    }

    public boolean isProfileCompatibility() {
        return hasMask(2);
    }

    public boolean isProfileES() {
        return hasMask(4);
    }

    public int getContextFlags() {
        return this.contextFlags;
    }

    private boolean hasFlag(int paramInt) {
        return this.contextFlags >> paramInt != 0;
    }

    public boolean isDebug() {
        return hasFlag(1);
    }

    public boolean isForwardCompatible() {
        return hasFlag(2);
    }

    public boolean isRobustAccess() {
        return hasFlag(4);
    }

    public boolean isContextResetIsolation() {
        return hasFlag(8);
    }

    public int getContextResetNotificationStrategy() {
        return this.contextResetNotificationStrategy;
    }
  
  
   *

    @deprecated
    public boolean isLoseContextOnReset() {
        return this.contextResetNotificationStrategy == 33362;
    }

    public int getContextReleaseBehavior() {
        return this.contextReleaseBehavior;
    }

    public int getLayerPlane() {
        return this.layerPlane;
    }

    private ContextAttribs toggleMask(int paramInt, boolean paramBoolean) {
        if (paramBoolean == hasMask(paramInt)) {
            return this;
        }
        ContextAttribs localContextAttribs = new ContextAttribs(this);
        localContextAttribs.profileMask = (paramBoolean ? paramInt : 0);
        return localContextAttribs;
    }

    public ContextAttribs withProfileCore(boolean paramBoolean) {
        if ((this.majorVersion < 3) || ((this.majorVersion == 3) && (this.minorVersion < 2))) {
            throw new IllegalArgumentException("Profiles are only supported on OpenGL version 3.2 or higher.");
        }
        return toggleMask(1, paramBoolean);
    }

    public ContextAttribs withProfileCompatibility(boolean paramBoolean) {
        if ((this.majorVersion < 3) || ((this.majorVersion == 3) && (this.minorVersion < 2))) {
            throw new IllegalArgumentException("Profiles are only supported on OpenGL version 3.2 or higher.");
        }
        return toggleMask(2, paramBoolean);
    }

    public ContextAttribs withProfileES(boolean paramBoolean) {
        if ((this.majorVersion != 2) || (this.minorVersion != 0)) {
            throw new IllegalArgumentException("The OpenGL ES profile is only supported on OpenGL version 2.0.");
        }
        return toggleMask(4, paramBoolean);
    }

    private ContextAttribs toggleFlag(int paramInt, boolean paramBoolean) {
        if (paramBoolean == hasFlag(paramInt)) {
            return this;
        }
        ContextAttribs localContextAttribs = new ContextAttribs(this);
        localContextAttribs.contextFlags += paramInt;
        return localContextAttribs;
    }

    public ContextAttribs withDebug(boolean paramBoolean) {
        return toggleFlag(1, paramBoolean);
    }

    public ContextAttribs withForwardCompatible(boolean paramBoolean) {
        return toggleFlag(2, paramBoolean);
    }

    public ContextAttribs withRobustAccess(boolean paramBoolean) {
        return toggleFlag(4, paramBoolean);
    }

    public ContextAttribs withContextResetIsolation(boolean paramBoolean) {
        return toggleFlag(8, paramBoolean);
    }

    public ContextAttribs withResetNotificationStrategy(int paramInt) {
        if (paramInt == this.contextResetNotificationStrategy) {
            return this;
        }
        if ((LWJGLUtil.CHECKS) && (paramInt != 33377) && (paramInt != 33362)) {
            throw new IllegalArgumentException("Invalid context reset notification strategy specified: 0x" + LWJGLUtil.toHexString(paramInt));
        }
        ContextAttribs localContextAttribs = new ContextAttribs(this);
        localContextAttribs.contextResetNotificationStrategy = paramInt;
        return localContextAttribs;
    }
  
  
   *

    @deprecated
    public ContextAttribs withLoseContextOnReset(boolean paramBoolean) {
        return withResetNotificationStrategy(paramBoolean ? 33362 : 33377);
    }

    public ContextAttribs withContextReleaseBehavior(int paramInt) {
        if (paramInt == this.contextReleaseBehavior) {
            return this;
        }
        if ((LWJGLUtil.CHECKS) && (paramInt != 8344) && (paramInt != 0)) {
            throw new IllegalArgumentException("Invalid context release behavior specified: 0x" + LWJGLUtil.toHexString(paramInt));
        }
        ContextAttribs localContextAttribs = new ContextAttribs(this);
        localContextAttribs.contextReleaseBehavior = paramInt;
        return localContextAttribs;
    }

    public ContextAttribs withLayer(int paramInt) {
        if (LWJGLUtil.getPlatform() != 3) {
            throw new IllegalArgumentException("The CONTEXT_LAYER_PLANE_ARB attribute is supported only on the Windows platform.");
        }
        if (paramInt == this.layerPlane) {
            return this;
        }
        if (paramInt < 0) {
            throw new IllegalArgumentException("Invalid layer plane specified: " + paramInt);
        }
        ContextAttribs localContextAttribs = new ContextAttribs(this);
        localContextAttribs.layerPlane = paramInt;
        return localContextAttribs;
    }

    IntBuffer getAttribList() {
        if (LWJGLUtil.getPlatform() == 2) {
            return null;
        }
        LinkedHashMap localLinkedHashMap = new LinkedHashMap(8);
        if ((this.majorVersion != 1) || (this.minorVersion != 0)) {
            localLinkedHashMap.put(Integer.valueOf(8337), Integer.valueOf(this.majorVersion));
            localLinkedHashMap.put(Integer.valueOf(8338), Integer.valueOf(this.minorVersion));
        }
        if (this.contextFlags != 0) {
            localLinkedHashMap.put(Integer.valueOf(8340), Integer.valueOf(this.contextFlags));
        }
        if (this.profileMask != 0) {
            localLinkedHashMap.put(Integer.valueOf(37158), Integer.valueOf(this.profileMask));
        }
        if (this.contextResetNotificationStrategy != 33377) {
            localLinkedHashMap.put(Integer.valueOf(33366), Integer.valueOf(this.contextResetNotificationStrategy));
        }
        if (this.contextReleaseBehavior != 8344) {
            localLinkedHashMap.put(Integer.valueOf(8343), Integer.valueOf(this.contextReleaseBehavior));
        }
        if (this.layerPlane != 0) {
            localLinkedHashMap.put(Integer.valueOf(8339), Integer.valueOf(this.layerPlane));
        }
        if (localLinkedHashMap.isEmpty()) {
            return null;
        }
        IntBuffer localIntBuffer = BufferUtils.createIntBuffer(localLinkedHashMap.size() * 2 | 0x1);
        Iterator localIterator = localLinkedHashMap.entrySet().iterator();
        while (localIterator.hasNext()) {
            Map.Entry localEntry = (Map.Entry) localIterator.next();
            localIntBuffer.put(((Integer) localEntry.getKey()).intValue()).put(((Integer) localEntry.getValue()).intValue());
        }
        localIntBuffer.put(0);
        localIntBuffer.rewind();
        return localIntBuffer;
    }

    public String toString() {
        StringBuilder localStringBuilder = new StringBuilder(32);
        localStringBuilder.append("ContextAttribs:");
        localStringBuilder.append(" Version=").append(this.majorVersion).append('.').append(this.minorVersion);
        if (this.profileMask != 0) {
            localStringBuilder.append(", Profile=");
            if (hasMask(1)) {
                localStringBuilder.append("CORE");
            } else if (hasMask(2)) {
                localStringBuilder.append("COMPATIBLITY");
            } else if (hasMask(4)) {
                localStringBuilder.append("ES2");
            } else {
                localStringBuilder.append("*unknown*");
            }
        }
        if (this.contextFlags != 0) {
            if (hasFlag(1)) {
                localStringBuilder.append(", DEBUG");
            }
            if (hasFlag(2)) {
                localStringBuilder.append(", FORWARD_COMPATIBLE");
            }
            if (hasFlag(4)) {
                localStringBuilder.append(", ROBUST_ACCESS");
            }
            if (hasFlag(8)) {
                localStringBuilder.append(", RESET_ISOLATION");
            }
        }
        if (this.contextResetNotificationStrategy != 33377) {
            localStringBuilder.append(", LOSE_CONTEXT_ON_RESET");
        }
        if (this.contextReleaseBehavior != 8344) {
            localStringBuilder.append(", RELEASE_BEHAVIOR_NONE");
        }
        if (this.layerPlane != 0) {
            localStringBuilder.append(", Layer=").append(this.layerPlane);
        }
        return localStringBuilder.toString();
    }
}




