// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.TreeSet;
import java.util.Set;
import com.ibm.icu.util.UResourceTypeMismatchException;
import com.ibm.icu.util.UResourceBundleIterator;
import java.nio.ByteBuffer;
import com.ibm.icu.util.UResourceBundle;
import java.util.HashMap;

class ICUResourceBundleImpl extends ICUResourceBundle
{
    protected ICUResourceBundleImpl(final ICUResourceBundleReader reader, final String key, final String resPath, final int resource, final ICUResourceBundleImpl container) {
        super(reader, key, resPath, resource, container);
    }
    
    protected final ICUResourceBundle createBundleObject(final String _key, final int _resource, final HashMap<String, String> table, final UResourceBundle requested, final boolean[] isAlias) {
        if (isAlias != null) {
            isAlias[0] = false;
        }
        final String _resPath = this.resPath + "/" + _key;
        switch (ICUResourceBundleReader.RES_GET_TYPE(_resource)) {
            case 0:
            case 6: {
                return new ResourceString(this.reader, _key, _resPath, _resource, this);
            }
            case 1: {
                return new ResourceBinary(this.reader, _key, _resPath, _resource, this);
            }
            case 3: {
                if (isAlias != null) {
                    isAlias[0] = true;
                }
                return this.findResource(_key, _resPath, _resource, table, requested);
            }
            case 7: {
                return new ResourceInt(this.reader, _key, _resPath, _resource, this);
            }
            case 14: {
                return new ResourceIntVector(this.reader, _key, _resPath, _resource, this);
            }
            case 8:
            case 9: {
                return new ResourceArray(this.reader, _key, _resPath, _resource, this);
            }
            case 2:
            case 4:
            case 5: {
                return new ResourceTable(this.reader, _key, _resPath, _resource, this);
            }
            default: {
                throw new IllegalStateException("The resource type is unknown");
            }
        }
    }
    
    private static final class ResourceBinary extends ICUResourceBundleImpl
    {
        @Override
        public ByteBuffer getBinary() {
            return this.reader.getBinary(this.resource);
        }
        
        @Override
        public byte[] getBinary(final byte[] ba) {
            return this.reader.getBinary(this.resource, ba);
        }
        
        ResourceBinary(final ICUResourceBundleReader reader, final String key, final String resPath, final int resource, final ICUResourceBundleImpl container) {
            super(reader, key, resPath, resource, container);
        }
    }
    
    private static final class ResourceInt extends ICUResourceBundleImpl
    {
        @Override
        public int getInt() {
            return ICUResourceBundleReader.RES_GET_INT(this.resource);
        }
        
        @Override
        public int getUInt() {
            return ICUResourceBundleReader.RES_GET_UINT(this.resource);
        }
        
        ResourceInt(final ICUResourceBundleReader reader, final String key, final String resPath, final int resource, final ICUResourceBundleImpl container) {
            super(reader, key, resPath, resource, container);
        }
    }
    
    private static final class ResourceString extends ICUResourceBundleImpl
    {
        private String value;
        
        @Override
        public String getString() {
            return this.value;
        }
        
        ResourceString(final ICUResourceBundleReader reader, final String key, final String resPath, final int resource, final ICUResourceBundleImpl container) {
            super(reader, key, resPath, resource, container);
            this.value = reader.getString(resource);
        }
    }
    
    private static final class ResourceIntVector extends ICUResourceBundleImpl
    {
        private int[] value;
        
        @Override
        public int[] getIntVector() {
            return this.value;
        }
        
        ResourceIntVector(final ICUResourceBundleReader reader, final String key, final String resPath, final int resource, final ICUResourceBundleImpl container) {
            super(reader, key, resPath, resource, container);
            this.value = reader.getIntVector(resource);
        }
    }
    
    private static class ResourceContainer extends ICUResourceBundleImpl
    {
        protected ICUResourceBundleReader.Container value;
        
        @Override
        public int getSize() {
            return this.value.getSize();
        }
        
        protected int getContainerResource(final int index) {
            return this.value.getContainerResource(index);
        }
        
        protected UResourceBundle createBundleObject(final int index, final String resKey, final HashMap<String, String> table, final UResourceBundle requested, final boolean[] isAlias) {
            final int item = this.getContainerResource(index);
            if (item == -1) {
                throw new IndexOutOfBoundsException();
            }
            return this.createBundleObject(resKey, item, table, requested, isAlias);
        }
        
        ResourceContainer(final ICUResourceBundleReader reader, final String key, final String resPath, final int resource, final ICUResourceBundleImpl container) {
            super(reader, key, resPath, resource, container);
        }
    }
    
    private static class ResourceArray extends ResourceContainer
    {
        @Override
        protected String[] handleGetStringArray() {
            final String[] strings = new String[this.value.getSize()];
            final UResourceBundleIterator iter = this.getIterator();
            int i = 0;
            while (iter.hasNext()) {
                strings[i++] = iter.next().getString();
            }
            return strings;
        }
        
        @Override
        public String[] getStringArray() {
            return this.handleGetStringArray();
        }
        
        @Override
        protected UResourceBundle handleGetImpl(final String indexStr, final HashMap<String, String> table, final UResourceBundle requested, final int[] index, final boolean[] isAlias) {
            final int i = (indexStr.length() > 0) ? Integer.valueOf(indexStr) : -1;
            if (index != null) {
                index[0] = i;
            }
            if (i < 0) {
                throw new UResourceTypeMismatchException("Could not get the correct value for index: " + indexStr);
            }
            return this.createBundleObject(i, indexStr, table, requested, isAlias);
        }
        
        @Override
        protected UResourceBundle handleGetImpl(final int index, final HashMap<String, String> table, final UResourceBundle requested, final boolean[] isAlias) {
            return this.createBundleObject(index, Integer.toString(index), table, requested, isAlias);
        }
        
        ResourceArray(final ICUResourceBundleReader reader, final String key, final String resPath, final int resource, final ICUResourceBundleImpl container) {
            super(reader, key, resPath, resource, container);
            this.value = reader.getArray(resource);
            this.createLookupCache();
        }
    }
    
    static class ResourceTable extends ResourceContainer
    {
        @Override
        protected String getKey(final int index) {
            return ((ICUResourceBundleReader.Table)this.value).getKey(index);
        }
        
        @Override
        protected Set<String> handleKeySet() {
            final TreeSet<String> keySet = new TreeSet<String>();
            final ICUResourceBundleReader.Table table = (ICUResourceBundleReader.Table)this.value;
            for (int i = 0; i < table.getSize(); ++i) {
                keySet.add(table.getKey(i));
            }
            return keySet;
        }
        
        @Override
        protected int getTableResource(final String resKey) {
            return ((ICUResourceBundleReader.Table)this.value).getTableResource(resKey);
        }
        
        @Override
        protected int getTableResource(final int index) {
            return this.getContainerResource(index);
        }
        
        @Override
        protected UResourceBundle handleGetImpl(final String resKey, final HashMap<String, String> table, final UResourceBundle requested, final int[] index, final boolean[] isAlias) {
            final int i = ((ICUResourceBundleReader.Table)this.value).findTableItem(resKey);
            if (index != null) {
                index[0] = i;
            }
            if (i < 0) {
                return null;
            }
            return this.createBundleObject(i, resKey, table, requested, isAlias);
        }
        
        @Override
        protected UResourceBundle handleGetImpl(final int index, final HashMap<String, String> table, final UResourceBundle requested, final boolean[] isAlias) {
            final String itemKey = ((ICUResourceBundleReader.Table)this.value).getKey(index);
            if (itemKey == null) {
                throw new IndexOutOfBoundsException();
            }
            return this.createBundleObject(index, itemKey, table, requested, isAlias);
        }
        
        ResourceTable(final ICUResourceBundleReader reader, final String key, final String resPath, final int resource, final ICUResourceBundleImpl container) {
            super(reader, key, resPath, resource, container);
            this.value = reader.getTable(resource);
            this.createLookupCache();
        }
    }
}
