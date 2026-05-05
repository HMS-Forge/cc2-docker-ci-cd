package xyz.vanez.tracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import xyz.vanez.tracker.dto.TrainModelDto;
import xyz.vanez.tracker.model.TrainModel;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrainModelMapper {
    TrainModelMapper INSTANCE = Mappers.getMapper(TrainModelMapper.class);

    TrainModelDto toDto(TrainModel model);
    TrainModel toEntity(TrainModelDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(TrainModelDto dto, @MappingTarget TrainModel entity);
}