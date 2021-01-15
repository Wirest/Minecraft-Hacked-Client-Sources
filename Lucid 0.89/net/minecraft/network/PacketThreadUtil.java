package net.minecraft.network;

import net.minecraft.util.IThreadListener;

public class PacketThreadUtil
{

    /**
     * Handles pumping inbound packets across threads by checking whether the current thread is actually the main thread
     * for the side in question. If not, then the packet is pumped into the handler queue in the main thread so that it
     * can be handled synchronously by the recipient, it then throws an exception to terminate the current packet
     * handler thread.
     */
    public static void checkThreadAndEnqueue(final Packet p_180031_0_, final INetHandler p_180031_1_, IThreadListener p_180031_2_)
    {
        if (!p_180031_2_.isCallingFromMinecraftThread())
        {
            p_180031_2_.addScheduledTask(new Runnable()
            {
                @Override
				public void run()
                {
                    p_180031_0_.processPacket(p_180031_1_);
                }
            });
            throw ThreadQuickExitException.field_179886_a;
        }
    }
}
