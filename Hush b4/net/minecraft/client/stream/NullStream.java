// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.stream;

import tv.twitch.ErrorCode;
import tv.twitch.chat.ChatUserInfo;
import tv.twitch.broadcast.IngestServer;

public class NullStream implements IStream
{
    private final Throwable field_152938_a;
    
    public NullStream(final Throwable p_i1006_1_) {
        this.field_152938_a = p_i1006_1_;
    }
    
    @Override
    public void shutdownStream() {
    }
    
    @Override
    public void func_152935_j() {
    }
    
    @Override
    public void func_152922_k() {
    }
    
    @Override
    public boolean func_152936_l() {
        return false;
    }
    
    @Override
    public boolean isReadyToBroadcast() {
        return false;
    }
    
    @Override
    public boolean isBroadcasting() {
        return false;
    }
    
    @Override
    public void func_152911_a(final Metadata p_152911_1_, final long p_152911_2_) {
    }
    
    @Override
    public void func_176026_a(final Metadata p_176026_1_, final long p_176026_2_, final long p_176026_4_) {
    }
    
    @Override
    public boolean isPaused() {
        return false;
    }
    
    @Override
    public void requestCommercial() {
    }
    
    @Override
    public void pause() {
    }
    
    @Override
    public void unpause() {
    }
    
    @Override
    public void updateStreamVolume() {
    }
    
    @Override
    public void func_152930_t() {
    }
    
    @Override
    public void stopBroadcasting() {
    }
    
    @Override
    public IngestServer[] func_152925_v() {
        return new IngestServer[0];
    }
    
    @Override
    public void func_152909_x() {
    }
    
    @Override
    public IngestServerTester func_152932_y() {
        return null;
    }
    
    @Override
    public boolean func_152908_z() {
        return false;
    }
    
    @Override
    public int func_152920_A() {
        return 0;
    }
    
    @Override
    public boolean func_152927_B() {
        return false;
    }
    
    @Override
    public String func_152921_C() {
        return null;
    }
    
    @Override
    public ChatUserInfo func_152926_a(final String p_152926_1_) {
        return null;
    }
    
    @Override
    public void func_152917_b(final String p_152917_1_) {
    }
    
    @Override
    public boolean func_152928_D() {
        return false;
    }
    
    @Override
    public ErrorCode func_152912_E() {
        return null;
    }
    
    @Override
    public boolean func_152913_F() {
        return false;
    }
    
    @Override
    public void muteMicrophone(final boolean p_152910_1_) {
    }
    
    @Override
    public boolean func_152929_G() {
        return false;
    }
    
    @Override
    public AuthFailureReason func_152918_H() {
        return AuthFailureReason.ERROR;
    }
    
    public Throwable func_152937_a() {
        return this.field_152938_a;
    }
}
