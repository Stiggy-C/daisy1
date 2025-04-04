package io.openenterprise.daisy.data.jpa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.Nullable;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;

import java.io.UncheckedIOException;
import java.util.Map;
import java.util.Objects;

@Converter
public class MapConverter implements AttributeConverter<Map<String, Object>, String> {

    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules().disable(
            SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Nullable
    @Override
    public String convertToDatabaseColumn(@Nullable Map<String, Object> stringObjectMap) {
        if (Objects.isNull(stringObjectMap)) {
            return null;
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(stringObjectMap);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Nullable
    @Override
    public Map<String, Object> convertToEntityAttribute(@Nullable String string) {
        try {
            return StringUtils.isBlank(string)? null :
                    OBJECT_MAPPER.readerFor(new TypeReference<Map<String, Object>>() {}).readValue(string);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }
}
