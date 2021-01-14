package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Rotations;
import org.apache.commons.lang3.ObjectUtils;

public class DataWatcher {
    private final Entity owner;

    /**
     * When isBlank is true the DataWatcher is not watching any objects
     */
    private boolean isBlank = true;
    private static final Map dataTypes = Maps.newHashMap();
    private final Map watchedObjects = Maps.newHashMap();

    /**
     * true if one or more object was changed
     */
    private boolean objectChanged;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private static final String __OBFID = "CL_00001559";

    public DataWatcher(Entity owner) {
        this.owner = owner;
    }

    /**
     * adds a new object to dataWatcher to watch, to update an already existing object see updateObject. Arguments: data
     * Value Id, Object to add
     */
    public void addObject(int id, Object object) {
        Integer var3 = (Integer) dataTypes.get(object.getClass());

        if (var3 == null) {
            throw new IllegalArgumentException("Unknown data type: " + object.getClass());
        } else if (id > 31) {
            throw new IllegalArgumentException("Data value id is too big with " + id + "! (Max is " + 31 + ")");
        } else if (this.watchedObjects.containsKey(Integer.valueOf(id))) {
            throw new IllegalArgumentException("Duplicate id value for " + id + "!");
        } else {
            DataWatcher.WatchableObject var4 = new DataWatcher.WatchableObject(var3.intValue(), id, object);
            this.lock.writeLock().lock();
            this.watchedObjects.put(Integer.valueOf(id), var4);
            this.lock.writeLock().unlock();
            this.isBlank = false;
        }
    }

    /**
     * Add a new object for the DataWatcher to watch, using the specified data type.
     */
    public void addObjectByDataType(int id, int type) {
        DataWatcher.WatchableObject var3 = new DataWatcher.WatchableObject(type, id, (Object) null);
        this.lock.writeLock().lock();
        this.watchedObjects.put(Integer.valueOf(id), var3);
        this.lock.writeLock().unlock();
        this.isBlank = false;
    }

    /**
     * gets the bytevalue of a watchable object
     */
    public byte getWatchableObjectByte(int id) {
        return ((Byte) this.getWatchedObject(id).getObject()).byteValue();
    }

    public short getWatchableObjectShort(int id) {
        return ((Short) this.getWatchedObject(id).getObject()).shortValue();
    }

    /**
     * gets a watchable object and returns it as a Integer
     */
    public int getWatchableObjectInt(int id) {
        return ((Integer) this.getWatchedObject(id).getObject()).intValue();
    }

    public float getWatchableObjectFloat(int id) {
        return ((Float) this.getWatchedObject(id).getObject()).floatValue();
    }

    /**
     * gets a watchable object and returns it as a String
     */
    public String getWatchableObjectString(int id) {
        return (String) this.getWatchedObject(id).getObject();
    }

    /**
     * Get a watchable object as an ItemStack.
     */
    public ItemStack getWatchableObjectItemStack(int id) {
        return (ItemStack) this.getWatchedObject(id).getObject();
    }

    /**
     * is threadsafe, unless it throws an exception, then
     */
    private DataWatcher.WatchableObject getWatchedObject(int id) {
        this.lock.readLock().lock();
        DataWatcher.WatchableObject var2;

        try {
            var2 = (DataWatcher.WatchableObject) this.watchedObjects.get(Integer.valueOf(id));
        } catch (Throwable var6) {
            CrashReport var4 = CrashReport.makeCrashReport(var6, "Getting synched entity data");
            CrashReportCategory var5 = var4.makeCategory("Synched entity data");
            var5.addCrashSection("Data ID", Integer.valueOf(id));
            throw new ReportedException(var4);
        }

        this.lock.readLock().unlock();
        return var2;
    }

    public Rotations getWatchableObjectRotations(int id) {
        return (Rotations) this.getWatchedObject(id).getObject();
    }

    /**
     * updates an already existing object
     */
    public void updateObject(int id, Object newData) {
        DataWatcher.WatchableObject var3 = this.getWatchedObject(id);

        if (ObjectUtils.notEqual(newData, var3.getObject())) {
            var3.setObject(newData);
            this.owner.func_145781_i(id);
            var3.setWatched(true);
            this.objectChanged = true;
        }
    }

    public void setObjectWatched(int id) {
        this.getWatchedObject(id).watched = true;
        this.objectChanged = true;
    }

    /**
     * true if one or more object was changed
     */
    public boolean hasObjectChanged() {
        return this.objectChanged;
    }

    /**
     * Writes the list of watched objects (entity attribute of type {byte, short, int, float, string, ItemStack,
     * ChunkCoordinates}) to the specified PacketBuffer
     */
    public static void writeWatchedListToPacketBuffer(List objectsList, PacketBuffer buffer) throws IOException {
        if (objectsList != null) {
            Iterator var2 = objectsList.iterator();

            while (var2.hasNext()) {
                DataWatcher.WatchableObject var3 = (DataWatcher.WatchableObject) var2.next();
                writeWatchableObjectToPacketBuffer(buffer, var3);
            }
        }

        buffer.writeByte(127);
    }

    public List getChanged() {
        ArrayList var1 = null;

        if (this.objectChanged) {
            this.lock.readLock().lock();
            Iterator var2 = this.watchedObjects.values().iterator();

            while (var2.hasNext()) {
                DataWatcher.WatchableObject var3 = (DataWatcher.WatchableObject) var2.next();

                if (var3.isWatched()) {
                    var3.setWatched(false);

                    if (var1 == null) {
                        var1 = Lists.newArrayList();
                    }

                    var1.add(var3);
                }
            }

            this.lock.readLock().unlock();
        }

        this.objectChanged = false;
        return var1;
    }

    public void writeTo(PacketBuffer buffer) throws IOException {
        this.lock.readLock().lock();
        Iterator var2 = this.watchedObjects.values().iterator();

        while (var2.hasNext()) {
            DataWatcher.WatchableObject var3 = (DataWatcher.WatchableObject) var2.next();
            writeWatchableObjectToPacketBuffer(buffer, var3);
        }

        this.lock.readLock().unlock();
        buffer.writeByte(127);
    }

    public List getAllWatched() {
        ArrayList var1 = null;
        this.lock.readLock().lock();
        DataWatcher.WatchableObject var3;

        for (Iterator var2 = this.watchedObjects.values().iterator(); var2.hasNext(); var1.add(var3)) {
            var3 = (DataWatcher.WatchableObject) var2.next();

            if (var1 == null) {
                var1 = Lists.newArrayList();
            }
        }

        this.lock.readLock().unlock();
        return var1;
    }

    /**
     * Writes a watchable object (entity attribute of type {byte, short, int, float, string, ItemStack,
     * ChunkCoordinates}) to the specified PacketBuffer
     */
    private static void writeWatchableObjectToPacketBuffer(PacketBuffer buffer, DataWatcher.WatchableObject object) throws IOException {
        int var2 = (object.getObjectType() << 5 | object.getDataValueId() & 31) & 255;
        buffer.writeByte(var2);

        switch (object.getObjectType()) {
            case 0:
                buffer.writeByte(((Byte) object.getObject()).byteValue());
                break;

            case 1:
                buffer.writeShort(((Short) object.getObject()).shortValue());
                break;

            case 2:
                buffer.writeInt(((Integer) object.getObject()).intValue());
                break;

            case 3:
                buffer.writeFloat(((Float) object.getObject()).floatValue());
                break;

            case 4:
                buffer.writeString((String) object.getObject());
                break;

            case 5:
                ItemStack var3 = (ItemStack) object.getObject();
                buffer.writeItemStackToBuffer(var3);
                break;

            case 6:
                BlockPos var4 = (BlockPos) object.getObject();
                buffer.writeInt(var4.getX());
                buffer.writeInt(var4.getY());
                buffer.writeInt(var4.getZ());
                break;

            case 7:
                Rotations var5 = (Rotations) object.getObject();
                buffer.writeFloat(var5.func_179415_b());
                buffer.writeFloat(var5.func_179416_c());
                buffer.writeFloat(var5.func_179413_d());
        }
    }

    /**
     * Reads a list of watched objects (entity attribute of type {byte, short, int, float, string, ItemStack,
     * ChunkCoordinates}) from the supplied PacketBuffer
     */
    public static List readWatchedListFromPacketBuffer(PacketBuffer buffer) throws IOException {
        ArrayList var1 = null;

        for (byte var2 = buffer.readByte(); var2 != 127; var2 = buffer.readByte()) {
            if (var1 == null) {
                var1 = Lists.newArrayList();
            }

            int var3 = (var2 & 224) >> 5;
            int var4 = var2 & 31;
            DataWatcher.WatchableObject var5 = null;

            switch (var3) {
                case 0:
                    var5 = new DataWatcher.WatchableObject(var3, var4, Byte.valueOf(buffer.readByte()));
                    break;

                case 1:
                    var5 = new DataWatcher.WatchableObject(var3, var4, Short.valueOf(buffer.readShort()));
                    break;

                case 2:
                    var5 = new DataWatcher.WatchableObject(var3, var4, Integer.valueOf(buffer.readInt()));
                    break;

                case 3:
                    var5 = new DataWatcher.WatchableObject(var3, var4, Float.valueOf(buffer.readFloat()));
                    break;

                case 4:
                    var5 = new DataWatcher.WatchableObject(var3, var4, buffer.readStringFromBuffer(32767));
                    break;

                case 5:
                    var5 = new DataWatcher.WatchableObject(var3, var4, buffer.readItemStackFromBuffer());
                    break;

                case 6:
                    int var6 = buffer.readInt();
                    int var7 = buffer.readInt();
                    int var8 = buffer.readInt();
                    var5 = new DataWatcher.WatchableObject(var3, var4, new BlockPos(var6, var7, var8));
                    break;

                case 7:
                    float var9 = buffer.readFloat();
                    float var10 = buffer.readFloat();
                    float var11 = buffer.readFloat();
                    var5 = new DataWatcher.WatchableObject(var3, var4, new Rotations(var9, var10, var11));
            }

            var1.add(var5);
        }

        return var1;
    }

    public void updateWatchedObjectsFromList(List p_75687_1_) {
        this.lock.writeLock().lock();
        Iterator var2 = p_75687_1_.iterator();

        while (var2.hasNext()) {
            DataWatcher.WatchableObject var3 = (DataWatcher.WatchableObject) var2.next();
            DataWatcher.WatchableObject var4 = (DataWatcher.WatchableObject) this.watchedObjects.get(Integer.valueOf(var3.getDataValueId()));

            if (var4 != null) {
                var4.setObject(var3.getObject());
                this.owner.func_145781_i(var3.getDataValueId());
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

    static {
        dataTypes.put(Byte.class, Integer.valueOf(0));
        dataTypes.put(Short.class, Integer.valueOf(1));
        dataTypes.put(Integer.class, Integer.valueOf(2));
        dataTypes.put(Float.class, Integer.valueOf(3));
        dataTypes.put(String.class, Integer.valueOf(4));
        dataTypes.put(ItemStack.class, Integer.valueOf(5));
        dataTypes.put(BlockPos.class, Integer.valueOf(6));
        dataTypes.put(Rotations.class, Integer.valueOf(7));
    }

    public static class WatchableObject {
        private final int objectType;
        private final int dataValueId;
        private Object watchedObject;
        private boolean watched;
        private static final String __OBFID = "CL_00001560";

        public WatchableObject(int type, int id, Object object) {
            this.dataValueId = id;
            this.watchedObject = object;
            this.objectType = type;
            this.watched = true;
        }

        public int getDataValueId() {
            return this.dataValueId;
        }

        public void setObject(Object object) {
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

        public void setWatched(boolean watched) {
            this.watched = watched;
        }
    }
}
