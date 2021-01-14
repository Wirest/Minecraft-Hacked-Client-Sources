package splash.client.modules.visual;

import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.ModeValue;
import splash.api.value.impl.NumberValue;


public class Animations extends Module {
	
	public ModeValue<Mode> mode = new ModeValue<>("Mode", Mode.DEFAULT, this);

	public Animations() {
		super("Animations", "Changes autoblock animations.", ModuleCategory.VISUALS);
	}

	public enum Mode {
		DEFAULT, SMOOTH, SIGMA, HELIUM, OHARE, SPLASH, REMIX
		
	}

}
