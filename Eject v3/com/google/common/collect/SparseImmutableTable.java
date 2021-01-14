package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

import javax.annotation.concurrent.Immutable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

@GwtCompatible
@Immutable
final class SparseImmutableTable<R, C, V>
        extends RegularImmutableTable<R, C, V> {
    private final ImmutableMap<R, Map<C, V>> rowMap;
    private final ImmutableMap<C, Map<R, V>> columnMap;
    private final int[] iterationOrderRow;
    private final int[] iterationOrderColumn;

    SparseImmutableTable(ImmutableList<Table.Cell<R, C, V>> paramImmutableList, ImmutableSet<R> paramImmutableSet, ImmutableSet<C> paramImmutableSet1) {
        HashMap localHashMap = Maps.newHashMap();
        LinkedHashMap localLinkedHashMap = Maps.newLinkedHashMap();
        Object localObject1 = paramImmutableSet.iterator();
        while (((Iterator) localObject1).hasNext()) {
            localObject2 = ((Iterator) localObject1).next();
            localHashMap.put(localObject2, Integer.valueOf(localLinkedHashMap.size()));
            localLinkedHashMap.put(localObject2, new LinkedHashMap());
        }
        localObject1 = Maps.newLinkedHashMap();
        Object localObject2 = paramImmutableSet1.iterator();
        while (((Iterator) localObject2).hasNext()) {
            localObject3 = ((Iterator) localObject2).next();
            ((Map) localObject1).put(localObject3, new LinkedHashMap());
        }
        localObject2 = new int[paramImmutableList.size()];
        Object localObject3 = new int[paramImmutableList.size()];
        Object localObject6;
        for (int i = 0; i < paramImmutableList.size(); i++) {
            localObject4 = (Table.Cell) paramImmutableList.get(i);
            localObject5 = ((Table.Cell) localObject4).getRowKey();
            localObject6 = ((Table.Cell) localObject4).getColumnKey();
            Object localObject7 = ((Table.Cell) localObject4).getValue();
            localObject2[i] = ((Integer) localHashMap.get(localObject5)).intValue();
            Map localMap = (Map) localLinkedHashMap.get(localObject5);
            localObject3[i] = localMap.size();
            Object localObject8 = localMap.put(localObject6, localObject7);
            if (localObject8 != null) {
                throw new IllegalArgumentException("Duplicate value for row=" + localObject5 + ", column=" + localObject6 + ": " + localObject7 + ", " + localObject8);
            }
            ((Map) ((Map) localObject1).get(localObject6)).put(localObject5, localObject7);
        }
        this.iterationOrderRow = ((int[]) localObject2);
        this.iterationOrderColumn = ((int[]) localObject3);
        ImmutableMap.Builder localBuilder = ImmutableMap.builder();
        Object localObject4 = localLinkedHashMap.entrySet().iterator();
        while (((Iterator) localObject4).hasNext()) {
            localObject5 = (Map.Entry) ((Iterator) localObject4).next();
            localBuilder.put(((Map.Entry) localObject5).getKey(), ImmutableMap.copyOf((Map) ((Map.Entry) localObject5).getValue()));
        }
        this.rowMap = localBuilder.build();
        localObject4 = ImmutableMap.builder();
        Object localObject5 = ((Map) localObject1).entrySet().iterator();
        while (((Iterator) localObject5).hasNext()) {
            localObject6 = (Map.Entry) ((Iterator) localObject5).next();
            ((ImmutableMap.Builder) localObject4).put(((Map.Entry) localObject6).getKey(), ImmutableMap.copyOf((Map) ((Map.Entry) localObject6).getValue()));
        }
        this.columnMap = ((ImmutableMap.Builder) localObject4).build();
    }

    public ImmutableMap<C, Map<R, V>> columnMap() {
        return this.columnMap;
    }

    public ImmutableMap<R, Map<C, V>> rowMap() {
        return this.rowMap;
    }

    public int size() {
        return this.iterationOrderRow.length;
    }

    Table.Cell<R, C, V> getCell(int paramInt) {
        int i = this.iterationOrderRow[paramInt];
        Map.Entry localEntry1 = (Map.Entry) this.rowMap.entrySet().asList().get(i);
        ImmutableMap localImmutableMap = (ImmutableMap) localEntry1.getValue();
        int j = this.iterationOrderColumn[paramInt];
        Map.Entry localEntry2 = (Map.Entry) localImmutableMap.entrySet().asList().get(j);
        return cellOf(localEntry1.getKey(), localEntry2.getKey(), localEntry2.getValue());
    }

    V getValue(int paramInt) {
        int i = this.iterationOrderRow[paramInt];
        ImmutableMap localImmutableMap = (ImmutableMap) this.rowMap.values().asList().get(i);
        int j = this.iterationOrderColumn[paramInt];
        return (V) localImmutableMap.values().asList().get(j);
    }
}




