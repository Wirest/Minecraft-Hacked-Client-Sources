// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

final class DIEffectInfo
{
    private final IDirectInputDevice device;
    private final byte[] guid;
    private final int guid_id;
    private final int effect_type;
    private final int static_params;
    private final int dynamic_params;
    private final String name;
    
    public DIEffectInfo(final IDirectInputDevice device, final byte[] guid, final int guid_id, final int effect_type, final int static_params, final int dynamic_params, final String name) {
        this.device = device;
        this.guid = guid;
        this.guid_id = guid_id;
        this.effect_type = effect_type;
        this.static_params = static_params;
        this.dynamic_params = dynamic_params;
        this.name = name;
    }
    
    public final byte[] getGUID() {
        return this.guid;
    }
    
    public final int getGUIDId() {
        return this.guid_id;
    }
    
    public final int getDynamicParams() {
        return this.dynamic_params;
    }
    
    public final int getEffectType() {
        return this.effect_type;
    }
    
    public final String getName() {
        return this.name;
    }
}
