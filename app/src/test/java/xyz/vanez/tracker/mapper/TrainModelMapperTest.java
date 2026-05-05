package xyz.vanez.tracker.mapper;

import xyz.vanez.tracker.dto.TrainModelDto;
import xyz.vanez.tracker.model.TrainModel;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.assertj.core.api.Assertions.assertThat;

class TrainModelMapperTest {

    private final TrainModelMapper mapper = Mappers.getMapper(TrainModelMapper.class);

    @Test
    void toDto_ShouldMapEntityToDto() {
        TrainModel entity = new TrainModel();
        entity.setId(1);
        entity.setName("EP20");
        entity.setManufacturer("NEVZ");
        entity.setYearOfProduction(2012);
        entity.setPowerKw(8800);
        entity.setMaxSpeedKmh(200);

        TrainModelDto dto = mapper.toDto(entity);

        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getName()).isEqualTo("EP20");
        assertThat(dto.getManufacturer()).isEqualTo("NEVZ");
        assertThat(dto.getYearOfProduction()).isEqualTo(2012);
        assertThat(dto.getPowerKw()).isEqualTo(8800);
        assertThat(dto.getMaxSpeedKmh()).isEqualTo(200);
    }

    @Test
    void toEntity_ShouldMapDtoToEntity() {
        TrainModelDto dto = new TrainModelDto();
        dto.setId(1);
        dto.setName("VL80");
        dto.setManufacturer("NEVZ");
        dto.setYearOfProduction(1979);
        dto.setPowerKw(6520);
        dto.setMaxSpeedKmh(110);

        TrainModel entity = mapper.toEntity(dto);

        assertThat(entity.getId()).isEqualTo(1);
        assertThat(entity.getName()).isEqualTo("VL80");
        assertThat(entity.getManufacturer()).isEqualTo("NEVZ");
        assertThat(entity.getYearOfProduction()).isEqualTo(1979);
        assertThat(entity.getPowerKw()).isEqualTo(6520);
        assertThat(entity.getMaxSpeedKmh()).isEqualTo(110);
    }

    @Test
    void updateEntityFromDto_ShouldUpdateFields() {
        TrainModel entity = new TrainModel();
        entity.setId(1);
        entity.setName("OldName");

        TrainModelDto dto = new TrainModelDto();
        dto.setName("NewName");
        dto.setManufacturer("NewMan");
        dto.setYearOfProduction(2000);
        dto.setPowerKw(5000);
        dto.setMaxSpeedKmh(150);

        mapper.updateEntityFromDto(dto, entity);

        assertThat(entity.getName()).isEqualTo("NewName");
        assertThat(entity.getManufacturer()).isEqualTo("NewMan");
        assertThat(entity.getYearOfProduction()).isEqualTo(2000);
        assertThat(entity.getPowerKw()).isEqualTo(5000);
        assertThat(entity.getMaxSpeedKmh()).isEqualTo(150);
        assertThat(entity.getId()).isEqualTo(1); // id не должно измениться
    }
}