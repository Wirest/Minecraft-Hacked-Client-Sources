// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public interface Guid
{
    public static class GUID extends Structure
    {
        public int Data1;
        public short Data2;
        public short Data3;
        public byte[] Data4;
        
        public GUID() {
            this.Data4 = new byte[8];
        }
        
        public GUID(final Pointer memory) {
            super(memory);
            this.Data4 = new byte[8];
            this.read();
        }
        
        public GUID(final byte[] data) {
            this.Data4 = new byte[8];
            if (data.length != 16) {
                throw new IllegalArgumentException("Invalid data length: " + data.length);
            }
            long data1Temp = data[3] & 0xFF;
            data1Temp <<= 8;
            data1Temp |= (data[2] & 0xFF);
            data1Temp <<= 8;
            data1Temp |= (data[1] & 0xFF);
            data1Temp <<= 8;
            data1Temp |= (data[0] & 0xFF);
            this.Data1 = (int)data1Temp;
            int data2Temp = data[5] & 0xFF;
            data2Temp <<= 8;
            data2Temp |= (data[4] & 0xFF);
            this.Data2 = (short)data2Temp;
            int data3Temp = data[7] & 0xFF;
            data3Temp <<= 8;
            data3Temp |= (data[6] & 0xFF);
            this.Data3 = (short)data3Temp;
            this.Data4[0] = data[8];
            this.Data4[1] = data[9];
            this.Data4[2] = data[10];
            this.Data4[3] = data[11];
            this.Data4[4] = data[12];
            this.Data4[5] = data[13];
            this.Data4[6] = data[14];
            this.Data4[7] = data[15];
        }
        
        public static class ByReference extends GUID implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final GUID guid) {
                super(guid.getPointer());
                this.Data1 = guid.Data1;
                this.Data2 = guid.Data2;
                this.Data3 = guid.Data3;
                this.Data4 = guid.Data4;
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
}
