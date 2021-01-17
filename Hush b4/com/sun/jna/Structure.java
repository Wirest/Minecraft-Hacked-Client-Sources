// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.nio.Buffer;
import java.util.LinkedHashMap;
import java.util.AbstractCollection;
import java.util.WeakHashMap;
import java.util.zip.Adler32;
import java.lang.reflect.Array;
import java.util.Collections;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Set;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Structure
{
    private static final boolean REVERSE_FIELDS;
    private static final boolean REQUIRES_FIELD_ORDER;
    static final boolean isPPC;
    static final boolean isSPARC;
    static final boolean isARM;
    public static final int ALIGN_DEFAULT = 0;
    public static final int ALIGN_NONE = 1;
    public static final int ALIGN_GNUC = 2;
    public static final int ALIGN_MSVC = 3;
    static final int MAX_GNUC_ALIGNMENT;
    protected static final int CALCULATE_SIZE = -1;
    static final Map layoutInfo;
    private Pointer memory;
    private int size;
    private int alignType;
    private int structAlignment;
    private Map structFields;
    private final Map nativeStrings;
    private TypeMapper typeMapper;
    private long typeInfo;
    private List fieldOrder;
    private boolean autoRead;
    private boolean autoWrite;
    private Structure[] array;
    private static final ThreadLocal reads;
    private static final ThreadLocal busy;
    
    protected Structure() {
        this((Pointer)null);
    }
    
    protected Structure(final TypeMapper mapper) {
        this(null, 0, mapper);
    }
    
    protected Structure(final Pointer p) {
        this(p, 0);
    }
    
    protected Structure(final Pointer p, final int alignType) {
        this(p, alignType, null);
    }
    
    protected Structure(final Pointer p, final int alignType, final TypeMapper mapper) {
        this.size = -1;
        this.nativeStrings = new HashMap();
        this.autoRead = true;
        this.autoWrite = true;
        this.setAlignType(alignType);
        this.setTypeMapper(mapper);
        if (p != null) {
            this.useMemory(p);
        }
        else {
            this.allocateMemory(-1);
        }
    }
    
    Map fields() {
        return this.structFields;
    }
    
    TypeMapper getTypeMapper() {
        return this.typeMapper;
    }
    
    protected void setTypeMapper(TypeMapper mapper) {
        if (mapper == null) {
            final Class declaring = this.getClass().getDeclaringClass();
            if (declaring != null) {
                mapper = Native.getTypeMapper(declaring);
            }
        }
        this.typeMapper = mapper;
        this.size = -1;
        if (this.memory instanceof AutoAllocated) {
            this.memory = null;
        }
    }
    
    protected void setAlignType(int alignType) {
        if (alignType == 0) {
            final Class declaring = this.getClass().getDeclaringClass();
            if (declaring != null) {
                alignType = Native.getStructureAlignment(declaring);
            }
            if (alignType == 0) {
                if (Platform.isWindows()) {
                    alignType = 3;
                }
                else {
                    alignType = 2;
                }
            }
        }
        this.alignType = alignType;
        this.size = -1;
        if (this.memory instanceof AutoAllocated) {
            this.memory = null;
        }
    }
    
    protected Memory autoAllocate(final int size) {
        return new AutoAllocated(size);
    }
    
    protected void useMemory(final Pointer m) {
        this.useMemory(m, 0);
    }
    
    protected void useMemory(final Pointer m, final int offset) {
        try {
            this.memory = m.share(offset);
            if (this.size == -1) {
                this.size = this.calculateSize(false);
            }
            if (this.size != -1) {
                this.memory = m.share(offset, this.size);
            }
            this.array = null;
        }
        catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Structure exceeds provided memory bounds");
        }
    }
    
    protected void ensureAllocated() {
        this.ensureAllocated(false);
    }
    
    private void ensureAllocated(final boolean avoidFFIType) {
        if (this.memory == null) {
            this.allocateMemory(avoidFFIType);
        }
        else if (this.size == -1) {
            this.size = this.calculateSize(true, avoidFFIType);
        }
    }
    
    protected void allocateMemory() {
        this.allocateMemory(false);
    }
    
    private void allocateMemory(final boolean avoidFFIType) {
        this.allocateMemory(this.calculateSize(true, avoidFFIType));
    }
    
    protected void allocateMemory(int size) {
        if (size == -1) {
            size = this.calculateSize(false);
        }
        else if (size <= 0) {
            throw new IllegalArgumentException("Structure size must be greater than zero: " + size);
        }
        if (size != -1) {
            if (this.memory == null || this.memory instanceof AutoAllocated) {
                this.memory = this.autoAllocate(size);
            }
            this.size = size;
        }
    }
    
    public int size() {
        this.ensureAllocated();
        if (this.size == -1) {
            this.size = this.calculateSize(true);
        }
        return this.size;
    }
    
    public void clear() {
        this.memory.clear(this.size());
    }
    
    public Pointer getPointer() {
        this.ensureAllocated();
        return this.memory;
    }
    
    static Set busy() {
        return Structure.busy.get();
    }
    
    static Map reading() {
        return Structure.reads.get();
    }
    
    public void read() {
        this.ensureAllocated();
        if (busy().contains(this)) {
            return;
        }
        busy().add(this);
        if (this instanceof ByReference) {
            reading().put(this.getPointer(), this);
        }
        try {
            final Iterator i = this.fields().values().iterator();
            while (i.hasNext()) {
                this.readField(i.next());
            }
        }
        finally {
            busy().remove(this);
            if (reading().get(this.getPointer()) == this) {
                reading().remove(this.getPointer());
            }
        }
    }
    
    protected int fieldOffset(final String name) {
        this.ensureAllocated();
        final StructField f = this.fields().get(name);
        if (f == null) {
            throw new IllegalArgumentException("No such field: " + name);
        }
        return f.offset;
    }
    
    public Object readField(final String name) {
        this.ensureAllocated();
        final StructField f = this.fields().get(name);
        if (f == null) {
            throw new IllegalArgumentException("No such field: " + name);
        }
        return this.readField(f);
    }
    
    Object getField(final StructField structField) {
        try {
            return structField.field.get(this);
        }
        catch (Exception e) {
            throw new Error("Exception reading field '" + structField.name + "' in " + this.getClass() + ": " + e);
        }
    }
    
    void setField(final StructField structField, final Object value) {
        this.setField(structField, value, false);
    }
    
    void setField(final StructField structField, final Object value, final boolean overrideFinal) {
        try {
            structField.field.set(this, value);
        }
        catch (IllegalAccessException e) {
            final int modifiers = structField.field.getModifiers();
            if (!Modifier.isFinal(modifiers)) {
                throw new Error("Unexpectedly unable to write to field '" + structField.name + "' within " + this.getClass() + ": " + e);
            }
            if (overrideFinal) {
                throw new UnsupportedOperationException("This VM does not support Structures with final fields (field '" + structField.name + "' within " + this.getClass() + ")");
            }
            throw new UnsupportedOperationException("Attempt to write to read-only field '" + structField.name + "' within " + this.getClass());
        }
    }
    
    static Structure updateStructureByReference(final Class type, Structure s, final Pointer address) {
        if (address == null) {
            s = null;
        }
        else {
            if (s == null || !address.equals(s.getPointer())) {
                final Structure s2 = reading().get(address);
                if (s2 != null && type.equals(s2.getClass())) {
                    s = s2;
                }
                else {
                    s = newInstance(type);
                    s.useMemory(address);
                }
            }
            s.autoRead();
        }
        return s;
    }
    
    Object readField(final StructField structField) {
        final int offset = structField.offset;
        Class fieldType = structField.type;
        final FromNativeConverter readConverter = structField.readConverter;
        if (readConverter != null) {
            fieldType = readConverter.nativeType();
        }
        final Object currentValue = (Structure.class.isAssignableFrom(fieldType) || Callback.class.isAssignableFrom(fieldType) || (Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(fieldType)) || Pointer.class.isAssignableFrom(fieldType) || NativeMapped.class.isAssignableFrom(fieldType) || fieldType.isArray()) ? this.getField(structField) : null;
        Object result = this.memory.getValue(offset, fieldType, currentValue);
        if (readConverter != null) {
            result = readConverter.fromNative(result, structField.context);
        }
        this.setField(structField, result, true);
        return result;
    }
    
    public void write() {
        this.ensureAllocated();
        if (this instanceof ByValue) {
            this.getTypeInfo();
        }
        if (busy().contains(this)) {
            return;
        }
        busy().add(this);
        try {
            for (final StructField sf : this.fields().values()) {
                if (!sf.isVolatile) {
                    this.writeField(sf);
                }
            }
        }
        finally {
            busy().remove(this);
        }
    }
    
    public void writeField(final String name) {
        this.ensureAllocated();
        final StructField f = this.fields().get(name);
        if (f == null) {
            throw new IllegalArgumentException("No such field: " + name);
        }
        this.writeField(f);
    }
    
    public void writeField(final String name, final Object value) {
        this.ensureAllocated();
        final StructField f = this.fields().get(name);
        if (f == null) {
            throw new IllegalArgumentException("No such field: " + name);
        }
        this.setField(f, value);
        this.writeField(f);
    }
    
    void writeField(final StructField structField) {
        if (structField.isReadOnly) {
            return;
        }
        final int offset = structField.offset;
        Object value = this.getField(structField);
        Class fieldType = structField.type;
        final ToNativeConverter converter = structField.writeConverter;
        if (converter != null) {
            value = converter.toNative(value, new StructureWriteContext(this, structField.field));
            fieldType = converter.nativeType();
        }
        if (String.class == fieldType || WString.class == fieldType) {
            final boolean wide = fieldType == WString.class;
            if (value != null) {
                final NativeString nativeString = new NativeString(value.toString(), wide);
                this.nativeStrings.put(structField.name, nativeString);
                value = nativeString.getPointer();
            }
            else {
                value = null;
                this.nativeStrings.remove(structField.name);
            }
        }
        try {
            this.memory.setValue(offset, value, fieldType);
        }
        catch (IllegalArgumentException e) {
            final String msg = "Structure field \"" + structField.name + "\" was declared as " + structField.type + ((structField.type == fieldType) ? "" : (" (native type " + fieldType + ")")) + ", which is not supported within a Structure";
            throw new IllegalArgumentException(msg);
        }
    }
    
    private boolean hasFieldOrder() {
        synchronized (this) {
            return this.fieldOrder != null;
        }
    }
    
    protected List getFieldOrder() {
        synchronized (this) {
            if (this.fieldOrder == null) {
                this.fieldOrder = new ArrayList();
            }
            return this.fieldOrder;
        }
    }
    
    protected void setFieldOrder(final String[] fields) {
        this.getFieldOrder().addAll(Arrays.asList(fields));
        this.size = -1;
        if (this.memory instanceof AutoAllocated) {
            this.memory = null;
        }
    }
    
    protected void sortFields(final List fields, final List names) {
        for (int i = 0; i < names.size(); ++i) {
            final String name = names.get(i);
            for (int f = 0; f < fields.size(); ++f) {
                final Field field = fields.get(f);
                if (name.equals(field.getName())) {
                    Collections.swap(fields, i, f);
                    break;
                }
            }
        }
    }
    
    protected List getFields(final boolean force) {
        final List flist = new ArrayList();
        for (Class cls = this.getClass(); !cls.equals(Structure.class); cls = cls.getSuperclass()) {
            final List classFields = new ArrayList();
            final Field[] fields = cls.getDeclaredFields();
            for (int i = 0; i < fields.length; ++i) {
                final int modifiers = fields[i].getModifiers();
                if (!Modifier.isStatic(modifiers)) {
                    if (Modifier.isPublic(modifiers)) {
                        classFields.add(fields[i]);
                    }
                }
            }
            if (Structure.REVERSE_FIELDS) {
                Collections.reverse(classFields);
            }
            flist.addAll(0, classFields);
        }
        if (Structure.REQUIRES_FIELD_ORDER || this.hasFieldOrder()) {
            final List fieldOrder = this.getFieldOrder();
            if (fieldOrder.size() < flist.size()) {
                if (force) {
                    throw new Error("This VM does not store fields in a predictable order; you must use Structure.setFieldOrder to explicitly indicate the field order: " + System.getProperty("java.vendor") + ", " + System.getProperty("java.version"));
                }
                return null;
            }
            else {
                this.sortFields(flist, fieldOrder);
            }
        }
        return flist;
    }
    
    private synchronized boolean fieldOrderMatch(final List fieldOrder) {
        return this.fieldOrder == fieldOrder || (this.fieldOrder != null && this.fieldOrder.equals(fieldOrder));
    }
    
    private int calculateSize(final boolean force) {
        return this.calculateSize(force, false);
    }
    
    int calculateSize(final boolean force, final boolean avoidFFIType) {
        boolean needsInit = true;
        LayoutInfo info;
        synchronized (Structure.layoutInfo) {
            info = Structure.layoutInfo.get(this.getClass());
        }
        if (info == null || this.alignType != info.alignType || this.typeMapper != info.typeMapper || !this.fieldOrderMatch(info.fieldOrder)) {
            info = this.deriveLayout(force, avoidFFIType);
            needsInit = false;
        }
        if (info != null) {
            this.structAlignment = info.alignment;
            this.structFields = info.fields;
            info.alignType = this.alignType;
            info.typeMapper = this.typeMapper;
            info.fieldOrder = this.fieldOrder;
            if (!info.variable) {
                synchronized (Structure.layoutInfo) {
                    Structure.layoutInfo.put(this.getClass(), info);
                }
            }
            if (needsInit) {
                this.initializeFields();
            }
            return info.size;
        }
        return -1;
    }
    
    private LayoutInfo deriveLayout(final boolean force, final boolean avoidFFIType) {
        final LayoutInfo info = new LayoutInfo();
        int calculatedSize = 0;
        final List fields = this.getFields(force);
        if (fields == null) {
            return null;
        }
        boolean firstField = true;
        for (final Field field : fields) {
            final int modifiers = field.getModifiers();
            final Class type = field.getType();
            if (type.isArray()) {
                info.variable = true;
            }
            final StructField structField = new StructField();
            structField.isVolatile = Modifier.isVolatile(modifiers);
            structField.isReadOnly = Modifier.isFinal(modifiers);
            if (structField.isReadOnly) {
                if (!Platform.RO_FIELDS) {
                    throw new IllegalArgumentException("This VM does not support read-only fields (field '" + field.getName() + "' within " + this.getClass() + ")");
                }
                field.setAccessible(true);
            }
            structField.field = field;
            structField.name = field.getName();
            structField.type = type;
            if (Callback.class.isAssignableFrom(type) && !type.isInterface()) {
                throw new IllegalArgumentException("Structure Callback field '" + field.getName() + "' must be an interface");
            }
            if (type.isArray() && Structure.class.equals(type.getComponentType())) {
                final String msg = "Nested Structure arrays must use a derived Structure type so that the size of the elements can be determined";
                throw new IllegalArgumentException(msg);
            }
            int fieldAlignment = 1;
            if (Modifier.isPublic(field.getModifiers())) {
                Object value = this.getField(structField);
                if (value == null && type.isArray()) {
                    if (force) {
                        throw new IllegalStateException("Array fields must be initialized");
                    }
                    return null;
                }
                else {
                    Class nativeType = type;
                    if (NativeMapped.class.isAssignableFrom(type)) {
                        final NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
                        nativeType = tc.nativeType();
                        structField.writeConverter = tc;
                        structField.readConverter = tc;
                        structField.context = new StructureReadContext(this, field);
                    }
                    else if (this.typeMapper != null) {
                        final ToNativeConverter writeConverter = this.typeMapper.getToNativeConverter(type);
                        final FromNativeConverter readConverter = this.typeMapper.getFromNativeConverter(type);
                        if (writeConverter != null && readConverter != null) {
                            value = writeConverter.toNative(value, new StructureWriteContext(this, structField.field));
                            nativeType = ((value != null) ? value.getClass() : Pointer.class);
                            structField.writeConverter = writeConverter;
                            structField.readConverter = readConverter;
                            structField.context = new StructureReadContext(this, field);
                        }
                        else if (writeConverter != null || readConverter != null) {
                            final String msg2 = "Structures require bidirectional type conversion for " + type;
                            throw new IllegalArgumentException(msg2);
                        }
                    }
                    if (value == null) {
                        value = this.initializeField(structField, type);
                    }
                    try {
                        structField.size = this.getNativeSize(nativeType, value);
                        fieldAlignment = this.getNativeAlignment(nativeType, value, firstField);
                    }
                    catch (IllegalArgumentException e) {
                        if (!force && this.typeMapper == null) {
                            return null;
                        }
                        final String msg3 = "Invalid Structure field in " + this.getClass() + ", field name '" + structField.name + "', " + structField.type + ": " + e.getMessage();
                        throw new IllegalArgumentException(msg3);
                    }
                    info.alignment = Math.max(info.alignment, fieldAlignment);
                    if (calculatedSize % fieldAlignment != 0) {
                        calculatedSize += fieldAlignment - calculatedSize % fieldAlignment;
                    }
                    structField.offset = calculatedSize;
                    calculatedSize += structField.size;
                    info.fields.put(structField.name, structField);
                }
            }
            firstField = false;
        }
        if (calculatedSize > 0) {
            final int size = this.calculateAlignedSize(calculatedSize, info.alignment);
            if (this instanceof ByValue && !avoidFFIType) {
                this.getTypeInfo();
            }
            if (this.memory != null && !(this.memory instanceof AutoAllocated)) {
                this.memory = this.memory.share(0L, size);
            }
            info.size = size;
            return info;
        }
        throw new IllegalArgumentException("Structure " + this.getClass() + " has unknown size (ensure " + "all fields are public)");
    }
    
    private void initializeFields() {
        for (final StructField f : this.fields().values()) {
            this.initializeField(f, f.type);
        }
    }
    
    private Object initializeField(final StructField structField, final Class type) {
        Object value = null;
        if (Structure.class.isAssignableFrom(type) && !ByReference.class.isAssignableFrom(type)) {
            try {
                value = newInstance(type);
                this.setField(structField, value);
                return value;
            }
            catch (IllegalArgumentException e) {
                final String msg = "Can't determine size of nested structure: " + e.getMessage();
                throw new IllegalArgumentException(msg);
            }
        }
        if (NativeMapped.class.isAssignableFrom(type)) {
            final NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
            value = tc.defaultValue();
            this.setField(structField, value);
        }
        return value;
    }
    
    int calculateAlignedSize(final int calculatedSize) {
        return this.calculateAlignedSize(calculatedSize, this.structAlignment);
    }
    
    private int calculateAlignedSize(int calculatedSize, final int alignment) {
        if (this.alignType != 1 && calculatedSize % alignment != 0) {
            calculatedSize += alignment - calculatedSize % alignment;
        }
        return calculatedSize;
    }
    
    protected int getStructAlignment() {
        if (this.size == -1) {
            this.calculateSize(true);
        }
        return this.structAlignment;
    }
    
    protected int getNativeAlignment(Class type, Object value, final boolean isFirstElement) {
        int alignment = 1;
        if (NativeMapped.class.isAssignableFrom(type)) {
            final NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
            type = tc.nativeType();
            value = tc.toNative(value, new ToNativeContext());
        }
        final int size = Native.getNativeSize(type, value);
        if (type.isPrimitive() || Long.class == type || Integer.class == type || Short.class == type || Character.class == type || Byte.class == type || Boolean.class == type || Float.class == type || Double.class == type) {
            alignment = size;
        }
        else if (Pointer.class == type || (Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(type)) || Callback.class.isAssignableFrom(type) || WString.class == type || String.class == type) {
            alignment = Pointer.SIZE;
        }
        else if (Structure.class.isAssignableFrom(type)) {
            if (ByReference.class.isAssignableFrom(type)) {
                alignment = Pointer.SIZE;
            }
            else {
                if (value == null) {
                    value = newInstance(type);
                }
                alignment = ((Structure)value).getStructAlignment();
            }
        }
        else {
            if (!type.isArray()) {
                throw new IllegalArgumentException("Type " + type + " has unknown " + "native alignment");
            }
            alignment = this.getNativeAlignment(type.getComponentType(), null, isFirstElement);
        }
        if (this.alignType == 1) {
            alignment = 1;
        }
        else if (this.alignType == 3) {
            alignment = Math.min(8, alignment);
        }
        else if (this.alignType == 2 && (!isFirstElement || !Platform.isMac() || !Structure.isPPC)) {
            alignment = Math.min(Structure.MAX_GNUC_ALIGNMENT, alignment);
        }
        return alignment;
    }
    
    public String toString() {
        return this.toString(Boolean.getBoolean("jna.dump_memory"));
    }
    
    public String toString(final boolean debug) {
        return this.toString(0, true, true);
    }
    
    private String format(final Class type) {
        final String s = type.getName();
        final int dot = s.lastIndexOf(".");
        return s.substring(dot + 1);
    }
    
    private String toString(final int indent, final boolean showContents, final boolean dumpMemory) {
        this.ensureAllocated();
        final String LS = System.getProperty("line.separator");
        String name = this.format(this.getClass()) + "(" + this.getPointer() + ")";
        if (!(this.getPointer() instanceof Memory)) {
            name = name + " (" + this.size() + " bytes)";
        }
        String prefix = "";
        for (int idx = 0; idx < indent; ++idx) {
            prefix += "  ";
        }
        String contents = LS;
        if (!showContents) {
            contents = "...}";
        }
        else {
            final Iterator i = this.fields().values().iterator();
            while (i.hasNext()) {
                final StructField sf = i.next();
                Object value = this.getField(sf);
                String type = this.format(sf.type);
                String index = "";
                contents += prefix;
                if (sf.type.isArray() && value != null) {
                    type = this.format(sf.type.getComponentType());
                    index = "[" + Array.getLength(value) + "]";
                }
                contents = contents + "  " + type + " " + sf.name + index + "@" + Integer.toHexString(sf.offset);
                if (value instanceof Structure) {
                    value = ((Structure)value).toString(indent + 1, !(value instanceof ByReference), dumpMemory);
                }
                contents += "=";
                if (value instanceof Long) {
                    contents += Long.toHexString((long)value);
                }
                else if (value instanceof Integer) {
                    contents += Integer.toHexString((int)value);
                }
                else if (value instanceof Short) {
                    contents += Integer.toHexString((short)value);
                }
                else if (value instanceof Byte) {
                    contents += Integer.toHexString((byte)value);
                }
                else {
                    contents += String.valueOf(value).trim();
                }
                contents += LS;
                if (!i.hasNext()) {
                    contents = contents + prefix + "}";
                }
            }
        }
        if (indent == 0 && dumpMemory) {
            final int BYTES_PER_ROW = 4;
            contents = contents + LS + "memory dump" + LS;
            final byte[] buf = this.getPointer().getByteArray(0L, this.size());
            for (int j = 0; j < buf.length; ++j) {
                if (j % 4 == 0) {
                    contents += "[";
                }
                if (buf[j] >= 0 && buf[j] < 16) {
                    contents += "0";
                }
                contents += Integer.toHexString(buf[j] & 0xFF);
                if (j % 4 == 3 && j < buf.length - 1) {
                    contents = contents + "]" + LS;
                }
            }
            contents += "]";
        }
        return name + " {" + contents;
    }
    
    public Structure[] toArray(final Structure[] array) {
        this.ensureAllocated();
        if (this.memory instanceof AutoAllocated) {
            final Memory m = (Memory)this.memory;
            final int requiredSize = array.length * this.size();
            if (m.size() < requiredSize) {
                this.useMemory(this.autoAllocate(requiredSize));
            }
        }
        array[0] = this;
        final int size = this.size();
        for (int i = 1; i < array.length; ++i) {
            (array[i] = newInstance(this.getClass())).useMemory(this.memory.share(i * size, size));
            array[i].read();
        }
        if (!(this instanceof ByValue)) {
            this.array = array;
        }
        return array;
    }
    
    public Structure[] toArray(final int size) {
        return this.toArray((Structure[])Array.newInstance(this.getClass(), size));
    }
    
    private Class baseClass() {
        if ((this instanceof ByReference || this instanceof ByValue) && Structure.class.isAssignableFrom(this.getClass().getSuperclass())) {
            return this.getClass().getSuperclass();
        }
        return this.getClass();
    }
    
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Structure)) {
            return false;
        }
        if (o.getClass() != this.getClass() && ((Structure)o).baseClass() != this.baseClass()) {
            return false;
        }
        final Structure s = (Structure)o;
        if (s.getPointer().equals(this.getPointer())) {
            return true;
        }
        if (s.size() == this.size()) {
            this.clear();
            this.write();
            final byte[] buf = this.getPointer().getByteArray(0L, this.size());
            s.clear();
            s.write();
            final byte[] sbuf = s.getPointer().getByteArray(0L, s.size());
            return Arrays.equals(buf, sbuf);
        }
        return false;
    }
    
    public int hashCode() {
        this.clear();
        this.write();
        final Adler32 code = new Adler32();
        code.update(this.getPointer().getByteArray(0L, this.size()));
        return (int)code.getValue();
    }
    
    protected void cacheTypeInfo(final Pointer p) {
        this.typeInfo = p.peer;
    }
    
    protected Pointer getFieldTypeInfo(final StructField f) {
        Class type = f.type;
        Object value = this.getField(f);
        if (this.typeMapper != null) {
            final ToNativeConverter nc = this.typeMapper.getToNativeConverter(type);
            if (nc != null) {
                type = nc.nativeType();
                value = nc.toNative(value, new ToNativeContext());
            }
        }
        return get(value, type);
    }
    
    Pointer getTypeInfo() {
        final Pointer p = getTypeInfo(this);
        this.cacheTypeInfo(p);
        return p;
    }
    
    public void setAutoSynch(final boolean auto) {
        this.setAutoRead(auto);
        this.setAutoWrite(auto);
    }
    
    public void setAutoRead(final boolean auto) {
        this.autoRead = auto;
    }
    
    public boolean getAutoRead() {
        return this.autoRead;
    }
    
    public void setAutoWrite(final boolean auto) {
        this.autoWrite = auto;
    }
    
    public boolean getAutoWrite() {
        return this.autoWrite;
    }
    
    static Pointer getTypeInfo(final Object obj) {
        return FFIType.get(obj);
    }
    
    public static Structure newInstance(final Class type) throws IllegalArgumentException {
        try {
            final Structure s = type.newInstance();
            if (s instanceof ByValue) {
                s.allocateMemory();
            }
            return s;
        }
        catch (InstantiationException e) {
            final String msg = "Can't instantiate " + type + " (" + e + ")";
            throw new IllegalArgumentException(msg);
        }
        catch (IllegalAccessException e2) {
            final String msg = "Instantiation of " + type + " not allowed, is it public? (" + e2 + ")";
            throw new IllegalArgumentException(msg);
        }
    }
    
    private static void structureArrayCheck(final Structure[] ss) {
        final Pointer base = ss[0].getPointer();
        final int size = ss[0].size();
        for (int si = 1; si < ss.length; ++si) {
            if (ss[si].getPointer().peer != base.peer + size * si) {
                final String msg = "Structure array elements must use contiguous memory (bad backing address at Structure array index " + si + ")";
                throw new IllegalArgumentException(msg);
            }
        }
    }
    
    public static void autoRead(final Structure[] ss) {
        structureArrayCheck(ss);
        if (ss[0].array == ss) {
            ss[0].autoRead();
        }
        else {
            for (int si = 0; si < ss.length; ++si) {
                ss[si].autoRead();
            }
        }
    }
    
    public void autoRead() {
        if (this.getAutoRead()) {
            this.read();
            if (this.array != null) {
                for (int i = 1; i < this.array.length; ++i) {
                    this.array[i].autoRead();
                }
            }
        }
    }
    
    public static void autoWrite(final Structure[] ss) {
        structureArrayCheck(ss);
        if (ss[0].array == ss) {
            ss[0].autoWrite();
        }
        else {
            for (int si = 0; si < ss.length; ++si) {
                ss[si].autoWrite();
            }
        }
    }
    
    public void autoWrite() {
        if (this.getAutoWrite()) {
            this.write();
            if (this.array != null) {
                for (int i = 1; i < this.array.length; ++i) {
                    this.array[i].autoWrite();
                }
            }
        }
    }
    
    protected int getNativeSize(final Class nativeType, final Object value) {
        return Native.getNativeSize(nativeType, value);
    }
    
    static {
        final Field[] fields = MemberOrder.class.getFields();
        final List names = new ArrayList();
        for (int i = 0; i < fields.length; ++i) {
            names.add(fields[i].getName());
        }
        final List expected = Arrays.asList(MemberOrder.FIELDS);
        final List reversed = new ArrayList(expected);
        Collections.reverse(reversed);
        REVERSE_FIELDS = names.equals(reversed);
        REQUIRES_FIELD_ORDER = (!names.equals(expected) && !Structure.REVERSE_FIELDS);
        final String arch = System.getProperty("os.arch").toLowerCase();
        isPPC = ("ppc".equals(arch) || "powerpc".equals(arch));
        isSPARC = "sparc".equals(arch);
        isARM = "arm".equals(arch);
        MAX_GNUC_ALIGNMENT = ((Structure.isSPARC || ((Structure.isPPC || Structure.isARM) && Platform.isLinux())) ? 8 : Native.LONG_SIZE);
        layoutInfo = new WeakHashMap();
        reads = new ThreadLocal() {
            protected synchronized Object initialValue() {
                return new HashMap();
            }
        };
        busy = new ThreadLocal() {
            protected synchronized Object initialValue() {
                return new StructureSet();
            }
            
            class StructureSet extends AbstractCollection implements Set
            {
                private Structure[] elements;
                private int count;
                
                private void ensureCapacity(final int size) {
                    if (this.elements == null) {
                        this.elements = new Structure[size * 3 / 2];
                    }
                    else if (this.elements.length < size) {
                        final Structure[] e = new Structure[size * 3 / 2];
                        System.arraycopy(this.elements, 0, e, 0, this.elements.length);
                        this.elements = e;
                    }
                }
                
                public int size() {
                    return this.count;
                }
                
                public boolean contains(final Object o) {
                    return this.indexOf(o) != -1;
                }
                
                public boolean add(final Object o) {
                    if (!this.contains(o)) {
                        this.ensureCapacity(this.count + 1);
                        this.elements[this.count++] = (Structure)o;
                    }
                    return true;
                }
                
                private int indexOf(final Object o) {
                    final Structure s1 = (Structure)o;
                    for (int i = 0; i < this.count; ++i) {
                        final Structure s2 = this.elements[i];
                        if (s1 == s2 || (s1.getClass() == s2.getClass() && s1.size() == s2.size() && s1.getPointer().equals(s2.getPointer()))) {
                            return i;
                        }
                    }
                    return -1;
                }
                
                public boolean remove(final Object o) {
                    final int idx = this.indexOf(o);
                    if (idx != -1) {
                        if (--this.count > 0) {
                            this.elements[idx] = this.elements[this.count];
                            this.elements[this.count] = null;
                        }
                        return true;
                    }
                    return false;
                }
                
                public Iterator iterator() {
                    final Structure[] e = new Structure[this.count];
                    if (this.count > 0) {
                        System.arraycopy(this.elements, 0, e, 0, this.count);
                    }
                    return Arrays.asList(e).iterator();
                }
            }
        };
    }
    
    private static class MemberOrder
    {
        private static final String[] FIELDS;
        public int first;
        public int second;
        public int middle;
        public int penultimate;
        public int last;
        
        static {
            FIELDS = new String[] { "first", "second", "middle", "penultimate", "last" };
        }
    }
    
    private class LayoutInfo
    {
        int size;
        int alignment;
        Map fields;
        int alignType;
        TypeMapper typeMapper;
        List fieldOrder;
        boolean variable;
        
        private LayoutInfo() {
            this.size = -1;
            this.alignment = 1;
            this.fields = Collections.synchronizedMap(new LinkedHashMap<Object, Object>());
            this.alignType = 0;
        }
    }
    
    class StructField
    {
        public String name;
        public Class type;
        public Field field;
        public int size;
        public int offset;
        public boolean isVolatile;
        public boolean isReadOnly;
        public FromNativeConverter readConverter;
        public ToNativeConverter writeConverter;
        public FromNativeContext context;
        
        StructField() {
            this.size = -1;
            this.offset = -1;
        }
        
        public String toString() {
            return this.name + "@" + this.offset + "[" + this.size + "] (" + this.type + ")";
        }
    }
    
    static class FFIType extends Structure
    {
        private static Map typeInfoMap;
        private static final int FFI_TYPE_STRUCT = 13;
        public size_t size;
        public short alignment;
        public short type;
        public Pointer elements;
        
        private FFIType(final Structure ref) {
            this.type = 13;
            ref.ensureAllocated(true);
            Pointer[] els;
            if (ref instanceof Union) {
                final StructField sf = ((Union)ref).biggestField;
                els = new Pointer[] { get(ref.getField(sf), sf.type), null };
            }
            else {
                els = new Pointer[ref.fields().size() + 1];
                int idx = 0;
                for (final StructField sf2 : ref.fields().values()) {
                    els[idx++] = ref.getFieldTypeInfo(sf2);
                }
            }
            this.init(els);
        }
        
        private FFIType(final Object array, final Class type) {
            this.type = 13;
            final int length = Array.getLength(array);
            final Pointer[] els = new Pointer[length + 1];
            final Pointer p = get(null, type.getComponentType());
            for (int i = 0; i < length; ++i) {
                els[i] = p;
            }
            this.init(els);
        }
        
        private void init(final Pointer[] els) {
            (this.elements = new Memory(Pointer.SIZE * els.length)).write(0L, els, 0, els.length);
            this.write();
        }
        
        static Pointer get(final Object obj) {
            if (obj == null) {
                return FFITypes.ffi_type_pointer;
            }
            if (obj instanceof Class) {
                return get(null, (Class)obj);
            }
            return get(obj, obj.getClass());
        }
        
        private static Pointer get(Object obj, Class cls) {
            final TypeMapper mapper = Native.getTypeMapper(cls);
            if (mapper != null) {
                final ToNativeConverter nc = mapper.getToNativeConverter(cls);
                if (nc != null) {
                    cls = nc.nativeType();
                }
            }
            synchronized (FFIType.typeInfoMap) {
                final Object o = FFIType.typeInfoMap.get(cls);
                if (o instanceof Pointer) {
                    return (Pointer)o;
                }
                if (o instanceof FFIType) {
                    return ((FFIType)o).getPointer();
                }
                if ((Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(cls)) || Callback.class.isAssignableFrom(cls)) {
                    FFIType.typeInfoMap.put(cls, FFITypes.ffi_type_pointer);
                    return FFITypes.ffi_type_pointer;
                }
                if (Structure.class.isAssignableFrom(cls)) {
                    if (obj == null) {
                        obj = Structure.newInstance(cls);
                    }
                    if (ByReference.class.isAssignableFrom(cls)) {
                        FFIType.typeInfoMap.put(cls, FFITypes.ffi_type_pointer);
                        return FFITypes.ffi_type_pointer;
                    }
                    final FFIType type = new FFIType((Structure)obj);
                    FFIType.typeInfoMap.put(cls, type);
                    return type.getPointer();
                }
                else {
                    if (NativeMapped.class.isAssignableFrom(cls)) {
                        final NativeMappedConverter c = NativeMappedConverter.getInstance(cls);
                        return get(c.toNative(obj, new ToNativeContext()), c.nativeType());
                    }
                    if (cls.isArray()) {
                        final FFIType type = new FFIType(obj, cls);
                        FFIType.typeInfoMap.put(obj, type);
                        return type.getPointer();
                    }
                    throw new IllegalArgumentException("Unsupported Structure field type " + cls);
                }
            }
        }
        
        static {
            FFIType.typeInfoMap = new WeakHashMap();
            if (Native.POINTER_SIZE == 0) {
                throw new Error("Native library not initialized");
            }
            if (FFITypes.ffi_type_void == null) {
                throw new Error("FFI types not initialized");
            }
            FFIType.typeInfoMap.put(Void.TYPE, FFITypes.ffi_type_void);
            FFIType.typeInfoMap.put(Void.class, FFITypes.ffi_type_void);
            FFIType.typeInfoMap.put(Float.TYPE, FFITypes.ffi_type_float);
            FFIType.typeInfoMap.put(Float.class, FFITypes.ffi_type_float);
            FFIType.typeInfoMap.put(Double.TYPE, FFITypes.ffi_type_double);
            FFIType.typeInfoMap.put(Double.class, FFITypes.ffi_type_double);
            FFIType.typeInfoMap.put(Long.TYPE, FFITypes.ffi_type_sint64);
            FFIType.typeInfoMap.put(Long.class, FFITypes.ffi_type_sint64);
            FFIType.typeInfoMap.put(Integer.TYPE, FFITypes.ffi_type_sint32);
            FFIType.typeInfoMap.put(Integer.class, FFITypes.ffi_type_sint32);
            FFIType.typeInfoMap.put(Short.TYPE, FFITypes.ffi_type_sint16);
            FFIType.typeInfoMap.put(Short.class, FFITypes.ffi_type_sint16);
            final Pointer ctype = (Native.WCHAR_SIZE == 2) ? FFITypes.ffi_type_uint16 : FFITypes.ffi_type_uint32;
            FFIType.typeInfoMap.put(Character.TYPE, ctype);
            FFIType.typeInfoMap.put(Character.class, ctype);
            FFIType.typeInfoMap.put(Byte.TYPE, FFITypes.ffi_type_sint8);
            FFIType.typeInfoMap.put(Byte.class, FFITypes.ffi_type_sint8);
            FFIType.typeInfoMap.put(Pointer.class, FFITypes.ffi_type_pointer);
            FFIType.typeInfoMap.put(String.class, FFITypes.ffi_type_pointer);
            FFIType.typeInfoMap.put(WString.class, FFITypes.ffi_type_pointer);
            FFIType.typeInfoMap.put(Boolean.TYPE, FFITypes.ffi_type_uint32);
            FFIType.typeInfoMap.put(Boolean.class, FFITypes.ffi_type_uint32);
        }
        
        public static class size_t extends IntegerType
        {
            public size_t() {
                this(0L);
            }
            
            public size_t(final long value) {
                super(Native.POINTER_SIZE, value);
            }
        }
        
        private static class FFITypes
        {
            private static Pointer ffi_type_void;
            private static Pointer ffi_type_float;
            private static Pointer ffi_type_double;
            private static Pointer ffi_type_longdouble;
            private static Pointer ffi_type_uint8;
            private static Pointer ffi_type_sint8;
            private static Pointer ffi_type_uint16;
            private static Pointer ffi_type_sint16;
            private static Pointer ffi_type_uint32;
            private static Pointer ffi_type_sint32;
            private static Pointer ffi_type_uint64;
            private static Pointer ffi_type_sint64;
            private static Pointer ffi_type_pointer;
        }
    }
    
    private class AutoAllocated extends Memory
    {
        public AutoAllocated(final int size) {
            super(size);
            super.clear();
        }
    }
    
    public interface ByReference
    {
    }
    
    public interface ByValue
    {
    }
}
