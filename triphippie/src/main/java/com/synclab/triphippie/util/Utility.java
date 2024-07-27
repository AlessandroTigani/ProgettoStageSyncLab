package com.synclab.triphippie.util;

import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class Utility {

    /**
     * Updates a value using the provided setter if the value is not null.
     * @param value the value to be checked and potentially set. If this value is null, the setter will not be called.
     * @param setter a Consumer function that sets the value. This function will be called with the provided value if it is not null.
     * @param <T> the type of the value to be set.
     */
    public <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
