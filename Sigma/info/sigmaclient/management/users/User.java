package info.sigmaclient.management.users;

/**
 * Created by Arithmo on 8/11/2017 at 9:31 PM.
 */
public class User implements Upgradable {

    private String name;

    private String HWID;

    @Override
    public void applyModules() {
    }

    @Override
    public void upgradeFeatures() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHWID() {
        return HWID;
    }

    public void setHWID(String HWID) {
        this.HWID = HWID;
    }

}
