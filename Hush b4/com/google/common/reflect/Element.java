// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.reflect;

import javax.annotation.Nullable;
import java.lang.reflect.Modifier;
import java.lang.annotation.Annotation;
import com.google.common.base.Preconditions;
import java.lang.reflect.Member;
import java.lang.reflect.AccessibleObject;

class Element extends AccessibleObject implements Member
{
    private final AccessibleObject accessibleObject;
    private final Member member;
    
     <M extends java.lang.reflect.AccessibleObject> Element(final M member) {
        Preconditions.checkNotNull(member);
        this.accessibleObject = (AccessibleObject)member;
        this.member = (Member)member;
    }
    
    public TypeToken<?> getOwnerType() {
        return TypeToken.of(this.getDeclaringClass());
    }
    
    @Override
    public final boolean isAnnotationPresent(final Class<? extends Annotation> annotationClass) {
        return this.accessibleObject.isAnnotationPresent(annotationClass);
    }
    
    @Override
    public final <A extends Annotation> A getAnnotation(final Class<A> annotationClass) {
        return this.accessibleObject.getAnnotation(annotationClass);
    }
    
    @Override
    public final Annotation[] getAnnotations() {
        return this.accessibleObject.getAnnotations();
    }
    
    @Override
    public final Annotation[] getDeclaredAnnotations() {
        return this.accessibleObject.getDeclaredAnnotations();
    }
    
    @Override
    public final void setAccessible(final boolean flag) throws SecurityException {
        this.accessibleObject.setAccessible(flag);
    }
    
    @Override
    public final boolean isAccessible() {
        return this.accessibleObject.isAccessible();
    }
    
    @Override
    public Class<?> getDeclaringClass() {
        return this.member.getDeclaringClass();
    }
    
    @Override
    public final String getName() {
        return this.member.getName();
    }
    
    @Override
    public final int getModifiers() {
        return this.member.getModifiers();
    }
    
    @Override
    public final boolean isSynthetic() {
        return this.member.isSynthetic();
    }
    
    public final boolean isPublic() {
        return Modifier.isPublic(this.getModifiers());
    }
    
    public final boolean isProtected() {
        return Modifier.isProtected(this.getModifiers());
    }
    
    public final boolean isPackagePrivate() {
        return !this.isPrivate() && !this.isPublic() && !this.isProtected();
    }
    
    public final boolean isPrivate() {
        return Modifier.isPrivate(this.getModifiers());
    }
    
    public final boolean isStatic() {
        return Modifier.isStatic(this.getModifiers());
    }
    
    public final boolean isFinal() {
        return Modifier.isFinal(this.getModifiers());
    }
    
    public final boolean isAbstract() {
        return Modifier.isAbstract(this.getModifiers());
    }
    
    public final boolean isNative() {
        return Modifier.isNative(this.getModifiers());
    }
    
    public final boolean isSynchronized() {
        return Modifier.isSynchronized(this.getModifiers());
    }
    
    final boolean isVolatile() {
        return Modifier.isVolatile(this.getModifiers());
    }
    
    final boolean isTransient() {
        return Modifier.isTransient(this.getModifiers());
    }
    
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj instanceof Element) {
            final Element that = (Element)obj;
            return this.getOwnerType().equals(that.getOwnerType()) && this.member.equals(that.member);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.member.hashCode();
    }
    
    @Override
    public String toString() {
        return this.member.toString();
    }
}
