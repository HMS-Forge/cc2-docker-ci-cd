package xyz.vanez.tracker.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

class SightingDtoTest {

    @Test
    void noArgsConstructor_ShouldCreateEmptyDto() {
        SightingDto dto = new SightingDto();
        assertThat(dto).isNotNull();
    }

    @Test
    void allArgsConstructor_ShouldSetFields() {
        LocalDate date = LocalDate.now();
        SightingDto dto = new SightingDto(1, "Московская обл.", "Октябрьская", date, "http://img.jpg", "Заметка", 3);
        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getRegion()).isEqualTo("Московская обл.");
        assertThat(dto.getRailway()).isEqualTo("Октябрьская");
        assertThat(dto.getSightingDate()).isEqualTo(date);
        assertThat(dto.getImageUrl()).isEqualTo("http://img.jpg");
        assertThat(dto.getNote()).isEqualTo("Заметка");
        assertThat(dto.getInstanceId()).isEqualTo(3);
    }

    @Test
    void setters_ShouldUpdateFields() {
        SightingDto dto = new SightingDto();
        dto.setId(2);
        dto.setRegion("Ленинградская обл.");
        dto.setRailway("Октябрьская");
        dto.setSightingDate(LocalDate.of(2023, 1, 1));
        dto.setImageUrl("http://new.jpg");
        dto.setNote("Новая заметка");
        dto.setInstanceId(5);

        assertThat(dto.getId()).isEqualTo(2);
        assertThat(dto.getRegion()).isEqualTo("Ленинградская обл.");
        assertThat(dto.getRailway()).isEqualTo("Октябрьская");
        assertThat(dto.getSightingDate()).isEqualTo(LocalDate.of(2023, 1, 1));
        assertThat(dto.getImageUrl()).isEqualTo("http://new.jpg");
        assertThat(dto.getNote()).isEqualTo("Новая заметка");
        assertThat(dto.getInstanceId()).isEqualTo(5);
    }
}