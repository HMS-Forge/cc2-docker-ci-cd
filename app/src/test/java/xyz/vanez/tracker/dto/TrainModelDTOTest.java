package xyz.vanez.tracker.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TrainModelDtoTest {

    @Test
    void noArgsConstructor_ShouldCreateEmptyDto() {
        TrainModelDto dto = new TrainModelDto();
        assertThat(dto).isNotNull();
    }

    @Test
    void allArgsConstructor_ShouldSetFields() {
        TrainModelDto dto = new TrainModelDto(1, "EP20", "NEVZ", 2012, 8800, 200);
        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getName()).isEqualTo("EP20");
        assertThat(dto.getManufacturer()).isEqualTo("NEVZ");
        assertThat(dto.getYearOfProduction()).isEqualTo(2012);
        assertThat(dto.getPowerKw()).isEqualTo(8800);
        assertThat(dto.getMaxSpeedKmh()).isEqualTo(200);
    }

    @Test
    void setters_ShouldUpdateFields() {
        TrainModelDto dto = new TrainModelDto();
        dto.setId(2);
        dto.setName("VL80");
        dto.setManufacturer("NEVZ");
        dto.setYearOfProduction(1979);
        dto.setPowerKw(6520);
        dto.setMaxSpeedKmh(110);

        assertThat(dto.getId()).isEqualTo(2);
        assertThat(dto.getName()).isEqualTo("VL80");
        assertThat(dto.getManufacturer()).isEqualTo("NEVZ");
        assertThat(dto.getYearOfProduction()).isEqualTo(1979);
        assertThat(dto.getPowerKw()).isEqualTo(6520);
        assertThat(dto.getMaxSpeedKmh()).isEqualTo(110);
    }
}