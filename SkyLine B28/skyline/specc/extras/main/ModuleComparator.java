package skyline.specc.extras.main;

import java.util.Comparator;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.specc.utils.FontUtil;

public class ModuleComparator implements Comparator<Module> {

		@Override
		public int compare(Module o1, Module o2) {
			if (FontUtil.roboto.getStringWidth(o1.getDisplayName()) < FontUtil.roboto
					.getStringWidth(o2.getDisplayName())) {
				return 1;
			} else if (FontUtil.roboto.getStringWidth(o1.getDisplayName()) > FontUtil.roboto
					.getStringWidth(o2.getDisplayName())) {
				return -1;
			} else {
				return 0;
			}
		}

	}