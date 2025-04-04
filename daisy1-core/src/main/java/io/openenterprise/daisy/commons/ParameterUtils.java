package io.openenterprise.daisy.commons;

import io.openenterprise.daisy.Parameter;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.reflections.scanners.Scanners.SubTypes;

public final class ParameterUtils {

    private static final Reflections REFLECTIONS = new Reflections("io.openenterprise.daisy");

    private static final Set<Class<?>> SUB_TYPES = REFLECTIONS.get(SubTypes.of(Parameter.class).asClass());

    private ParameterUtils() {}

    @Nullable
    public static Parameter getByKey(@Nonnull String key) {
        return SUB_TYPES.stream()
                .flatMap(clazz -> Arrays.stream(clazz.getEnumConstants()))
                .map(enumConstant -> (Parameter) enumConstant)
                .filter(parameter -> StringUtils.equals(key, parameter.getKey()))
                .findFirst()
                .orElse(null);
    }

    @Nonnull
    public static Map<String, Object> toStringKeyValueMap(@Nonnull Map<Parameter, Object> parameters) {
        return parameters.entrySet().stream()
                .map(entry -> Pair.of(entry.getKey().getKey(), entry.getValue()))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    @Nonnull
    public static Map<Parameter, Object> toParameterKeyValueMap(@Nonnull Map<String, Object> map) {
        return map.entrySet().stream()
                .map(entry -> Pair.of(getByKey(entry.getKey()), entry.getValue()))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }
}
