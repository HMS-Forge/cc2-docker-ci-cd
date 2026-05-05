package xyz.vanez.tracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import xyz.vanez.tracker.dto.TrainInstanceDto;
import xyz.vanez.tracker.model.TrainInstance;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrainInstanceMapper {
    TrainInstanceMapper INSTANCE = Mappers.getMapper(TrainInstanceMapper.class);

    @Mapping(source = "model.id", target = "modelId")
    TrainInstanceDto toDto(TrainInstance instance);

    @Mapping(source = "modelId", target = "model.id")
    TrainInstance toEntity(TrainInstanceDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "modelId", target = "model.id")
    void updateEntityFromDto(TrainInstanceDto dto, @MappingTarget TrainInstance entity);
}