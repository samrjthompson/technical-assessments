package org.example.config;

import java.util.HashMap;
import java.util.Map;

public enum CucumberContext {

    CONTEXT;

    private final ThreadLocal<Map<String, Object>> testContext = ThreadLocal.withInitial(HashMap::new);

    @SuppressWarnings(value = {"unchecked"})
    public <T> T get(String name) {
        return (T) testContext.get().get(name);
    }

    public <T> T set(String name, T object) {
        testContext.get().put(name, object);
        return object;
    }

    public void clear() {
        testContext.get().clear();
    }
}
