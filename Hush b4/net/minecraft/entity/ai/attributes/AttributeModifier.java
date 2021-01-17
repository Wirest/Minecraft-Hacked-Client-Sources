// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai.attributes;

import org.apache.commons.lang3.Validate;
import java.util.Random;
import net.minecraft.util.MathHelper;
import io.netty.util.internal.ThreadLocalRandom;
import java.util.UUID;

public class AttributeModifier
{
    private final double amount;
    private final int operation;
    private final String name;
    private final UUID id;
    private boolean isSaved;
    
    public AttributeModifier(final String nameIn, final double amountIn, final int operationIn) {
        this(MathHelper.getRandomUuid(ThreadLocalRandom.current()), nameIn, amountIn, operationIn);
    }
    
    public AttributeModifier(final UUID idIn, final String nameIn, final double amountIn, final int operationIn) {
        this.isSaved = true;
        this.id = idIn;
        this.name = nameIn;
        this.amount = amountIn;
        this.operation = operationIn;
        Validate.notEmpty(nameIn, "Modifier name cannot be empty", new Object[0]);
        Validate.inclusiveBetween(0L, 2L, operationIn, "Invalid operation");
    }
    
    public UUID getID() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getOperation() {
        return this.operation;
    }
    
    public double getAmount() {
        return this.amount;
    }
    
    public boolean isSaved() {
        return this.isSaved;
    }
    
    public AttributeModifier setSaved(final boolean saved) {
        this.isSaved = saved;
        return this;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            final AttributeModifier attributemodifier = (AttributeModifier)p_equals_1_;
            if (this.id != null) {
                if (!this.id.equals(attributemodifier.id)) {
                    return false;
                }
            }
            else if (attributemodifier.id != null) {
                return false;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (this.id != null) ? this.id.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "AttributeModifier{amount=" + this.amount + ", operation=" + this.operation + ", name='" + this.name + '\'' + ", id=" + this.id + ", serialize=" + this.isSaved + '}';
    }
}
