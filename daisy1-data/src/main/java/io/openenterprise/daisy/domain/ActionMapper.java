package io.openenterprise.daisy.domain;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ActionMapper {

    ActionMapper INSTANCE = Mappers.getMapper(ActionMapper.class);

    @Mapping(expression = "java(java.util.Objects.isNull(entity.getOperation())? null : " +
            "(io.openenterprise.daisy.Operation<Object>) io.openenterprise.daisy.commons.context.ApplicationContextUtils.getBean(" +
            "org.apache.commons.lang3.ClassUtils.getClass(entity.getOperation())))", target = "operation")
    @Mapping(expression = "java(java.util.Objects.isNull(entity.getParameters())? null : " +
            "io.openenterprise.daisy.commons.ParameterUtils.toParameterKeyValueMap(entity.getParameters()))",
            target = "parameters")
    Action<Object> entityToModel(@Nonnull io.openenterprise.daisy.data.jpa.domain.Action entity) throws Exception;

    @Mapping(expression = "java(java.util.Objects.isNull(model.getOperation())? null : " +
            "model.getOperation().getClass().getName())", target = "operation")
    @Mapping(expression = "java(java.util.Objects.isNull(model.getParameters())? null : " +
            "io.openenterprise.daisy.commons.ParameterUtils.toStringKeyValueMap(model.getParameters()))",
            target = "parameters")
    io.openenterprise.daisy.data.jpa.domain.Action modelToEntity(@Nullable Action<Object> model);
}
