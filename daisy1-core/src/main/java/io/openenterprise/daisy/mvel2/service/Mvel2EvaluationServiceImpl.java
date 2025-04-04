package io.openenterprise.daisy.mvel2.service;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.mvel2.MVEL;
import org.mvel2.ParserConfiguration;
import org.mvel2.ParserContext;
import org.mvel2.compiler.ExecutableStatement;
import org.mvel2.integration.VariableResolverFactory;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

@Named
public class Mvel2EvaluationServiceImpl implements Mvel2EvaluationService {

    /**
     * Default org.mvel2.ParserContext
     */
    @Inject
    protected ParserConfiguration parserConfiguration;

    @Nonnull
    @Override
    public ParserContext buildParserContext(@Nonnull Set<Class<?>> classImports, Set<String> packageImports) {
        var parserContext = new ParserContext(parserConfiguration);

        classImports.forEach(parserContext::addImport);
        packageImports.forEach(parserContext::addPackageImport);

        return parserContext;
    }

    @Nonnull
    @Override
    @SuppressWarnings("rawtypes")
    public ParserContext buildParserContext(@Nonnull Set<Class<?>> classImports, @Nonnull Set<String> packageImports,
                                            @Nonnull Map<String, Class> variables) {
        var parserContext = buildParserContext(classImports, packageImports);
        parserContext.addVariables(variables);

        return parserContext;
    }

    @Nonnull
    @Override
    public ExecutableStatement compileExpression(@Nonnull String expression, @Nonnull ParserContext parserContext) {
        return (ExecutableStatement) MVEL.compileExpression(expression, parserContext);
    }

    @Nullable
    @Override
    public Object evaluate(@Nonnull ExecutableStatement compiledExpression,
                           @Nonnull VariableResolverFactory variableResolverFactory) {
        return this.evaluate(compiledExpression, variableResolverFactory, Object.class);
    }

    @Nullable
    @Override
    public <T> T evaluate(@Nonnull ExecutableStatement compiledExpression,
                          @Nonnull VariableResolverFactory variableResolverFactory,
                          @Nonnull Class<T> resultClass) {
        return this.evaluate(compiledExpression, variableResolverFactory, resultClass, null,
                null);
    }

    @Nullable
    public Object evaluate(@Nonnull ExecutableStatement compiledExpression,
                           @Nonnull VariableResolverFactory variableResolverFactory,
                           @Nullable Consumer<Object> postEvalCallback,
                           @Nullable Consumer<Exception> postEvalExceptionThrownCallback) {
        return this.evaluate(compiledExpression, variableResolverFactory, Object.class, null,
                null);
    }

    @Nullable
    @Override
    public <T> T evaluate(@Nonnull ExecutableStatement compiledExpression,
                          @Nonnull VariableResolverFactory variableResolverFactory,
                          @Nonnull Class<T> resultClass, @Nullable Consumer<T> postEvalCallback,
                          @Nullable Consumer<Exception> postEvalExceptionThrownCallback) {
        T result;

        try {
            result = resultClass.cast(MVEL.executeExpression(compiledExpression,
                    variableResolverFactory));
        } catch (Exception e) {
            if (Objects.nonNull(postEvalExceptionThrownCallback)) {
                postEvalExceptionThrownCallback.accept(e);
            }

            throw e;
        }

        try {
            return result;
        } finally {
            if (Objects.nonNull(postEvalCallback)) {
                postEvalCallback.accept(result);
            }
        }
    }
}
