package io.openenterprise.daisy.data.jpa.domain;

import org.jetbrains.annotations.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ActionsChainMapper extends EntityMapper<ActionsChain, io.openenterprise.daisy.rs.model.ActionsChain> {

    ActionsChainMapper INSTANCE = Mappers.getMapper(ActionsChainMapper.class);

    @Nullable
    default io.openenterprise.daisy.rs.model.Action actionEntityToActionRestModel(@Nullable Action action) {
        return ActionMapper.INSTANCE.entityToRestModel(action);
    }

    @Nullable
    default Action actionRestModelToActionEntity(@Nullable io.openenterprise.daisy.rs.model.Action model) {
        return ActionMapper.INSTANCE.restModelToEntity(model);
    }

    @Mapping(expression = "java(java.util.Objects.isNull(entity)? null : java.time.OffsetDateTime.ofInstant(entity.getCreatedInstant(), java.time.ZoneId.of(\"UTC\")))",
            target = "createdInstant")
    @Mapping(expression = "java(java.util.Objects.isNull(entity)? null : java.time.OffsetDateTime.ofInstant(entity.getUpdatedInstant(), java.time.ZoneId.of(\"UTC\")))",
            target = "updatedInstant")
    @Nullable
    @Override
    io.openenterprise.daisy.rs.model.ActionsChain entityToRestModel(@Nullable ActionsChain entity);

    @Mapping(expression = "java(java.util.Objects.isNull(model)? null : model.getCreatedInstant().toInstant())",
            target = "createdInstant")
    @Mapping(expression = "java(java.util.Objects.isNull(model)? null : model.getUpdatedInstant().toInstant())",
            target = "updatedInstant")
    @Nullable
    @Override
    ActionsChain restModelToEntity(@Nullable io.openenterprise.daisy.rs.model.ActionsChain model);
}
