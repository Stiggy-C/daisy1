package io.openenterprise.commons.util;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochRandomGenerator;
import com.fasterxml.uuid.impl.UUIDUtil;

import jakarta.annotation.Nonnull;
import java.util.UUID;

public final class UUIDUtils {

    private static final TimeBasedEpochRandomGenerator TIME_BASED_EPOCH_RANDOM_GENERATOR =
            Generators.timeBasedEpochRandomGenerator();

    private UUIDUtils() {
    }

    @Nonnull
    public static UUID randomUUIDv7() {
        return TIME_BASED_EPOCH_RANDOM_GENERATOR.generate();
    }

    @Nonnull
    public static UUID fromString(@Nonnull String uuidString) {
        return UUIDUtil.uuid(uuidString);
    }
}
