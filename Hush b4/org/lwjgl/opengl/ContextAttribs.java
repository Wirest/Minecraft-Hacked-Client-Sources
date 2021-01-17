// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.util.Iterator;
import java.util.Map;
import org.lwjgl.BufferUtils;
import java.util.LinkedHashMap;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLUtil;

public final class ContextAttribs
{
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
    private int contextResetNotificationStrategy;
    private int contextReleaseBehavior;
    private int layerPlane;
    
    public ContextAttribs() {
        this(1, 0);
    }
    
    public ContextAttribs(final int majorVersion, final int minorVersion) {
        this(majorVersion, minorVersion, 0, 0);
    }
    
    public ContextAttribs(final int majorVersion, final int minorVersion, final int profileMask) {
        this(majorVersion, minorVersion, 0, profileMask);
    }
    
    public ContextAttribs(final int majorVersion, final int minorVersion, final int profileMask, final int contextFlags) {
        this.contextResetNotificationStrategy = 33377;
        this.contextReleaseBehavior = 8344;
        if (majorVersion < 0 || 4 < majorVersion || minorVersion < 0 || (majorVersion == 4 && 5 < minorVersion) || (majorVersion == 3 && 3 < minorVersion) || (majorVersion == 2 && 1 < minorVersion) || (majorVersion == 1 && 5 < minorVersion)) {
            throw new IllegalArgumentException("Invalid OpenGL version specified: " + majorVersion + '.' + minorVersion);
        }
        if (LWJGLUtil.CHECKS) {
            if (1 < Integer.bitCount(profileMask) || 4 < profileMask) {
                throw new IllegalArgumentException("Invalid profile mask specified: " + Integer.toBinaryString(profileMask));
            }
            if (15 < contextFlags) {
                throw new IllegalArgumentException("Invalid context flags specified: " + Integer.toBinaryString(profileMask));
            }
        }
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.profileMask = profileMask;
        this.contextFlags = contextFlags;
    }
    
    private ContextAttribs(final ContextAttribs other) {
        this.contextResetNotificationStrategy = 33377;
        this.contextReleaseBehavior = 8344;
        this.majorVersion = other.majorVersion;
        this.minorVersion = other.minorVersion;
        this.profileMask = other.profileMask;
        this.contextFlags = other.contextFlags;
        this.contextResetNotificationStrategy = other.contextResetNotificationStrategy;
        this.contextReleaseBehavior = other.contextReleaseBehavior;
        this.layerPlane = other.layerPlane;
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
    
    private boolean hasMask(final int mask) {
        return this.profileMask == mask;
    }
    
    public boolean isProfileCore() {
        return this.hasMask(1);
    }
    
    public boolean isProfileCompatibility() {
        return this.hasMask(2);
    }
    
    public boolean isProfileES() {
        return this.hasMask(4);
    }
    
    public int getContextFlags() {
        return this.contextFlags;
    }
    
    private boolean hasFlag(final int flag) {
        return (this.contextFlags & flag) != 0x0;
    }
    
    public boolean isDebug() {
        return this.hasFlag(1);
    }
    
    public boolean isForwardCompatible() {
        return this.hasFlag(2);
    }
    
    public boolean isRobustAccess() {
        return this.hasFlag(4);
    }
    
    public boolean isContextResetIsolation() {
        return this.hasFlag(8);
    }
    
    public int getContextResetNotificationStrategy() {
        return this.contextResetNotificationStrategy;
    }
    
    @Deprecated
    public boolean isLoseContextOnReset() {
        return this.contextResetNotificationStrategy == 33362;
    }
    
    public int getContextReleaseBehavior() {
        return this.contextReleaseBehavior;
    }
    
    public int getLayerPlane() {
        return this.layerPlane;
    }
    
    private ContextAttribs toggleMask(final int mask, final boolean value) {
        if (value == this.hasMask(mask)) {
            return this;
        }
        final ContextAttribs attribs = new ContextAttribs(this);
        attribs.profileMask = (value ? mask : 0);
        return attribs;
    }
    
    public ContextAttribs withProfileCore(final boolean profileCore) {
        if (this.majorVersion < 3 || (this.majorVersion == 3 && this.minorVersion < 2)) {
            throw new IllegalArgumentException("Profiles are only supported on OpenGL version 3.2 or higher.");
        }
        return this.toggleMask(1, profileCore);
    }
    
    public ContextAttribs withProfileCompatibility(final boolean profileCompatibility) {
        if (this.majorVersion < 3 || (this.majorVersion == 3 && this.minorVersion < 2)) {
            throw new IllegalArgumentException("Profiles are only supported on OpenGL version 3.2 or higher.");
        }
        return this.toggleMask(2, profileCompatibility);
    }
    
    public ContextAttribs withProfileES(final boolean profileES) {
        if (this.majorVersion != 2 || this.minorVersion != 0) {
            throw new IllegalArgumentException("The OpenGL ES profile is only supported on OpenGL version 2.0.");
        }
        return this.toggleMask(4, profileES);
    }
    
    private ContextAttribs toggleFlag(final int flag, final boolean value) {
        if (value == this.hasFlag(flag)) {
            return this;
        }
        final ContextAttribs contextAttribs;
        final ContextAttribs attribs = contextAttribs = new ContextAttribs(this);
        contextAttribs.contextFlags ^= flag;
        return attribs;
    }
    
    public ContextAttribs withDebug(final boolean debug) {
        return this.toggleFlag(1, debug);
    }
    
    public ContextAttribs withForwardCompatible(final boolean forwardCompatible) {
        return this.toggleFlag(2, forwardCompatible);
    }
    
    public ContextAttribs withRobustAccess(final boolean robustAccess) {
        return this.toggleFlag(4, robustAccess);
    }
    
    public ContextAttribs withContextResetIsolation(final boolean contextResetIsolation) {
        return this.toggleFlag(8, contextResetIsolation);
    }
    
    public ContextAttribs withResetNotificationStrategy(final int strategy) {
        if (strategy == this.contextResetNotificationStrategy) {
            return this;
        }
        if (LWJGLUtil.CHECKS && strategy != 33377 && strategy != 33362) {
            throw new IllegalArgumentException("Invalid context reset notification strategy specified: 0x" + LWJGLUtil.toHexString(strategy));
        }
        final ContextAttribs attribs = new ContextAttribs(this);
        attribs.contextResetNotificationStrategy = strategy;
        return attribs;
    }
    
    @Deprecated
    public ContextAttribs withLoseContextOnReset(final boolean loseContextOnReset) {
        return this.withResetNotificationStrategy(loseContextOnReset ? 33362 : 33377);
    }
    
    public ContextAttribs withContextReleaseBehavior(final int behavior) {
        if (behavior == this.contextReleaseBehavior) {
            return this;
        }
        if (LWJGLUtil.CHECKS && behavior != 8344 && behavior != 0) {
            throw new IllegalArgumentException("Invalid context release behavior specified: 0x" + LWJGLUtil.toHexString(behavior));
        }
        final ContextAttribs attribs = new ContextAttribs(this);
        attribs.contextReleaseBehavior = behavior;
        return attribs;
    }
    
    public ContextAttribs withLayer(final int layerPlane) {
        if (LWJGLUtil.getPlatform() != 3) {
            throw new IllegalArgumentException("The CONTEXT_LAYER_PLANE_ARB attribute is supported only on the Windows platform.");
        }
        if (layerPlane == this.layerPlane) {
            return this;
        }
        if (layerPlane < 0) {
            throw new IllegalArgumentException("Invalid layer plane specified: " + layerPlane);
        }
        final ContextAttribs attribs = new ContextAttribs(this);
        attribs.layerPlane = layerPlane;
        return attribs;
    }
    
    IntBuffer getAttribList() {
        if (LWJGLUtil.getPlatform() == 2) {
            return null;
        }
        final LinkedHashMap<Integer, Integer> map = new LinkedHashMap<Integer, Integer>(8);
        if (this.majorVersion != 1 || this.minorVersion != 0) {
            map.put(8337, this.majorVersion);
            map.put(8338, this.minorVersion);
        }
        if (this.contextFlags != 0) {
            map.put(8340, this.contextFlags);
        }
        if (this.profileMask != 0) {
            map.put(37158, this.profileMask);
        }
        if (this.contextResetNotificationStrategy != 33377) {
            map.put(33366, this.contextResetNotificationStrategy);
        }
        if (this.contextReleaseBehavior != 8344) {
            map.put(8343, this.contextReleaseBehavior);
        }
        if (this.layerPlane != 0) {
            map.put(8339, this.layerPlane);
        }
        if (map.isEmpty()) {
            return null;
        }
        final IntBuffer attribs = BufferUtils.createIntBuffer(map.size() * 2 + 1);
        for (final Map.Entry<Integer, Integer> attrib : map.entrySet()) {
            attribs.put(attrib.getKey()).put(attrib.getValue());
        }
        attribs.put(0);
        attribs.rewind();
        return attribs;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(32);
        sb.append("ContextAttribs:");
        sb.append(" Version=").append(this.majorVersion).append('.').append(this.minorVersion);
        if (this.profileMask != 0) {
            sb.append(", Profile=");
            if (this.hasMask(1)) {
                sb.append("CORE");
            }
            else if (this.hasMask(2)) {
                sb.append("COMPATIBLITY");
            }
            else if (this.hasMask(4)) {
                sb.append("ES2");
            }
            else {
                sb.append("*unknown*");
            }
        }
        if (this.contextFlags != 0) {
            if (this.hasFlag(1)) {
                sb.append(", DEBUG");
            }
            if (this.hasFlag(2)) {
                sb.append(", FORWARD_COMPATIBLE");
            }
            if (this.hasFlag(4)) {
                sb.append(", ROBUST_ACCESS");
            }
            if (this.hasFlag(8)) {
                sb.append(", RESET_ISOLATION");
            }
        }
        if (this.contextResetNotificationStrategy != 33377) {
            sb.append(", LOSE_CONTEXT_ON_RESET");
        }
        if (this.contextReleaseBehavior != 8344) {
            sb.append(", RELEASE_BEHAVIOR_NONE");
        }
        if (this.layerPlane != 0) {
            sb.append(", Layer=").append(this.layerPlane);
        }
        return sb.toString();
    }
}
