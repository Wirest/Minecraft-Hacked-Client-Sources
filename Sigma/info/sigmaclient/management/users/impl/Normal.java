package info.sigmaclient.management.users.impl;

import info.sigmaclient.management.users.User;

public class Normal extends User {

    public Normal(String name, String HWID) {
        setName(name);
        setHWID(HWID);
    }
}
