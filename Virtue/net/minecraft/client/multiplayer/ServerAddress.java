package net.minecraft.client.multiplayer;

import java.net.IDN;
import java.util.Hashtable;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;

public class ServerAddress
{
    private final String ipAddress;
    private final int serverPort;
    private static final String __OBFID = "CL_00000889";

    private ServerAddress(String p_i1192_1_, int p_i1192_2_)
    {
        this.ipAddress = p_i1192_1_;
        this.serverPort = p_i1192_2_;
    }

    public String getIP()
    {
        return IDN.toASCII(this.ipAddress);
    }

    public int getPort()
    {
        return this.serverPort;
    }

    public static ServerAddress func_78860_a(String p_78860_0_)
    {
        if (p_78860_0_ == null)
        {
            return null;
        }
        else
        {
            String[] var1 = p_78860_0_.split(":");

            if (p_78860_0_.startsWith("["))
            {
                int var2 = p_78860_0_.indexOf("]");

                if (var2 > 0)
                {
                    String var3 = p_78860_0_.substring(1, var2);
                    String var4 = p_78860_0_.substring(var2 + 1).trim();

                    if (var4.startsWith(":") && var4.length() > 0)
                    {
                        var4 = var4.substring(1);
                        var1 = new String[] {var3, var4};
                    }
                    else
                    {
                        var1 = new String[] {var3};
                    }
                }
            }

            if (var1.length > 2)
            {
                var1 = new String[] {p_78860_0_};
            }

            String var5 = var1[0];
            int var6 = var1.length > 1 ? parseIntWithDefault(var1[1], 25565) : 25565;

            if (var6 == 25565)
            {
                String[] var7 = getServerAddress(var5);
                var5 = var7[0];
                var6 = parseIntWithDefault(var7[1], 25565);
            }

            return new ServerAddress(var5, var6);
        }
    }

    /**
     * Returns a server's address and port for the specified hostname, looking up the SRV record if possible
     */
    private static String[] getServerAddress(String p_78863_0_)
    {
        try
        {
            String var1 = "com.sun.jndi.dns.DnsContextFactory";
            Class.forName("com.sun.jndi.dns.DnsContextFactory");
            Hashtable var2 = new Hashtable();
            var2.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            var2.put("java.naming.provider.url", "dns:");
            var2.put("com.sun.jndi.dns.timeout.retries", "1");
            InitialDirContext var3 = new InitialDirContext(var2);
            Attributes var4 = var3.getAttributes("_minecraft._tcp." + p_78863_0_, new String[] {"SRV"});
            String[] var5 = var4.get("srv").get().toString().split(" ", 4);
            return new String[] {var5[3], var5[2]};
        }
        catch (Throwable var6)
        {
            return new String[] {p_78863_0_, Integer.toString(25565)};
        }
    }

    private static int parseIntWithDefault(String p_78862_0_, int p_78862_1_)
    {
        try
        {
            return Integer.parseInt(p_78862_0_.trim());
        }
        catch (Exception var3)
        {
            return p_78862_1_;
        }
    }
}
