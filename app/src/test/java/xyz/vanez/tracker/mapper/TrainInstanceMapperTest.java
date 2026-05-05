package xyz.vanez.tracker.mapper;

import xyz.vanez.tracker.dto.TrainInstanceDto;
import xyz.vanez.tracker.model.TrainInstance;
import xyz.vanez.tracker.model.TrainModel;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

class TrainInstanceMapperTest {

    private final TrainInstanceMapper mapper = Mappers.getMapper(TrainInstanceMapper.class);

    @Test
    void toDto_ShouldMapEntityToDto() {
        TrainModel model = new TrainModel();
        model.setId(10);
        TrainInstance entity = new TrainInstance();
        entity.setId(1);
        entity.setSerialNumber("EP20-001");
        entity.setManufactureDate(LocalDate.of(2013, 5, 1));
        entity.setImageUrl("http://example.com/img.jpg");
        entity.setModel(model);

        TrainInstanceDto dto = mapper.toDto(entity);

        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getSerialNumber()).isEqualTo("EP20-001");
        assertThat(dto.getManufactureDate()).isEqualTo(LocalDate.of(2013, 5, 1));
        assertThat(dto.getImageUrl()).isEqualTo("http://example.com/img.jpg");
        assertThat(dto.getModelId()).isEqualTo(10);
    }

    @Test
    void toEntity_ShouldMapDtoToEntity() {
        TrainInstanceDto dto = new TrainInstanceDto();
        dto.setId(2);
        dto.setSerialNumber("VL80-002");
        dto.setManufactureDate(LocalDate.of(1980, 6, 1));
        dto.setImageUrl("http://example.com/vl80.jpg");
        dto.setModelId(5);

        TrainInstance entity = mapper.toEntity(dto);

        assertThat(entity.getId()).isEqualTo(2);
        assertThat(entity.getSerialNumber()).isEqualTo("VL80-002");
        assertThat(entity.getManufactureDate()).isEqualTo(LocalDate.of(1980, 6, 1));
        assertThat(entity.getImageUrl()).isEqualTo("http://example.com/vl80.jpg");
        assertThat(entity.getModel().getId()).isEqualTo(5);
    }

    @Test
    void updateEntityFromDto_ShouldUpdateFields() {
        TrainModel oldModel = new TrainModel();
        oldModel.setId(1);
        TrainInstance entity = new TrainInstance();
        entity.setId(1);
        entity.setSerialNumber("OLD");
        entity.setModel(oldModel);

        TrainInstanceDto dto = new TrainInstanceDto();
        dto.setSerialNumber("NEW");
        dto.setManufactureDate(LocalDate.of(2000, 1, 1));
        dto.setImageUrl("new.jpg");
        dto.setModelId(2);

        mapper.updateEntityFromDto(dto, entity);

        assertThat(entity.getSerialNumber()).isEqualTo("NEW");
        assertThat(entity.getManufactureDate()).isEqualTo(LocalDate.of(2000, 1, 1));
        assertThat(entity.getImageUrl()).isEqualTo("new.jpg");
        assertThat(entity.getModel().getId()).isEqualTo(2);
        assertThat(entity.getId()).isEqualTo(1); // id не должно измениться
    }
}