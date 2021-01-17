// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import java.util.ArrayList;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public abstract class Netapi32Util
{
    public static String getDCName() {
        return getDCName(null, null);
    }
    
    public static String getDCName(final String serverName, final String domainName) {
        final PointerByReference bufptr = new PointerByReference();
        try {
            final int rc = Netapi32.INSTANCE.NetGetDCName(domainName, serverName, bufptr);
            if (0 != rc) {
                throw new Win32Exception(rc);
            }
            return bufptr.getValue().getString(0L, true);
        }
        finally {
            if (0 != Netapi32.INSTANCE.NetApiBufferFree(bufptr.getValue())) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
        }
    }
    
    public static int getJoinStatus() {
        return getJoinStatus(null);
    }
    
    public static int getJoinStatus(final String computerName) {
        final PointerByReference lpNameBuffer = new PointerByReference();
        final IntByReference bufferType = new IntByReference();
        try {
            final int rc = Netapi32.INSTANCE.NetGetJoinInformation(computerName, lpNameBuffer, bufferType);
            if (0 != rc) {
                throw new Win32Exception(rc);
            }
            return bufferType.getValue();
        }
        finally {
            if (lpNameBuffer.getPointer() != null) {
                final int rc2 = Netapi32.INSTANCE.NetApiBufferFree(lpNameBuffer.getValue());
                if (0 != rc2) {
                    throw new Win32Exception(rc2);
                }
            }
        }
    }
    
    public static String getDomainName(final String computerName) {
        final PointerByReference lpNameBuffer = new PointerByReference();
        final IntByReference bufferType = new IntByReference();
        try {
            final int rc = Netapi32.INSTANCE.NetGetJoinInformation(computerName, lpNameBuffer, bufferType);
            if (0 != rc) {
                throw new Win32Exception(rc);
            }
            return lpNameBuffer.getValue().getString(0L, true);
        }
        finally {
            if (lpNameBuffer.getPointer() != null) {
                final int rc2 = Netapi32.INSTANCE.NetApiBufferFree(lpNameBuffer.getValue());
                if (0 != rc2) {
                    throw new Win32Exception(rc2);
                }
            }
        }
    }
    
    public static LocalGroup[] getLocalGroups() {
        return getLocalGroups(null);
    }
    
    public static LocalGroup[] getLocalGroups(final String serverName) {
        final PointerByReference bufptr = new PointerByReference();
        final IntByReference entriesRead = new IntByReference();
        final IntByReference totalEntries = new IntByReference();
        try {
            final int rc = Netapi32.INSTANCE.NetLocalGroupEnum(serverName, 1, bufptr, -1, entriesRead, totalEntries, null);
            if (0 != rc || bufptr.getValue() == Pointer.NULL) {
                throw new Win32Exception(rc);
            }
            final LMAccess.LOCALGROUP_INFO_1 group = new LMAccess.LOCALGROUP_INFO_1(bufptr.getValue());
            final LMAccess.LOCALGROUP_INFO_1[] groups = (LMAccess.LOCALGROUP_INFO_1[])group.toArray(entriesRead.getValue());
            final ArrayList<LocalGroup> result = new ArrayList<LocalGroup>();
            for (final LMAccess.LOCALGROUP_INFO_1 lgpi : groups) {
                final LocalGroup lgp = new LocalGroup();
                lgp.name = lgpi.lgrui1_name.toString();
                lgp.comment = lgpi.lgrui1_comment.toString();
                result.add(lgp);
            }
            return result.toArray(new LocalGroup[0]);
        }
        finally {
            if (bufptr.getValue() != Pointer.NULL) {
                final int rc2 = Netapi32.INSTANCE.NetApiBufferFree(bufptr.getValue());
                if (0 != rc2) {
                    throw new Win32Exception(rc2);
                }
            }
        }
    }
    
    public static Group[] getGlobalGroups() {
        return getGlobalGroups(null);
    }
    
    public static Group[] getGlobalGroups(final String serverName) {
        final PointerByReference bufptr = new PointerByReference();
        final IntByReference entriesRead = new IntByReference();
        final IntByReference totalEntries = new IntByReference();
        try {
            final int rc = Netapi32.INSTANCE.NetGroupEnum(serverName, 1, bufptr, -1, entriesRead, totalEntries, null);
            if (0 != rc || bufptr.getValue() == Pointer.NULL) {
                throw new Win32Exception(rc);
            }
            final LMAccess.GROUP_INFO_1 group = new LMAccess.GROUP_INFO_1(bufptr.getValue());
            final LMAccess.GROUP_INFO_1[] groups = (LMAccess.GROUP_INFO_1[])group.toArray(entriesRead.getValue());
            final ArrayList<LocalGroup> result = new ArrayList<LocalGroup>();
            for (final LMAccess.GROUP_INFO_1 lgpi : groups) {
                final LocalGroup lgp = new LocalGroup();
                lgp.name = lgpi.grpi1_name.toString();
                lgp.comment = lgpi.grpi1_comment.toString();
                result.add(lgp);
            }
            return result.toArray(new LocalGroup[0]);
        }
        finally {
            if (bufptr.getValue() != Pointer.NULL) {
                final int rc2 = Netapi32.INSTANCE.NetApiBufferFree(bufptr.getValue());
                if (0 != rc2) {
                    throw new Win32Exception(rc2);
                }
            }
        }
    }
    
    public static User[] getUsers() {
        return getUsers(null);
    }
    
    public static User[] getUsers(final String serverName) {
        final PointerByReference bufptr = new PointerByReference();
        final IntByReference entriesRead = new IntByReference();
        final IntByReference totalEntries = new IntByReference();
        try {
            final int rc = Netapi32.INSTANCE.NetUserEnum(serverName, 1, 0, bufptr, -1, entriesRead, totalEntries, null);
            if (0 != rc || bufptr.getValue() == Pointer.NULL) {
                throw new Win32Exception(rc);
            }
            final LMAccess.USER_INFO_1 user = new LMAccess.USER_INFO_1(bufptr.getValue());
            final LMAccess.USER_INFO_1[] users = (LMAccess.USER_INFO_1[])user.toArray(entriesRead.getValue());
            final ArrayList<User> result = new ArrayList<User>();
            for (final LMAccess.USER_INFO_1 lu : users) {
                final User auser = new User();
                auser.name = lu.usri1_name.toString();
                result.add(auser);
            }
            return result.toArray(new User[0]);
        }
        finally {
            if (bufptr.getValue() != Pointer.NULL) {
                final int rc2 = Netapi32.INSTANCE.NetApiBufferFree(bufptr.getValue());
                if (0 != rc2) {
                    throw new Win32Exception(rc2);
                }
            }
        }
    }
    
    public static Group[] getCurrentUserLocalGroups() {
        return getUserLocalGroups(Secur32Util.getUserNameEx(2));
    }
    
    public static Group[] getUserLocalGroups(final String userName) {
        return getUserLocalGroups(userName, null);
    }
    
    public static Group[] getUserLocalGroups(final String userName, final String serverName) {
        final PointerByReference bufptr = new PointerByReference();
        final IntByReference entriesread = new IntByReference();
        final IntByReference totalentries = new IntByReference();
        try {
            final int rc = Netapi32.INSTANCE.NetUserGetLocalGroups(serverName, userName, 0, 0, bufptr, -1, entriesread, totalentries);
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
            final LMAccess.LOCALGROUP_USERS_INFO_0 lgroup = new LMAccess.LOCALGROUP_USERS_INFO_0(bufptr.getValue());
            final LMAccess.LOCALGROUP_USERS_INFO_0[] lgroups = (LMAccess.LOCALGROUP_USERS_INFO_0[])lgroup.toArray(entriesread.getValue());
            final ArrayList<Group> result = new ArrayList<Group>();
            for (final LMAccess.LOCALGROUP_USERS_INFO_0 lgpi : lgroups) {
                final LocalGroup lgp = new LocalGroup();
                lgp.name = lgpi.lgrui0_name.toString();
                result.add(lgp);
            }
            return result.toArray(new Group[0]);
        }
        finally {
            if (bufptr.getValue() != Pointer.NULL) {
                final int rc2 = Netapi32.INSTANCE.NetApiBufferFree(bufptr.getValue());
                if (0 != rc2) {
                    throw new Win32Exception(rc2);
                }
            }
        }
    }
    
    public static Group[] getUserGroups(final String userName) {
        return getUserGroups(userName, null);
    }
    
    public static Group[] getUserGroups(final String userName, final String serverName) {
        final PointerByReference bufptr = new PointerByReference();
        final IntByReference entriesread = new IntByReference();
        final IntByReference totalentries = new IntByReference();
        try {
            final int rc = Netapi32.INSTANCE.NetUserGetGroups(serverName, userName, 0, bufptr, -1, entriesread, totalentries);
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
            final LMAccess.GROUP_USERS_INFO_0 lgroup = new LMAccess.GROUP_USERS_INFO_0(bufptr.getValue());
            final LMAccess.GROUP_USERS_INFO_0[] lgroups = (LMAccess.GROUP_USERS_INFO_0[])lgroup.toArray(entriesread.getValue());
            final ArrayList<Group> result = new ArrayList<Group>();
            for (final LMAccess.GROUP_USERS_INFO_0 lgpi : lgroups) {
                final Group lgp = new Group();
                lgp.name = lgpi.grui0_name.toString();
                result.add(lgp);
            }
            return result.toArray(new Group[0]);
        }
        finally {
            if (bufptr.getValue() != Pointer.NULL) {
                final int rc2 = Netapi32.INSTANCE.NetApiBufferFree(bufptr.getValue());
                if (0 != rc2) {
                    throw new Win32Exception(rc2);
                }
            }
        }
    }
    
    public static DomainController getDC() {
        final DsGetDC.PDOMAIN_CONTROLLER_INFO.ByReference pdci = new DsGetDC.PDOMAIN_CONTROLLER_INFO.ByReference();
        int rc = Netapi32.INSTANCE.DsGetDcName(null, null, null, null, 0, pdci);
        if (0 != rc) {
            throw new Win32Exception(rc);
        }
        final DomainController dc = new DomainController();
        dc.address = pdci.dci.DomainControllerAddress.toString();
        dc.addressType = pdci.dci.DomainControllerAddressType;
        dc.clientSiteName = pdci.dci.ClientSiteName.toString();
        dc.dnsForestName = pdci.dci.DnsForestName.toString();
        dc.domainGuid = pdci.dci.DomainGuid;
        dc.domainName = pdci.dci.DomainName.toString();
        dc.flags = pdci.dci.Flags;
        dc.name = pdci.dci.DomainControllerName.toString();
        rc = Netapi32.INSTANCE.NetApiBufferFree(pdci.dci.getPointer());
        if (0 != rc) {
            throw new Win32Exception(rc);
        }
        return dc;
    }
    
    public static DomainTrust[] getDomainTrusts() {
        return getDomainTrusts(null);
    }
    
    public static DomainTrust[] getDomainTrusts(final String serverName) {
        final NativeLongByReference domainCount = new NativeLongByReference();
        final DsGetDC.PDS_DOMAIN_TRUSTS.ByReference domains = new DsGetDC.PDS_DOMAIN_TRUSTS.ByReference();
        int rc = Netapi32.INSTANCE.DsEnumerateDomainTrusts(serverName, new NativeLong(63L), domains, domainCount);
        if (0 != rc) {
            throw new Win32Exception(rc);
        }
        try {
            final int domainCountValue = domainCount.getValue().intValue();
            final ArrayList<DomainTrust> trusts = new ArrayList<DomainTrust>(domainCountValue);
            for (final DsGetDC.DS_DOMAIN_TRUSTS trust : domains.getTrusts(domainCountValue)) {
                final DomainTrust t = new DomainTrust();
                t.DnsDomainName = trust.DnsDomainName.toString();
                t.NetbiosDomainName = trust.NetbiosDomainName.toString();
                t.DomainSid = trust.DomainSid;
                t.DomainSidString = Advapi32Util.convertSidToStringSid(trust.DomainSid);
                t.DomainGuid = trust.DomainGuid;
                t.DomainGuidString = Ole32Util.getStringFromGUID(trust.DomainGuid);
                t.flags = trust.Flags.intValue();
                trusts.add(t);
            }
            return trusts.toArray(new DomainTrust[0]);
        }
        finally {
            rc = Netapi32.INSTANCE.NetApiBufferFree(domains.getPointer());
            if (0 != rc) {
                throw new Win32Exception(rc);
            }
        }
    }
    
    public static UserInfo getUserInfo(final String accountName) {
        return getUserInfo(accountName, getDCName());
    }
    
    public static UserInfo getUserInfo(final String accountName, final String domainName) {
        final PointerByReference bufptr = new PointerByReference();
        int rc = -1;
        try {
            rc = Netapi32.INSTANCE.NetUserGetInfo(getDCName(), accountName, 23, bufptr);
            if (rc == 0) {
                final LMAccess.USER_INFO_23 info_23 = new LMAccess.USER_INFO_23(bufptr.getValue());
                final UserInfo userInfo = new UserInfo();
                userInfo.comment = info_23.usri23_comment.toString();
                userInfo.flags = info_23.usri23_flags;
                userInfo.fullName = info_23.usri23_full_name.toString();
                userInfo.name = info_23.usri23_name.toString();
                userInfo.sidString = Advapi32Util.convertSidToStringSid(info_23.usri23_user_sid);
                userInfo.sid = info_23.usri23_user_sid;
                return userInfo;
            }
            throw new Win32Exception(rc);
        }
        finally {
            if (bufptr.getValue() != Pointer.NULL) {
                Netapi32.INSTANCE.NetApiBufferFree(bufptr.getValue());
            }
        }
    }
    
    public static class Group
    {
        public String name;
    }
    
    public static class User
    {
        public String name;
        public String comment;
    }
    
    public static class UserInfo extends User
    {
        public String fullName;
        public String sidString;
        public WinNT.PSID sid;
        public int flags;
    }
    
    public static class LocalGroup extends Group
    {
        public String comment;
    }
    
    public static class DomainController
    {
        public String name;
        public String address;
        public int addressType;
        public Guid.GUID domainGuid;
        public String domainName;
        public String dnsForestName;
        public int flags;
        public String clientSiteName;
    }
    
    public static class DomainTrust
    {
        public String NetbiosDomainName;
        public String DnsDomainName;
        public WinNT.PSID DomainSid;
        public String DomainSidString;
        public Guid.GUID DomainGuid;
        public String DomainGuidString;
        private int flags;
        
        public boolean isInForest() {
            return (this.flags & 0x1) != 0x0;
        }
        
        public boolean isOutbound() {
            return (this.flags & 0x2) != 0x0;
        }
        
        public boolean isRoot() {
            return (this.flags & 0x4) != 0x0;
        }
        
        public boolean isPrimary() {
            return (this.flags & 0x8) != 0x0;
        }
        
        public boolean isNativeMode() {
            return (this.flags & 0x10) != 0x0;
        }
        
        public boolean isInbound() {
            return (this.flags & 0x20) != 0x0;
        }
    }
}
