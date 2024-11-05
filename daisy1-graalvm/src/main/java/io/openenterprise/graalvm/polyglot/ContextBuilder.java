package io.openenterprise.graalvm.polyglot;

import org.graalvm.polyglot.Context;

import javax.annotation.Nonnull;

public interface ContextBuilder {

    @Nonnull
    Context createContext();

    String getSupportedLanguage();
}
