/*
 * Copyright 2020 Hippo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package me.hippo.api.lwjeb.wrapped;

import java.util.Optional;

/**
 * @author Hippo
 * @version 5.0.0, 11/4/19
 * @since 5.0.0
 * <p>
 * A container to object that is meant for multiple types.
 */
public class WrappedType {

    /**
     * The raw type.
     */
    private final Object type;

    /**
     * Creates a new {@link WrappedType} with {@code type} as the raw type.
     *
     * @param type The raw type.
     */
    public WrappedType(Object type) {
        this.type = type;
    }

    /**
     * Cast {@link #type} to {@code type} and wraps it in an {@link Optional}.
     * <p>
     * Returns {@link Optional#empty()} if {@link #type} cannot be casted to {@code type}.
     * </p>
     *
     * @param type The type to cast class.
     * @param <T>  The type to cast.
     * @return The casted type.
     */
    public <T> Optional<T> as(Class<T> type) {
        try {
            return Optional.of(type.cast(this.type));
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets the raw type.
     *
     * @return The raw type.
     */
    public Object raw() {
        return type;
    }

    /**
     * Returns the raw type as a {@link String}.
     *
     * @return The raw type's string.
     */
    @Override
    public String toString() {
        return type.toString();
    }
}
