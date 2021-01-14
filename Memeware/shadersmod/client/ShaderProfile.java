package shadersmod.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ShaderProfile {
    private String name = null;
    private Map<String, String> mapOptionValues = new HashMap();
    private Set<String> disabledPrograms = new HashSet();

    public ShaderProfile(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void addOptionValue(String option, String value) {
        this.mapOptionValues.put(option, value);
    }

    public void addOptionValues(ShaderProfile prof) {
        if (prof != null) {
            this.mapOptionValues.putAll(prof.mapOptionValues);
        }
    }

    public void applyOptionValues(ShaderOption[] options) {
        for (int i = 0; i < options.length; ++i) {
            ShaderOption so = options[i];
            String key = so.getName();
            String val = (String) this.mapOptionValues.get(key);

            if (val != null) {
                so.setValue(val);
            }
        }
    }

    public String[] getOptions() {
        Set keys = this.mapOptionValues.keySet();
        String[] opts = (String[]) ((String[]) keys.toArray(new String[keys.size()]));
        return opts;
    }

    public String getValue(String key) {
        return (String) this.mapOptionValues.get(key);
    }

    public void addDisabledProgram(String program) {
        this.disabledPrograms.add(program);
    }

    public Collection<String> getDisabledPrograms() {
        return new HashSet(this.disabledPrograms);
    }

    public void addDisabledPrograms(Collection<String> programs) {
        this.disabledPrograms.addAll(programs);
    }

    public boolean isProgramDisabled(String program) {
        return this.disabledPrograms.contains(program);
    }
}
