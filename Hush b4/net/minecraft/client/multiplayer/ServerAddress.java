// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.multiplayer;

import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import java.net.IDN;

public class ServerAddress
{
    private final String ipAddress;
    private final int serverPort;
    
    private ServerAddress(final String p_i1192_1_, final int p_i1192_2_) {
        this.ipAddress = p_i1192_1_;
        this.serverPort = p_i1192_2_;
    }
    
    public String getIP() {
        return IDN.toASCII(this.ipAddress);
    }
    
    public int getPort() {
        return this.serverPort;
    }
    
    public static ServerAddress func_78860_a(final String p_78860_0_) {
        if (p_78860_0_ == null) {
            return null;
        }
        String[] astring = p_78860_0_.split(":");
        if (p_78860_0_.startsWith("[")) {
            final int i = p_78860_0_.indexOf("]");
            if (i > 0) {
                final String s = p_78860_0_.substring(1, i);
                String s2 = p_78860_0_.substring(i + 1).trim();
                if (s2.startsWith(":") && s2.length() > 0) {
                    s2 = s2.substring(1);
                    astring = new String[] { s, s2 };
                }
                else {
                    astring = new String[] { s };
                }
            }
        }
        if (astring.length > 2) {
            astring = new String[] { p_78860_0_ };
        }
        String s3 = astring[0];
        int j = (astring.length > 1) ? parseIntWithDefault(astring[1], 25565) : 25565;
        if (j == 25565) {
            final String[] astring2 = getServerAddress(s3);
            s3 = astring2[0];
            j = parseIntWithDefault(astring2[1], 25565);
        }
        return new ServerAddress(s3, j);
    }
    
    private static String[] getServerAddress(final String p_78863_0_) {
        try {
            final String s = "com.sun.jndi.dns.DnsContextFactory";
            Class.forName("com.sun.jndi.dns.DnsContextFactory");
            final Hashtable hashtable = new Hashtable();
            hashtable.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            hashtable.put("java.naming.provider.url", "dns:");
            hashtable.put("com.sun.jndi.dns.timeout.retries", "1");
            final DirContext dircontext = new InitialDirContext(hashtable);
            final Attributes attributes = dircontext.getAttributes("_minecraft._tcp." + p_78863_0_, new String[] { "SRV" });
            final String[] astring = attributes.get("srv").get().toString().split(" ", 4);
            return new String[] { astring[3], astring[2] };
        }
        catch (Throwable var6) {
            return new String[] { p_78863_0_, Integer.toString(25565) };
        }
    }
    
    private static int parseIntWithDefault(final String p_78862_0_, final int p_78862_1_) {
        try {
            return Integer.parseInt(p_78862_0_.trim());
        }
        catch (Exception var3) {
            return p_78862_1_;
        }
    }
}
