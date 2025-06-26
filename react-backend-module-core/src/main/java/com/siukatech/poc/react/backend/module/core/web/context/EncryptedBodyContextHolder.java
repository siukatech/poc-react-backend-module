package com.siukatech.poc.react.backend.module.core.web.context;

public class EncryptedBodyContextHolder {

    private static final ThreadLocal<EncryptedBodyContext> THREAD_LOCAL_CONTEXT = new ThreadLocal<>();

    public void setContext(EncryptedBodyContext encryptedBodyContext) {
        THREAD_LOCAL_CONTEXT.set(encryptedBodyContext);
    }

    public EncryptedBodyContext getContext() {
        return THREAD_LOCAL_CONTEXT.get();
    }

    public void removeContext() {
        THREAD_LOCAL_CONTEXT.remove();
    }
}
