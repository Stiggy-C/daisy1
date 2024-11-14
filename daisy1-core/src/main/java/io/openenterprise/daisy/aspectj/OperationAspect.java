package io.openenterprise.daisy.aspectj;

import io.openenterprise.daisy.Invocation;
import io.openenterprise.daisy.Operation;
import org.apache.commons.lang3.ClassUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

@Aspect
public class OperationAspect {

    @Before("target(io.openenterprise.daisy.Operation) && execution(* *.invoke(..)")
    public void beforeInvoke(@Nonnull JoinPoint joinPoint) {
        var invocation = getInvocation(joinPoint);

        invocation.getOperation().preInvoke(invocation);
    }

    @AfterReturning(
            pointcut = "target(io.openenterprise.daisy.Operation) && execution(* *.invoke(..)",
            returning = "result"
    )
    public void afterInvoke(@Nonnull JoinPoint joinPoint, @Nullable Object result) {
        var invocation = getInvocation(joinPoint);

        invocation.getOperation().postInvoke(invocation, result);
    }

    @Nonnull
    protected static Invocation<? extends Operation<Object>, Object> getInvocation(@Nonnull JoinPoint joinPoint) {
        var args = joinPoint.getArgs();

        assert args.length == 1;
        assert Objects.nonNull(args[0]);
        assert ClassUtils.isAssignable(args[0].getClass(), Invocation.class);

        @SuppressWarnings("unchecked")
        var invocation = (Invocation<? extends Operation<?>, Object>) args[0];

        return invocation;
    }
}
