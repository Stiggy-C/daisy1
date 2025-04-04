package io.openenterprise.daisy.data.jpa.domain;

import jakarta.annotation.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ActionMapper extends EntityMapper<Action, io.openenterprise.daisy.rs.model.Action> {

    ActionMapper INSTANCE = Mappers.getMapper(ActionMapper.class);

    @Mapping(expression = "java(java.util.Objects.isNull(entity)? null : java.time.OffsetDateTime.ofInstant(entity.getCreatedInstant(), java.time.ZoneId.of(\"UTC\")))",
            target = "createdInstant")
    @Mapping(expression = "java(java.util.Objects.isNull(entity)? null : java.time.OffsetDateTime.ofInstant(entity.getUpdatedInstant(), java.time.ZoneId.of(\"UTC\")))",
            target = "updatedInstant")
    @Nullable
    io.openenterprise.daisy.rs.model.Action entityToRestModel(@Nullable Action entity);

    @Mapping(expression = "java(java.util.Objects.isNull(model)? null : model.getCreatedInstant().toInstant())",
            target = "createdInstant")
    @Mapping(expression = "java(java.util.Objects.isNull(model)? null : model.getUpdatedInstant().toInstant())",
            target = "updatedInstant")
    @Nullable
    Action restModelToEntity(@Nullable io.openenterprise.daisy.rs.model.Action model);


}
