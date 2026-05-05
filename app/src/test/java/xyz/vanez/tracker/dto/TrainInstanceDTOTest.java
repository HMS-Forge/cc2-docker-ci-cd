package xyz.vanez.tracker.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

class TrainInstanceDtoTest {

    @Test
    void noArgsConstructor_ShouldCreateEmptyDto() {
        TrainInstanceDto dto = new TrainInstanceDto();
        assertThat(dto).isNotNull();
    }

    @Test
    void allArgsConstructor_ShouldSetFields() {
        LocalDate date = LocalDate.of(2013, 5, 1);
        TrainInstanceDto dto = new TrainInstanceDto(1, "EP20-001", date, "http://img.jpg", 5);
        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getSerialNumber()).isEqualTo("EP20-001");
        assertThat(dto.getManufactureDate()).isEqualTo(date);
        assertThat(dto.getImageUrl()).isEqualTo("http://img.jpg");
        assertThat(dto.getModelId()).isEqualTo(5);
    }

    @Test
    void setters_ShouldUpdateFields() {
        TrainInstanceDto dto = new TrainInstanceDto();
        dto.setId(2);
        dto.setSerialNumber("VL80-002");
        dto.setManufactureDate(LocalDate.of(1980, 6, 1));
        dto.setImageUrl("http://new.jpg");
        dto.setModelId(10);

        assertThat(dto.getId()).isEqualTo(2);
        assertThat(dto.getSerialNumber()).isEqualTo("VL80-002");
        assertThat(dto.getManufactureDate()).isEqualTo(LocalDate.of(1980, 6, 1));
        assertThat(dto.getImageUrl()).isEqualTo("http://new.jpg");
        assertThat(dto.getModelId()).isEqualTo(10);
    }
}