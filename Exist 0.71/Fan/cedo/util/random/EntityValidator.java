package cedo.util.random;

import net.minecraft.entity.Entity;

import java.util.HashSet;
import java.util.Set;

/**
 * @author SoonTM
 * @since 1/15/2020
 */
public final class EntityValidator {
    private final Set<ICheck> checks = new HashSet<>();

    public final boolean validate(Entity entity) {
        for (ICheck check : checks) {
            if (check.validate(entity)) continue;
            return false;
        }
        return true;
    }

    public void add(ICheck check) {
        checks.add(check);
    }

    public interface ICheck {
        boolean validate(Entity entity);
    }
}
