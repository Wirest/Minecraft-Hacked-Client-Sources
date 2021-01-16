/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.irc.pircBot;

public class User {
    public int timer;
    public int messages;
    public boolean muted;
    private String _prefix;
    private String _nick;
    private String _lowerNick;

    User(String prefix, String nick) {
        this._prefix = prefix;
        this._nick = nick;
        this._lowerNick = nick.toLowerCase();
    }

    public String getPrefix() {
        return this._prefix;
    }

    public boolean isOp() {
        if (this._prefix.indexOf(64) >= 0) {
            return true;
        }
        return false;
    }

    public boolean hasVoice() {
        if (this._prefix.indexOf(43) >= 0) {
            return true;
        }
        return false;
    }

    public String getNick() {
        return this._nick;
    }

    public String toString() {
        return String.valueOf(String.valueOf(this.getPrefix())) + this.getNick();
    }

    public boolean equals(String nick) {
        return nick.toLowerCase().equals(this._lowerNick);
    }

    public boolean equals(Object o) {
        if (o instanceof User) {
            User other = (User)o;
            return other._lowerNick.equals(this._lowerNick);
        }
        return false;
    }

    public int hashCode() {
        return this._lowerNick.hashCode();
    }

    public int compareTo(Object o) {
        if (o instanceof User) {
            User other = (User)o;
            return other._lowerNick.compareTo(this._lowerNick);
        }
        return -1;
    }
}

