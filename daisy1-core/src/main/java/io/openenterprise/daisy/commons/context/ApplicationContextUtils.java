package io.openenterprise.daisy.commons.context;

import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ApplicationContextUtils implements ApplicationContextAware {

    protected static final Container CONTAINER = new Container();

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        synchronized (CONTAINER) {
            if (Objects.isNull(CONTAINER.applicationContext)) {
                CONTAINER.applicationContext = applicationContext;
            }
        }
    }

    @Nonnull
    public static Object getBean(@Nonnull Class<?> type) {
        Class<?> superclass = ClassUtils.getAllInterfaces(type).getFirst();
        Class<?> actualType = Objects.isNull(superclass)? type : superclass;

        return CONTAINER.applicationContext.getBean(actualType);
    }

    protected static class Container {

        protected ApplicationContext applicationContext;

    }
}
