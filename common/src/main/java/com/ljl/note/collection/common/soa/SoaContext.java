package com.ljl.note.collection.common.soa;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class SoaContext {

    private static final ThreadLocal<SoaContext> LOCAL = new ThreadLocal<SoaContext>() {
        @Override
        protected SoaContext initialValue() {
            return new SoaContext();
        }
    };

    private final Map<String, String> global = new HashMap<>();

    private final Map<String, Object> local = new HashMap<>();

    public static SoaContext getContext() {
        return LOCAL.get();
    }

    public static Map<String, String> getGlobal() {
        return LOCAL.get().global;
    }

    public static Map<String, Object> getLocal() {
        return LOCAL.get().local;
    }
}