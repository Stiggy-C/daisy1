package io.openenterprise.graalvm.polyglot
import org.apache.commons.pool2.BasePooledObjectFactory
import org.apache.commons.pool2.PooledObject
import org.apache.commons.pool2.impl.DefaultPooledObject

class PooledContextDelegateFactory(val contextBuilder: ContextBuilder): BasePooledObjectFactory<ContextDelegate>() {

    override fun activateObject(p: PooledObject<ContextDelegate>) {
       p.`object`.enter()
    }

    override fun create(): ContextDelegateImpl {
        return ContextDelegateImpl(contextBuilder.createContext(), contextBuilder.supportedLanguage)
    }

    override fun wrap(p0: ContextDelegate): PooledObject<ContextDelegate> {
        return DefaultPooledObject(p0)
    }
}