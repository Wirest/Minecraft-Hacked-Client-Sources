// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.network.PacketBuffer;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import net.minecraft.util.Rotations;
import net.minecraft.util.BlockPos;
import net.minecraft.item.ItemStack;
import com.google.common.collect.Maps;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.Map;

public class DataWatcher
{
    private final Entity owner;
    private boolean isBlank;
    private static final Map<Class<?>, Integer> dataTypes;
    private final Map<Integer, WatchableObject> watchedObjects;
    private boolean objectChanged;
    private ReadWriteLock lock;
    
    static {
        (dataTypes = Maps.newHashMap()).put(Byte.class, 0);
        DataWatcher.dataTypes.put(Short.class, 1);
        DataWatcher.dataTypes.put(Integer.class, 2);
        DataWatcher.dataTypes.put(Float.class, 3);
        DataWatcher.dataTypes.put(String.class, 4);
        DataWatcher.dataTypes.put(ItemStack.class, 5);
        DataWatcher.dataTypes.put(BlockPos.class, 6);
        DataWatcher.dataTypes.put(Rotations.class, 7);
    }
    
    public DataWatcher(final Entity owner) {
        this.isBlank = true;
        this.watchedObjects = (Map<Integer, WatchableObject>)Maps.newHashMap();
        this.lock = new ReentrantReadWriteLock();
        this.owner = owner;
    }
    
    public <T> void addObject(final int id, final T object) {
        final Integer integer = DataWatcher.dataTypes.get(object.getClass());
        if (integer == null) {
            throw new IllegalArgumentException("Unknown data type: " + object.getClass());
        }
        if (id > 31) {
            throw new IllegalArgumentException("Data value id is too big with " + id + "! (Max is " + 31 + ")");
        }
        if (this.watchedObjects.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate id value for " + id + "!");
        }
        final WatchableObject datawatcher$watchableobject = new WatchableObject(integer, id, object);
        this.lock.writeLock().lock();
        this.watchedObjects.put(id, datawatcher$watchableobject);
        this.lock.writeLock().unlock();
        this.isBlank = false;
    }
    
    public void addObjectByDataType(final int id, final int type) {
        final WatchableObject datawatcher$watchableobject = new WatchableObject(type, id, null);
        this.lock.writeLock().lock();
        this.watchedObjects.put(id, datawatcher$watchableobject);
        this.lock.writeLock().unlock();
        this.isBlank = false;
    }
    
    public byte getWatchableObjectByte(final int id) {
        return (byte)this.getWatchedObject(id).getObject();
    }
    
    public short getWatchableObjectShort(final int id) {
        return (short)this.getWatchedObject(id).getObject();
    }
    
    public int getWatchableObjectInt(final int id) {
        return (int)this.getWatchedObject(id).getObject();
    }
    
    public float getWatchableObjectFloat(final int id) {
        return (float)this.getWatchedObject(id).getObject();
    }
    
    public String getWatchableObjectString(final int id) {
        return (String)this.getWatchedObject(id).getObject();
    }
    
    public ItemStack getWatchableObjectItemStack(final int id) {
        return (ItemStack)this.getWatchedObject(id).getObject();
    }
    
    private WatchableObject getWatchedObject(final int id) {
        this.lock.readLock().lock();
        WatchableObject datawatcher$watchableobject;
        try {
            datawatcher$watchableobject = this.watchedObjects.get(id);
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting synched entity data");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Synched entity data");
            crashreportcategory.addCrashSection("Data ID", id);
            throw new ReportedException(crashreport);
        }
        this.lock.readLock().unlock();
        return datawatcher$watchableobject;
    }
    
    public Rotations getWatchableObjectRotations(final int id) {
        return (Rotations)this.getWatchedObject(id).getObject();
    }
    
    public <T> void updateObject(final int id, final T newData) {
        final WatchableObject datawatcher$watchableobject = this.getWatchedObject(id);
        if (ObjectUtils.notEqual(newData, datawatcher$watchableobject.getObject())) {
            datawatcher$watchableobject.setObject(newData);
            this.owner.onDataWatcherUpdate(id);
            datawatcher$watchableobject.setWatched(true);
            this.objectChanged = true;
        }
    }
    
    public void setObjectWatched(final int id) {
        WatchableObject.access$0(this.getWatchedObject(id), true);
        this.objectChanged = true;
    }
    
    public boolean hasObjectChanged() {
        return this.objectChanged;
    }
    
    public static void writeWatchedListToPacketBuffer(final List<WatchableObject> objectsList, final PacketBuffer buffer) throws IOException {
        if (objectsList != null) {
            for (final WatchableObject datawatcher$watchableobject : objectsList) {
                writeWatchableObjectToPacketBuffer(buffer, datawatcher$watchableobject);
            }
        }
        buffer.writeByte(127);
    }
    
    public List<WatchableObject> getChanged() {
        List<WatchableObject> list = null;
        if (this.objectChanged) {
            this.lock.readLock().lock();
            for (final WatchableObject datawatcher$watchableobject : this.watchedObjects.values()) {
                if (datawatcher$watchableobject.isWatched()) {
                    datawatcher$watchableobject.setWatched(false);
                    if (list == null) {
                        list = (List<WatchableObject>)Lists.newArrayList();
                    }
                    list.add(datawatcher$watchableobject);
                }
            }
            this.lock.readLock().unlock();
        }
        this.objectChanged = false;
        return list;
    }
    
    public void writeTo(final PacketBuffer buffer) throws IOException {
        this.lock.readLock().lock();
        for (final WatchableObject datawatcher$watchableobject : this.watchedObjects.values()) {
            writeWatchableObjectToPacketBuffer(buffer, datawatcher$watchableobject);
        }
        this.lock.readLock().unlock();
        buffer.writeByte(127);
    }
    
    public List<WatchableObject> getAllWatched() {
        List<WatchableObject> list = null;
        this.lock.readLock().lock();
        for (final WatchableObject datawatcher$watchableobject : this.watchedObjects.values()) {
            if (list == null) {
                list = (List<WatchableObject>)Lists.newArrayList();
            }
            list.add(datawatcher$watchableobject);
        }
        this.lock.readLock().unlock();
        return list;
    }
    
    private static void writeWatchableObjectToPacketBuffer(final PacketBuffer buffer, final WatchableObject object) throws IOException {
        final int i = (object.getObjectType() << 5 | (object.getDataValueId() & 0x1F)) & 0xFF;
        buffer.writeByte(i);
        switch (object.getObjectType()) {
            case 0: {
                buffer.writeByte((byte)object.getObject());
                break;
            }
            case 1: {
                buffer.writeShort((short)object.getObject());
                break;
            }
            case 2: {
                buffer.writeInt((int)object.getObject());
                break;
            }
            case 3: {
                buffer.writeFloat((float)object.getObject());
                break;
            }
            case 4: {
                buffer.writeString((String)object.getObject());
                break;
            }
            case 5: {
                final ItemStack itemstack = (ItemStack)object.getObject();
                buffer.writeItemStackToBuffer(itemstack);
                break;
            }
            case 6: {
                final BlockPos blockpos = (BlockPos)object.getObject();
                buffer.writeInt(blockpos.getX());
                buffer.writeInt(blockpos.getY());
                buffer.writeInt(blockpos.getZ());
                break;
            }
            case 7: {
                final Rotations rotations = (Rotations)object.getObject();
                buffer.writeFloat(rotations.getX());
                buffer.writeFloat(rotations.getY());
                buffer.writeFloat(rotations.getZ());
                break;
            }
        }
    }
    
    public static List<WatchableObject> readWatchedListFromPacketBuffer(final PacketBuffer buffer) throws IOException {
        List<WatchableObject> list = null;
        for (int i = buffer.readByte(); i != 127; i = buffer.readByte()) {
            if (list == null) {
                list = (List<WatchableObject>)Lists.newArrayList();
            }
            final int j = (i & 0xE0) >> 5;
            final int k = i & 0x1F;
            WatchableObject datawatcher$watchableobject = null;
            switch (j) {
                case 0: {
                    datawatcher$watchableobject = new WatchableObject(j, k, buffer.readByte());
                    break;
                }
                case 1: {
                    datawatcher$watchableobject = new WatchableObject(j, k, buffer.readShort());
                    break;
                }
                case 2: {
                    datawatcher$watchableobject = new WatchableObject(j, k, buffer.readInt());
                    break;
                }
                case 3: {
                    datawatcher$watchableobject = new WatchableObject(j, k, buffer.readFloat());
                    break;
                }
                case 4: {
                    datawatcher$watchableobject = new WatchableObject(j, k, buffer.readStringFromBuffer(32767));
                    break;
                }
                case 5: {
                    datawatcher$watchableobject = new WatchableObject(j, k, buffer.readItemStackFromBuffer());
                    break;
                }
                case 6: {
                    final int l = buffer.readInt();
                    final int i2 = buffer.readInt();
                    final int j2 = buffer.readInt();
                    datawatcher$watchableobject = new WatchableObject(j, k, new BlockPos(l, i2, j2));
                    break;
                }
                case 7: {
                    final float f = buffer.readFloat();
                    final float f2 = buffer.readFloat();
                    final float f3 = buffer.readFloat();
                    datawatcher$watchableobject = new WatchableObject(j, k, new Rotations(f, f2, f3));
                    break;
                }
            }
            list.add(datawatcher$watchableobject);
        }
        return list;
    }
    
    public void updateWatchedObjectsFromList(final List<WatchableObject> p_75687_1_) {
        this.lock.writeLock().lock();
        for (final WatchableObject datawatcher$watchableobject : p_75687_1_) {
            final WatchableObject datawatcher$watchableobject2 = this.watchedObjects.get(datawatcher$watchableobject.getDataValueId());
            if (datawatcher$watchableobject2 != null) {
                datawatcher$watchableobject2.setObject(datawatcher$watchableobject.getObject());
                this.owner.onDataWatcherUpdate(datawatcher$watchableobject.getDataValueId());
            }
        }
        this.lock.writeLock().unlock();
        this.objectChanged = true;
    }
    
    public boolean getIsBlank() {
        return this.isBlank;
    }
    
    public void func_111144_e() {
        this.objectChanged = false;
    }
    
    public static class WatchableObject
    {
        private final int objectType;
        private final int dataValueId;
        private Object watchedObject;
        private boolean watched;
        
        public WatchableObject(final int type, final int id, final Object object) {
            this.dataValueId = id;
            this.watchedObject = object;
            this.objectType = type;
            this.watched = true;
        }
        
        public int getDataValueId() {
            return this.dataValueId;
        }
        
        public void setObject(final Object object) {
            this.watchedObject = object;
        }
        
        public Object getObject() {
            return this.watchedObject;
        }
        
        public int getObjectType() {
            return this.objectType;
        }
        
        public boolean isWatched() {
            return this.watched;
        }
        
        public void setWatched(final boolean watched) {
            this.watched = watched;
        }
        
        static /* synthetic */ void access$0(final WatchableObject watchableObject, final boolean watched) {
            watchableObject.watched = watched;
        }
    }
}
