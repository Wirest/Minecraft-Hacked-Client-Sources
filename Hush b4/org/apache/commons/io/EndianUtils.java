// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io;

import java.io.EOFException;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

public class EndianUtils
{
    public static short swapShort(final short value) {
        return (short)(((value >> 0 & 0xFF) << 8) + ((value >> 8 & 0xFF) << 0));
    }
    
    public static int swapInteger(final int value) {
        return ((value >> 0 & 0xFF) << 24) + ((value >> 8 & 0xFF) << 16) + ((value >> 16 & 0xFF) << 8) + ((value >> 24 & 0xFF) << 0);
    }
    
    public static long swapLong(final long value) {
        return ((value >> 0 & 0xFFL) << 56) + ((value >> 8 & 0xFFL) << 48) + ((value >> 16 & 0xFFL) << 40) + ((value >> 24 & 0xFFL) << 32) + ((value >> 32 & 0xFFL) << 24) + ((value >> 40 & 0xFFL) << 16) + ((value >> 48 & 0xFFL) << 8) + ((value >> 56 & 0xFFL) << 0);
    }
    
    public static float swapFloat(final float value) {
        return Float.intBitsToFloat(swapInteger(Float.floatToIntBits(value)));
    }
    
    public static double swapDouble(final double value) {
        return Double.longBitsToDouble(swapLong(Double.doubleToLongBits(value)));
    }
    
    public static void writeSwappedShort(final byte[] data, final int offset, final short value) {
        data[offset + 0] = (byte)(value >> 0 & 0xFF);
        data[offset + 1] = (byte)(value >> 8 & 0xFF);
    }
    
    public static short readSwappedShort(final byte[] data, final int offset) {
        return (short)(((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8));
    }
    
    public static int readSwappedUnsignedShort(final byte[] data, final int offset) {
        return ((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8);
    }
    
    public static void writeSwappedInteger(final byte[] data, final int offset, final int value) {
        data[offset + 0] = (byte)(value >> 0 & 0xFF);
        data[offset + 1] = (byte)(value >> 8 & 0xFF);
        data[offset + 2] = (byte)(value >> 16 & 0xFF);
        data[offset + 3] = (byte)(value >> 24 & 0xFF);
    }
    
    public static int readSwappedInteger(final byte[] data, final int offset) {
        return ((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8) + ((data[offset + 2] & 0xFF) << 16) + ((data[offset + 3] & 0xFF) << 24);
    }
    
    public static long readSwappedUnsignedInteger(final byte[] data, final int offset) {
        final long low = ((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8) + ((data[offset + 2] & 0xFF) << 16);
        final long high = data[offset + 3] & 0xFF;
        return (high << 24) + (0xFFFFFFFFL & low);
    }
    
    public static void writeSwappedLong(final byte[] data, final int offset, final long value) {
        data[offset + 0] = (byte)(value >> 0 & 0xFFL);
        data[offset + 1] = (byte)(value >> 8 & 0xFFL);
        data[offset + 2] = (byte)(value >> 16 & 0xFFL);
        data[offset + 3] = (byte)(value >> 24 & 0xFFL);
        data[offset + 4] = (byte)(value >> 32 & 0xFFL);
        data[offset + 5] = (byte)(value >> 40 & 0xFFL);
        data[offset + 6] = (byte)(value >> 48 & 0xFFL);
        data[offset + 7] = (byte)(value >> 56 & 0xFFL);
    }
    
    public static long readSwappedLong(final byte[] data, final int offset) {
        final long low = ((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8) + ((data[offset + 2] & 0xFF) << 16) + ((data[offset + 3] & 0xFF) << 24);
        final long high = ((data[offset + 4] & 0xFF) << 0) + ((data[offset + 5] & 0xFF) << 8) + ((data[offset + 6] & 0xFF) << 16) + ((data[offset + 7] & 0xFF) << 24);
        return (high << 32) + (0xFFFFFFFFL & low);
    }
    
    public static void writeSwappedFloat(final byte[] data, final int offset, final float value) {
        writeSwappedInteger(data, offset, Float.floatToIntBits(value));
    }
    
    public static float readSwappedFloat(final byte[] data, final int offset) {
        return Float.intBitsToFloat(readSwappedInteger(data, offset));
    }
    
    public static void writeSwappedDouble(final byte[] data, final int offset, final double value) {
        writeSwappedLong(data, offset, Double.doubleToLongBits(value));
    }
    
    public static double readSwappedDouble(final byte[] data, final int offset) {
        return Double.longBitsToDouble(readSwappedLong(data, offset));
    }
    
    public static void writeSwappedShort(final OutputStream output, final short value) throws IOException {
        output.write((byte)(value >> 0 & 0xFF));
        output.write((byte)(value >> 8 & 0xFF));
    }
    
    public static short readSwappedShort(final InputStream input) throws IOException {
        return (short)(((read(input) & 0xFF) << 0) + ((read(input) & 0xFF) << 8));
    }
    
    public static int readSwappedUnsignedShort(final InputStream input) throws IOException {
        final int value1 = read(input);
        final int value2 = read(input);
        return ((value1 & 0xFF) << 0) + ((value2 & 0xFF) << 8);
    }
    
    public static void writeSwappedInteger(final OutputStream output, final int value) throws IOException {
        output.write((byte)(value >> 0 & 0xFF));
        output.write((byte)(value >> 8 & 0xFF));
        output.write((byte)(value >> 16 & 0xFF));
        output.write((byte)(value >> 24 & 0xFF));
    }
    
    public static int readSwappedInteger(final InputStream input) throws IOException {
        final int value1 = read(input);
        final int value2 = read(input);
        final int value3 = read(input);
        final int value4 = read(input);
        return ((value1 & 0xFF) << 0) + ((value2 & 0xFF) << 8) + ((value3 & 0xFF) << 16) + ((value4 & 0xFF) << 24);
    }
    
    public static long readSwappedUnsignedInteger(final InputStream input) throws IOException {
        final int value1 = read(input);
        final int value2 = read(input);
        final int value3 = read(input);
        final int value4 = read(input);
        final long low = ((value1 & 0xFF) << 0) + ((value2 & 0xFF) << 8) + ((value3 & 0xFF) << 16);
        final long high = value4 & 0xFF;
        return (high << 24) + (0xFFFFFFFFL & low);
    }
    
    public static void writeSwappedLong(final OutputStream output, final long value) throws IOException {
        output.write((byte)(value >> 0 & 0xFFL));
        output.write((byte)(value >> 8 & 0xFFL));
        output.write((byte)(value >> 16 & 0xFFL));
        output.write((byte)(value >> 24 & 0xFFL));
        output.write((byte)(value >> 32 & 0xFFL));
        output.write((byte)(value >> 40 & 0xFFL));
        output.write((byte)(value >> 48 & 0xFFL));
        output.write((byte)(value >> 56 & 0xFFL));
    }
    
    public static long readSwappedLong(final InputStream input) throws IOException {
        final byte[] bytes = new byte[8];
        for (int i = 0; i < 8; ++i) {
            bytes[i] = (byte)read(input);
        }
        return readSwappedLong(bytes, 0);
    }
    
    public static void writeSwappedFloat(final OutputStream output, final float value) throws IOException {
        writeSwappedInteger(output, Float.floatToIntBits(value));
    }
    
    public static float readSwappedFloat(final InputStream input) throws IOException {
        return Float.intBitsToFloat(readSwappedInteger(input));
    }
    
    public static void writeSwappedDouble(final OutputStream output, final double value) throws IOException {
        writeSwappedLong(output, Double.doubleToLongBits(value));
    }
    
    public static double readSwappedDouble(final InputStream input) throws IOException {
        return Double.longBitsToDouble(readSwappedLong(input));
    }
    
    private static int read(final InputStream input) throws IOException {
        final int value = input.read();
        if (-1 == value) {
            throw new EOFException("Unexpected EOF reached");
        }
        return value;
    }
}
