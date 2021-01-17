// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.TreeSet;
import com.ibm.icu.util.TimeZone;

public class JavaTimeZone extends TimeZone
{
    private static final long serialVersionUID = 6977448185543929364L;
    private static final TreeSet<String> AVAILABLESET;
    private java.util.TimeZone javatz;
    private transient Calendar javacal;
    private static Method mObservesDaylightTime;
    private transient boolean isFrozen;
    
    public JavaTimeZone() {
        this(java.util.TimeZone.getDefault(), null);
    }
    
    public JavaTimeZone(final java.util.TimeZone jtz, String id) {
        this.isFrozen = false;
        if (id == null) {
            id = jtz.getID();
        }
        this.javatz = jtz;
        this.setID(id);
        this.javacal = new GregorianCalendar(this.javatz);
    }
    
    public static JavaTimeZone createTimeZone(final String id) {
        java.util.TimeZone jtz = null;
        if (JavaTimeZone.AVAILABLESET.contains(id)) {
            jtz = java.util.TimeZone.getTimeZone(id);
        }
        if (jtz == null) {
            final boolean[] isSystemID = { false };
            final String canonicalID = TimeZone.getCanonicalID(id, isSystemID);
            if (isSystemID[0] && JavaTimeZone.AVAILABLESET.contains(canonicalID)) {
                jtz = java.util.TimeZone.getTimeZone(canonicalID);
            }
        }
        if (jtz == null) {
            return null;
        }
        return new JavaTimeZone(jtz, id);
    }
    
    @Override
    public int getOffset(final int era, final int year, final int month, final int day, final int dayOfWeek, final int milliseconds) {
        return this.javatz.getOffset(era, year, month, day, dayOfWeek, milliseconds);
    }
    
    @Override
    public void getOffset(final long date, final boolean local, final int[] offsets) {
        synchronized (this.javacal) {
            if (local) {
                final int[] fields = new int[6];
                Grego.timeToFields(date, fields);
                int tmp = fields[5];
                final int mil = tmp % 1000;
                tmp /= 1000;
                final int sec = tmp % 60;
                tmp /= 60;
                final int min = tmp % 60;
                final int hour = tmp / 60;
                this.javacal.clear();
                this.javacal.set(fields[0], fields[1], fields[2], hour, min, sec);
                this.javacal.set(14, mil);
                final int doy1 = this.javacal.get(6);
                final int hour2 = this.javacal.get(11);
                final int min2 = this.javacal.get(12);
                final int sec2 = this.javacal.get(13);
                final int mil2 = this.javacal.get(14);
                if (fields[4] != doy1 || hour != hour2 || min != min2 || sec != sec2 || mil != mil2) {
                    final int dayDelta = (Math.abs(doy1 - fields[4]) > 1) ? 1 : (doy1 - fields[4]);
                    final int delta = (((dayDelta * 24 + hour2 - hour) * 60 + min2 - min) * 60 + sec2 - sec) * 1000 + mil2 - mil;
                    this.javacal.setTimeInMillis(this.javacal.getTimeInMillis() - delta - 1L);
                }
            }
            else {
                this.javacal.setTimeInMillis(date);
            }
            offsets[0] = this.javacal.get(15);
            offsets[1] = this.javacal.get(16);
        }
    }
    
    @Override
    public int getRawOffset() {
        return this.javatz.getRawOffset();
    }
    
    @Override
    public boolean inDaylightTime(final Date date) {
        return this.javatz.inDaylightTime(date);
    }
    
    @Override
    public void setRawOffset(final int offsetMillis) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen JavaTimeZone instance.");
        }
        this.javatz.setRawOffset(offsetMillis);
    }
    
    @Override
    public boolean useDaylightTime() {
        return this.javatz.useDaylightTime();
    }
    
    @Override
    public boolean observesDaylightTime() {
        if (JavaTimeZone.mObservesDaylightTime != null) {
            try {
                return (boolean)JavaTimeZone.mObservesDaylightTime.invoke(this.javatz, (Object[])null);
            }
            catch (IllegalAccessException e) {}
            catch (IllegalArgumentException e2) {}
            catch (InvocationTargetException ex) {}
        }
        return super.observesDaylightTime();
    }
    
    @Override
    public int getDSTSavings() {
        return this.javatz.getDSTSavings();
    }
    
    public java.util.TimeZone unwrap() {
        return this.javatz;
    }
    
    @Override
    public Object clone() {
        if (this.isFrozen()) {
            return this;
        }
        return this.cloneAsThawed();
    }
    
    @Override
    public int hashCode() {
        return super.hashCode() + this.javatz.hashCode();
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.javacal = new GregorianCalendar(this.javatz);
    }
    
    @Override
    public boolean isFrozen() {
        return this.isFrozen;
    }
    
    @Override
    public TimeZone freeze() {
        this.isFrozen = true;
        return this;
    }
    
    @Override
    public TimeZone cloneAsThawed() {
        final JavaTimeZone tz = (JavaTimeZone)super.cloneAsThawed();
        tz.javatz = (java.util.TimeZone)this.javatz.clone();
        tz.javacal = (GregorianCalendar)this.javacal.clone();
        tz.isFrozen = false;
        return tz;
    }
    
    static {
        AVAILABLESET = new TreeSet<String>();
        final String[] availableIds = java.util.TimeZone.getAvailableIDs();
        for (int i = 0; i < availableIds.length; ++i) {
            JavaTimeZone.AVAILABLESET.add(availableIds[i]);
        }
        try {
            JavaTimeZone.mObservesDaylightTime = java.util.TimeZone.class.getMethod("observesDaylightTime", (Class<?>[])null);
        }
        catch (NoSuchMethodException e) {}
        catch (SecurityException ex) {}
    }
}
