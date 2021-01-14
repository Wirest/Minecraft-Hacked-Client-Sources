package net.minecraft.client.stream;

import tv.twitch.ErrorCode;
import tv.twitch.broadcast.IngestServer;
import tv.twitch.chat.ChatUserInfo;

public class NullStream implements IStream
{
    private final Throwable field_152938_a;

    public NullStream(Throwable p_i1006_1_)
    {
        this.field_152938_a = p_i1006_1_;
    }

    /**
     * Shuts down a steam
     */
    @Override
	public void shutdownStream()
    {
    }

    @Override
	public void func_152935_j()
    {
    }

    @Override
	public void func_152922_k()
    {
    }

    @Override
	public boolean func_152936_l()
    {
        return false;
    }

    @Override
	public boolean isReadyToBroadcast()
    {
        return false;
    }

    @Override
	public boolean isBroadcasting()
    {
        return false;
    }

    @Override
	public void func_152911_a(Metadata p_152911_1_, long p_152911_2_)
    {
    }

    @Override
	public void func_176026_a(Metadata p_176026_1_, long p_176026_2_, long p_176026_4_)
    {
    }

    @Override
	public boolean isPaused()
    {
        return false;
    }

    @Override
	public void requestCommercial()
    {
    }

    /**
     * pauses a stream
     */
    @Override
	public void pause()
    {
    }

    /**
     * unpauses a stream
     */
    @Override
	public void unpause()
    {
    }

    @Override
	public void updateStreamVolume()
    {
    }

    @Override
	public void func_152930_t()
    {
    }

    @Override
	public void stopBroadcasting()
    {
    }

    @Override
	public IngestServer[] func_152925_v()
    {
        return new IngestServer[0];
    }

    @Override
	public void func_152909_x()
    {
    }

    @Override
	public IngestServerTester func_152932_y()
    {
        return null;
    }

    @Override
	public boolean func_152908_z()
    {
        return false;
    }

    @Override
	public int func_152920_A()
    {
        return 0;
    }

    @Override
	public boolean func_152927_B()
    {
        return false;
    }

    @Override
	public String func_152921_C()
    {
        return null;
    }

    @Override
	public ChatUserInfo func_152926_a(String p_152926_1_)
    {
        return null;
    }

    @Override
	public void func_152917_b(String p_152917_1_)
    {
    }

    @Override
	public boolean func_152928_D()
    {
        return false;
    }

    @Override
	public ErrorCode func_152912_E()
    {
        return null;
    }

    @Override
	public boolean func_152913_F()
    {
        return false;
    }

    /**
     * mutes or unmutes the microphone based on the boolean parameter passed into the method
     */
    @Override
	public void muteMicrophone(boolean p_152910_1_)
    {
    }

    @Override
	public boolean func_152929_G()
    {
        return false;
    }

    @Override
	public IStream.AuthFailureReason func_152918_H()
    {
        return IStream.AuthFailureReason.ERROR;
    }

    public Throwable func_152937_a()
    {
        return this.field_152938_a;
    }
}
