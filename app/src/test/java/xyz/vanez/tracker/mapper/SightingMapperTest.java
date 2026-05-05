package xyz.vanez.tracker.mapper;

import xyz.vanez.tracker.dto.SightingDto;
import xyz.vanez.tracker.model.Sighting;
import xyz.vanez.tracker.model.TrainInstance;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

class SightingMapperTest {

    private final SightingMapper mapper = Mappers.getMapper(SightingMapper.class);

    @Test
    void toDto_ShouldMapEntityToDto() {
        TrainInstance instance = new TrainInstance();
        instance.setId(7);
        Sighting entity = new Sighting();
        entity.setId(1);
        entity.setRegion("Московская обл.");
        entity.setRailway("Октябрьская");
        entity.setSightingDate(LocalDate.of(2025, 4, 20));
        entity.setImageUrl("http://example.com/sight.jpg");
        entity.setNote("Заметка");
        entity.setInstance(instance);

        SightingDto dto = mapper.toDto(entity);

        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getRegion()).isEqualTo("Московская обл.");
        assertThat(dto.getRailway()).isEqualTo("Октябрьская");
        assertThat(dto.getSightingDate()).isEqualTo(LocalDate.of(2025, 4, 20));
        assertThat(dto.getImageUrl()).isEqualTo("http://example.com/sight.jpg");
        assertThat(dto.getNote()).isEqualTo("Заметка");
        assertThat(dto.getInstanceId()).isEqualTo(7);
    }

    @Test
    void toEntity_ShouldMapDtoToEntity() {
        SightingDto dto = new SightingDto();
        dto.setId(2);
        dto.setRegion("Ленинградская обл.");
        dto.setRailway("Октябрьская");
        dto.setSightingDate(LocalDate.now());
        dto.setImageUrl("http://example.com/another.jpg");
        dto.setNote("Другая заметка");
        dto.setInstanceId(3);

        Sighting entity = mapper.toEntity(dto);

        assertThat(entity.getId()).isEqualTo(2);
        assertThat(entity.getRegion()).isEqualTo("Ленинградская обл.");
        assertThat(entity.getRailway()).isEqualTo("Октябрьская");
        assertThat(entity.getImageUrl()).isEqualTo("http://example.com/another.jpg");
        assertThat(entity.getNote()).isEqualTo("Другая заметка");
        assertThat(entity.getInstance().getId()).isEqualTo(3);
    }

    @Test
    void updateEntityFromDto_ShouldUpdateFields() {
        TrainInstance oldInstance = new TrainInstance();
        oldInstance.setId(1);
        Sighting entity = new Sighting();
        entity.setId(1);
        entity.setRegion("Старый регион");
        entity.setInstance(oldInstance);

        SightingDto dto = new SightingDto();
        dto.setRegion("Новый регион");
        dto.setRailway("Новая дорога");
        dto.setSightingDate(LocalDate.of(2023, 1, 1));
        dto.setImageUrl("updated.jpg");
        dto.setNote("Обновлённая заметка");
        dto.setInstanceId(2);

        mapper.updateEntityFromDto(dto, entity);

        assertThat(entity.getRegion()).isEqualTo("Новый регион");
        assertThat(entity.getRailway()).isEqualTo("Новая дорога");
        assertThat(entity.getSightingDate()).isEqualTo(LocalDate.of(2023, 1, 1));
        assertThat(entity.getImageUrl()).isEqualTo("updated.jpg");
        assertThat(entity.getNote()).isEqualTo("Обновлённая заметка");
        assertThat(entity.getInstance().getId()).isEqualTo(2);
        assertThat(entity.getId()).isEqualTo(1);
    }
}