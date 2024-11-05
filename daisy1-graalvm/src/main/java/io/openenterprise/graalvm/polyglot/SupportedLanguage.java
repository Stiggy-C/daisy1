package io.openenterprise.graalvm.polyglot;

import lombok.Getter;

import javax.annotation.Nonnull;

@Getter
public enum SupportedLanguage {

    PYTHON("python");

    private final String languageId;

    SupportedLanguage(@Nonnull String languageId) {
        this.languageId = languageId;
    }

    public String getLanguageId() {
        return languageId;
    }
}
