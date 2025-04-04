package io.openenterprise.daisy.domain;

import com.google.common.collect.Maps;
import io.openenterprise.daisy.Operation;
import io.openenterprise.daisy.Parameter;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Action<T> {

    protected Operation<T> operation;

    protected Map<Parameter, Object> parameters;

    public static class Builder<T> {

        protected Operation<T> operation;

        protected Map<Parameter, Object> parameters = Maps.newHashMap();

        @Nonnull
        public Action<T> build() {
            Action<T> action = new Action<>();
            action.operation = this.operation;
            action.parameters = this.parameters;

            return action;
        }

        @Nonnull
        public Builder<T> operation(@Nonnull Operation<T> operation) {
            this.operation = operation;

            return this;
        }

        @Nonnull
        public Builder<T> parameter(@Nonnull Parameter parameter, @Nullable Object value) {
            parameters.put(parameter, value);

            return this;
        }

        @Nonnull
        public Builder<T> parameters(@Nonnull Map<Parameter, Object> parameters) {
            this.parameters.putAll(parameters);

            return this;
        }
    }
}
