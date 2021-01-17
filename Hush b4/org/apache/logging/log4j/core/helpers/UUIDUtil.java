// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.helpers;

import java.util.Random;
import java.util.Enumeration;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.net.UnknownHostException;
import java.net.NetworkInterface;
import java.net.InetAddress;
import org.apache.logging.log4j.util.PropertiesUtil;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public final class UUIDUtil
{
    public static final String UUID_SEQUENCE = "org.apache.logging.log4j.uuidSequence";
    private static final String ASSIGNED_SEQUENCES = "org.apache.logging.log4j.assignedSequences";
    private static AtomicInteger count;
    private static final long TYPE1 = 4096L;
    private static final byte VARIANT = Byte.MIN_VALUE;
    private static final int SEQUENCE_MASK = 16383;
    private static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 122192928000000000L;
    private static long uuidSequence;
    private static long least;
    private static final long LOW_MASK = 4294967295L;
    private static final long MID_MASK = 281470681743360L;
    private static final long HIGH_MASK = 1152640029630136320L;
    private static final int NODE_SIZE = 8;
    private static final int SHIFT_2 = 16;
    private static final int SHIFT_4 = 32;
    private static final int SHIFT_6 = 48;
    private static final int HUNDRED_NANOS_PER_MILLI = 10000;
    
    private UUIDUtil() {
    }
    
    public static UUID getTimeBasedUUID() {
        final long time = System.currentTimeMillis() * 10000L + 122192928000000000L + UUIDUtil.count.incrementAndGet() % 10000;
        final long timeLow = (time & 0xFFFFFFFFL) << 32;
        final long timeMid = (time & 0xFFFF00000000L) >> 16;
        final long timeHi = (time & 0xFFF000000000000L) >> 48;
        final long most = timeLow | timeMid | 0x1000L | timeHi;
        return new UUID(most, UUIDUtil.least);
    }
    
    static {
        UUIDUtil.count = new AtomicInteger(0);
        UUIDUtil.uuidSequence = PropertiesUtil.getProperties().getLongProperty("org.apache.logging.log4j.uuidSequence", 0L);
        byte[] mac = null;
        try {
            final InetAddress address = InetAddress.getLocalHost();
            try {
                NetworkInterface ni = NetworkInterface.getByInetAddress(address);
                if (ni != null && !ni.isLoopback() && ni.isUp()) {
                    final Method method = ni.getClass().getMethod("getHardwareAddress", (Class<?>[])new Class[0]);
                    if (method != null) {
                        mac = (byte[])method.invoke(ni, new Object[0]);
                    }
                }
                if (mac == null) {
                    final Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
                    while (enumeration.hasMoreElements() && mac == null) {
                        ni = enumeration.nextElement();
                        if (ni != null && ni.isUp() && !ni.isLoopback()) {
                            final Method method2 = ni.getClass().getMethod("getHardwareAddress", (Class<?>[])new Class[0]);
                            if (method2 == null) {
                                continue;
                            }
                            mac = (byte[])method2.invoke(ni, new Object[0]);
                        }
                    }
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            if (mac == null || mac.length == 0) {
                mac = address.getAddress();
            }
        }
        catch (UnknownHostException ex2) {}
        final Random randomGenerator = new SecureRandom();
        if (mac == null || mac.length == 0) {
            mac = new byte[6];
            randomGenerator.nextBytes(mac);
        }
        final int length = (mac.length >= 6) ? 6 : mac.length;
        final int index = (mac.length >= 6) ? (mac.length - 6) : 0;
        final byte[] node = new byte[8];
        node[0] = -128;
        node[1] = 0;
        for (int i = 2; i < 8; ++i) {
            node[i] = 0;
        }
        System.arraycopy(mac, index, node, index + 2, length);
        final ByteBuffer buf = ByteBuffer.wrap(node);
        long rand = UUIDUtil.uuidSequence;
        final Runtime runtime = Runtime.getRuntime();
        synchronized (runtime) {
            String assigned = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.assignedSequences");
            long[] sequences;
            if (assigned == null) {
                sequences = new long[0];
            }
            else {
                final String[] array = assigned.split(",");
                sequences = new long[array.length];
                int j = 0;
                for (final String value : array) {
                    sequences[j] = Long.parseLong(value);
                    ++j;
                }
            }
            if (rand == 0L) {
                rand = randomGenerator.nextLong();
            }
            rand &= 0x3FFFL;
            boolean duplicate;
            do {
                duplicate = false;
                for (final long sequence : sequences) {
                    if (sequence == rand) {
                        duplicate = true;
                        break;
                    }
                }
                if (duplicate) {
                    rand = (rand + 1L & 0x3FFFL);
                }
            } while (duplicate);
            assigned = ((assigned == null) ? Long.toString(rand) : (assigned + "," + Long.toString(rand)));
            System.setProperty("org.apache.logging.log4j.assignedSequences", assigned);
        }
        UUIDUtil.least = (buf.getLong() | rand << 48);
    }
}
