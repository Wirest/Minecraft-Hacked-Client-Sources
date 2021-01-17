// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.management;

import java.text.ParseException;
import com.google.gson.JsonObject;
import java.util.Date;
import java.text.SimpleDateFormat;

public abstract class BanEntry<T> extends UserListEntry<T>
{
    public static final SimpleDateFormat dateFormat;
    protected final Date banStartDate;
    protected final String bannedBy;
    protected final Date banEndDate;
    protected final String reason;
    
    static {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    }
    
    public BanEntry(final T valueIn, final Date startDate, final String banner, final Date endDate, final String banReason) {
        super(valueIn);
        this.banStartDate = ((startDate == null) ? new Date() : startDate);
        this.bannedBy = ((banner == null) ? "(Unknown)" : banner);
        this.banEndDate = endDate;
        this.reason = ((banReason == null) ? "Banned by an operator." : banReason);
    }
    
    protected BanEntry(final T p_i1174_1_, final JsonObject p_i1174_2_) {
        super(p_i1174_1_, p_i1174_2_);
        Date date;
        try {
            date = (p_i1174_2_.has("created") ? BanEntry.dateFormat.parse(p_i1174_2_.get("created").getAsString()) : new Date());
        }
        catch (ParseException var7) {
            date = new Date();
        }
        this.banStartDate = date;
        this.bannedBy = (p_i1174_2_.has("source") ? p_i1174_2_.get("source").getAsString() : "(Unknown)");
        Date date2;
        try {
            date2 = (p_i1174_2_.has("expires") ? BanEntry.dateFormat.parse(p_i1174_2_.get("expires").getAsString()) : null);
        }
        catch (ParseException var8) {
            date2 = null;
        }
        this.banEndDate = date2;
        this.reason = (p_i1174_2_.has("reason") ? p_i1174_2_.get("reason").getAsString() : "Banned by an operator.");
    }
    
    public Date getBanEndDate() {
        return this.banEndDate;
    }
    
    public String getBanReason() {
        return this.reason;
    }
    
    @Override
    boolean hasBanExpired() {
        return this.banEndDate != null && this.banEndDate.before(new Date());
    }
    
    @Override
    protected void onSerialization(final JsonObject data) {
        data.addProperty("created", BanEntry.dateFormat.format(this.banStartDate));
        data.addProperty("source", this.bannedBy);
        data.addProperty("expires", (this.banEndDate == null) ? "forever" : BanEntry.dateFormat.format(this.banEndDate));
        data.addProperty("reason", this.reason);
    }
}
