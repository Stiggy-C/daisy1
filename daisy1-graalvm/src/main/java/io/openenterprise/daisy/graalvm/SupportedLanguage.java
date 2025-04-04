package io.openenterprise.daisy.graalvm;

import lombok.Getter;

import jakarta.annotation.Nonnull;

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
