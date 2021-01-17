// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.serialization;

import java.io.ObjectStreamClass;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectOutputStream;

class CompactObjectOutputStream extends ObjectOutputStream
{
    static final int TYPE_FAT_DESCRIPTOR = 0;
    static final int TYPE_THIN_DESCRIPTOR = 1;
    
    CompactObjectOutputStream(final OutputStream out) throws IOException {
        super(out);
    }
    
    @Override
    protected void writeStreamHeader() throws IOException {
        this.writeByte(5);
    }
    
    @Override
    protected void writeClassDescriptor(final ObjectStreamClass desc) throws IOException {
        final Class<?> clazz = desc.forClass();
        if (clazz.isPrimitive() || clazz.isArray() || clazz.isInterface() || desc.getSerialVersionUID() == 0L) {
            this.write(0);
            super.writeClassDescriptor(desc);
        }
        else {
            this.write(1);
            this.writeUTF(desc.getName());
        }
    }
}
