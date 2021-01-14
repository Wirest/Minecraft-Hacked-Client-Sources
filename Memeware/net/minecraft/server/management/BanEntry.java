package net.minecraft.server.management;

import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BanEntry extends UserListEntry {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    protected final Date banStartDate;
    protected final String bannedBy;
    protected final Date banEndDate;
    protected final String reason;
    private static final String __OBFID = "CL_00001395";

    public BanEntry(Object p_i46334_1_, Date p_i46334_2_, String p_i46334_3_, Date p_i46334_4_, String p_i46334_5_) {
        super(p_i46334_1_);
        this.banStartDate = p_i46334_2_ == null ? new Date() : p_i46334_2_;
        this.bannedBy = p_i46334_3_ == null ? "(Unknown)" : p_i46334_3_;
        this.banEndDate = p_i46334_4_;
        this.reason = p_i46334_5_ == null ? "Banned by an operator." : p_i46334_5_;
    }

    protected BanEntry(Object p_i1174_1_, JsonObject p_i1174_2_) {
        super(p_i1174_1_, p_i1174_2_);
        Date var3;

        try {
            var3 = p_i1174_2_.has("created") ? dateFormat.parse(p_i1174_2_.get("created").getAsString()) : new Date();
        } catch (ParseException var7) {
            var3 = new Date();
        }

        this.banStartDate = var3;
        this.bannedBy = p_i1174_2_.has("source") ? p_i1174_2_.get("source").getAsString() : "(Unknown)";
        Date var4;

        try {
            var4 = p_i1174_2_.has("expires") ? dateFormat.parse(p_i1174_2_.get("expires").getAsString()) : null;
        } catch (ParseException var6) {
            var4 = null;
        }

        this.banEndDate = var4;
        this.reason = p_i1174_2_.has("reason") ? p_i1174_2_.get("reason").getAsString() : "Banned by an operator.";
    }

    public Date getBanEndDate() {
        return this.banEndDate;
    }

    public String getBanReason() {
        return this.reason;
    }

    boolean hasBanExpired() {
        return this.banEndDate == null ? false : this.banEndDate.before(new Date());
    }

    protected void onSerialization(JsonObject data) {
        data.addProperty("created", dateFormat.format(this.banStartDate));
        data.addProperty("source", this.bannedBy);
        data.addProperty("expires", this.banEndDate == null ? "forever" : dateFormat.format(this.banEndDate));
        data.addProperty("reason", this.reason);
    }
}
