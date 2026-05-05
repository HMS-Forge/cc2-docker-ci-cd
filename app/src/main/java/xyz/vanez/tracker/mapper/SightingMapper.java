package xyz.vanez.tracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import xyz.vanez.tracker.dto.SightingDto;
import xyz.vanez.tracker.model.Sighting;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SightingMapper {
    SightingMapper INSTANCE = Mappers.getMapper(SightingMapper.class);

    @Mapping(source = "instance.id", target = "instanceId")
    SightingDto toDto(Sighting sighting);

    @Mapping(source = "instanceId", target = "instance.id")
    Sighting toEntity(SightingDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "instanceId", target = "instance.id")
    void updateEntityFromDto(SightingDto dto, @MappingTarget Sighting entity);
}