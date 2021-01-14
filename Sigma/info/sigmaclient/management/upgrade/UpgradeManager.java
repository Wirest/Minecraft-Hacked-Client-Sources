package info.sigmaclient.management.upgrade;

/**
 * Created by Arithmo on 8/13/2017 at 4:38 PM.
 */
public class UpgradeManager {

    /*
     *
     * TODO: Add particle effects
     *
     */

    private static HeadModels activeHeadModel;

    private static BodyModels activeBodyModel;

    public UpgradeManager() {

        //Check if there are any saved player mods
        /*
         * if(fileExists) {
         *   loadModels();
         * } else {
         *   creatModelsFile();
         * }
         *
         * */

    }

    public static BodyModels getActiveBodyModel() {
        return activeBodyModel;
    }

    public static HeadModels getActiveHeadModel() {
        return activeHeadModel;
    }

    public void saveModels() {
        //TODO: Save current player modifications.
    }

    private void loadModels() {
        //TODO: Load the saved player modifications.
    }

    public enum BodyModels {
        //Add different minecon capes (20XX-2016)
        DragonWings, Willy, MineconCape, SigmaCape
    }

    public enum HeadModels {
        CatEars, MouseEars, DragonHead
    }

}
