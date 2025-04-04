package io.openenterprise.graalvm.polyglot;

import jakarta.annotation.Nonnull;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class PooledContextDelegateFactory extends BasePooledObjectFactory<ContextDelegate> {

    public PooledContextDelegateFactory(@Nonnull ContextBuilder contextBuilder) {
        this.contextBuilder = contextBuilder;
    }

    protected ContextBuilder contextBuilder;

    @Override
    public void activateObject(@Nonnull PooledObject<ContextDelegate> pooledObject) throws Exception {
        pooledObject.getObject().enter();
    }

    @Override
    public ContextDelegate create() throws Exception {
        return new ContextDelegateImpl(contextBuilder.createContext(), contextBuilder.getSupportedLanguage());
    }

    @Override
    public void passivateObject(PooledObject<ContextDelegate> pooledObject) throws Exception {
        pooledObject.getObject().close();
    }

    @Override
    public PooledObject<ContextDelegate> wrap(@Nonnull ContextDelegate contextDelegate) {
        return new DefaultPooledObject<>(contextDelegate);
    }
}
